package group3.ssd.blockchain.p2p.Node;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.util.Arrays;

public class NodeTriplet implements Comparable<NodeTriplet> {

    @Getter
    private final InetAddress ipAddress;

    @Getter
    private final int port;

    @Getter
    private final ByteWrapper nodeId;

    @Getter
    @Setter
    private long lastSeen;

    public NodeTriplet(InetAddress ipAddress, int port, byte[] nodeId, long lastSeen) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.nodeId = new ByteWrapper(nodeId);
        this.lastSeen = lastSeen;
    }

    @Override
    public int compareTo(NodeTriplet o) {
        return Arrays.compare(nodeId.getBytes(), o.getNodeId().getBytes());
    }

    @Override
    public String toString() {
        return "NodeTriple{" +
                "ipAddress=" + this.ipAddress +
                ", udpPort=" + this.port +
                ", nodeID=" + this.nodeId.toHexString() +
                ", lastSeen=" + this.lastSeen +
                '}';
    }
}
