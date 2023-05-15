package group3.ssd.blockchain;

import group3.ssd.blockchain.p2p.Grpc.KadServer;
import group3.ssd.blockchain.p2p.Node.Node;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        KadServer server = new KadServer();
        int port = 4040;
        try {
            Node node = server.start(null, port);
            System.out.println(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
