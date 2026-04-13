package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

public class HelpCommand extends Command {

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- CCA Manager Help Menu ---\n\n");

        sb.append("[CCA Management]\n");
        sb.append("> add-cca <name>; <level (HIGH, MEDIUM, LOW)>\n");
        sb.append("> delete-cca <name>\n");
        sb.append("> view-cca\n");
        sb.append("> add-exco-to-cca <matric>; <cca name>\n");
        sb.append("> view-exco <cca name>\n");
        sb.append("> cca-stats\n\n");

        sb.append("[Resident Management]\n");
        sb.append("> add-resident <name>; <matric>\n");
        sb.append("> delete-resident <matric>\n");
        sb.append("> view-resident\n");
        sb.append("> add-resident-to-cca <matric>; <cca name>; <points>\n");
        sb.append("> view-points\n");
        sb.append("> sort-points\n");
        sb.append("> update-point <matric>; <cca name>; <points>\n");
        sb.append("> resident-stats\n\n");

        sb.append("[Event Management]\n");
        sb.append("> add-event <name>; <cca name>; <date (YYYY-MM-DD)>\n");
        sb.append("> add-resident-to-event <matric>; <event name>; <cca name>\n");
        sb.append("> view-cca-events <cca name>\n");
        sb.append("> view-my-events <matric>\n\n");

        sb.append("[General]\n");
        sb.append("> help\n");
        sb.append("> bye");

        ui.showMessage(sb.toString());
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
