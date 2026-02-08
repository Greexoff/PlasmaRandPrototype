package model.hashStrategy;

import java.nio.ByteBuffer;

public class DJB2Strategy implements HashStrategy {
    @Override
    public String getName() { return "DJB2"; }
    
    @Override
    public byte[] generateHash(byte[] data) {
        long hash = 5381;
        for (byte b : data) {
            hash = ((hash << 5) + hash) ^ (b & 0xFF);
        }
        return ByteBuffer.allocate(4).putInt((int)hash).array();
    }
}