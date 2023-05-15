package group3.ssd.blockchain.p2p.Grpc;

import com.google.protobuf.ByteString;
import group3.ssd.blockchain.p2p.KademliaProtocolGrpc;
import group3.ssd.blockchain.p2p.Messages;
import group3.ssd.blockchain.p2p.Node.ByteWrapper;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import group3.ssd.blockchain.p2p.Operation.BroadcastMessageOperation;
import group3.ssd.blockchain.p2p.Operation.NodeLookUpOperation;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private final Map<ByteWrapper, ManagedChannel> cachedChannels;

    @Getter
    private static ClientManager instance;

    public ClientManager() {
        this.instance = this;
        cachedChannels = new ConcurrentHashMap<>();
    }

    public void performBroadcastForNode(Node origin, BroadcastMessageOperation broadcastMessageOperation, NodeTriplet removedNode, int i, byte[] messageID, byte[] messageContent) {
    }


    private ManagedChannel getCachedChannel(ByteWrapper nodeID) {

        if (cachedChannels.containsKey(nodeID)) {
            final var managedChannel = this.cachedChannels.get(nodeID);

            if (managedChannel.isTerminated() || managedChannel.isTerminated()) {
                return null;
            }

            return managedChannel;
        }

        return null;
    }

    private ManagedChannel createConnection(NodeTriplet triple) {

        return ManagedChannelBuilder
                .forAddress(triple.getIpAddress().getHostAddress(), triple.getPort())
                .usePlaintext()
                .build();

    }

    private void cacheConnection(NodeTriplet triple, ManagedChannel channel) {
        this.cachedChannels.put(triple.getNodeId(), channel);
    }

    private ManagedChannel setupConnection(NodeTriplet nodeTriple) throws IOException {
        final var cachedChannel = getCachedChannel(nodeTriple.getNodeId());

        if (cachedChannel == null || cachedChannel.isShutdown() || cachedChannel.isTerminated()) {
            final var createdConnection = createConnection(nodeTriple);

            cacheConnection(nodeTriple, createdConnection);

            return createdConnection;
        } else if (!cachedChannel.isTerminated() && !cachedChannel.isShutdown()) {
            return cachedChannel;
        }

        throw new IOException("Failed to connect to: " + nodeTriple.getIpAddress().getHostAddress());
    }

    public KademliaProtocolGrpc.KademliaProtocolStub newStub(NodeTriplet triple) throws IOException {
        final var connection = setupConnection(triple);
        return KademliaProtocolGrpc.newStub(connection);
    }

    public void performPingFor(Node node, NodeTriplet nodeTriplet) {
        try {
            KademliaProtocolGrpc.KademliaProtocolStub stub = this.newStub(nodeTriplet);
            stub.ping(Messages.Ping.newBuilder().setNodeID(ByteString.copyFrom(node.getNodeId())).build(),
                    new StreamObserver<>() {
                        @Override
                        public void onNext(Messages.Ping value) {
                            node.handleSeenNode(nodeTriplet);
                        }

                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                            node.handleFailedNodePing(nodeTriplet);
                        }

                        @Override
                        public void onCompleted() {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            node.handleFailedNodePing(nodeTriplet);
        }
    }

    public void performLookupFor(Node node, NodeLookUpOperation operation, NodeTriplet target, byte[] lookup) {
        try {
            KademliaProtocolGrpc.KademliaProtocolStub stub = this.newStub(target);
            List<NodeTriplet> nodeTripletList = new LinkedList<>();

            Messages.TargetID targetID = Messages.TargetID.newBuilder()
                    .setRequestingNodeID(ByteString.copyFrom(node.getNodeId()))
                    .setRequestNodePort(node.getPort())
                    .setTargetID(ByteString.copyFrom(lookup))
                    .build();

            stub.findNode(targetID, new StreamObserver<Messages.FoundNode>() {
                @Override
                public void onNext(Messages.FoundNode foundNode) {
                    byte[] nodeID = foundNode.getNodeID().toByteArray();
                    String nodeAddress = foundNode.getNodeAddress();
                    int port = foundNode.getPort();

                    try {
                        InetAddress ipAddress = InetAddress.getByName(nodeAddress);
                        nodeTripletList.add(new NodeTriplet(ipAddress, port, nodeID, foundNode.getLastSeen()));
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    operation.handleFindNodeFailed(target);
                }

                @Override
                public void onCompleted() {
                    operation.handleFindNodeReturned(target, nodeTripletList);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
