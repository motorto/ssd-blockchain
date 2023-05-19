package group3.ssd.blockchain.blockchain;

import com.google.protobuf.ByteString;
import group3.ssd.blockchain.p2p.User;
import group3.ssd.blockchain.p2p.grpc.BlockChain;
import group3.ssd.blockchain.p2p.grpc.TransactionsList;

import java.util.ArrayList;

public class GRPCConverter {

    public static BlockChain mkBlockChain(Blockchain bc) {
        ArrayList<group3.ssd.blockchain.p2p.grpc.Block> Bchain = new ArrayList<>();
        for (Block b : bc.chain) {
            Bchain.add(mkBlock(b));
        }

        return BlockChain.newBuilder().addAllChain(Bchain).setPendingTransactions(mkTransactionList(bc.getPendingTransactions())).build();
    }

    //create block
    public static group3.ssd.blockchain.p2p.grpc.Block mkBlock(Block block) {

        return group3.ssd.blockchain.p2p.grpc.Block.newBuilder()
                .setHashId(block.hashId)
                .setHash(block.hash)
                .setPreviousHash(block.previousHash)
                .setTimestamp(block.timestamp)
                .setTransactionsList(mkTransactionList(block.transactionsList))
                .setNonce(block.nonce)
                .setPublicKey(block.publicKey)
                .setNodeId(User.id)
                .build();
    }


    //create transaction List
    public static TransactionsList mkTransactionList(ArrayList<Transaction> transactions) {
        ArrayList<group3.ssd.blockchain.p2p.grpc.Transaction> tlist = new ArrayList<>();
        for (Transaction t : transactions) {
            tlist.add(group3.ssd.blockchain.p2p.grpc.Transaction.newBuilder()
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

    public static group3.ssd.blockchain.p2p.grpc.Transaction mkTransaction(Transaction t) {
        group3.ssd.blockchain.p2p.grpc.Transaction new_t = group3.ssd.blockchain.p2p.grpc.Transaction.newBuilder()
                .setHash(t.hash)
                .setSenderPK(t.senderPK)
                .setReceiverPK(t.receiverPK)
                .setSignature(ByteString.copyFrom(t.signature))
                .setTimestamp(t.timestamp)
                .setAmount(t.amount)
                .setMisc(t.misc)
                .setNodeId(User.id)
                .build();
        return new_t;
    }
}
