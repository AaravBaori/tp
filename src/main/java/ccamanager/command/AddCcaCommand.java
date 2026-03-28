package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.InvalidCcaLevelException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

import ccamanager.exceptions.DuplicateCcaException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to create and add a new CCA to the CCA manager
 */
public class AddCcaCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddCcaCommand.class.getName());
    private String ccaName;
    private CcaLevel ccaLevel;

    public AddCcaCommand(String name, CcaLevel level) {
        assert name != null && !name.isBlank() : "CCA name cannot be null or empty";
        assert level != null : "CCA level cannot be null";

        this.ccaName = name;
        this.ccaLevel = level;

        logger.log(Level.INFO, "Created AddCcaCommand for {0} with level {1}",
                new Object[]{ccaName, ccaLevel});
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            ccaManager.addCCA(ccaName, ccaLevel);
            ui.showMessage("CCA added: " + ccaName + "(" + ccaLevel + ")");
        } catch (DuplicateCcaException | InvalidCcaLevelException e) {
            ui.showError(e.getMessage());
        }
    }
}

