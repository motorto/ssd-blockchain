package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.util.Config;
import group3.ssd.blockchain.util.Misc;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static group3.ssd.blockchain.util.Misc.applyEncryption;

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
        System.out.print(getBalance());
    }

    public double getBalance() {
        return User.ledger.getBalance(this.publicKey);
    }

    private void genKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            SecureRandom random = Misc.createFixedRandom();
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

        //byte[] msgHash = applyEncryption(this.privateKey.toString().concat(String.valueOf(receiverPubKey)).concat(String.valueOf(message))).getBytes();
        byte[] msgHash = applyEncryption(this.publicKey.concat(String.valueOf(message))).getBytes();
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

}
