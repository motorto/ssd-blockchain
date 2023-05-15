package group3.ssd.blockchain.p2p.Node;

import group3.ssd.blockchain.p2p.Grpc.ClientManager;
import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Operation.NodeLookUpOperation;
import group3.ssd.blockchain.p2p.Operation.Operation;
import group3.ssd.blockchain.p2p.Routing.kBuckets;
import group3.ssd.blockchain.p2p.Storage.Storage;
import lombok.Getter;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;

public class Node {

    @Getter
    private final byte[] nodeId;

    @Getter
    private final int port;

    @Getter
    private final ClientManager clientManager;

    @Getter
    private final kBuckets routingTable;

    private final Set<ByteWrapper> seenMessages;

    private final List<Operation> onGoingOperations;

    private final Map<ByteWrapper, Pair<Integer, Integer>> interactionsPerPeer;

    @Getter
    Storage storage;


    public Node(byte[] nodeID, ClientManager clientManager, int port) {
        this.nodeId = nodeID;
        this.port = port;
        this.clientManager = clientManager;
        this.storage = new Storage(this);
        this.routingTable = new kBuckets();
        this.seenMessages = new ConcurrentSkipListSet<>();
        this.onGoingOperations = Collections.synchronizedList(new LinkedList<>());
        this.interactionsPerPeer = new ConcurrentSkipListMap<>();
    }

    public boolean registerSeenMessage(byte[] messageID) {
        return this.seenMessages.add(new ByteWrapper(messageID));
    }

    public void bootstrap(List<NodeTriplet> bootstrapNodes) {
        for (NodeTriplet n :
                bootstrapNodes) {
            int bucket = this.routingTable.getKBucketFor(n.getNodeId().getBytes(), this.getNodeId());

            ConcurrentLinkedDeque<NodeTriplet> kBucket = this.routingTable.getKBuckets().get(bucket);

            kBucket.addFirst(n);

            this.routingTable.getKBuckets().set(bucket, kBucket);
        }

        new NodeLookUpOperation(this, this.getNodeId(), (_nodes) -> {
        }).execute();
    }

    public List<NodeTriplet> findClosestNodes(int nodeCount, byte[] nodeId) {
        int bucket = this.routingTable.getKBucketFor(nodeId, this.getNodeId());
        TreeSet<NodeTriplet> sortedNodes = new TreeSet<>();

        for (int i = 0; i < NetworkStandards.KEY_SIZE; i++) {
            if (bucket + i < NetworkStandards.KEY_SIZE) {
                findClosestNodesAux(nodeCount, bucket + i, sortedNodes, nodeId);
            }

            if (bucket - i >= 0 && i != 0) {
                findClosestNodesAux(nodeCount, bucket - i, sortedNodes, nodeId);
            }

            if ((bucket + i > NetworkStandards.KEY_SIZE && bucket - i < 0) || sortedNodes.size() >= nodeCount)
                break;
        }

        return new ArrayList<>(sortedNodes);
    }

    private void findClosestNodesAux(int nodeCount, int bucket, TreeSet<NodeTriplet> sortedNodes, byte[] centerNodeId) {
        ConcurrentLinkedDeque<NodeTriplet> nodeTriplets = this.routingTable.getKBuckets().get(bucket);

        if (nodeTriplets == null) {
            return;
        }

        for (NodeTriplet n :
                nodeTriplets) {
            if (sortedNodes.size() < nodeCount) {
                sortedNodes.add(n);
            } else {
                NodeTriplet lastNode = sortedNodes.last();

                BigInteger lastNodeDistance = ByteWrapper.nodeDistance(lastNode.getNodeId().getBytes(), centerNodeId);
                BigInteger nodeTripletDistance = ByteWrapper.nodeDistance(n.getNodeId().getBytes(), centerNodeId);

                if (nodeTripletDistance.compareTo(lastNodeDistance) < 0) {
                    sortedNodes.pollLast();
                    sortedNodes.add(n);
                }
            }
        }
    }

    public void registerOngoingOperation(Operation operation) {
        this.onGoingOperations.add(operation);
    }

    public void registerOperationDone(Operation operation) {
        this.onGoingOperations.remove(operation);
    }

    public void handleSeenNode(NodeTriplet nodeSeen) {
        final int bucket = kBuckets.getKBucketFor(this.nodeId, nodeSeen.getNodeId().getBytes());
        ReentrantLock lock = this.routingTable.getKBucketWriteLocks().get(bucket);

        try {
            lock.lock();

            var nodeTriplets = this.routingTable.getKBuckets().get(bucket);

            nodeSeen.setLastSeen(System.currentTimeMillis());

            final var iterator = nodeTriplets.iterator();

            boolean alreadyPresent = false;

            while (iterator.hasNext()) {
                final var currentNode = iterator.next();

                if (nodeSeen.getNodeId().equals(currentNode.getNodeId())) {

                    alreadyPresent = true;

                    iterator.remove();

                    break;
                }
            }

            if (alreadyPresent) {
                nodeTriplets.addLast(nodeSeen);
            } else {

                if (nodeTriplets.size() >= NetworkStandards.K_BUCKET_SIZE) {
                    var node = nodeTriplets.peekFirst();

                    if (node != null) {
                        this.clientManager.performPingFor(this, node);
                    }

                } else {
                    nodeTriplets.addLast(nodeSeen);
                }
            }

            this.routingTable.getKBuckets().set(bucket, nodeTriplets);
        } finally {
            lock.unlock();
        }

    }

    public void handleFailedNodePing(NodeTriplet failedNode) {
        final int bucket = kBuckets.getKBucketFor(this.nodeId, failedNode.getNodeId().getBytes());

        ReentrantLock lock = this.routingTable.getKBucketWriteLocks().get(bucket);

        try {
            lock.lock();
            final var bucketNodes = this.routingTable.getKBuckets().get(bucket);

            final var iterator = bucketNodes.iterator();

            boolean isPresent = false;

            while (iterator.hasNext()) {
                final var currentNode = iterator.next();

                if (failedNode.getNodeId().equals(currentNode.getNodeId())) {
                    isPresent = true;
                    iterator.remove();
                    break;
                }
            }

            if (isPresent) {
                //TODO: Create waiting list and replace node here
            }
        } finally {
            lock.unlock();
        }
    }

    public void registerNegativeInteraction(byte[] nodeId) {
        this.interactionsPerPeer.compute(new ByteWrapper(nodeId), (id, current) -> {
            if (current == null) {
                return Pair.of(0, 1);
            }
            return current.mapValue((currentBad) -> currentBad + 1);
        });
    }

    public void registerPositiveInteraction(byte[] node) {
        this.interactionsPerPeer.compute(new ByteWrapper(node), (nodeID, current) -> {

            if (current == null) {
                return Pair.of(1, 0);
            }

            return current.mapKey((currentGood) -> currentGood + 1);
        });
    }

}
