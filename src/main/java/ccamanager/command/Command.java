package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;


public abstract class Command {

    /**
     * Executes the command.
     * All real logic goes here — call manager methods, then call ui methods to print.
     *
     * @param ccaManager      manages the list of CCAs
     * @param residentManager manages the list of Residents
     * @param ui              used to display output — ONLY class that should print
     */
    public abstract void execute(CcaManager ccaManager, ResidentManager residentManager,
                                 EventManager eventManager, Ui ui);

    /**
     * Returns true if this command should end the application loop.
     * Only ExitCommand should override this to return true.
     *
     * @return false by default
     */
    public boolean isExit() {
        return false;
    }
}

