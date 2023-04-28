package group3.ssd.blockchain.p2p.Routing;

import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.Identifier;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;

import java.util.ArrayList;
import java.util.List;

public class RoutingTable {

    private final Node localNode;
    private KademliaBucket[] k_buckets;

    public RoutingTable(Node localNode) {
        this.localNode = localNode;
    }

    public final void initialize() {
        this.k_buckets = new KademliaBucket[NetworkStandards.KEY_SIZE];
        for (int i = 0; i < NetworkStandards.KEY_SIZE; i++) {
            k_buckets[i] = new KademliaBucket(i);
        }
    }

    public final int findBucketId(Identifier nid) {
        int bId = this.localNode.getNodeId().getDistance(nid) - 1;

        return Math.max(bId, 0);
    }

    public synchronized void insert(NodeTriplet n) {
        this.k_buckets[this.findBucketId(n.getNodeId())].insert(n);
    }

    public void setUnresponsive(NodeTriplet n) {
        int bucketID = this.findBucketId(n.getNodeId());
        this.k_buckets[bucketID].remove(n);
    }

    public synchronized final List<NodeTriplet> getAllNodes() {
        List<NodeTriplet> nodeTripletArrayList = new ArrayList<>();
        for (KademliaBucket b :
                this.k_buckets) {
            nodeTripletArrayList.addAll(b.getNodes());
        }

        return nodeTripletArrayList;
    }
}
