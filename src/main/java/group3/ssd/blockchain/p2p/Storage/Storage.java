package group3.ssd.blockchain.p2p.Storage;

import group3.ssd.blockchain.p2p.Node.ByteWrapper;
import group3.ssd.blockchain.p2p.Node.Node;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Storage {

    private final Node localNode;
    @Getter
    private final Map<ByteWrapper, Metadata> storedValues, publishedValues;

    public Storage(Node localNode) {
        this.localNode = localNode;
        this.storedValues = new ConcurrentSkipListMap<>();
        this.publishedValues = new ConcurrentSkipListMap<>();
    }

    public void insertValue(byte[] originId, byte[] contentId, byte[] value) {
        ByteWrapper wrappedIdentifier = new ByteWrapper(contentId);
        if (Arrays.equals(originId, this.localNode.getNodeId())) {
            this.publishedValues.put(wrappedIdentifier, new Metadata(contentId, value, this.localNode.getNodeId()));
        }

        if (this.storedValues.containsKey(wrappedIdentifier)) {
            Metadata storedKey = this.storedValues.get(wrappedIdentifier);
            storedKey.setValue(value);
        } else {
            Metadata storedKey = new Metadata(contentId, value, originId);
            this.storedValues.put(wrappedIdentifier, storedKey);
        }
    }

    public void deleteValue(byte[] ID) {
        var wrappedIdentifier = new ByteWrapper(ID);

        this.publishedValues.remove(wrappedIdentifier);

        this.storedValues.remove(wrappedIdentifier);
    }

    public byte[] loadValue(byte[] contentId) {
        return this.storedValues.get(new ByteWrapper(contentId)).getValue();
    }
}
