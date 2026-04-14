package ccamanager.command;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

public class UpdateCcaPointCommand extends Command{
    private final String matricNumber;
    private final String ccaName;
    private int point;

    public UpdateCcaPointCommand(String matricNumber, String ccaName, String point) {
        assert matricNumber != null : "Matriculation number should not be null";
        assert ccaName != null : "CCA name should not be null";
        assert point != null : "Points scored should not be null";
        assert Integer.parseInt(point) >= 0 : "Points scored must be greater than or equal to 0";
        this.matricNumber = matricNumber;
        this.ccaName = ccaName;
        this.point = Integer.parseInt(point);
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equalsIgnoreCase(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

            Resident resident = residentManager.getResidentList().stream()
                    .filter(x -> x.getMatricNumber().equalsIgnoreCase(matricNumber))
                    .findFirst()
                    .orElseThrow(() -> new ResidentNotFoundException(matricNumber + " not found."));

            resident.updatePoint(cca,point);

            ui.showMessage("Resident " + resident + " points updated to: " + point);

        } catch (CcaNotFoundException | ResidentNotFoundException e) {
            ui.showError(e.getMessage());
        }
    }

}
