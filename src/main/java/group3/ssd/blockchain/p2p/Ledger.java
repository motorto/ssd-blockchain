package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.blockchain.Block;
import group3.ssd.blockchain.transactions.Transaction;
import group3.ssd.blockchain.util.Config;

import java.util.Hashtable;

public class Ledger {
    Hashtable<String, Integer> userList = new Hashtable<>();
    int minCoin = 15;

    public Ledger() {
        userList.put(KadClient.publicKey, minCoin);
    }

    public Integer getBalance(String pubKey) {
        return userList.get(pubKey);
    }

    public void reset() {
        userList.clear();
        userList.put(KadClient.publicKey, minCoin);
    }

    public void restartLedger() {
        reset();
        for (Block block : KadClient.blockchain.getChain()) {
            updateLedger(block);
        }
    }

    public void updateLedger(Block block) {
        for (Transaction transaction : block.getTransactionList()) {
            int senderAmount, receiverAmount;
            if (userList.get(transaction.senderPK) != null) {
                senderAmount = userList.get(transaction.senderPK);
            } else {
                senderAmount = minCoin;
            }
            if (userList.get(transaction.receiverPK) != null) {
                receiverAmount = userList.get(transaction.receiverPK);
            } else {
                receiverAmount = minCoin;
            }
            userList.put(transaction.senderPK, (senderAmount - transaction.amount));
            userList.put(transaction.receiverPK, (receiverAmount + transaction.amount));
        }

        if (userList.get(block.publicKey) != null) {
            userList.put(block.publicKey, userList.get(block.publicKey) + 1);
        } else {
            userList.put(block.publicKey, minCoin + Config.MINING_REWARD);
        }

    }
}