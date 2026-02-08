package java.model;

import model.PlasmaService;
import model.repository.SQLiteRepository;
import org.junit.jupiter.api.BeforeEach;

public class DatabaseIntegrationTest {

    private SQLiteRepository repository;
    private PlasmaService serviceHelper;

    @BeforeEach
    public void setUp() {
        repository = new SQLiteRepository();
        // Tworzymy serwis tylko po to, by mieć dostęp do metody getAvailableAlgorithms()
        // Możemy przekazać null zamiast repo, bo nie będziemy używać metod zapisu z serwisu, tylko same nazwy
        serviceHelper = new PlasmaService(repository); 
    }

    //sprawdza: czy operacja zapisu nie wyrzuciła błędu SQL, czy lista pobrana z bazy nie jest pusta, czy dla każdego algorytmu udało się znaleźć w bazie ten konkretny wpis, który przed chwilą dodaliśmy.
   /* @Test
    public void testSaveAndReadForAllAlgorithms() {
        String[] allAlgos = serviceHelper.getAvailableAlgorithms();
        long testTime = 100L;
        byte[] dummyHash = new byte[]{1, 2, 3, 4, 5}; // Przykładowy hash

        for (String algoName : allAlgos) {
            long testValue = Math.abs(algoName.hashCode()); 
            
            // Testujemy czy baza przyjmuje byte[] oraz inne dane
            assertDoesNotThrow(() -> {
                repository.saveResult(algoName, dummyHash, testValue, testTime);
            }, "Błąd zapisu dla algorytmu: " + algoName);
        }
}*/
}
