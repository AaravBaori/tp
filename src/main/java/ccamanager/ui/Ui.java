package ccamanager.ui;

import ccamanager.model.Cca;
import ccamanager.model.Event;
import ccamanager.model.Resident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Ui {

    private static final String DIVIDER = "_____________________________________________________________________" +
            "____________";
    private final Scanner scanner;
    private String lastMessage;


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
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }


    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(" Welcome to CCA Ledger!");
        System.out.println(" Type 'help' for available commands. Type 'bye' to exit.");
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
        lastMessage = message;
        System.out.println(DIVIDER);
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    /**
     * Displays an error message.
     * @param message the error to display
     */
    public void showError(String message) {
        lastMessage = message;
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

    /**
     * Prints the full list of EXCOs of a CCA.
     * @param excoList the list to display
     */
    public void showExcoList(ArrayList<Resident> excoList) {
        System.out.println(DIVIDER);
        if (excoList.isEmpty()) {
            showMessage("There are no EXCO assigned currently. Please add EXCOs using add-exco-to-cca command");
        } else {
            System.out.println("Here is the complete list of all the EXCOs :");
            for(int i = 1; i < excoList.size() + 1; i++) {
                Resident resident = excoList.get(i-1);
                System.out.println(i + ". " + resident);
            }
        }
        System.out.println(DIVIDER);
    }

    public void showCcaPoints(ArrayList<Resident> residentList){
        System.out.println(DIVIDER);
        if (residentList.isEmpty()) {
            showMessage("There are no residents currently. Please add residents using add-resident command");
        } else {
            System.out.println("Here are the points of each resident:");
            for(int i = 1; i < residentList.size() + 1; i++) {
                Resident resident = residentList.get(i-1);
                ArrayList<Cca> residentCcas= resident.getCcas();
                ArrayList<Integer> residentPoints = resident.getPoints();

                if(residentCcas.isEmpty()){
                    System.out.println(i + ". " + resident + ": 0 point");
                    System.out.println(DIVIDER);
                }else {
                    int totalPoints = resident.getTotalPoints();
                    System.out.println(i + ". " + resident + ": " +totalPoints +" points");
                    System.out.println(DIVIDER);
                    System.out.println("Here is the breakdown:");

                    for (int j = 0; j < residentCcas.size(); j++) {
                        System.out.println(residentCcas.get(j).getName() + " " + residentPoints.get(j));
                    }
                    System.out.println(DIVIDER);
                }
            }
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays per-CCA statistics
     * @param avgPoints a hashmap containing CCAs and the average points of the registered residents
     * @param mostPopularCcas a list of the most popular CCAs based on their average points
     * @param mostActiveResidents a hashmap containing CCAs and their most active members
     */
    public void showCcaStats(HashMap<Cca, Double> avgPoints, ArrayList<Cca> mostPopularCcas, HashMap<Cca,
            Resident> mostActiveResidents) {
        System.out.println(DIVIDER);
        System.out.println("Average points and most active resident per CCA:");
        int index = 1;
        for (Cca cca : avgPoints.keySet()) {
            Resident mostActiveResident = mostActiveResidents.get(cca);
            if (mostActiveResident == null) {
                System.out.println(index + ". " + cca + ", average points: " + avgPoints.get(cca) + ", most active: " +
                        "N/A");
            } else {
                System.out.println(index + ". " + cca + ", average points: " + avgPoints.get(cca) + ", most active: " +
                        mostActiveResident);
            }
            index++;
        }
        System.out.println();
        System.out.println("Most popular CCAs:");
        index = 1;
        for (Cca cca : mostPopularCcas) {
            System.out.println(index + ". " + cca + ", average points: " + avgPoints.get(cca));
            index++;
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays per-resident statistics
     * @param totalPoints a hashmap containing residents and their total points across all CCAs
     * @param mostActiveResident a list of the most active residents based on their total points
     */
    public void showResidentStats(HashMap<Resident, Integer> totalPoints, ArrayList<Resident> mostActiveResident) {
        System.out.println(DIVIDER);
        System.out.println("Total points for each resident:");
        int index = 1;
        for (Resident resident : totalPoints.keySet()) {
            System.out.println(index + ". " + resident + ", total points: " + totalPoints.get(resident));
            index++;
        }
        System.out.println();
        System.out.println("Most active residents across all CCAs:");
        index = 1;
        for (Resident resident : mostActiveResident) {
            System.out.println(index + ". " + resident + ", total points: " + totalPoints.get(resident));
            index++;
        }
        System.out.println(DIVIDER);
    }
    /**
     * Displays the matching events
     * @param events an arraylist of events that matches the requirement
     */
    public void viewMatchingCcas(ArrayList<Event> events){
        System.out.println(DIVIDER);
        System.out.println("Here are the events:");
        for(int i=0; i<events.size();i++){
            System.out.println((i+1)+"." +" " +events.get(i).getEventName()+ " date: " + events.get(i).getEventDate());
        }
        System.out.println(DIVIDER);
    }
    /**
     * Displays the matching events
     * @param events an arraylist of events that matches the requirement
     */
    public void viewMyCcas(ArrayList<Event> events){
        System.out.println(DIVIDER);

        for(int i=0; i<events.size();i++){
            String ccaName = events.get(i).getCca().getName();
            String eventName=events.get(i).getEventName();
            System.out.println((i+1)+". "+ ccaName +": "+eventName+ " date: " + events.get(i).getEventDate());
        }
        System.out.println(DIVIDER);
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
