package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.util.Config;

import java.util.ArrayList;

public class KBucket {

    public ArrayList<Node> lastSeen = new ArrayList<>();

    public int seenSize() {
        return lastSeen.size();
    }

    public void removeNode(Node node) {
        lastSeen.remove(node);
    }

    public Node oldestNode() {
        return lastSeen.get(0);
    }

    public void sendToLast(Node node) {
        lastSeen.remove(node);
        lastSeen.add(node);
    }

    public void addNode(Node node) {
        lastSeen.add(node);
    }

    public Node getNode(String nodeId) {
        for (Node node : this.lastSeen) {
            if (node.id.equals(nodeId))
                return node;
        }
        return null;
    }

    public ArrayList<Node> getNClosestNodes(String targetId, ArrayList<Node> nodes) {

        if (nodes.size() > Config.ALPHA) {
            return (ArrayList<Node>) getNeighboursByDistance(targetId, nodes).subList(0, Config.ALPHA - 1);
        } else {
            return getNeighboursByDistance(targetId, nodes);
        }
    }

    public void checkNodeListExistence(ArrayList<Node> userList) {
        for (Node node : userList) {
            checkNodeExistence(node, -1, "");
        }
    }

    public boolean checkNodeExistence(Node node, int proof, String pubKey) {
        if (node.id.equals(KadClient.id)) {
            return true;
        }

        if (proof != -1) {
            if (!Kademlia.checkNodeValidity(node.id, node.ip, node.port, proof, pubKey))
                return false;
        }

        if (lastSeen.contains(node)) {
            sendToLast(node);
            return true;
        }

        if (seenSize() < Config.MAX_NODES_BUCKET) {
            addNode(node);
        } else {
            if (Kademlia.pingNode(oldestNode())) {
                sendToLast(oldestNode());
            } else {
                removeNode((oldestNode()));
                addNode(node);
            }
        }
        return true;
    }

    public ArrayList<Node> getNeighboursByDistance(String targetId, ArrayList<Node> list) {

        ArrayList<Node> result = (ArrayList<Node>) list.clone();

        if (list.size() == 1) {
            return list;
        }

        result.sort((r1, r2) -> {

            String xorDist = Kademlia.xorDistance(r1.id, targetId);
            String xorDist2 = Kademlia.xorDistance(r2.id, targetId);

            return Kademlia.compareDistance(xorDist, xorDist2);

        });

        return result;
    }

    public ArrayList<Node> getCloneNodesList() {
        return (ArrayList) lastSeen.clone();
    }

    public void print() {
        System.out.println("Bucket:");
        for (Node n : lastSeen) {
            System.out.print(n.toString() + " ");
        }
    }
}
