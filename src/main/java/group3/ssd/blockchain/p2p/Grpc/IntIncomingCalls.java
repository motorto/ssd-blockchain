package group3.ssd.blockchain.p2p.Grpc;


// Intercepts incoming calls before being handled by Server

import group3.ssd.blockchain.p2p.Messages;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import io.grpc.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class IntIncomingCalls implements ServerInterceptor {

    private final Node node;

    public IntIncomingCalls(Node node) {
        this.node = node;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        final var remoteAddr = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);

        InetSocketAddress socketAddress = (InetSocketAddress) remoteAddr;

        InetAddress finalAddress = socketAddress.getAddress();

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(serverCallHandler.startCall(serverCall, metadata)) {

            public void onMessage(ReqT message) {
                byte[] nodeId;
                int port;

                if (message instanceof Messages.Ping) {
                    nodeId = ((Messages.Ping) message).getNodeID().toByteArray();
                    port = ((Messages.Ping) message).getRequestingNodePort();
                } else if (message instanceof Messages.TargetID) {
                    nodeId = ((Messages.TargetID) message).getRequestingNodeID().toByteArray();
                    port = ((Messages.TargetID) message).getRequestNodePort();
                } else if (message instanceof Messages.Message) {
                    nodeId = ((Messages.Message) message).getSendingNodeID().toByteArray();
                    port = ((Messages.Message) message).getSendingNodePort();
                } else if (message instanceof Messages.Store) {
                    nodeId = ((Messages.Store) message).getRequestingNodeID().toByteArray();
                    port = ((Messages.Store) message).getRequestingNodePort();
                } else if (message instanceof Messages.Broadcast) {
                    nodeId = ((Messages.Broadcast) message).getRequestingNodeID().toByteArray();
                    port = ((Messages.Broadcast) message).getRequestingNodePort();
                } else {
                    super.onMessage(message);

                    return;
                }

                NodeTriplet seen = new NodeTriplet(finalAddress, port, nodeId, System.currentTimeMillis());
                Context fork = Context.current().fork();
                fork.run(() -> node.handleSeenNode(seen));
                super.onMessage(message);
            }
        };
    }
}
