package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;
import java.util.HashMap;

public class ResidentStatsCommand extends Command {

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        ArrayList<Resident> residents = residentManager.getResidentList();
        try {
            HashMap<Resident, Integer> totalPoints = totalPoints(residents);
            ArrayList<Resident> mostActiveResident = mostActiveResidents(totalPoints);
            ui.showResidentStats(totalPoints, mostActiveResident);
        } catch (IllegalArgumentException e) {
            ui.showMessage("There are no residents currently. Please add residents using add-resident command");
        }
    }

    /**
     * Computes the total points across all CCAs for each resident
     * @param residents a list of residents
     * @return a hashmap containing the residents in <code>residents</code> and their corresponding total points
     * @throws IllegalArgumentException if <code>residents</code> is empty
     */
    public static HashMap<Resident, Integer> totalPoints(ArrayList<Resident> residents)
            throws IllegalArgumentException {
        if (residents.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HashMap<Resident, Integer> totalPoints = new HashMap<>();
        for (Resident resident : residents) {
            ArrayList<Integer> points = resident.getPoints();
            if (points.isEmpty()) {
                totalPoints.put(resident, 0);
            } else {
                int sum = points.stream().mapToInt(Integer::intValue).sum();
                totalPoints.put(resident, sum);
            }
        }
        return totalPoints;
    }

    /**
     * Finds the most active residents across all CCAs based on their total points
     * @param totalPoints a hashmap containing the residents and their corresponding total points
     * @return a list of the most active residents
     * @throws IllegalArgumentException if <code>totalPoints</code> is empty
     */
    public static ArrayList<Resident> mostActiveResidents (HashMap<Resident,Integer> totalPoints)
            throws IllegalArgumentException {
        if (totalPoints.isEmpty()) {
            throw new IllegalArgumentException();
        }
        ArrayList<Resident> mostActiveResidents = new ArrayList<>();
        int max = 0;
        for (Resident resident : totalPoints.keySet()) {
            if (totalPoints.get(resident) > max) {
                max = totalPoints.get(resident);
            }
        }
        for (Resident resident : totalPoints.keySet()) {
            if (totalPoints.get(resident) == max) {
                mostActiveResidents.add(resident);
            }
        }
        return mostActiveResidents;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
