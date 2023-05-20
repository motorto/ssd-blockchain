package group3.ssd.blockchain.p2p;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Kademlia {

    public static boolean pingNode(Node oldestNode) {
        return new PeerOperations(oldestNode.ip, oldestNode.port).ping();
    }

    public static boolean checkNodeValidity(String id, String ip, int port, int proof, String pubKey) {
        return User.calculateNeighbourHash(ip, port, proof, pubKey).equals(id);
    }

    //find a Node from kbucket through nodeid
    public static Node findNode(String targetId) throws InterruptedException {

        ArrayList<Node> closestNodes = User.kbucket.getNClosestNodes(targetId, User.kbucket.lastSeen);
        ArrayList<Node> foundNodes = new ArrayList<>();
        ArrayList<Node> lastDiscovered = null;

        while (true) {
            for (Node user : closestNodes) {

                ArrayList<Node> n = new PeerOperations(user.ip, user.port).findNodePeer(targetId);

                foundNodes.addAll(n);
            }

            removeRepeated(foundNodes);

            for (Node node : foundNodes) {

                if (node.id.equals(targetId)) {
                    foundNodes.remove(node);
                    break;
                }
            }

            removeRepeated(foundNodes);
            User.kbucket.checkNodeListExistence(foundNodes);


            if (lastDiscovered == null) {
                lastDiscovered = new ArrayList<>();
            } else if (lastDiscovered.containsAll(foundNodes)) {
                return null;
            }

            lastDiscovered.clear();
            lastDiscovered.addAll(foundNodes);

            closestNodes = User.kbucket.getNeighboursByDistance(targetId, foundNodes);
            foundNodes.clear();

        }
    }

    //Remove repeated nodes from kbucket
    private static void removeRepeated(ArrayList<Node> users) {

        Set<Node> user = new HashSet<>(users);

        user.clear();
        user.addAll(users);
    }

    //distance between 2 nodes
    public static String xorDistance(String nodeHash1, String nodeHash2) {

        nodeHash1 = new BigInteger(nodeHash1, 16).toString(2);
        nodeHash2 = new BigInteger(nodeHash2, 16).toString(2);

        int node1Size = nodeHash1.length();
        int node2Size = nodeHash2.length();
        String xorResult = "";

        boolean isDifferent = node1Size != node2Size;
        int maxSize = Math.max(node1Size, node2Size);

        if (isDifferent) {
            if (node1Size > node2Size) {
                nodeHash2 = addPadding(node1Size - node2Size) + nodeHash2;
            } else {
                nodeHash1 = addPadding(node2Size - node1Size) + nodeHash1;
            }
        }

        for (int i = maxSize - 1; i >= 0; i--) {

            String temp = nodeHash1.charAt(i) != nodeHash2.charAt(i) ? "1" : "0";
            xorResult = temp + xorResult;

        }

        return xorResult;
    }

    //compare distance between nodes
    public static int compareDistance(String distance1, String distance2) {
        int n;
        String padding;

        //Prepare distances for numeric compare
        if (distance2.length() > distance1.length()) {
            n = distance2.length() - distance1.length();
            padding = addPadding(n);
            distance1 = padding + distance1;
        } else {
            n = distance1.length() - distance2.length();
            padding = addPadding(n);
            distance2 = padding + distance2;
        }

        for (int i = 0; i < distance1.length(); i++) {
            if (distance1.length() > distance2.length()) {
                return 1;
            } else if (distance1.length() < distance2.length()) {
                return -1;
            }
        }
        return 0;
    }

    //Add padding in node with different sizes
    public static String addPadding(int n) {
        return "0".repeat(Math.max(0, n));
    }


}
