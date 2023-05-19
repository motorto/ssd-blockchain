package group3.ssd.blockchain.p2p;

import java.util.ArrayList;

public class User {

    public static String id;
    public static String ip;
    public static int port;
    public static int proof;
    public static String publicKey;

    public static ArrayList<Node> blacklist = new ArrayList<>();


    public String getHash() {
        return id;
    }

    public User() {

    }


}