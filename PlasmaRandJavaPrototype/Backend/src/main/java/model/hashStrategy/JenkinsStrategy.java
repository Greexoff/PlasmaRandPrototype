package model.hashStrategy;

import java.nio.ByteBuffer;

public class JenkinsStrategy implements HashStrategy {
    @Override
    public String getName() { return "Jenkins"; }

    @Override
    public byte[] generateHash(byte[] data) {
        int hash = 0;
        for (byte b : data) {
            hash += (b & 0xFF);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return ByteBuffer.allocate(4).putInt(hash).array();
    }
}