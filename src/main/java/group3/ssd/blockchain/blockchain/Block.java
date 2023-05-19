package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.p2p.Wallet;
import group3.ssd.blockchain.util.Config;

import java.util.ArrayList;
import java.util.Date;

import static group3.ssd.blockchain.util.Misc.applyEncryption;
import static group3.ssd.blockchain.util.Misc.printTabs;

public class Block {
    public String hashId;
    public String hash;
    public String previousHash;
    public ArrayList<Transaction> transactionsList = new ArrayList<>();
    public int nonce = 0;
    public long timestamp;
    public String publicKey;

    //When creating blocks
    public Block(String hashId, ArrayList<Transaction> transactions, String lastBlockHash, Wallet miner) {
        this.hashId = hashId;
        this.hash = this.calculateHash();
        this.transactionsList = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = new Date().getTime();
        //this.nonce = mineBlock();
        this.hash = calculateHash();
        this.publicKey = miner.getPublicKey();
    }


    //When receiving blocks from other nodes
    public Block(String hashId, String hash, String lastBlockHash, ArrayList<Transaction> transactions, int nonce, long timestamp, String minerPK) {
        this.hashId = hashId;
        this.transactionsList = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.hash = hash;
        this.publicKey = minerPK;
    }

    public boolean isValid() {
        return this.hashId.equals(calculateHash());
    }

    public int getTransactionListSize() {
        return transactionsList.size();
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionsList;
    }

    /**/
    public String calculateHash() {
        MerkleTree merkleTree = new MerkleTree(transactionsList);
        merkleTree.genRoot();
        return applyEncryption(this.hashId + this.previousHash + this.timestamp + this.nonce + merkleTree.getRoot());
    }

    @Override
    public String toString() {
        return "Block{" +
                "hashId='" + hashId + '\'' +
                ", hash='" + hash + '\'' +
                ", transactionsList=" + transactionsList +
                ", nonce=" + nonce +
                ", timestamp=" + timestamp +
                ", publicKey=" + publicKey +
                '}';
    }

    public int mineBlock() {
        System.out.println("Mining " + transactionsList.size() + " transactions...");
        String hashPuzzle = new String(new char[Config.MINING_DIFFICULTY]).replace('\0', '0');
        while (!this.hash.substring(0, Config.MINING_DIFFICULTY).equals(hashPuzzle)) {
            this.nonce++;
            this.hash = this.calculateHash();
            //System.out.println(this.hash);
        }
        System.out.println("Block Mined! Nonce to Solve proof of work:" + nonce + "\nHash Calculated: " + this.hash);
        return nonce;
    }

    public void printBlock(int numTabs) {
        printTabs(numTabs);
        System.out.println("hash: " + this.hash);
        printTabs(numTabs);
        System.out.println("prev: " + this.previousHash);
        printTabs(numTabs);
        System.out.println("nonce: " + this.nonce);
        printTabs(numTabs);
        System.out.println("transactions:");
        for (Transaction transaction : transactionsList) {
            transaction.printTransaction(numTabs + 1);
            System.out.println();
        }
    }

    public boolean verify() {
        return calculateHash().equals(hash);
    }

    public String getFirstTransactionHash() {
        return transactionsList.get(0).hash;
    }
}