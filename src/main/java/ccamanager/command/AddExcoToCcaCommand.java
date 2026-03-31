package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;

import ccamanager.model.Cca;
import ccamanager.model.Resident;

import ccamanager.ui.Ui;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInCcaException;


public class AddExcoToCcaCommand extends Command {

    private final String matriculationNo;
    private final String ccaName;

    public AddExcoToCcaCommand(String matriculationNo, String ccaName) {
        assert matriculationNo != null : "Matriculation number should not be null";
        assert ccaName != null : "CCA name should not be null";
        this.matriculationNo = matriculationNo;
        this.ccaName = ccaName;

    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equals(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

            Resident resident = residentManager.getResidentList().stream()
                    .filter(x -> x.getMatricNumber().equals(matriculationNo))
                    .findFirst()
                    .orElseThrow(() -> new ResidentNotFoundException(matriculationNo + " not found."));

            cca.addExcoToCca(resident);
            resident.addCcaToResident(cca, pointsScored);

            ui.showMessage("Resident " + resident + " was added to CCA: " + cca.getName() +
                    " with " + pointsScored + " points.");

        } catch (CcaNotFoundException | ResidentNotFoundException | ResidentAlreadyInCcaException e) {
            ui.showError(e.getMessage());
        }
    }
}



