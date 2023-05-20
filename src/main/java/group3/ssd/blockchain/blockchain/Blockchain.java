package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.p2p.User;
import group3.ssd.blockchain.p2p.Wallet;
import group3.ssd.blockchain.util.Config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class Blockchain {
    public static ArrayList<Block> chain = new ArrayList<>(); //blocos em cadeia
    public static ArrayList<Transaction> pendingTransactions = new ArrayList<>(); //transações pendentes

    public Blockchain(Wallet genesisWallet) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        createGenesisBlock(genesisWallet);
    }

    // vazio
    public Blockchain() throws InvalidKeySpecException, NoSuchAlgorithmException {
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
    }

    // criar cópia de outra blockchain
    public Blockchain(Blockchain oldBlockchain) {
        setChain(oldBlockchain.getChain());
        setPendingTransactions(oldBlockchain.getPendingTransactions());

    }

    public void setPendingTransactions(ArrayList<Transaction> oldTransactionArrayList) {
        for (Transaction curTransaction : oldTransactionArrayList) {
            pendingTransactions.add(new Transaction(curTransaction.hash, curTransaction.senderPK, curTransaction.receiverPK, curTransaction.signature, curTransaction.timestamp, curTransaction.amount, curTransaction.misc));
        }
    }

    public ArrayList<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setChain(ArrayList<Block> oldChain) {
        for (Block curBlock : oldChain) {
            ArrayList<Transaction> newTransactionList = new ArrayList<>();
            for (Transaction oldTransaction : curBlock.getTransactionList()) {
                newTransactionList.add(new Transaction(oldTransaction.hash, oldTransaction.senderPK, oldTransaction.receiverPK, oldTransaction.signature, oldTransaction.timestamp, oldTransaction.amount, oldTransaction.misc));
            }
            chain.add(new Block(curBlock.hashId, curBlock.hash, curBlock.previousHash, newTransactionList, curBlock.nonce, curBlock.timestamp, curBlock.publicKey));
        }
    }

    public ArrayList<Block> getChain() {
        return chain;
    }

    public void createGenesisBlock(Wallet genesisWallet) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException {
        Wallet tempWallet = new Wallet();
        this.addTransaction(tempWallet, genesisWallet, 100);
        this.minePendingTransactions(genesisWallet);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }


    public boolean addBlock(Block newBlock) {

        if (!newBlock.verify()) {
            System.out.println("Block add FAILED. The Hash didn't match with the existing contents.");
            return false;
        }
        int transactionSize = newBlock.getTransactionListSize();
        for (int i = 0; i < transactionSize; i++) {
            System.out.println(pendingTransactions.size());
            if (pendingTransactions.size() == 0) {
                return false;
            } else if (!pendingTransactions.get(i).equals(newBlock.transactionsList.get(i))) {
                System.out.println("Block add FAILED. The Transactions in the block didn't match the pending ones.");
                pendingTransactions.get(i).printTransaction();
                newBlock.transactionsList.get(i).printTransaction();
                return false;
            }

        }

        pendingTransactions.subList(0, transactionSize).clear();

        newBlock.previousHash = this.getLatestBlock().hash;

        chain.add(newBlock);
        System.out.println("Block add SUCCESSFULLY.");
        return true;
    }

    public boolean isValid() {
        Block cur, prev;
        String hashTarget = new String(new char[Config.MINING_DIFFICULTY]).replace('\0', '0');

        for (int i = 1; i < chain.size(); i++) {
            cur = chain.get(i);
            prev = chain.get(i - 1);

            //verify hash
            if (!cur.hash.equals(cur.calculateHash())) {
                return false;
            }

            //verify parent
            if (!cur.previousHash.equals(prev.hash)) {
                return false;
            }

            //verify if hash is mined
            if (!cur.hash.substring(0, Config.MINING_DIFFICULTY).equals(hashTarget)) {
                return false;
            }

            //verify transactions
            for (int j = 0; j < cur.getTransactionListSize(); j++) {
                Transaction curTransaction = cur.getTransactionList().get(j);
                if (!curTransaction.hash.equals(curTransaction.calculateHash())) {
                    return false;
                }
                //verify signature

            }
        }
        return true;

    }

    /**/
    public void printBlockChain() {
        System.out.println("blockchain:");
        for (Block currentBlock : chain) {
            currentBlock.printBlock();
        }

        String isChainValid = this.isValid() ? "True" : "False";
        System.out.println("\nThe Chain is valid: " + isChainValid);
    }

    public Transaction addTransaction(Wallet sender, Wallet receiver, int amount) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        Transaction newTransaction = new Transaction(sender.getPublicKey(), receiver.getPublicKey(), amount);
        newTransaction.signTransaction(sender);

        pendingTransactions.add(newTransaction);
        System.out.println("Transaction of " + amount + " signed from " + sender.getPublicKey() + "\nto " + receiver.getPublicKey());

        return newTransaction;
    }

    public int getPendingTransactionsLength() {
        return pendingTransactions.size();
    }

    public Block minePendingTransactions(Wallet miner) {
        int pendingTransactionsLength = getPendingTransactionsLength();
        if (pendingTransactionsLength == 0) {
            System.out.println("There are no transactions pending to mine");
            return null;
        }

        ArrayList<Transaction> newBlockTransactions = new ArrayList<>();
        if (pendingTransactionsLength >= Config.MAX_TRANSACTIONS_PER_BLOCK) {
            for (int i = 0; i < Config.MAX_TRANSACTIONS_PER_BLOCK; i++) {
                newBlockTransactions.add(pendingTransactions.get(i));
            }
        } else {
            System.out.println("Not enough transactions to mine a full block (" + Config.MAX_TRANSACTIONS_PER_BLOCK + "), mining " + pendingTransactionsLength);
            newBlockTransactions.addAll(pendingTransactions);
        }


        Block newBlock;
        if (chain.size() == 0) {
            newBlock = new Block(0 + "", newBlockTransactions, "", User.wallet);
        } else {
            newBlock = new Block(chain.size() + "", newBlockTransactions, this.getLatestBlock().hash, User.wallet);
        }
        newBlock.mineBlock();
        chain.add(newBlock);
        pendingTransactions.subList(0, newBlockTransactions.size()).clear();
        System.out.println("Mining Transaction Success!");
        return newBlock;

    }
}
