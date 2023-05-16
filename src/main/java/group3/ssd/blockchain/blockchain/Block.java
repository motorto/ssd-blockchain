package group3.ssd.blockchain.blockchain;
import java.util.Date;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {

    public String hashId;
    public String hash;
    public String previousHash;
    public int nonce = 0; 
    public long timestamp;
    public String blockData;
    public ArrayList<Transaction> transactionsList = new ArrayList<>();
    public String merkleRoot;


    //create blocks and calculate the hash 
    public Block(String previousHash, String blockData){
        this.blockData = blockData;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public Block(String hashId,ArrayList<Transaction> transaction,String lastblockHash, String blockData, String merkleRoot, long timestamp, int nonce){
        this.hashId = hashId;//block identifier
        this.timestamp = new Date().getTime();//time which the block was created or mined
        this.previousHash = lastblockHash;//reference to the has of the previous block
        this.blockData= blockData;
        this.transactionsList = transaction;
        this.merkleRoot = merkleRoot;
        this.nonce= nonce;
    }



    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String dataToHash = hashId + hash + previousHash + nonce + timestamp + blockData + transactionsList.toString() + merkleRoot;
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
    }


    public String gethashId() {
            return hashId;
        }

        public void setHashId(String hashId) {
            this.hashId = hashId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getPreviousHash() {
            return previousHash;
        }

        public void setPreviousHash(String previousHash) {
            this.previousHash = previousHash;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getblockData() {
            return blockData;
        }

        public void setBlockData(String blockData) {
            this.blockData = blockData;
        }
    
        public String getMerkleRoot() {
            return merkleRoot;
        }
    
        public void setMerkleRoot(String merkleRoot) {
            this.merkleRoot = merkleRoot;
        }

        public ArrayList<Transaction> getTransactionsList() {
            return transactionsList;
        }
    
        public void setTransactionsList(ArrayList<Transaction> transactionsList) {
            this.transactionsList = transactionsList;
        }

        public int getNonce() {
            return nonce;
        }
    
        public void setNonce(int nonce) {
            this.nonce = nonce;
        }

}