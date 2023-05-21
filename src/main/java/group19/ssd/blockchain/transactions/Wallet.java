package group19.ssd.blockchain.transactions;

import group19.ssd.blockchain.p2p.KadClient;
import group19.ssd.blockchain.util.Config;
import group19.ssd.blockchain.util.Misc;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Wallet {

    private String privateKey;
    private String publicKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Wallet() {
        genKeyPair();
    }

    public void printWalletBalance() {
        System.out.print("Your account balance is: ");
        System.out.println(getBalance());
    }

    public double getBalance() {
        return KadClient.ledger.getBalance(this.publicKey);
    }

    private void genKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            SecureRandom random = new FixedRand();
            keyGen.initialize(Config.KEY_SIZE, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            this.privateKey = new String(Base64.encode(privateKey.getEncoded()));
            this.publicKey = new String(Base64.encode(publicKey.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[][] sign(String message) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {

        byte[] msgHash = Misc.applyEncryption(this.publicKey.concat(String.valueOf(message))).getBytes();
        Signature signature = Signature.getInstance("RSA");

        System.out.println(privateKey);

        byte[] keyBytes = Base64.decode(privateKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = fact.generatePrivate(keySpec);

        signature.initSign(privateKey);
        signature.update(msgHash);
        byte[] signatureBytes = signature.sign();
        return new byte[][]{msgHash, signatureBytes};

    }

    public String getPublicKey() {
        return publicKey;
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

}
