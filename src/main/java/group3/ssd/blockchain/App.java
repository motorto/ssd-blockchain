package group3.ssd.blockchain;

import group3.ssd.blockchain.p2p.Grpc.KadServer;
import group3.ssd.blockchain.p2p.Node.Node;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {

    KadServer server;
    Node node;
    private Thread serverThread;

    public App(String[] args) {
        p2pServer();
    }

    private void p2pServer() {
        this.server = new KadServer();

        try {
            int port = 4004;
            this.node = this.server.start(null, port);
            System.out.println("Started server.");

            this.serverThread = new Thread(
                    () -> {
                        try {
                            this.server.blockUntilShutdown();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            menu();

            this.serverThread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void menu(){
        int i = 1;
        System.out.println("MENU\n" +
                (i++) + "   Print Blockchain\n" +
                //more options
                "Insert an option.\n");
    }

    public static void main(String[] args) {
        new App(args);
    }
}
