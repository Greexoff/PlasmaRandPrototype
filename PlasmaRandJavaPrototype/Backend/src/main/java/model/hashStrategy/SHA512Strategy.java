package model.hashStrategy;

import java.security.MessageDigest;

public class SHA512Strategy implements HashStrategy {
    @Override
    public String getName() { return "SHA-512"; }

    @Override
    public byte[] generateHash(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(data);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}