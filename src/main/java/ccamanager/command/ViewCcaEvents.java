package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import ccamanager.model.Event;
import java.util.ArrayList;

/**
 * Command to view the list of all the events for one CCA
 */
public class ViewCcaEvents extends Command{
    private final String ccaName;

    public ViewCcaEvents(String ccaName){
        this.ccaName=ccaName;
    }

    public String getCcaName(){
        return ccaName;
    }
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui){
        ArrayList <Event> ccaEvents = eventManager.viewCcaEvents(ccaName);
        ui.viewMatchingCcas(ccaEvents);
    }
}
