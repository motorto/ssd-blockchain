package group3.ssd.blockchain.p2p;

import group3.ssd.blockchain.util.Config;
import group3.ssd.blockchain.util.Misc;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static group3.ssd.blockchain.util.Misc.applyEncryption;

public class Wallet {

    private String id;
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
        System.out.println(" " + Config.COIN_NAME);
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
            PrivateKey privKey = keyPair.getPrivate();
            PublicKey pubKey = keyPair.getPublic();
            this.privateKey = new String(Base64.encode(privKey.getEncoded()));
            this.publicKey = new String(Base64.encode(pubKey.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[][] sign(String message) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {

        //byte[] msgHash = applyEncryption(this.privateKey.toString().concat(String.valueOf(receiverPubKey)).concat(String.valueOf(message))).getBytes();
        byte[] msgHash = applyEncryption(this.publicKey.concat(String.valueOf(message))).getBytes();
        Signature signature = Signature.getInstance("RSA");

        System.out.println(privateKey);

        // extract the private key

        byte[] keyBytes = Base64.decode(privateKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);

        signature.initSign(priv);
        signature.update(msgHash);
        byte[] signatureBytes = signature.sign();
        //System.out.println("Signature:" + Arrays.toString(Base64.encode(signatureBytes)));
        return new byte[][]{msgHash, signatureBytes};

    }

    public boolean verify(byte[] msgHash, byte[] receiverSignature, String receiverPubKeyStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        byte[] pkcs8EncodedBytes = receiverPubKeyStr.getBytes();

        // extract the private key

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey receiverPubKey = kf.generatePublic(keySpec);


        Signature signature = Signature.getInstance("RSA");
        signature.initVerify(receiverPubKey);
        signature.update(msgHash);

        return signature.verify(receiverSignature);
    }

    private String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
