package ccamanager.command;

import ccamanager.exceptions.ResidentAlreadyInExcoException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;

import ccamanager.model.Cca;
import ccamanager.model.Resident;

import ccamanager.ui.Ui;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInCcaException;

/**
 * Command to promote an existing resident to an Executive Committee (EXCO)
 * position within a specific CCA.
 * * <p>The command identifies the CCA and Resident by their unique names and
 * matriculation numbers respectively, then updates both objects to reflect the
 * new relationship.</p>
 */
public class AddExcoToCcaCommand extends Command {

    private final String matriculationNo;
    private final String ccaName;

    public AddExcoToCcaCommand(String matriculationNo, String ccaName) {
        this.matriculationNo = matriculationNo != null ? matriculationNo.trim() : null;
        this.ccaName = ccaName != null ? ccaName.trim() : null;

        assert this.matriculationNo != null : "Matriculation number should not be null";
        assert this.ccaName != null : "CCA name should not be null";

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
            resident.addCcaToResident(cca);

            ui.showMessage("Resident " + resident + " was added as an EXCO to CCA: " + cca.getName());

        } catch (CcaNotFoundException | ResidentNotFoundException |
                 ResidentAlreadyInCcaException | ResidentAlreadyInExcoException e) {
            ui.showError(e.getMessage());
        }
    }
}



