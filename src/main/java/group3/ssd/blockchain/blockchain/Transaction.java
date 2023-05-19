package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.p2p.Wallet;
import group3.ssd.blockchain.util.Misc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;

import static group3.ssd.blockchain.util.Misc.printTabs;

public class Transaction {
    public String hash;
    public String senderPK;
    public String receiverPK;
    public byte[] signature;
    public long timestamp;
    public int amount;
    public String misc = "";

    //When creating a transaction
    public Transaction(String senderPK, String receiverPK, int amount) {
        this.senderPK = senderPK;
        this.receiverPK = receiverPK;
        this.amount = amount;
        //Timestamp Server
        validateTransaction();
    }

    //When adding a transaction from outside
    public Transaction(String hash, String senderPK, String receiverPK, byte[] signature, long timestamp, int amount, String misc) {
        this.hash = hash;
        this.senderPK = senderPK;
        this.receiverPK = receiverPK;
        this.signature = signature;
        this.amount = amount;
        this.timestamp = timestamp;
        this.misc = misc;
    }

    public void validateTransaction() {
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Misc.applyEncryption(this.senderPK + this.receiverPK + this.timestamp + this.amount + this.misc);
    }

    public boolean isSigned() {
        return this.signature != null;
    }

    public void printTransaction(int numTabs) {
        printTabs(numTabs);
        System.out.println("hash: " + this.hash);
        printTabs(numTabs);
        System.out.println("from: " + this.senderPK);
        printTabs(numTabs);
        System.out.println("to: " + this.receiverPK);
        printTabs(numTabs);
        System.out.println("amount: " + this.amount);
        printTabs(numTabs);
        System.out.println("isSigned: " + this.isSigned());
        printTabs(numTabs);
        System.out.println("at: " + this.timestamp);
        printTabs(numTabs);
        System.out.println("info: " + ((this.misc == null) ? "''" : this.misc));
    }

    public boolean signTransaction(Wallet signer) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        if (!this.hash.equals(this.calculateHash())) {
            System.out.println("Transaction Tampered Error");
            return false;
        }
        System.out.println(signer.getPublicKey());
        System.out.println(this.senderPK);

        //        else if(!signer.getPublicKey().equals(senderPK)) {
//            System.out.println("Transaction attempt to be signed from another wallet");
//            return false;
//        }
        this.signature = signer.sign(hash)[1];
        return true;
    }

    public boolean equals(Transaction transaction) {

        return (this.hash.equals(transaction.hash) &&
                this.senderPK.equals(transaction.senderPK) &&
                this.receiverPK.equals(transaction.receiverPK) &&
                Arrays.equals(this.signature, transaction.signature) &&
                this.amount == transaction.amount &&
                this.timestamp == transaction.timestamp &&
                this.misc.equals(transaction.misc)
        );
    }
}
