package group3.ssd.blockchain.p2p.Operation;

import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import group3.ssd.blockchain.p2p.Routing.kBuckets;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NodeLookUpOperation implements Operation {

    Node node;
    byte[] lookupID;

    private boolean finished = false;

    private ScheduledFuture<?> future;

    private final Map<NodeTriplet, Long> waiting_response;
    private final Consumer<List<NodeTriplet>> callWhenDone;
    private final AtomicBoolean calledDone = new AtomicBoolean(false);
    private final Map<NodeTriplet, NodeOperationState> currentOperations;

    //TODO:
    public NodeLookUpOperation(Node node, byte[] lookupId, Consumer<List<NodeTriplet>> callWhenDone) {
        this.node = node;
        this.lookupID = lookupId;

        this.waiting_response = new ConcurrentSkipListMap<>();
        this.currentOperations = new ConcurrentSkipListMap<>();
        this.callWhenDone = callWhenDone != null ? callWhenDone : (_n) -> {
        };

        List<NodeTriplet> nodes = node.findClosestNodes(Integer.MAX_VALUE, lookupID);

        nodes.forEach(closeNode -> currentOperations.put(closeNode, NodeOperationState.NOT_ASKED));
    }

    @Override
    public void execute() {
        this.node.registerOngoingOperation(this);

        this.future = scheduledExecutor.scheduleAtFixedRate(this::iterate, 1, 1, TimeUnit.SECONDS);

        iterate();
    }

    private synchronized boolean ask() {
        if (this.waiting_response.size() >= NetworkStandards.NETWORK_PARALLELIZATION) {
            return false;
        }

        int asked = this.waiting_response.size();

        List<NodeTriplet> nodeTriples = closestKNodesWithState(NodeOperationState.NOT_ASKED);

        if (nodeTriples.isEmpty() && waiting_response.isEmpty()) {
            return true;
        }

        System.out.println(nodeTriples);

        for (NodeTriplet nodeTriplet : nodeTriples) {
            currentOperations.put(nodeTriplet, NodeOperationState.WAITING_RESPONSE);
            this.node.getClientManager().performLookupFor(this.node, this, nodeTriplet, this.lookupID);
            waiting_response.put(nodeTriplet, System.currentTimeMillis());
            asked++;
            if (asked >= NetworkStandards.NETWORK_PARALLELIZATION) {
                break;
            }
        }

        return false;
    }

    private void iterate() {
        if (this.ask()) {

            //When we are done, cancel the task
            this.future.cancel(true);

            if (calledDone.compareAndSet(false, true)) {
                this.callWhenDone.accept(this.closestKNodesWithState(NodeOperationState.RESPONDED));

                int bucket = kBuckets.getKBucketFor(this.node.getNodeId(), this.lookupID);

                this.node.getRoutingTable().getLastKBucketUpdate().set(bucket, System.currentTimeMillis());

            }

            setFinished(true);
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;

        if (finished) {
            this.node.registerOperationDone(this);
        }
    }


    @Override
    public boolean hasFinished() {
        return this.finished;
    }


    private List<NodeTriplet> closestKNodesWithState(NodeOperationState operationState) {

        List<NodeTriplet> nodes = new LinkedList<>();

        for (Map.Entry<NodeTriplet, NodeOperationState> node : this.currentOperations.entrySet()) {

            if (node.getValue() != operationState) {
                continue;
            }

            nodes.add(node.getKey());

            if (nodes.size() >= NetworkStandards.KEY_SIZE) break;
        }

        return nodes;
    }

    public void handleFindNodeFailed(NodeTriplet target) {
        this.currentOperations.put(target, NodeOperationState.FAILED);
        this.waiting_response.remove(target);
        this.node.handleFailedNodePing(target);
        this.node.registerNegativeInteraction(target.getNodeId().getBytes());
        this.iterate();
    }

    public void handleFindNodeReturned(NodeTriplet node, List<NodeTriplet> found) {
        this.currentOperations.put(node, NodeOperationState.RESPONDED);

        this.waiting_response.remove(node);

        if (found.isEmpty()) {
            this.node.registerNegativeInteraction(node.getNodeId().getBytes());
        } else {
            this.node.registerPositiveInteraction(node.getNodeId().getBytes());
        }

        this.node.handleSeenNode(node);

        for (NodeTriplet nodeTriplet :
                found) {
            this.currentOperations.putIfAbsent(nodeTriplet, NodeOperationState.NOT_ASKED);
        }
        this.iterate();
    }

}
