package ccamanager.command;

import ccamanager.exceptions.InvalidPointsException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;

import ccamanager.model.Cca;
import ccamanager.model.Resident;

import ccamanager.ui.Ui;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInCcaException;


public class AddResidentToCcaCommand extends Command {

    private final String matriculationNo;
    private final String ccaName;
    private final int pointsScored;

    public AddResidentToCcaCommand(String matriculationNo, String ccaName, String pointsScored)
            throws IllegalArgumentException, InvalidPointsException { // Declare that this can fail

        // 1. Basic null checks (using standard if-statements, not asserts)
        if (matriculationNo == null || ccaName == null || pointsScored == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        this.matriculationNo = matriculationNo;
        this.ccaName = ccaName;

        // 2. Parse and validate the integer
        try {
            this.pointsScored = Integer.parseInt(pointsScored.trim()); // .trim() removes accidental spaces

            // 3. Catch negative numbers!
            if (this.pointsScored < 0) {
                throw new IllegalArgumentException("Points scored must be 0 or greater.");
            }

        } catch (NumberFormatException e) {
            // 4. Catch non-number inputs (like "abc" or "10.5")
            throw new IllegalArgumentException("Points must be a valid whole number. You entered: '"
                    + pointsScored + "'");
        }
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equalsIgnoreCase(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

            Resident resident = residentManager.getResidentList().stream()
                    .filter(x -> x.getMatricNumber().equalsIgnoreCase(matriculationNo))
                    .findFirst()
                    .orElseThrow(() -> new ResidentNotFoundException(matriculationNo + " not found."));

            cca.addResidentToCca(resident);
            resident.addCcaToResident(cca, pointsScored);

            ui.showMessage("Resident " + resident + " was added to CCA: " + cca.getName() +
                    " with " + pointsScored + " points.");

        } catch (CcaNotFoundException | ResidentNotFoundException | ResidentAlreadyInCcaException e) {
            ui.showError(e.getMessage());
        }
    }
}



