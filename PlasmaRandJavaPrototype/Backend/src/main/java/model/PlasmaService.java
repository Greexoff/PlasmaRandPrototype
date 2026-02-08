package model;

import model.hashStrategy.*;
import model.numberGeneratorStrategy.*;
import model.repository.ResultRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PlasmaService {
    private final Map<String, HashStrategy> strategies = new HashMap<>();
    private final Map<String, GeneratorStrategy> generators = new HashMap<>();
    private final ServiceUtils serviceUtils = new ServiceUtils();
    private final ResultRepository repository;

    public PlasmaService(ResultRepository repository) {
        this.repository = repository;
        Stream.of(new DJB2Strategy(), new FNV1aStrategy(), new SDBMStrategy(),
                new JenkinsStrategy(), new KnuthStrategy(), new HsiehStrategy(),
                new SHA256Strategy(), new SHA3Strategy(), new MD5Strategy(),
                new SHA512Strategy()).forEach(strategy -> this.strategies.put(strategy.getName(),strategy));
        Stream.of(new CTR_DRBGGenerator(), new Hash_DRBGGenerator(), new HMAC_DRBGGenerator())
                .forEach(generator -> this.generators.put(generator.getName(),generator));

    }

    public String[] getAvailableAlgorithms() {
        return strategies.keySet().toArray(new String[0]);
    }
    public String[] getAvailableGenerators()
    {
        return generators.keySet().toArray(new String[0]);
    }

    public record ProcessedFrame(String algorithmName, String generatorName, byte[] hash, byte[] generatedResult, long time) {}

    private void validateStrategy(Object... objects) throws Exception
    {
        for(Object object : objects)
        {
            if(object == null)
            {
                throw new Exception ("Couldn't find specified algorithm or generator.");
            }
        }
    }

    private int commitBatch(List<ProcessedFrame> resultList, int framesCounter, String filePath) throws IOException {
        try {
            repository.saveResultAsList(resultList);
            ResultExport.exportResultsToFiles(resultList, resultList.getFirst().algorithmName, resultList.getFirst().generatorName, filePath);
            framesCounter += resultList.size();
            resultList.clear();
            return framesCounter;
        }
        catch (IOException e)
        {
            throw new IOException("Couldn't export generated results to files.",e);
        }
    }

    private ProcessedFrame processFrame(byte[] currentFrame, byte[] previousFrame, HashStrategy hashStrategy, GeneratorStrategy generatorStrategy) throws Exception {
        long start = System.nanoTime();
        byte[] rawHash= hashStrategy.generateHash(currentFrame, previousFrame);
        long time = System.nanoTime() - start;

        byte[] generatedResult = generatorStrategy.generateNumber(rawHash, 1);

        return new ProcessedFrame(hashStrategy.getName(), generatorStrategy.getName(), rawHash, generatedResult, time);
    }

    public long onProcessVideoClick(String algorithmName, String generatorName, String filePath) throws Exception {
        if(!serviceUtils.isFileVideo(filePath)){
            throw new Exception("Incorrect file type. Try inserting file with video extension.");
        }

        HashStrategy hashStrategy = strategies.get(algorithmName);
        GeneratorStrategy generatorStrategy = generators.get(generatorName);
        validateStrategy(hashStrategy,generatorStrategy);

        int framesCounter = 0;
        List<ProcessedFrame> resultList = new ArrayList<>();

        try(VideoGrabber grabber = new VideoGrabber()) {
            grabber.openVideo(filePath);
            Path path = Paths.get(filePath);

            String fileNameWithExtension = path.getFileName().toString();

            filePath= fileNameWithExtension.replaceFirst("[.][^.]+$", "");


            byte[] previousFrame = grabber.getNextFrameAsBytes();

            if(previousFrame==null) {throw new Exception("Selected file does not contain any video frames.");}
            byte[] currentFrame;
            while ((currentFrame = grabber.getNextFrameAsBytes()) != null) {

                resultList.add(processFrame(currentFrame, previousFrame, hashStrategy, generatorStrategy));
                previousFrame=currentFrame;

                if(resultList.size() == 1000)
                {
                    framesCounter= commitBatch(resultList,framesCounter, filePath);
                }
            }
        }
        if (!resultList.isEmpty()) {
          framesCounter= commitBatch(resultList,framesCounter,filePath);
        }

        return framesCounter;
    }

    public long onProcessFrameClick(String algorithmName, String generatorName, String filePath) throws Exception {
        if(!serviceUtils.isFileVideo(filePath)){throw new Exception("Incorrect file type. Try inserting file with video extension.");}

        HashStrategy hashStrategy = strategies.get(algorithmName);
        GeneratorStrategy generatorStrategy = generators.get(generatorName);
        validateStrategy(hashStrategy,generatorStrategy);

        long generatedNumber;

        try (VideoGrabber grabber = new VideoGrabber()) {
            grabber.openVideo(filePath);

            grabber.setVideoToSpecificFrame(serviceUtils.getRandomFrame(grabber.getVideoLenght()));
            byte[] previousFrame = grabber.getNextFrameAsBytes();
            byte[] currentFrame = grabber.getNextFrameAsBytes();

            if(currentFrame == null || previousFrame == null) {throw new Exception("Selected file does not contain required amount of video frames.");}

            ProcessedFrame processedFrame = processFrame(currentFrame, previousFrame, hashStrategy, generatorStrategy);

            generatedNumber= serviceUtils.convertBytesToLong(processedFrame.generatedResult);
            repository.saveResult(processedFrame);
            ResultExport.exportResultToFiles(processedFrame, processedFrame.algorithmName, processedFrame.generatorName);
        }
        return generatedNumber;

    }

    public BenchmarkResult runBenchmarkSingle(String algoName, byte[] data) {
        HashStrategy strategy = strategies.get(algoName);
        long start = System.nanoTime();
        byte[] hash = strategy.generateHash(data);
        long time = System.nanoTime() - start;
        return new BenchmarkResult(algoName, hash, time);
    }
    public record BenchmarkResult(String name, byte[] hash, long time) {}


}