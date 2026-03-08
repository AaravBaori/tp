package ccamanager.ui;

import ccamanager.model.Cca;
import ccamanager.model.Resident;

import java.util.ArrayList;
import java.util.Scanner;


public class Ui {

    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the trimmed input string
     */
    public String readInput() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }


    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(" Welcome to CCA Ledger!");
        System.out.println(" Type a command to get started. Type 'bye' to exit.");
        System.out.println(DIVIDER);
    }

    public void showGoodbye() {
        System.out.println(DIVIDER);
        System.out.println(" Bye! See you next time.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays a generic success/info message.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(DIVIDER);
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Displays an error message.
     * @param message the error to display
     */
    public void showError(String message) {
        System.out.println(DIVIDER);
        System.out.println(" ERROR: " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Prints the full list of CCAs.
     * @param ccaList the list to display
     */
    public void showCcaList(ArrayList<Cca> ccaList) {
        System.out.println(DIVIDER);
        if (ccaList.isEmpty()) {
            showMessage("There are no CCAs currently. Please add CCAs using add-cca command");
        } else {
            System.out.println("Here is the complete list of all the CCAs :");
            for(int i = 1; i < ccaList.size() + 1; i++ ) {
                Cca cca = ccaList.get(i-1);
                System.out.println(i + ". " + cca);
            }
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints the full list of residents.
     * @param residentList the list to display
     */
    public void showResidentList(ArrayList<Resident> residentList) {
        System.out.println(DIVIDER);
        if (residentList.isEmpty()) {
            showMessage("There are no residents currently. Please add residents using add-resident command");
        } else {
            System.out.println("Here is the complete list of all the residents :");
            for(int i = 1; i < residentList.size() + 1; i++) {
                Resident resident = residentList.get(i-1);
                System.out.println(i + ". " + resident);
            }
        }
        System.out.println(DIVIDER);
    }
}
