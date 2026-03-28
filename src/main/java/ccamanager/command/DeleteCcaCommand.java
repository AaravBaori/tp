package ccamanager.command;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

/**
 * Command to delete any exisiting CCA
 */
public class DeleteCcaCommand extends Command{
    private String ccaName;

    public DeleteCcaCommand(String ccaName) {
        assert ccaName != null : "CCA name should not be null";
        this.ccaName = ccaName;
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            ccaManager.deleteCca(ccaName);
            ui.showMessage("CCA deleted: " + ccaName);
        } catch (CcaNotFoundException e) {
            ui.showMessage(e.getMessage());
        }
    }
}

