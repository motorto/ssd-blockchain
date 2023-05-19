package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.p2p.grpc.BlockChain;
import group3.ssd.blockchain.p2p.grpc.TransactionsList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class BCConverter {

    public static Blockchain mkBlockChain(BlockChain bc) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Blockchain blockchain = new Blockchain();

        for (group3.ssd.blockchain.p2p.grpc.Block b : bc.getChainList()) {
            blockchain.chain.add(mkBlock(b));
        }
        blockchain.pendingTransactions = mkTransactionList(bc.getPendingTransactions());
        return blockchain;
    }

    //create block
    public static Block mkBlock(group3.ssd.blockchain.p2p.grpc.Block block) {

        return new Block(block.getHashId(),
                block.getHash(),
                block.getPreviousHash(),
                mkTransactionList(block.getTransactionsList()),
                block.getNonce(),
                block.getTimestamp(),
                block.getPublicKey());
    }


    private static ArrayList<Transaction> mkTransactionList(TransactionsList transactions) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        for (group3.ssd.blockchain.p2p.grpc.Transaction t : transactions.getTransactionListList()) {
            transactionList.add(mkTransaction(t));
        }
        return transactionList;
    }

    public static Transaction mkTransaction(group3.ssd.blockchain.p2p.grpc.Transaction transaction) {

        return new Transaction(transaction.getHash(),
                transaction.getSenderPK(),
                transaction.getReceiverPK(),
                transaction.getSignature().toByteArray(),
                transaction.getTimestamp(),
                transaction.getAmount(),
                transaction.getMisc());
    }

}
