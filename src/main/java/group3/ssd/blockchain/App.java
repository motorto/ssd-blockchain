package group3.ssd.blockchain;

import group3.ssd.blockchain.p2p.Node.Identifier;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws UnknownHostException {
        Node a = new Node(new Identifier(), InetAddress.getByName("localhost"), 3000);

        for (int i = 0; i < 100; i++) {
            NodeTriplet tmp = new NodeTriplet(new Identifier(), InetAddress.getByName("localhost"), 3000, 0);
            a.getRoutingTable().insert(tmp);
        }

        assert (a.getRoutingTable().getAllNodes().size() == 100);
    }
}
