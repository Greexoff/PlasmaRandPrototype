package view;

public class HasherView {

    public void displayHeader(String fileName) {
        System.out.println("\n=== ANALIZA OBRAZU: " + fileName + " ===");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %-20s\n", "Algorytm", "Czas (ns)", "Wygenerowana Liczba");
        System.out.println("----------------------------------------------------------------------");
    }

    public void displayRow(String name, long time, String result) {
        System.out.printf("%-20s | %-15d | %-20s\n", name, time, result);
    }

    public void displayError(String message) {
        System.err.println("BŁĄD: " + message);
    }

    public void displayFooter() {
        System.out.println("----------------------------------------------------------------------");
    }

}