package model.numberGeneratorStrategy;

public interface GeneratorStrategy {
    String getName();
    byte[] generateNumber(byte[] seed, int length) throws Exception;

}
