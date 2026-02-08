package model.hashStrategy;

public interface HashStrategy {
    String getName();
    byte[] generateHash(byte[] data);

    default byte[] generateHash(byte[]  currentFrame, byte[] previousFrame) {
        int length = Math.min(currentFrame.length, previousFrame.length);

        byte[] frameDifference = new byte[length];

        for (int i = 0; i < length; i++) {
            frameDifference[i] = (byte) (currentFrame[i] ^ previousFrame[i]);
        }

        return generateHash(frameDifference);
    }
}