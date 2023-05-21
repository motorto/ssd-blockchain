package group19.ssd.blockchain.blockchain;

import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.transactions.Wallet;
import group19.ssd.blockchain.util.Config;
import group19.ssd.blockchain.util.Misc;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hashId; //identificador
    public String hash;
    public String previousHash;
    public ArrayList<Transaction> transactionsList = new ArrayList<>();
    public long timestamp;
    public String publicKey;
    public int nonce = 0;

    //Criar um novo bloco
    public Block(String hashId, ArrayList<Transaction> transactions, String lastBlockHash, Wallet miner) {
        this.hashId = hashId;
        this.transactionsList = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
        this.publicKey = miner.getPublicKey();
    }


    //Criar um bloco a partir de um bloco recebido de outro nó
    public Block(String hashId, String hash, String lastBlockHash, ArrayList<Transaction> transactions, int nonce, long timestamp, String minerPK) {
        this.hashId = hashId;
        this.transactionsList = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = timestamp;
        this.hash = hash;
        this.publicKey = minerPK;
        this.nonce = nonce;
    }

    public int getTransactionListSize() {
        return transactionsList.size();
    }

    public boolean isValid() {
        return this.hashId.equals(calculateHash());
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionsList;
    }

    public String calculateHash() {
        MerkleTree merkleTree = new MerkleTree(transactionsList);
        merkleTree.genRoot();
        return Misc.applyEncryption(this.hashId + this.previousHash + this.timestamp + this.nonce + merkleTree.getRoot());
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

    // calcula a hash repetidamente, incrementando o nonce, até atingir mining dificulty
    public int mineBlock() {
        System.out.println("Numero de transações: " + transactionsList.size());
        String hashPuzzle = new String(new char[Config.MINING_DIFFICULTY]).replace('\0', '0');
        while (!this.hash.substring(0, Config.MINING_DIFFICULTY).equals(hashPuzzle)) {
            this.nonce++;
            this.hash = this.calculateHash();
        }
        System.out.println("Nonce:" + nonce + "\nHash: " + this.hash);
        return nonce;
    }

    public void printBlock() {
        System.out.println();
        System.out.println("hash: " + this.hash);
        System.out.println("hash anterior: " + this.previousHash);
        System.out.println("nonce: " + this.nonce);
        System.out.println("transações:");
        for (Transaction transaction : transactionsList) {
            transaction.printTransaction();
            System.out.println();
        }
    }

    public boolean verify() {
        return calculateHash().equals(hash);
    }
}