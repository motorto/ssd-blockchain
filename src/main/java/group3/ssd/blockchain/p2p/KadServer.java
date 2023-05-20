package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.blockchain.BCConverter;
import group3.ssd.blockchain.blockchain.Blockchain;
import group3.ssd.blockchain.blockchain.GRPCConverter;
import group3.ssd.blockchain.p2p.grpc.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class KadServer {

    private static final Logger logger = Logger.getLogger(KadServer.class.getName());
    private Server server;
    public String ip;
    public int port;
    PeerImpl broker = new PeerImpl();

    public KadServer(String ip, int port) {
        this.ip = ip + ":" + port;
        this.port = port;
    }

    public void start() throws IOException {

        server = ServerBuilder.forPort(this.port)
                .addService(broker)
                .build()
                .start();
        logger.info("Server started, listening on " + ip);

        broker = new PeerImpl();

        Runnable task = () -> {

        };


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    KadServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    class PeerImpl extends PeerGrpc.PeerImplBase {
        @Override
        public void ping(Ping request, StreamObserver<Pong> responseObserver) {
            KadClient.kbucket.checkNodeExistence(new Node(request.getId(), request.getIp(), request.getPort()), request.getProof(), request.getPubKey());
            BlockChain blockchain = GRPCConverter.mkBlockChain(KadClient.blockchain);

            responseObserver.onNext(Pong.newBuilder().setPong(true).setBlockchain(blockchain).build());

            responseObserver.onCompleted();

        }

        @Override
        public void broadcastBlock(Block request, StreamObserver<Status> responseObserver) {
            group3.ssd.blockchain.blockchain.Block new_block = BCConverter.mkBlock(request);
            if (!(KadClient.blockchain.getLatestBlock().hash).equals(new_block.hash)) {
                KadClient.blockchain.addBlock(new_block);
                KadClient.kbucket.getNode(request.getNodeId()).addSuccessfulInteraction();
                KadClient.blockchain.addBlock(new_block);
                KadClient.ledger.updateLedger(new_block);
                KadClient.shareBlock(new_block, request.getNodeId());

            } else {
                responseObserver.onNext(Status.newBuilder().setStatus("Block already exists").build());
                responseObserver.onCompleted();
                return;
            }

        }

        @Override
        public void broadcastTransaction(Transaction request, StreamObserver<Status> responseObserver) {

            group3.ssd.blockchain.blockchain.Transaction new_transaction = BCConverter.mkTransaction(request);

            if (Blockchain.pendingTransactions.size() == 0) {

                KadClient.blockchain.pendingTransactions.add(new_transaction);
            } else if (!KadClient.blockchain.pendingTransactions.get(KadClient.blockchain.pendingTransactions.size() - 1).equals(new_transaction)) {

                KadClient.blockchain.pendingTransactions.add(new_transaction);
            } else {
                responseObserver.onNext(Status.newBuilder().setStatus("Transaction already exists").build());
                responseObserver.onCompleted();
                return;
            }
            responseObserver.onNext(Status.newBuilder().setStatus("Sent").build());
            responseObserver.onCompleted();
        }

        @Override
        public void broadcastBlockchain(BlockChain request, StreamObserver<Status> responseObserver) {
            super.broadcastBlockchain(request, responseObserver);
        }

        @Override
        public void findNodes(FindNode request, StreamObserver<KBucket_GRPC> responseObserver) {

            if (KadClient.kbucket.checkNodeExistence(new Node(request.getId(), request.getIp(), request.getPort()), request.getProof(), request.getPubKey())) {

                responseObserver.onNext(NodeSerializable.KBucket_to_GRPC(KadClient.kbucket.getNeighboursByDistance(request.getTargetId(), KadClient.kbucket.lastSeen)));
            }
            responseObserver.onCompleted();

        }

    }
}

