package group3.ssd.blockchain.p2p.Node;

import lombok.Getter;

import java.math.BigInteger;
import java.util.Arrays;

public class ByteWrapper implements Comparable<ByteWrapper> {

    @Getter
    private final byte[] bytes;

    public ByteWrapper(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object rhs) {

        if (rhs == null) return false;

        if (rhs instanceof byte[]) {
            return Arrays.equals(bytes, (byte[]) rhs);
        } else
            return rhs instanceof ByteWrapper
                    && Arrays.equals(bytes, ((ByteWrapper) rhs).bytes);
    }

    @Override
    public int compareTo(ByteWrapper wrapper) {
        return Arrays.compare(this.bytes, wrapper.bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    public String toHexString() {
        return toHexString(this.bytes);
    }

    private String toHexString(byte[] array) {
        StringBuilder result = new StringBuilder();

        for (byte aByte : array) {

            result.append(String.format("%02x", aByte));
        }

        return result.toString();
    }

    public static BigInteger nodeDistance(byte[] node1, byte[] node2) {
        final var node1ID = new BigInteger(1, node1);
        final var node2ID = new BigInteger(1, node2);

        return node1ID.xor(node2ID);
    }

}
