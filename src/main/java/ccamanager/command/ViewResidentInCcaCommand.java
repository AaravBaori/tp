package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;

/**
 * Command to view the list of all residents in a specific CCA.
 */
public class ViewResidentInCcaCommand extends Command {

    private final String ccaName;

    public ViewResidentInCcaCommand(String ccaName) {
        this.ccaName = ccaName;
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager,
                        EventManager eventManager, Ui ui) {

        // 1. Find the CCA
        Cca cca = ccaManager.findByName(ccaName);

        // 2. Check if it exists
        if (cca == null) {
            ui.showError("CCA '" + ccaName + "' does not exist.");
            return;
        }

        // 3. Get the residents and pass them to the UI
        ArrayList<Resident> members = cca.getResidents();
        ArrayList<Resident> excos = cca.getExcos(); // Optional, but good for UI formatting

        ui.showResidentsInCca(cca.getName(), members, excos);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
