package Util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import java.math.BigInteger;

public class UtilMisc {
    
    public static final String HASH_TYPE = "SHA-256";
    public static final String COIN_NAME = "UnnamedCoin";
    public static final int MINING_DIFFICULTY = 2;
    public static final int NODE_CREATION_DIFFICULTY = 2;
    public static final int MAX_NODES_BUCKET = 20;
    public static final int ALPHA = 3;

    public static final int KEY_SIZE = 1024;
    public static final int MAX_TRANSACTIONS_PER_BLOCK = 8;
    public static final double MIN_NODE_REPUTATION=0.7;
    public static final int MINING_REWARD=1;
    public static final String knownNode = "00161ec8fc86b769fea44a8216c9ce60cedfea5e8040e3ca3a4cd34ae17231d3";
  
    
    public static String applyEncryption(String base) {

        //System.out.println(base);

        try{
            MessageDigest digest = MessageDigest.getInstance(UtilMisc.HASH_TYPE);
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static SecureRandom createFixedRandom() {
        return new FixedRand();
    }

    private static class FixedRand extends SecureRandom {

        MessageDigest sha;
        byte[] state;

        FixedRand() {
            try
            {
                this.sha = MessageDigest.getInstance(UtilMisc.HASH_TYPE);
                this.state = sha.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("Couldn't find "+UtilMisc.HASH_TYPE);
            }
        }
    }

    public static PrivateKey getPrivateKeyFromString(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        return keyFactory.generatePrivate(keySpecPKCS8);
    }

    public static PublicKey getPublicKeyFromString(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] byteKey = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(X509publicKey);
    }


    public static String stringToBitString(String str){
        return new BigInteger(str,16).toString(2);
    }

}