package group19.ssd.blockchain.p2p;

import group19.ssd.blockchain.blockchain.Block;
import group19.ssd.blockchain.blockchain.Blockchain;
import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.transactions.Wallet;
import group19.ssd.blockchain.util.Config;
import group19.ssd.blockchain.util.Misc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class KadClient {

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
    private static boolean alreadyRunningMineBlockThread = false;
    public static KeepAliveThread keepAliveThread = new KeepAliveThread();

    public String getHash() {
        return id;
    }

    public KadClient() {

    }

    public void setup(int node_port, String node_ip) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KadClient.proof = 0;
        KadClient.port = node_port;
        KadClient.ip = node_ip;
        KadClient.publicKey = KadClient.wallet.getPublicKey();
        KadClient.ledger = new Ledger();
        try {
            if (Config.knownNode.equals("")) {
                KadClient.blockchain = new Blockchain(KadClient.wallet);
            } else {
                KadClient.blockchain = new Blockchain();
            }
        } catch (SignatureException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        id = obtainNodeId();


        System.out.println(Config.knownNode);

        if (Config.knownNode.equals("")) {
            Config.knownNode = KadClient.id;
        } else {
            KadClient.kbucket.addNode(new Node(Config.knownNode, node_ip, 8080));
            Kademlia.pingNode(new Node(Config.knownNode, node_ip, 8080));
        }

    }

    public static void startMining() {
        if (!alreadyRunningMineBlockThread) {
            alreadyRunningMineBlockThread = true;
            KadClient.mineBlockThread.start();
        } else {
            System.out.println("Already Mining");
        }
    }

    public static void startPinging() {
        KadClient.keepAliveThread.start();
    }

    private static String obtainNodeId() {
        return calculateHash(KadClient.proof, KadClient.ip, KadClient.port, KadClient.publicKey);
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
                    KadClient.kbucket.getNode(node.id).addSuccessfulInteraction();
                else {
                    KadClient.kbucket.getNode(node.id).addUnsuccessfulInteraction();
                }
            }
        }
    }

    public static void shareBlock(Block block, String sender) {
        ArrayList<Node> destinations = KadClient.kbucket.getCloneNodesList();

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

        ArrayList<Node> destinations = KadClient.kbucket.getCloneNodesList();

        try {
            transaction.signTransaction(KadClient.wallet);
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

            while (true) {
                try {

                    TimeUnit.MICROSECONDS.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (KadClient.blockchain.getPendingTransactions().size() >= 1) {

                    String blockHashId = Misc.applyEncryption(String.valueOf(new Date().getTime()));
                    System.out.println("mining: " + blockHashId);

                    KadClient.blockchain.minePendingTransactions(KadClient.wallet);
                    KadClient.ledger.updateLedger(KadClient.blockchain.getLatestBlock());

                    KadClient.shareBlock(KadClient.blockchain.getLatestBlock(), KadClient.id);
                    KadClient.blockchain.printBlockChain();
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

                KadClient.evaluateTrust();

                for (int i = 0; i < KadClient.kbucket.seenSize(); i++) {
                    if (!new PeerOperations(KadClient.kbucket.lastSeen.get(i).ip, KadClient.kbucket.lastSeen.get(i).port).ping()) {
                        KadClient.kbucket.lastSeen.remove(i);
                        i--;
                    }
                }

            }
        }
    }


}