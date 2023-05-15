package group3.ssd.blockchain.p2p.Routing;

import com.google.common.math.BigIntegerMath;
import group3.ssd.blockchain.p2p.NetworkStandards;
import group3.ssd.blockchain.p2p.Node.NodeTriplet;
import lombok.Getter;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

public class kBuckets {

    @Getter
    private final ArrayList<ConcurrentLinkedDeque<NodeTriplet>> kBuckets;

    @Getter
    private final ArrayList<Long> lastKBucketUpdate;

    @Getter
    private final ArrayList<ReentrantLock> kBucketWriteLocks;

    public kBuckets() {
        this.kBuckets = new ArrayList<>(NetworkStandards.KEY_SIZE);
        this.lastKBucketUpdate = new ArrayList<>(NetworkStandards.KEY_SIZE);
        this.kBucketWriteLocks = new ArrayList<>(NetworkStandards.KEY_SIZE);

        for (int i = 0; i < NetworkStandards.KEY_SIZE; i++) {
            kBuckets.add(new ConcurrentLinkedDeque<>());
            kBucketWriteLocks.add(new ReentrantLock());
            lastKBucketUpdate.add(System.currentTimeMillis());
        }
    }

    public static BigInteger nodeDistance(BigInteger node1, BigInteger node2) {
        return node1.xor(node2);
    }

    public static int getKBucketFor(byte[] node1, byte[] node2) {
        final BigInteger nodeDistance = nodeDistance(new BigInteger(1, node1), new BigInteger(1, node2));

        if (nodeDistance.equals(BigInteger.ZERO)) return 0;

        return BigIntegerMath.log2(nodeDistance, RoundingMode.DOWN);
    }
}
