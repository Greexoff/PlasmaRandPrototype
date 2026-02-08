package view;

import java.io.File;

public interface ViewInterface {
    void displayResult(String algo, long result);
    void displayError(String msg);
    void setAlgorithmsList(String[] algos);
    void setGeneratorsList(String [] generators);
    void setListener(ViewListener listener);
    void showView();

    // Interfejs dla Kontrolera 
    interface ViewListener {
        void onGenerateClicked(String algorithmName, String generatorName, boolean processingVideo);
        void onFileSelected(File file);
    }
}