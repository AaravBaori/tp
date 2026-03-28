package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

/**
 * UnknownCommand — returned by Parser when input doesn't match any known command.
 * Displays an error message. Teammates do not need to touch this file.
 */
public class UnknownCommand extends Command {
    private final String message;

    public UnknownCommand() {
        this.message = "Unknown command. Type 'help' for a list of commands.";
    }

    public UnknownCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        ui.showError(message);
    }
}

