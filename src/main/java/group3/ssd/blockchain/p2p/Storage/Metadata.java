package group3.ssd.blockchain.p2p.Storage;


import lombok.Getter;

import java.util.Arrays;

public class Metadata {

    @Getter
    private final byte[] key;
    private final byte[] ownerNodeID;

    @Getter
    private byte[] value;

    private long lastRepublished;

    private long lastUpdated;

    public Metadata(byte[] key, byte[] value, byte[] ownerNodeID) {

        this.key = key;
        this.ownerNodeID = ownerNodeID;
        this.value = value;

        this.lastRepublished = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
    }

    public void setValue(byte[] value) {
        this.value = value;

        this.registerUpdate();
    }

    public void registerUpdate() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public void registerRepublished() {
        this.lastRepublished = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata that = (Metadata) o;
        return Arrays.equals(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getKey());
    }

}
