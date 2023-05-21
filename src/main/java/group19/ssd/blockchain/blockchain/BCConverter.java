package group19.ssd.blockchain.blockchain;

import group19.ssd.blockchain.p2p.grpc.BlockChain;
import group19.ssd.blockchain.p2p.grpc.Transaction;
import group19.ssd.blockchain.p2p.grpc.TransactionsList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

// converte objeto definido em gRPC para objetos do tipo blockchain, block, transaction

public class BCConverter {

    public static Blockchain mkBlockChain(BlockChain bc) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Blockchain blockchain = new Blockchain();

        for (group19.ssd.blockchain.p2p.grpc.Block b : bc.getChainList()) {
            blockchain.chain.add(mkBlock(b));
        }
        blockchain.pendingTransactions = mkTransactionList(bc.getPendingTransactions());
        return blockchain;
    }

    //criar block
    public static Block mkBlock(group19.ssd.blockchain.p2p.grpc.Block block) {

        return new Block(block.getHashId(),
                block.getHash(),
                block.getPreviousHash(),
                mkTransactionList(block.getTransactionsList()),
                block.getNonce(),
                block.getTimestamp(),
                block.getPublicKey());
    }


    private static ArrayList<group19.ssd.blockchain.transactions.Transaction> mkTransactionList(TransactionsList transactions) {
        ArrayList<group19.ssd.blockchain.transactions.Transaction> transactionList = new ArrayList<>();
        for (Transaction t : transactions.getTransactionListList()) {
            transactionList.add(mkTransaction(t));
        }
        return transactionList;
    }

    public static group19.ssd.blockchain.transactions.Transaction mkTransaction(Transaction transaction) {

        return new group19.ssd.blockchain.transactions.Transaction(transaction.getHash(),
                transaction.getSenderPK(),
                transaction.getReceiverPK(),
                transaction.getSignature().toByteArray(),
                transaction.getTimestamp(),
                transaction.getAmount(),
                transaction.getMisc());
    }

}
