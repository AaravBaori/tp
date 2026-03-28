package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

/**
 * ExitCommand — signals the run loop to terminate.
 * Triggered by the "bye" command word in Parser.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        // Nothing to do — CcaLedger will call ui.showGoodbye() after checking isExit()
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

