package model.hashStrategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Strategy implements HashStrategy {

    @Override
    public String getName() {
        return "MD5";
    }

    @Override
    public byte[] generateHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            return md.digest(data);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Java nie obsługuje MD5", e);
        }
    }


}