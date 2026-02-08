package java.model;

import model.hashStrategy.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class AlgorithmTest {

    // Tworzymy listę wszystkich dostępnych strategii
    private List<HashStrategy> getAllStrategies() {
        return Arrays.asList(
            new DJB2Strategy(),
            new SDBMStrategy(),
            new JenkinsStrategy(),
            new KnuthStrategy(),
            new HsiehStrategy(),
            new FNV1aStrategy(),
            new SHA256Strategy(),
            new SHA512Strategy(),
            new SHA3Strategy(),
            new MD5Strategy());
    }

    // TEST 1: Determinizm
    // Sprawdza czy rzy ponownym uruchomieniu generuje ten sam has
    @Test
    public void testDeterminismForAll() {
        byte[] data = "TestoweDaneWejsciowe".getBytes();

        for (HashStrategy strategy : getAllStrategies()) {
            byte[] result1 = strategy.generateHash(data);
            byte[] result2 = strategy.generateHash(data);

            assertArrayEquals(result1, result2, 
                "BŁĄD w " + strategy.getName() + ": Algorytm musi zwracać to samo dla tych samych danych!");
        }
    }

    // TEST 2: Efekt Lawiny 
    // Sprawdza czy zmiana 1 znaku w wejściu zmienia wynik haszu
@Test
    public void testAvalancheEffect() {
        byte[] data1 = "Haslo123".getBytes();
        byte[] data2 = "Haslo124".getBytes(); 

        for (HashStrategy strategy : getAllStrategies()) {
            byte[] hash1 = strategy.generateHash(data1);
            byte[] hash2 = strategy.generateHash(data2);

            // Sprawdzamy czy tablice NIE są równe
            boolean areEqual = Arrays.equals(hash1, hash2);
            assertFalse(areEqual, 
                "BŁĄD w " + strategy.getName() + ": Zmiana danych wejściowych nie zmieniła hasza!");
        }
    }

    // TEST 3: Odporność na Puste Dane 
    // Sprawdza czy algorytm się nie wysypuje, gdy dostanie pusty plik
@Test
    public void testEmptyInput() {
        byte[] emptyData = new byte[0];

        for (HashStrategy strategy : getAllStrategies()) {
            try {
                byte[] result = strategy.generateHash(emptyData);
                assertNotNull(result, "Wynik nie może być nullem");
            } catch (Exception e) {
                fail("BŁĄD w " + strategy.getName() + ": Algorytm wyrzucił błąd przy pustych danych! " + e.getMessage());
            }
        }
    }

    // TEST 4: Unikalność względem siebie
    // Sprawdza czy każdy algorytm daje inny wynik dla tego samego wejścia
    @Test
    public void testStrategiesAreDistinct() {
        byte[] data = "WspolneDaneDlaWszystkich".getBytes();
        List<HashStrategy> strategies = getAllStrategies();

        for (int i = 0; i < strategies.size(); i++) {
            for (int j = i + 1; j < strategies.size(); j++) {
                
                HashStrategy s1 = strategies.get(i);
                HashStrategy s2 = strategies.get(j);

                byte[] hash1 = s1.generateHash(data);
                byte[] hash2 = s2.generateHash(data);

                boolean areEqual = Arrays.equals(hash1, hash2);
                assertFalse(areEqual, 
                    "Para: " + s1.getName() + " i " + s2.getName() + " zwróciły ten sam wynik - dziwne");
            }
        }
    }
}