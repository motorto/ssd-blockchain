package group3.ssd.blockchain.blockchain;
 //T+S

import java.security.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import Util.UtilMisc;


public class Transaction {
    public String hashBlock;
    public String sender; //seller
    public String recipient;//buyer
    public int amount;//value 
    public byte[] signature;//to ensure that the transaction has been authorizedby the owner
    public long timestamp;
    public String misc="";

    public Transaction(String sender, String recipient, int amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        validateTransaction();
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = sender + recipient + Double.toString(amount);
        try {
            Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
            signatureAlgorithm.initSign(privateKey);
            signatureAlgorithm.update(data.getBytes());
            this.signature = signatureAlgorithm.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
    }

    public boolean verifySignature(PublicKey publicKey) {
        String data = sender + recipient + Double.toString(amount);
        try {
            Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
            signatureAlgorithm.initVerify(publicKey);
            signatureAlgorithm.update(data.getBytes());
            return signatureAlgorithm.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void validateTransaction(){
        this.timestamp = new Date().getTime();
        this.hashBlock= calculateHash();
    }

    public String calculateHash(){
        return UtilMisc.applyEncryption(this.sender+this.recipient+this.timestamp+this.amount+this.misc);
    }

    public boolean isSigned(){

        return this.signature != null;
    }

    public boolean signTransaction(Wallet sign) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        if(!this.hashBlock.equals(this.calculateHash())) {
            System.out.println("Error: the transaction was tampered!");
            return false;
        }
        this.signature = sign.signMessage(hashBlock)[1];
        return true;
    }


    public static void printTabs(int numTabs){
        for(int i=0;i<numTabs;i++)
            System.out.print("\t");
    }

    public void showTransactions (int numTabs){
        printTabs(numTabs);
        System.out.println("hash:"+this.hashBlock);
        printTabs(numTabs);
        System.out.println("Sender:"+this.sender);
        printTabs(numTabs);
        System.out.println("Recipient:"+this.recipient);
        printTabs(numTabs);
        System.out.println("Amount:"+this.amount);
        printTabs(numTabs);
        System.out.println("Verifiy if is signed:"+this.isSigned());
        printTabs(numTabs);
        System.out.println("time:"+this.timestamp);
        printTabs(numTabs);
        System.out.println("information:"+((this.misc==null)?"''":this.misc));
    }

     //Equals and Hash Code
    
     public boolean equals(Transaction transaction){

        if(this.hashBlock.equals(transaction.hashBlock) && this.sender.equals(transaction.sender) && this.recipient.equals(transaction.recipient) && Arrays.equals(this.signature, transaction.signature) && this.amount==transaction.amount && this.timestamp==transaction.timestamp && this.misc.equals(transaction.misc)){
            return true;
        }
        else{
            return false;
        }
    }
 
}
