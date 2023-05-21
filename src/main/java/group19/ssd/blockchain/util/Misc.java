package group19.ssd.blockchain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Misc {

    public static String applyEncryption(String base) {

        try {
            MessageDigest digest = MessageDigest.getInstance(Config.HASH_TYPE);
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
