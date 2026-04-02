package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;
import java.util.HashMap;

public class CcaStatsCommand extends Command {

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        try {
            HashMap<Cca, Double> avgPoints = avgPoints(ccas);
            ArrayList<Cca> mostPopularCcas = mostPopularCcas(avgPoints);
            HashMap<Cca, Resident> mostActiveResidents = mostActiveResidents(ccas);
            ui.showCcaStats(avgPoints, mostPopularCcas, mostActiveResidents);
        } catch (IllegalArgumentException e) {
            ui.showMessage("There are no CCAs currently. Please add CCAs using add-cca command");
        }
    }

    /**
     * Computes the average number of points for each CCA in <code>ccas</code>
     * @param ccas the list of CCAs
     * @return a hashmap of CCAs and their corresponding average points
     * @throws IllegalArgumentException if <code>ccas</code> is empty
     */
    public static HashMap<Cca, Double> avgPoints(ArrayList<Cca> ccas) throws IllegalArgumentException {
        if (ccas.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HashMap<Cca, Double> avgPoints = new HashMap<>();
        for (Cca cca : ccas) {
            ArrayList<Resident> registeredResidents = cca.getRegisteredResidents();
            if (registeredResidents.isEmpty()) {
                avgPoints.put(cca, 0.0);
            } else {
                double totalPoints = 0.0;
                for (Resident resident : registeredResidents) {
                    totalPoints += resident.getCcaPoints().get(cca);
                }
                double avg = totalPoints / registeredResidents.size();
                avgPoints.put(cca, avg);
            }
        }
        return avgPoints;
    }

    /**
     * Finds the most popular CCAs based on their average points
     * @param avgPoints a hashmap of CCAs and their corresponding average points
     * @return a list containing the CCAs with the highest number of points
     * @throws IllegalArgumentException if <code>avgPoints</code> is empty
     */
    public static ArrayList<Cca> mostPopularCcas(HashMap<Cca, Double> avgPoints) throws IllegalArgumentException {
        if  (avgPoints.isEmpty()) {
            throw  new IllegalArgumentException();
        }
        double max = 0.0;
        for (Cca cca : avgPoints.keySet()) {
            if (avgPoints.get(cca) > max) {
                max = avgPoints.get(cca);
            }
        }
        ArrayList<Cca> mostPopularCcas = new ArrayList<>();
        for (Cca cca : avgPoints.keySet()) {
            if (avgPoints.get(cca) == max) {
                mostPopularCcas.add(cca);
            }
        }
        return mostPopularCcas;
    }

    /**
     * Finds the resident with the most points for each CCA
     * @param ccas the list of CCAs
     * @return a hashmap containing the CCAs and their most active member
     * @throws IllegalArgumentException if <code>ccas</code> is empty
     */
    public static HashMap<Cca, Resident> mostActiveResidents(ArrayList<Cca> ccas) throws IllegalArgumentException {
        if (ccas.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HashMap<Cca, Resident> mostActiveResidents = new HashMap<>();
        for (Cca cca : ccas) {
            ArrayList<Resident> registeredResidents = cca.getRegisteredResidents();
            Resident mostActiveResident = null;
            for (Resident resident : registeredResidents) {
                if (mostActiveResident == null) {
                    mostActiveResident = resident;
                } else if (resident.getCcaPoints().get(cca) > mostActiveResident.getCcaPoints().get(cca)) {
                    mostActiveResident = resident;
                }
            }
            mostActiveResidents.put(cca, mostActiveResident);
        }
        return mostActiveResidents;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
