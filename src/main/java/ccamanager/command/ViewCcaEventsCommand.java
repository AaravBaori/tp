package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import ccamanager.model.Event;
import ccamanager.model.Cca;
import java.util.ArrayList;

/**
 * Command to view the list of all the events for one CCA
 */
public class ViewCcaEventsCommand extends Command{
    private final String ccaName;

    public ViewCcaEventsCommand(String ccaName){
        this.ccaName=ccaName;
    }

    public String getCcaName(){
        return ccaName;
    }
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        Cca matchingCca = ccaManager.findByName(ccaName);
        if (matchingCca == null) {
            ui.showMessage("There is no matching CCA named " + ccaName);
        } else {
            ArrayList<Event> ccaEvents = eventManager.viewCcaEvents(ccaName);
            if (ccaEvents.isEmpty()) {
                ui.showMessage("There is no event for your CCA");
            } else {
                ui.viewMatchingCcas(ccaEvents);
            }
        }
    }
    @Override
    public boolean isReadOnly() {
        return true;
    }
}
