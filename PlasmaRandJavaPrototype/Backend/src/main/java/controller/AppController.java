package controller;

import model.PlasmaService;
import view.ViewInterface;
import java.io.File;
import java.nio.file.Files;
import java.util.HexFormat;

public class AppController implements ViewInterface.ViewListener {
    private final PlasmaService model;
    private final ViewInterface view;
    
    private String currentFilePath = "";

    public AppController(PlasmaService model, ViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setListener(this);
        this.view.setAlgorithmsList(model.getAvailableAlgorithms());
        this.view.setGeneratorsList(model.getAvailableGenerators());
    }

    // Uruchamia się na starcie 
    public void start() {
        runStartupBenchmark("lampa.jpg"); // Wyświetla tabelę w konsoli
        view.showView(); // Wyświetla GUI
    }

    @Override
    public void onFileSelected(File file) {
        if (file != null && file.exists()) {
            this.currentFilePath = file.getAbsolutePath();
            System.out.println("Zmieniono plik na: " + currentFilePath);
        }
    }

    // Wyświetla tabelkę w konsoli
    private void runStartupBenchmark(String imagePath) {
        System.out.println("\n=== URUCHAMIANIE BENCHMARKU DLA: " + imagePath + " ===");
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                System.err.println("Brak pliku " + imagePath + "! Pominiecie benchmarku.");
                return;
            }
            byte[] data = Files.readAllBytes(file.toPath());

            // ROZGRZEWKA MASZYNY
            System.out.print("Rozgrzewanie maszyny JVM dla wszystkich algorytmów...");

            String[] availableAlgos = model.getAvailableAlgorithms();

            for (int i = 0; i < 500; i++) {
                for (String algo : availableAlgos) {
                     model.runBenchmarkSingle(algo, data);
                }
            }
            System.out.println(" Gotowe!\n");


            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-20s | %-15s | %-20s\n", "Algorytm", "Czas (ns)", "Hash (Hex)");
            System.out.println("----------------------------------------------------------------------");

            for (String algo : model.getAvailableAlgorithms()) {
                PlasmaService.BenchmarkResult res = model.runBenchmarkSingle(algo, data);
                String hexString = HexFormat.of().withUpperCase().formatHex(res.hash());
                System.out.printf("%-15s | %-15d | %-20s\n", res.name(), res.time(), hexString.toUpperCase());
            }
            System.out.println("----------------------------------------------------------------------\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGenerateClicked(String algorithmName, String generatorName, boolean processingVideo) {
        try {
            if(processingVideo) {
                //long counter = model.onProcessVideoClick(algorithmName, generatorName, currentFilePath);
                //view.displayResult(algorithmName, counter);
                /* THAT LINES ARE TO AUTOMATE PROCESS OF GENERATING FILES FOR TESTS*/
                String[] algorithms = model.getAvailableAlgorithms();
                String[] generators = model.getAvailableGenerators();
                String[] filePath = {"lampa1_30_klatek.mov", "lampa2_30_klatek.mov", "lampa_lawowa_30_klatek.mov"};
                for(String cFilePath : filePath) {
                    for (String algorithm : algorithms) {
                        for (String generator : generators) {
                            long number = model.onProcessVideoClick(algorithm, generator, cFilePath);
                        }
                    }
                }
                } else {
                long number = model.onProcessFrameClick(algorithmName, generatorName, currentFilePath);
                view.displayResult(algorithmName, number);
            }

        } catch (Exception e) {
            view.displayError("Błąd (" + currentFilePath + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

}



