package group3.ssd.blockchain.blockchain;
import java.util.Date;

import Util.UtilMisc;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static Util.UtilMisc.applyEncryption;

public class Block {

    public String hashId;
    public String hashBlock;
    public String previousHash;
    public int nonce = 0; 
    public long timestamp;
    public String blockData;
    public ArrayList<Transaction> transactionList = new ArrayList<>();
    public String merkleRoot;
    public String publicKey;


    //create blocks and calculate the hash 
    public Block(String hashId,String previousHash,ArrayList<Transaction> transaction,String lastblockHash,Wallet minerPublicKey ){
        this.hashId=hashId;
        this.previousHash = previousHash;
        this.transactionList=transaction;
        this.publicKey = minerPublicKey.getPublicKey();
        this.timestamp = new Date().getTime();
        this.hashBlock = calculateHash();
    }

    public Block(String hashId,String hashBlock, String previousHash,ArrayList<Transaction> transaction,long timestamp, int nonce,String minerPublicKey){
        this.hashId = hashId;//block identifier
        this.timestamp = new Date().getTime();//time which the block was created or mined
        this.previousHash = previousHash;//reference to the has of the previous block
        this.transactionList=transaction;
        this.nonce= nonce;
        this.publicKey = minerPublicKey;
    }
    
    /*public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String dataToHash = hashId + hashBlock + previousHash + nonce + timestamp + blockData + transactionsList.toString() + merkleRoot;
            byte[] encodedHash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    //calculate the actual hash
    public String calculateHash(){

        MerkleTree merkleTree = new MerkleTree(transactionList);
        merkleTree.genRoot();
        return applyEncryption(this.hashId + this.previousHash + this.timestamp + this.nonce + merkleTree.getRoot());
    }

    
    //Mining the block
    public int minerBlock(){
        System.out.println("Mining "+ transactionList.size() +" transactions...");
        String hashPuzzle=new String(new char[UtilMisc.MINING_DIFFICULTY]).replace('\0','0');
        while(!this.hashBlock.substring(0,UtilMisc.MINING_DIFFICULTY).equals(hashPuzzle)){
            this.nonce++;
            this.hashBlock = this.calculateHash();
            //System.out.println(this.hash);
        }
        System.out.println("Block Mined! Nonce to Solve proof of work:"+nonce+"\nHash Calculated: " + this.hashBlock);
        return nonce;
    }

    public boolean isValid(){
        return this.hashId.equals(calculateHash());
    }

    public ArrayList<Transaction> getTransaction() {
        
        return transactionList;
    
    }

    public int getTransactionListSIze(){

        return transactionList.size();
    }



}