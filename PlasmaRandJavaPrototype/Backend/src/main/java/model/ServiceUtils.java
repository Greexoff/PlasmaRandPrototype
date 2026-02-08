package model;

import org.apache.tika.Tika;

import java.nio.ByteBuffer;

public class ServiceUtils {
    public long convertBytesToLong(byte[] hash) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        if (hash.length < 8) {
            int additionalZeros = Long.BYTES - hash.length;
            for (int i = 0; i < additionalZeros; i++) {
                buffer.put((byte) 0);
            }
            buffer.put(hash);
        } else {
            buffer.put(hash, 0, Long.BYTES);
        }
        buffer.flip();
        return buffer.getLong();
    }

    public int getRandomFrame(int totalFramesInVideo)
    {
        return (int) (Math.random()* totalFramesInVideo);
    }

    public boolean isFileVideo(String filePath) {
        Tika tika = new Tika();
        String mimeType = tika.detect(filePath);
        return (mimeType.startsWith("video/"));
    }

}
