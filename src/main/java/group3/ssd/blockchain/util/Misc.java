package group3.ssd.blockchain.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Misc {

    public static String applyEncryption(String base) {

        try {
            MessageDigest digest = MessageDigest.getInstance(Config.HASH_TYPE);
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
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
            try {
                this.sha = MessageDigest.getInstance(Config.HASH_TYPE);
                this.state = sha.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Couldn't find " + Config.HASH_TYPE);
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

    public static String stringToBitString(String str) {
        return new BigInteger(str, 16).toString(2);
    }

}
