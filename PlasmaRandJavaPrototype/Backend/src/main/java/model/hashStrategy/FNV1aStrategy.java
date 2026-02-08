package model.hashStrategy;

import java.nio.ByteBuffer;

public class FNV1aStrategy implements HashStrategy {
    @Override
    public String getName() { return "FNV-1a"; }
    
    @Override
    public byte[] generateHash(byte[] data) {
        long hash = 0xcbf29ce484222325L;
        for (byte b : data) {
            hash ^= (b & 0xFF);
            hash *= 1099511628211L;
        }
        return ByteBuffer.allocate(8).putLong(hash).array();
    }
}