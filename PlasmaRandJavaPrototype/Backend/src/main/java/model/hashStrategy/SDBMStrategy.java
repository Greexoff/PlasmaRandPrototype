package model.hashStrategy;

import java.nio.ByteBuffer;

public class SDBMStrategy implements HashStrategy {
    @Override
    public String getName() { return "SDBM"; }

    @Override
    public byte[] generateHash(byte[] data) {
        int hash = 0;
        for (byte b : data) {
            hash = (b & 0xFF) + (hash << 6) + (hash << 16) - hash;
        }
        return ByteBuffer.allocate(4).putInt(hash).array();
    }
}