package grupo3.ssd.blockchain.p2p.Node;

import grupo3.ssd.blockchain.p2p.NetworkStandards;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Identifier implements Comparable<Identifier> {

    @Getter
    private byte[] key;

    public Identifier() {
        key = new byte[NetworkStandards.KEY_SIZE / 8];
        new Random().nextBytes(key);
    }

    public Identifier(byte[] bytes) {
        if (bytes.length != NetworkStandards.KEY_SIZE / 8) {
            throw new IllegalArgumentException("Specified Data need to be " + (NetworkStandards.KEY_SIZE / 8)
                    + " characters long. Data Given: '" + new String(bytes) + "'");
        }
        this.key = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Identifier) {
            Identifier nid = (Identifier) o;
            return this.hashCode() == nid.hashCode();
        }
        return false;
    }

    public Identifier xor(Identifier nid) {
        byte[] result = new byte[NetworkStandards.KEY_SIZE / 8];
        byte[] nidBytes = nid.getKey();

        for (int i = 0; i < NetworkStandards.KEY_SIZE / 8; i++) {
            result[i] = (byte) (this.key[i] ^ nidBytes[i]);
        }

        Identifier resNid = new Identifier(result);

        return resNid;
    }

    public String hexRepresentation() {
        BigInteger bi = new BigInteger(1, this.key);
        return String.format("%0" + (this.key.length << 1) + "X", bi);
    }


    public int getDistance(Identifier t) {
        return NetworkStandards.KEY_SIZE - this.xor(t).getFirstSetBitIndex();
    }

    private int getFirstSetBitIndex() {
        int prefix = 0;

        for (byte b :
                this.key) {
            if (b == 0) {
                prefix += 8;
            } else {
                int count = 0;
                for (int i = 7; i >= 0; i--) {
                    if ((b & (1 << i)) == 0) {
                        count++;
                    } else {
                        break;
                    }
                }

                prefix += count;
                break;
            }
        }
        return prefix;
    }


    @Override
    public int hashCode() {
        return 83 * 7 + Arrays.hashCode(this.key);
    }

    @Override
    public int compareTo(Identifier o) {
        return Integer.compare(this.key.hashCode(), o.getKey().hashCode());
    }

    @Override
    public String toString() {
        return this.hexRepresentation();
    }

}
