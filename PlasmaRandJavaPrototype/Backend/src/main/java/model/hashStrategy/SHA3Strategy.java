package model.hashStrategy;

import java.security.MessageDigest;

public class SHA3Strategy implements HashStrategy {
    @Override
    public String getName() { return "SHA3"; }

    @Override
    public byte[] generateHash(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            return digest.digest(data);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}