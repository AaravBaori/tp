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
            Cca mostPopularCca = mostPopularCca(avgPoints);
            HashMap<Cca, Resident> mostActiveResidents = mostActiveResidents(ccas);
            ui.showCcaStats(avgPoints, mostPopularCca, mostActiveResidents);
        } catch (IllegalArgumentException e) {
            ui.showMessage("There are no CCAs currently. Please add CCAs using add-cca command");
        }
    }

    /**
     * Computes the average number of points for each CCA in <code>ccas</code>
     * @param ccas the list of CCAs
     * @return a hashmap of CCAs and their corresponding average points
     */
    private static HashMap<Cca, Double> avgPoints(ArrayList<Cca> ccas) throws IllegalArgumentException {
        if (ccas.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HashMap<Cca, Double> avgPoints = new HashMap<>();
        for (Cca cca : ccas) {
            ArrayList<Resident> registeredResidents = cca.getRegisteredResidents();
            double totalPoints = 0;
            for (Resident resident : registeredResidents) {
                totalPoints += resident.getCcaMap().get(cca);
            }
            double avg = totalPoints / registeredResidents.size();
            avgPoints.put(cca, avg);
        }
        return avgPoints;
    }

    /**
     * Finds the most popular CCA based on their average points
     * @param avgPoints a hashmap of CCAs and their corresponding average points
     * @return the most popular CCA
     */
    private static Cca mostPopularCca(HashMap<Cca, Double> avgPoints) throws IllegalArgumentException {
        if  (avgPoints.isEmpty()) {
            throw  new IllegalArgumentException();
        }
        Cca mostPopularCca = null;
        for (Cca cca : avgPoints.keySet()) {
            if (mostPopularCca == null) {
                mostPopularCca = cca;
            } else if (avgPoints.get(cca) > avgPoints.get(mostPopularCca)) {
                mostPopularCca = cca;
            }
        }
        return mostPopularCca;
    }

    /**
     * Finds the resident with the most points for each CCA
     * @param ccas the list of CCAs
     * @return a hashmap containing the CCAs and their most active member
     */
    private static HashMap<Cca, Resident> mostActiveResidents(ArrayList<Cca> ccas) throws IllegalArgumentException {
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
                } else if (resident.getCcaMap().get(cca) > mostActiveResident.getCcaMap().get(cca)) {
                    mostActiveResident = resident;
                }
            }
            mostActiveResidents.put(cca, mostActiveResident);
        }
        return mostActiveResidents;
    }
}
