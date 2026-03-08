package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.ui.Ui;

public class AddCcaCommand extends Command {
    private String ccaName;

    public AddCcaCommand(String ccaName) {
        this.ccaName = ccaName;
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
        ccaManager.addCCA(ccaName);
        ui.showMessage("CCA added: " + ccaName);
    }
}
