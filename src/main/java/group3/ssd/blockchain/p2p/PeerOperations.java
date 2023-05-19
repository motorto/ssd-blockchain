package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.blockchain.BCConverter;
import group3.ssd.blockchain.blockchain.Blockchain;
import group3.ssd.blockchain.blockchain.GRPCConverter;
import group3.ssd.blockchain.p2p.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//Operations made by the client side of the peer
public class PeerOperations {

    private String ip;
    public PeerGrpc.PeerBlockingStub blockingStub;
    public int port;
    private final ManagedChannel channel;

    public PeerOperations(String ip, int port) {
        this(ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build());
        this.port = port;
        this.ip = ip;
    }

    PeerOperations(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = PeerGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(4, TimeUnit.SECONDS);
    }

    public ArrayList<Node> findNodePeer(String targetId) {

        try {
            try {

                KBucket_GRPC foundNodes = blockingStub.findNodes(FindNode.newBuilder().setId(User.id).setIp(User.ip).setPort(User.port).setProof(User.proof).setPubKey(User.publicKey).setTargetId(targetId).build());
                ArrayList<Node> nodeList = NodeConverter.GRPC_to_KBucket(foundNodes);

                return nodeList;
            } catch (StatusRuntimeException e) {

                return null;
            }
        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendBlock(group3.ssd.blockchain.blockchain.Block b) {
        try {
            Block request = GRPCConverter.mkBlock(b);
            Status s = blockingStub.broadcastBlock(request);
        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTransaction(group3.ssd.blockchain.blockchain.Transaction t) {
        try {

            Transaction request = GRPCConverter.mkTransaction(t);

            Status s = blockingStub.broadcastTransaction(request);

        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean ping() {
        try {
            try {
                Ping request = Ping.newBuilder()
                        .setId(User.id)
                        .setIp(User.ip)
                        .setPort(User.port)
                        .setProof(User.proof)
                        .setPubKey(User.publicKey)
                        .build();

                Pong response = blockingStub.ping(request);
                if (response.getPong()) {
                    Blockchain received = BCConverter.mkBlockChain(response.getBlockchain());
                    if (received.getChain().size() > User.blockchain.getChain().size()) {
                        User.blockchain = received;
                        User.ledger.restartLedger();
                    }
                }
                return response.getPong();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Blockchain getBlockchain() {
        try {
            Ping request = Ping.newBuilder()
                    .setId(User.id)
                    .setIp(User.ip)
                    .setPort(User.port)
                    .setProof(User.proof)
                    .setPubKey(User.publicKey)
                    .build();

            Pong response = blockingStub.ping(request);
            if (response.getPong()) {
                Blockchain received = null;
                try {
                    received = BCConverter.mkBlockChain(response.getBlockchain());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return received;
            } else
                return null;
        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
