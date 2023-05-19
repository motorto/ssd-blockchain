package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.p2p.grpc.FindNode;
import group3.ssd.blockchain.p2p.grpc.KBucket_GRPC;
import group3.ssd.blockchain.p2p.grpc.PeerGrpc;
import io.grpc.ManagedChannel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static io.grpc.ManagedChannelBuilder.forAddress;

public class Operations {

    private String ip;

    public PeerGrpc.PeerBlockingStub blockingStub;

    public int port;
    private final ManagedChannel managedChannel;

    public Operations(String ip, int port) {
        this(forAddress(ip, port).usePlaintext().build());
        this.ip = ip;
        this.port = port;
    }

    public Operations(ManagedChannel build) {
        this.managedChannel = build;
        this.blockingStub = PeerGrpc.newBlockingStub(build);
    }

    public void shutdown() throws InterruptedException {
        managedChannel.shutdown().awaitTermination(4, TimeUnit.SECONDS);
    }

    public ArrayList<Node> findNodePeer(String target) {

        KBucket_GRPC foundNodes = blockingStub.findNodes(FindNode.newBuilder().setId(User.id).setIp(User.ip).setPort(User.port).setProof(User.proof).setPubKey(User.publicKey).setTargetId(target).build());
        ArrayList<Node> nodeArrayList = NodeConverter.GRPC_to_KBucket(foundNodes);

        return nodeArrayList;
    }


}
