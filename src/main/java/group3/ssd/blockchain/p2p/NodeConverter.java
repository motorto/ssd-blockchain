package group3.ssd.blockchain.p2p;


import group3.ssd.blockchain.p2p.grpc.KBucket_GRPC;
import group3.ssd.blockchain.p2p.grpc.Node_GRPC;

import java.util.ArrayList;

public class NodeConverter {

    public static Node_GRPC Node_to_GRPC(Node node) {
        return Node_GRPC.newBuilder().setId(node.id).setIp(node.ip).setPort(node.port).build();
    }

    public static Node GRPC_to_Node(Node_GRPC node) {
        return new Node(node.getId(), node.getIp(), node.getPort());
    }

    public static ArrayList<Node> GRPC_to_KBucket(KBucket_GRPC kbucket) {
        ArrayList<Node_GRPC> nodeList = new ArrayList<>(kbucket.getKbucketList());
        ArrayList<Node> a = new ArrayList<>();
        for (Node_GRPC node : nodeList)
            a.add(GRPC_to_Node(node));

        return a;
    }

    public static KBucket_GRPC KBucket_to_GRPC(ArrayList<Node> nodeList) {
        ArrayList<Node_GRPC> nodeListGrpc = new ArrayList<>();
        for (Node node : nodeList)
            nodeListGrpc.add(Node_to_GRPC(node));
        return KBucket_GRPC.newBuilder().addAllKbucket(nodeListGrpc).build();
    }


}
