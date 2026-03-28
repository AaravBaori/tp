package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;


/**
 * Command to view the list of all the residents
 */
public class ViewResidentCommand extends Command {

    /**
     * Executes the <code>view-resident</code> command.
     * @param ccaManager      manages the list of CCAs
     * @param residentManager manages the list of Residents
     * @param ui              used to display output — ONLY class that should print
     */
    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        ArrayList<Resident> residentList = residentManager.getResidentList();
        ui.showResidentList(residentList);
    }
}

