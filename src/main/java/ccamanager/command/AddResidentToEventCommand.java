package ccamanager.command;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.EventNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInEventException;
import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Event;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to add a resident to an event.
 */
public class AddResidentToEventCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddResidentToEventCommand.class.getName());
    private String matricNumber;
    private String eventName;
    private String ccaName;

    public AddResidentToEventCommand(String matricNumber, String eventName, String ccaName) {
        assert matricNumber != null && !matricNumber.isBlank() : "Matric number cannot be null or empty";
        assert eventName != null && !eventName.isBlank() : "Event name cannot be null or empty";
        assert ccaName != null && !ccaName.isBlank() : "CCA name cannot be null or empty";

        this.matricNumber = matricNumber;
        this.eventName = eventName;
        this.ccaName = ccaName;

        logger.log(Level.INFO, "Created AddResidentToEventCommand for resident {0} to event {1} under CCA {2}",
                new Object[]{matricNumber, eventName, ccaName});
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Resident resident = residentManager.getResidentList().stream()
                    .filter(r -> r.getMatricNumber().equalsIgnoreCase(matricNumber))
                    .findFirst()
                    .orElseThrow(() -> new ResidentNotFoundException("Resident with matric number " +
                            matricNumber + " not found."));

            Cca cca = ccaManager.getCCAList().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException("CCA " + ccaName + " not found."));

            Event event = eventManager.getEventList().stream()
                    .filter(e -> e.getEventName().equalsIgnoreCase(eventName))
                    .filter(e -> e.getCca().getName().equalsIgnoreCase(ccaName))
                    .findFirst().orElseThrow(() -> new CcaNotFoundException("Event " + eventName + " not found."));

            event.addResidentToEvent(resident);

            ui.showMessage("Successfully added " + resident.getName() + " to event " +
                    eventName + " under CCA " + ccaName + ".");

        } catch (ResidentNotFoundException | CcaNotFoundException | EventNotFoundException
                 | ResidentAlreadyInEventException e) {
            ui.showError(e.getMessage());
        }
    }
}

