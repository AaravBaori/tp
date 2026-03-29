package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

public class HelpCommand extends Command {

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        String help = "Here is a list of all commands:\n" +
                "> add-cca <cca name> <level (HIGH, MEDIUM, LOW or UNKNOWN)>\n" +
                "> view-cca\n" +
                "> delete-cca <cca name>\n" +
                "> add-event <event name> <cca name> <data time>\n" +
                "> add-resident <name> <matric number>\n" +
                "> view-resident\n" +
                "> add-resident-to-cca <matric number> <cca name> <points>\n" +
                "> add-resident-to-event <matric number> <event name> <cca name>\n" +
                "> view-points\n" +
                "> cca-stats\n" +
                "> resident-stats\n" +
                "> help\n" +
                "> bye";
        ui.showMessage(help);
    }
}
