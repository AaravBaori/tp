package ccamanager.command;

import ccamanager.manager.CcaManager;
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

    public AddResidentToCcaCommand(String matriculationNo, String ccaName, String pointsScored) {
        assert Integer.parseInt(pointsScored) >= 0 : "Points scored must be greater than or equal to 0";
        this.matriculationNo = matriculationNo;
        this.ccaName = ccaName;
        this.pointsScored = Integer.parseInt(pointsScored);
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equals(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

            Resident resident = residentManager.getResidentList().stream()
                    .filter(x -> x.getMatricNumber().equals(matriculationNo))
                    .findFirst()
                    .orElseThrow(() -> new ResidentNotFoundException(matriculationNo + " not found."));

            cca.addResidentToCca(resident);
            resident.addCcaToResident(cca, pointsScored);

            ui.showMessage("Resident " + resident + " was added to CCA: " + cca + " with " + pointsScored + " points.");

        } catch (CcaNotFoundException | ResidentNotFoundException | ResidentAlreadyInCcaException e) {
            ui.showError(e.getMessage());
        }
    }
}


