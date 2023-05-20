package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.blockchain.Block;
import group3.ssd.blockchain.blockchain.Blockchain;
import group3.ssd.blockchain.blockchain.Transaction;
import group3.ssd.blockchain.util.Config;
import group3.ssd.blockchain.util.Misc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class User {

    public static String id;
    public static String ip;
    public static int port;
    public static int proof;
    public static String publicKey;
    public static KBucket kbucket = new KBucket();
    public static Wallet wallet = new Wallet();
    public static Blockchain blockchain;
    public static Ledger ledger;
    public static MineBlockThread mineBlockThread = new MineBlockThread();
    public static KeepAliveThread keepAliveThread = new KeepAliveThread();

    public String getHash() {
        return id;
    }

    public User() {

    }

    public void setup(int node_port, String node_ip) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User.proof = 0;
        User.port = node_port;
        User.ip = node_ip;
        User.publicKey = User.wallet.getPublicKey();
        User.ledger = new Ledger();
        try {
            if (Config.knownNode.equals("")) {
                User.blockchain = new Blockchain(User.wallet);
            } else {
                User.blockchain = new Blockchain();
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        id = obtainNodeId();


        System.out.println(Config.knownNode);

        if (Config.knownNode.equals("")) {
            Config.knownNode = User.id;
        } else {
            User.kbucket.addNode(new Node(Config.knownNode, node_ip, 8080));
            Kademlia.pingNode(new Node(Config.knownNode, node_ip, 8080));
        }

    }

    public static void startMining() {
        User.mineBlockThread.start();
    }

    public static void startPinging() {
        User.keepAliveThread.start();
    }

    private static String obtainNodeId() {
        String hashPuzzle = new String(new char[Config.NODE_CREATION_DIFFICULTY]).replace('\0', '0');
        do {
            proof++;
            id = calculateHash(User.proof, User.ip, User.port, User.publicKey);
        } while (!id.substring(0, Config.MINING_DIFFICULTY).equals(hashPuzzle));

        return id;
    }

    private static String calculateHash(int proof, String ip, int port, String publicKey) {
        return Misc.applyEncryption(ip + port + proof + publicKey);
    }

    public static String calculateNeighbourHash(String ip, int port, int proof, String pubKey) {
        return Misc.applyEncryption(ip + port + proof + pubKey);
    }

    public static void evaluateTrust() {
        ArrayList<Node> bucket = kbucket.getCloneNodesList();
        for (Node node : bucket) {
            Blockchain newBlockchain = null;

            try {
                newBlockchain = new PeerOperations(node.ip, node.port).getBlockchain();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (newBlockchain != null) {
                if (newBlockchain.isValid())
                    User.kbucket.getNode(node.id).addSuccessfulInteraction();
                else {
                    User.kbucket.getNode(node.id).addUnsuccessfulInteraction();
                }
            }
        }
    }

    public static void shareBlock(Block block, String sender) {
        ArrayList<Node> destinations = User.kbucket.getCloneNodesList();

        for (Node destination : destinations) {
            if (!destination.id.equals(sender))
                try {
                    System.out.println("Send Block to " + destination.ip + ":" + destination.port);
                    new PeerOperations(destination.ip, destination.port).sendBlock(block);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public static void shareTransaction(Transaction transaction, String sender) {

        ArrayList<Node> destinations = User.kbucket.getCloneNodesList();

        try {
            transaction.signTransaction(User.wallet);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
                 SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            e.printStackTrace();
        }

        Blockchain.pendingTransactions.add(transaction);


        for (Node destination : destinations) {
            System.out.println(destination.ip + ":" + destination.port);
            if (!(destination.id.equals(sender)))
                try {
                    new PeerOperations(destination.ip, destination.port).sendTransaction(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


    private static class MineBlockThread extends Thread {

        public MineBlockThread() {
        }

        @Override
        public void run() {

            int transactionsPerBlock = Config.MAX_TRANSACTIONS_PER_BLOCK;

            while (true) {
                try {

                    TimeUnit.MICROSECONDS.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (User.blockchain.getPendingTransactions().size() >= 1) {

                    String blockHashId = Misc.applyEncryption(new Date().getTime() + "");
                    System.out.println("mining: " + blockHashId);

                    User.blockchain.minePendingTransactions(User.wallet);
                    User.ledger.updateLedger(User.blockchain.getLatestBlock());
                    //User.ledger.restartLedger();

                    User.shareBlock(User.blockchain.getLatestBlock(), User.id);
                    User.blockchain.printBlockChain();
                }
            }
        }
    }

    private static class KeepAliveThread extends Thread {

        public KeepAliveThread() {
        }

        @Override
        public void run() {

            while (true) {
                try {

                    TimeUnit.MILLISECONDS.sleep(20000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                User.evaluateTrust();

                for (int i = 0; i < User.kbucket.seenSize(); i++) {
                    if (!new PeerOperations(User.kbucket.lastSeen.get(i).ip, User.kbucket.lastSeen.get(i).port).ping()) {
                        User.kbucket.lastSeen.remove(i);
                        i--;
                    }
                }

            }
        }
    }


}