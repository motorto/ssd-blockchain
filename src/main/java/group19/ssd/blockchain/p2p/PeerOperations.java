package group19.ssd.blockchain.p2p;

import group19.ssd.blockchain.blockchain.BCConverter;
import group19.ssd.blockchain.blockchain.Block;
import group19.ssd.blockchain.blockchain.Blockchain;
import group19.ssd.blockchain.blockchain.GRPCConverter;
import group19.ssd.blockchain.p2p.grpc.*;
import group19.ssd.blockchain.transactions.Transaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PeerOperations {

    public PeerGrpc.PeerBlockingStub blockingStub;
    public int port;
    private final ManagedChannel channel;

    public PeerOperations(String ip, int port) {
        this(ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build());
        this.port = port;
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

                KBucket_GRPC foundNodes = blockingStub.findNodes(FindNode.newBuilder().setId(KadClient.id).setIp(KadClient.ip).setPort(KadClient.port).setProof(KadClient.proof).setPubKey(KadClient.publicKey).setTargetId(targetId).build());

                return NodeSerializable.GRPC_to_KBucket(foundNodes);
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

    public void sendBlock(Block b) {
        try {
            group19.ssd.blockchain.p2p.grpc.Block request = GRPCConverter.mkBlock(b);
            blockingStub.broadcastBlock(request);
        } finally {
            try {
                PeerOperations.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTransaction(Transaction t) {
        try {

            group19.ssd.blockchain.p2p.grpc.Transaction request = GRPCConverter.mkTransaction(t);

            blockingStub.broadcastTransaction(request);

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
                        .setId(KadClient.id)
                        .setIp(KadClient.ip)
                        .setPort(KadClient.port)
                        .setProof(KadClient.proof)
                        .setPubKey(KadClient.publicKey)
                        .build();

                Pong response = blockingStub.ping(request);
                if (response.getPong()) {
                    Blockchain received = BCConverter.mkBlockChain(response.getBlockchain());
                    if (received.getChain().size() > KadClient.blockchain.getChain().size()) {
                        KadClient.blockchain = received;
                        KadClient.ledger.restartLedger();
                    }
                }
                return response.getPong();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
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
                    .setId(KadClient.id)
                    .setIp(KadClient.ip)
                    .setPort(KadClient.port)
                    .setProof(KadClient.proof)
                    .setPubKey(KadClient.publicKey)
                    .build();

            Pong response = blockingStub.ping(request);
            if (response.getPong()) {
                Blockchain received = null;
                try {
                    received = BCConverter.mkBlockChain(response.getBlockchain());
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
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
