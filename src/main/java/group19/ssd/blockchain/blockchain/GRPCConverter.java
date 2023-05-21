package group19.ssd.blockchain.blockchain;

import com.google.protobuf.ByteString;
import group19.ssd.blockchain.p2p.KadClient;
import group19.ssd.blockchain.p2p.grpc.BlockChain;
import group19.ssd.blockchain.p2p.grpc.Transaction;
import group19.ssd.blockchain.p2p.grpc.TransactionsList;

import java.util.ArrayList;

public class GRPCConverter {

    //converter blockchain
    public static BlockChain mkBlockChain(Blockchain bc) {
        ArrayList<group19.ssd.blockchain.p2p.grpc.Block> Bchain = new ArrayList<>();
        for (Block b : bc.chain) {
            Bchain.add(mkBlock(b));
        }

        return BlockChain.newBuilder().addAllChain(Bchain).setPendingTransactions(mkTransactionList(bc.getPendingTransactions())).build();
    }

    //converter bloco
    public static group19.ssd.blockchain.p2p.grpc.Block mkBlock(Block block) {

        return group19.ssd.blockchain.p2p.grpc.Block.newBuilder()
                .setHashId(block.hashId)
                .setHash(block.hash)
                .setPreviousHash(block.previousHash)
                .setTimestamp(block.timestamp)
                .setTransactionsList(mkTransactionList(block.transactionsList))
                .setNonce(block.nonce)
                .setPublicKey(block.publicKey)
                .setNodeId(KadClient.id)
                .build();
    }

    //converter lista de transações
    public static TransactionsList mkTransactionList(ArrayList<group19.ssd.blockchain.transactions.Transaction> transactions) {
        ArrayList<Transaction> tlist = new ArrayList<>();
        for (group19.ssd.blockchain.transactions.Transaction t : transactions) {
            tlist.add(Transaction.newBuilder()
                    .setHash(t.hash)
                    .setSenderPK(t.senderPK)
                    .setReceiverPK(t.receiverPK)
                    .setSignature(ByteString.copyFrom(t.signature))
                    .setTimestamp(t.timestamp)
                    .setAmount(t.amount)
                    .setMisc(t.misc).build());
        }
        return TransactionsList.newBuilder().addAllTransactionList(tlist).build();
    }

    // converter transação
    public static Transaction mkTransaction(group19.ssd.blockchain.transactions.Transaction t) {
        Transaction new_t = Transaction.newBuilder()
                .setHash(t.hash)
                .setSenderPK(t.senderPK)
                .setReceiverPK(t.receiverPK)
                .setSignature(ByteString.copyFrom(t.signature))
                .setTimestamp(t.timestamp)
                .setAmount(t.amount)
                .setMisc(t.misc)
                .setNodeId(KadClient.id)
                .build();
        return new_t;
    }
}
