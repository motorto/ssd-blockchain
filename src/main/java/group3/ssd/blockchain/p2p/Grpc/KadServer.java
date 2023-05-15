package group3.ssd.blockchain.p2p.Grpc;

import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.Node;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.BiConsumer;

public class KadServer {
    private Server server;
    private ServerImpl serverImpl;
    private final ClientManager clientManager;
    private static final Random random = new Random();

    public KadServer() {
        this.clientManager = new ClientManager();
    }

    public void registerMessageListener(BiConsumer<byte[], byte[]> messageConsumer) {
        serverImpl.registerConsumer(messageConsumer);
    }

    public Node start(String hexNodeId, int port) throws IOException {
        byte[] nodeId = null;
        if (hexNodeId != null) {
            nodeId = new BigInteger(hexNodeId, 16).toByteArray();
        } else {
            nodeId = new byte[NetworkStandards.KEY_SIZE / Byte.SIZE];
            random.nextBytes(nodeId);
        }

        final var node = new Node(nodeId, clientManager, port);

        this.serverImpl = new ServerImpl(node);

        this.server = ServerBuilder.forPort(port)
                .intercept(new IntIncomingCalls(node))
                .addService(serverImpl)
                .build()
                .start();
        return node;
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
