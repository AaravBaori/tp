package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Event;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;

/**
 * Command to view the list of all the events for one resident
 */
public class ViewMyEvents extends Command {
    private String matricNumber;

    public ViewMyEvents(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        ArrayList<Event> ccaEvents = eventManager.viewMyEvents(matricNumber);
        Resident resident = residentManager.matchingResident(matricNumber);
        if (resident == null) {
            ui.showMessage("You("+matricNumber+") are not registered as resident!");
        } else {
            if (ccaEvents.isEmpty()) {
                ui.showMessage("There is no event for you!");
            } else {
                ui.showMessage("Hi " + resident.getName() + ", here are your events: ");
                ui.viewMyCcas(ccaEvents);
            }
        }
    }
    @Override
    public boolean isReadOnly() {
        return true;
    }
}
