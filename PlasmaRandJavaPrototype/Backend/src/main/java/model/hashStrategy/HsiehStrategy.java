package model.hashStrategy;

import java.nio.ByteBuffer;

public class HsiehStrategy implements HashStrategy {
    @Override
    public String getName() { return "Hsieh"; }

    @Override
    public byte[] generateHash(byte[] data) {
        long hash = 0x0123456789ABCDEFL;
        for (byte b : data) {
            int val = (b & 0xFF);
            hash += val;
            hash = Long.rotateLeft(hash, 3);
            hash ^= val;
        }
        return ByteBuffer.allocate(8).putLong(hash).array();
    }
}