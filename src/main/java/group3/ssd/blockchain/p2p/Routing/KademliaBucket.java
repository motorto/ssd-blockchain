package group3.ssd.blockchain.p2p.Routing;

import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * Class that represents a single K-bucket
 * as describe in the paper.
 * <p>
 * It Utilizes the optimizations defined in the chapter 4 of the original paper
 */
public class KademliaBucket {
    @Getter
    private final int depth;

    @Getter
    private final TreeSet<NodeTriplet> localBucket;

    public KademliaBucket(int depth) {
        this.depth = depth;
        this.localBucket = new TreeSet<>();
    }

    /**
     * Adds Node to the bucket
     *
     * @param n Node to insert
     */
    public synchronized void insert(NodeTriplet n) {

        if (this.localBucket.contains(n)) {
            NodeTriplet tmp = this.removeFromBucket(n);
            this.localBucket.add(tmp);
        } else {
            if (this.localBucket.size() > NetworkStandards.K_BUCKET_SIZE) {
                NodeTriplet lastSeen = null;

                for (NodeTriplet tmp :
                        this.localBucket) {
                    if (lastSeen == null)
                        lastSeen = tmp;
                    else if (tmp.getLastSeen() > lastSeen.getLastSeen()) {
                        lastSeen = tmp;
                    }
                }

            } else {
                this.localBucket.add(n);
            }
        }
    }

    /**
     * Removes Node from Bucket
     *
     * @param node Node to remove
     * @return Node Removed
     */
    private NodeTriplet removeFromBucket(NodeTriplet node) {
        for (NodeTriplet n : this.localBucket) {
            if (n.getNodeId().equals(node.getNodeId())) {
                this.localBucket.remove(n);
                return n;
            }
        }
        throw new NoSuchElementException("Node does not exist");
    }

    /**
     * Removes Node from Bucket
     *
     * @param node Node to remove
     */
    public synchronized void remove(NodeTriplet node) {
        if (this.localBucket.contains(node)) {
            this.removeFromBucket(node);
        } else {
            throw new NoSuchElementException("Node does not exist");
        }
    }

    /**
     * @return List of all nodes in the current bucket
     */
    public synchronized List<NodeTriplet> getNodes() {
        final ArrayList<NodeTriplet> nodeArrayList = new ArrayList<>();

        if (!this.localBucket.isEmpty()) {
            nodeArrayList.addAll(this.localBucket);

        }
        return nodeArrayList;
    }
}
