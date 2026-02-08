package model.numberGeneratorStrategy;

import java.security.DrbgParameters;
import java.security.SecureRandom;
import java.security.Security;

public class Hash_DRBGGenerator implements GeneratorStrategy{
    @Override
    public String getName() {
        return "Hash_DRBG";
    }

    @Override
    public byte[] generateNumber(byte[] seed, int length) throws Exception {
        Security.setProperty("securerandom.drbg.config", "Hash_DRBG,SHA-512,256,pr_and_reseed");
        SecureRandom sr = SecureRandom.getInstance("DRBG",
                DrbgParameters.instantiation(256, DrbgParameters.Capability.NONE, seed));
        byte[] output = new byte[length];
        sr.nextBytes(output);
        return output;
    }
}
