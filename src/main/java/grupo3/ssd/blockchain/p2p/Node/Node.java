package grupo3.ssd.blockchain.p2p.Node;

import grupo3.ssd.blockchain.p2p.Routing.RoutingTable;
import lombok.Getter;

import java.net.InetAddress;

public class Node {

    @Getter
    private final Identifier nodeId;

    @Getter
    private final InetAddress ip_address;

    @Getter
    private final int tcpPort;

    @Getter
    private final RoutingTable routingTable;

    {
        this.routingTable = new RoutingTable(this);
        this.routingTable.initialize();
    }

    public Node(Identifier nodeId, InetAddress ipAddress, int tcpPort) {
        this.nodeId = nodeId;
        this.ip_address = ipAddress;
        this.tcpPort = tcpPort;
    }
}
