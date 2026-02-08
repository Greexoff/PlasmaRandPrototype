package model.hashStrategy;

import java.security.MessageDigest;


public class SHA256Strategy implements HashStrategy {
    @Override
    public String getName() { return "SHA-256"; }
    
    @Override
    public byte[] generateHash(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}