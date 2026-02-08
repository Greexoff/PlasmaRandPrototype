package java.model;

import model.PlasmaService;
import model.repository.ResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class PlasmaServiceTest {

    @TempDir
    Path tempDir;

    // TEST 1: Sprawdza poprawność (zakres czy jest ujemny, czy za drugim razem jest taki sam, zapis) dla każdego algorytmu
    @Test
    public void testAllAlgorithmsBasics() {
        ResultRepository mockRepo = Mockito.mock(ResultRepository.class);
        PlasmaService service = new PlasmaService(mockRepo);
        
        // Symulujemy dane klatki wideo
        byte[] fakeFrameData = "symulowana_klatka_wideo".getBytes();

        String[] allAlgos = service.getAvailableAlgorithms();

        for (String algoName : allAlgos) {
            System.out.println("Testowanie serwisu dla: " + algoName);

            PlasmaService.BenchmarkResult result1 = service.runBenchmarkSingle(algoName, fakeFrameData);

            // Sprawdzenie czy wynik nie jest pusty
            assertNotNull(result1);
            assertNotNull(result1.hash());
            assertTrue(result1.time() >= 0);

            // Determinizm: Drugie wywołanie dla tych samych danych musi dać ten sam hash
            PlasmaService.BenchmarkResult result2 = service.runBenchmarkSingle(algoName, fakeFrameData);
            
            assertArrayEquals(result1.hash(), result2.hash(), 
                    "Serwis dla " + algoName + " zwrócił różne wyniki dla tych samych danych wejściowych");
        }
    }

    // Sprawdza unikalność haszy generowanych przez serwis
    @Test
    public void testAllAlgorithmsProduceUniqueResults() {
        ResultRepository mockRepo = Mockito.mock(ResultRepository.class);
        PlasmaService service = new PlasmaService(mockRepo);
        
        byte[] fakeFrameData = "dane_dla_testu_unikalnosci".getBytes();

        String[] allAlgos = service.getAvailableAlgorithms();

        Set<String> uniqueHashes = new HashSet<>();

        System.out.println("--- Sprawdzanie unikalności ---");

        for (String algoName : allAlgos) {
            PlasmaService.BenchmarkResult res = service.runBenchmarkSingle(algoName, fakeFrameData);

            String hashString = Arrays.toString(res.hash());
            
            boolean isUnique = uniqueHashes.add(hashString);
            
            if (!isUnique) {
                fail("Wykryto kolizję! Algorytm " + algoName + " dał wynik, który już wystąpił: " + hashString);
            }
        }

        assertEquals(allAlgos.length, uniqueHashes.size(), 
                "Nie wszystkie algorytmy dały unikalne wyniki w serwisie!");
    }
}