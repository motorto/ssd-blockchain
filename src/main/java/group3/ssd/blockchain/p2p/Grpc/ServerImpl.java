package group3.ssd.blockchain.p2p.Grpc;

import com.google.protobuf.ByteString;
import group3.ssd.blockchain.p2p.KademliaProtocolGrpc;
import group3.ssd.blockchain.p2p.Messages;
import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import group3.ssd.blockchain.p2p.Operation.BroadcastMessageOperation;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class ServerImpl extends KademliaProtocolGrpc.KademliaProtocolImplBase {
    private final Node node;
    private final List<BiConsumer<byte[], byte[]>> messagesConsumer;

    public ServerImpl(Node node) {
        this.node = node;
        messagesConsumer = new LinkedList<>();
    }

    @Override
    public void ping(Messages.Ping request, StreamObserver<Messages.Ping> responseObserver) {
        responseObserver.onNext(request);

        responseObserver.onCompleted();
    }

    @Override
    public void send(Messages.Message request, StreamObserver<Messages.MessageResponse> responseObserver) {
        byte[] message = request.getMessage().toByteArray();


        Context.current().fork().run(() -> {
            for (BiConsumer<byte[], byte[]> messageConsumer : this.messagesConsumer) {
                messageConsumer.accept(request.getSendingNodeID().toByteArray(),
                        message);
            }
        });

        responseObserver.onCompleted();
    }

    @Override
    public void findNode(Messages.TargetID request, StreamObserver<Messages.FoundNode> responseObserver) {
        final var kClosestNodes = this.node.findClosestNodes(NetworkStandards.KEY_SIZE, request.getTargetID().toByteArray());

        for (NodeTriplet n :
                kClosestNodes) {
            final var node = Messages.FoundNode.newBuilder()
                    .setNodeAddress(n.getIpAddress().getHostAddress())
                    .setPort(n.getPort())
                    .setLastSeen(n.getLastSeen())
                    .setNodeID(ByteString.copyFrom(n.getNodeId().getBytes()))
                    .build();

            responseObserver.onNext(node);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void broadcastMessage(Messages.Broadcast request, StreamObserver<Messages.BroadcastResponse> responseObserver) {
        byte[] messageId = request.getMessageID().toByteArray();

        Messages.BroadcastResponse.Builder builder = Messages.BroadcastResponse.newBuilder();

        if (this.node.registerSeenMessage(messageId)) {
            byte[] messageContent = request.getMessageContent().toByteArray();

            Context.current().fork().run(() -> {
                new BroadcastMessageOperation(this.node, request.getHeight(), messageId,
                        messageContent).execute();

                for (BiConsumer<byte[], byte[]> messageConsumer : this.messagesConsumer) {
                    messageConsumer.accept(request.getRequestingNodeID().toByteArray(), messageContent);
                }
            });
            builder.setSeen(false);
        } else {
            builder.setSeen(true);
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    public void registerConsumer(BiConsumer<byte[], byte[]> messageConsumer) {
        this.messagesConsumer.add(messageConsumer);
    }

    @Override
    public void store(Messages.Store request, StreamObserver<Messages.Store> responseObserver) {
        this.node.getStorage().insertValue(request.getOwningNodeID().toByteArray(), request.getKey().toByteArray(), request.getValue().toByteArray());

        responseObserver.onNext(Messages.Store.newBuilder().setValue(request.getValue()).build());
        responseObserver.onCompleted();
    }
}
