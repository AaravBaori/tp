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
        if (residents.isEmpty()) {
            ui.showMessage("There are no residents currently. Please add residents using add-resident command");
            return;
        }
        HashMap<Resident, Integer> totalPoints = totalPoints(residents);
        ArrayList<Resident> mostActiveResident = mostActiveResidents(totalPoints);
    }

    private static HashMap<Resident, Integer> totalPoints(ArrayList<Resident> residents) {
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

    private static ArrayList<Resident> mostActiveResidents (HashMap<Resident, Integer> totalPoints) {
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
}
