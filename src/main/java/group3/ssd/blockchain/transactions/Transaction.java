package group3.ssd.blockchain.transactions;

import group3.ssd.blockchain.util.Misc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;

public class Transaction {
    public String hash;
    public String senderPK;
    public String receiverPK;
    public byte[] signature;
    public long timestamp;
    public int amount; //quantidade de moedas transferidas
    public String misc = ""; //info adicional

    //criar transação
    public Transaction(String senderPK, String receiverPK, int amount) {
        this.senderPK = senderPK;
        this.receiverPK = receiverPK;
        this.amount = amount;
        validateTransaction();
    }

    public void validateTransaction() {
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    //adicionar transação a partir de fonte externa (recebe)
    public Transaction(String hash, String senderPK, String receiverPK, byte[] signature, long timestamp, int amount, String misc) {
        this.hash = hash;
        this.senderPK = senderPK;
        this.receiverPK = receiverPK;
        this.signature = signature;
        this.amount = amount;
        this.timestamp = timestamp;
        this.misc = misc;
    }

    public String calculateHash() {
        return Misc.applyEncryption(this.senderPK + this.receiverPK + this.timestamp + this.amount + this.misc);
    }

    public boolean isSigned() {
        return this.signature != null;
    }

    public void printTransaction() {
        System.out.println();
        System.out.println("hash: " + this.hash);
        System.out.println("de: " + this.senderPK);
        System.out.println("para: " + this.receiverPK);
        System.out.println("valor: " + this.amount);
        if (this.isSigned()) {
            System.out.print("Está assinado");
        } else {
            System.out.print("Não está assinado");
        }
        System.out.println("tempo: " + this.timestamp);
        System.out.println("info: " + ((this.misc == null) ? "''" : this.misc));
        System.out.println();
    }

    //assina a transação com uma wallet
    public boolean signTransaction(Wallet signer) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        if (!this.hash.equals(this.calculateHash())) { //garantir que transação não foi modificada
            System.out.println("ERRO");
            return false;
        }
        System.out.println(signer.getPublicKey());
        System.out.println(this.senderPK);

        this.signature = signer.sign(hash)[1];
        return true;
    }

    //verifica igualdade das transações
    public boolean equals(Transaction transaction) {

        if (this.hash.equals(transaction.hash) &&
                this.senderPK.equals(transaction.senderPK) &&
                this.receiverPK.equals(transaction.receiverPK) &&
                Arrays.equals(this.signature, transaction.signature) &&
                this.amount == transaction.amount &&
                this.timestamp == transaction.timestamp &&
                this.misc.equals(transaction.misc)) {
            return true;
        } else {
            return false;
        }
    }
}
