package model.hashStrategy;

import java.nio.ByteBuffer;

public class KnuthStrategy implements HashStrategy {
    @Override
    public String getName() { return "Knuth"; }

    @Override
    public byte[] generateHash(byte[] data) {
        long hash = 0;
        long constant = 0x9E3779B97F4A7C15L;
        for (byte b : data) {
            hash ^= (b & 0xFF);
            hash *= constant;
        }
        return ByteBuffer.allocate(8).putLong(hash).array();
    }
}