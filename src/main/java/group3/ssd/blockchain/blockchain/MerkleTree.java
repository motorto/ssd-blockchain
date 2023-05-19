package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.util.Config;

import java.util.ArrayList;

import static group3.ssd.blockchain.util.Misc.applyEncryption;

public class MerkleTree {
    ArrayList<Transaction> transactionsList; //A list of transaction
    String root; //Merkle Tree Root

    public MerkleTree(ArrayList<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
        root = "";
    }

    public void genRoot() {

        if (transactionsList.size() != Config.MAX_TRANSACTIONS_PER_BLOCK) return;

        ArrayList<String> oldHashList = new ArrayList<>();

        for (Transaction transaction : transactionsList)
            oldHashList.add(transaction.calculateHash());

        ArrayList<String> newHashList = getNewHashList(oldHashList);
        while (newHashList.size() != 1) {
            newHashList = getNewHashList(newHashList);
        }

        this.root = newHashList.get(0);
    }

    private ArrayList<String> getNewHashList(ArrayList<String> tempHashList) {

        ArrayList<String> newHashList = new ArrayList<String>();
        int index = 0;
        while (index < tempHashList.size()) {
            // left
            String left = tempHashList.get(index);
            index++;

            // right
            String right = "";
            if (index != tempHashList.size()) {
                right = tempHashList.get(index);
            }

            String sha2HexValue = applyEncryption(left + right);
            newHashList.add(sha2HexValue);
            index++;

        }

        return newHashList;
    }

    public String getRoot() {
        return this.root;
    }


}
