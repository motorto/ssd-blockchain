package group3.ssd.blockchain.p2p.Operation;

import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.Node;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;

public class BroadcastMessageOperation implements Operation {

    private final Node origin;
    private final int height;
    private final byte[] messageID, messageContent;
    private final Set<NodeTriplet> pending_requests;
    private boolean finished = false;


    public BroadcastMessageOperation(Node origin, int height, byte[] messageID, byte[] messageContent) {
        this.origin = origin;
        this.height = height;
        this.messageID = messageID;
        this.messageContent = messageContent;
        this.pending_requests = new ConcurrentSkipListSet<>();
    }

    @Override
    public void execute() {

        this.origin.registerOngoingOperation(this);

        Random random = new Random();

        for (int i = height; i < NetworkStandards.KEY_SIZE; i++) {

            ConcurrentLinkedDeque<NodeTriplet> kBucket = this.origin.getRoutingTable().getKBuckets().get(i);


            if (kBucket.isEmpty()) continue;

            List<NodeTriplet> nodes = new ArrayList<>(kBucket);

            for (int k = 0; k < NetworkStandards.BROADCAST_PER_HEIGHT && !nodes.isEmpty(); k++) {

                NodeTriplet removedNode = nodes.remove(random.nextInt(nodes.size()));

                this.origin.getClientManager().performBroadcastForNode(this.origin, this,
                        removedNode, i + 1, messageID, messageContent);

                this.pending_requests.add(removedNode);
            }
        }
    }

    public void handleNodeResponded(NodeTriplet nodeTriplet) {
        this.pending_requests.remove(nodeTriplet);
        this.origin.handleSeenNode(nodeTriplet);

        if (this.pending_requests.size() == 0) {
            setFinished(true);
        }
    }

    public void handleNodeFailedDelivery(NodeTriplet nodeTriplet) {
        this.pending_requests.remove(nodeTriplet);
        this.origin.handleFailedNodePing(nodeTriplet);

        if (this.pending_requests.size() == 0) {
            setFinished(true);
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;

        if (finished) {
            this.origin.registerOperationDone(this);
        }
    }


    @Override
    public boolean hasFinished() {
        return finished;
    }
}
