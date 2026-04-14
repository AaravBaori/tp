package ccamanager.command;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;

import ccamanager.ui.Ui;


/**
 * Command to view the list of all the residents
 */
public class ViewCcaExcoCommand extends Command {
    private String ccaName;

    public ViewCcaExcoCommand(String ccaName){
        this.ccaName = ccaName;
    }
    /**
     * Executes the <code>view-resident</code> command.
     * @param ccaManager      manages the list of CCAs
     * @param residentManager manages the list of Residents
     * @param ui              used to display output — ONLY class that should print
     */
    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equals(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));
            ui.showExcoList(cca.getExcos());
        } catch (CcaNotFoundException e) {
            ui.showError(e.getMessage());
        }
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
