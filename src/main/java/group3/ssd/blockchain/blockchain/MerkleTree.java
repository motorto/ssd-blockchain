package group3.ssd.blockchain.blockchain;


import java.util.ArrayList;

import static Util.UtilMisc.applyEncryption;

public class MerkleTree {

    ArrayList<Transaction> transactionList;
    String root;

    public MerkleTree(ArrayList<Transaction> transactionsList) {
        this.transactionList = transactionsList;
        root = "";
    }

    public void genRoot() {

        ArrayList<String> oldHashList = new ArrayList<>();

        for (Transaction transaction : transactionList)
            oldHashList.add(transaction.calculateHash());

        ArrayList<String> newHashList = getNewHashList(oldHashList);
        while (newHashList.size() != 1) {
            newHashList = getNewHashList(newHashList);
        }

        this.root = newHashList.get(0);
    }

    private ArrayList<String> getNewHashList(ArrayList<String> HashList) {

        ArrayList<String> newHashList = new ArrayList<String>();
        int index = 0;
        while ( HashList.size() > index) {
            // left
            String left = HashList.get(index);
            index++;

            // right
            String right = "";
            if (index != HashList.size()) {
                right = HashList.get(index);
            }

            String sha2HexValue = applyEncryption(left+right);
            newHashList.add(sha2HexValue);
            index++;

        }

        return newHashList;
    }

    public String getRoot() {
        return this.root;
    }

}
