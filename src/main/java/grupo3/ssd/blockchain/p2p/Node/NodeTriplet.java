package grupo3.ssd.blockchain.p2p.Node;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.util.Arrays;

public class NodeTriplet implements Comparable<NodeTriplet> {

    @Getter
    private final InetAddress ipAddress;

    @Getter
    private final int tcpPort;

    @Getter
    private final Identifier nodeId;

    @Setter
    @Getter
    private long lastSeen;

    public NodeTriplet(Identifier nodeId, InetAddress ipAddress, int tcpPort, long lastSeen) {
        this.ipAddress = ipAddress;
        this.tcpPort = tcpPort;
        this.nodeId = nodeId;
        this.lastSeen = lastSeen;
    }

    @Override
    public int compareTo(NodeTriplet o) {
        return Arrays.compare(nodeId.getKey(), o.nodeId.getKey());
    }

    @Override
    public String toString() {
        return "NodeTriple{" +
                "ipAddress=" + ipAddress +
                ", udpPort=" + tcpPort +
                ", nodeID=" + this.nodeId.getKey().toString() +
                ", lastSeen=" + lastSeen +
                '}';
    }

}
