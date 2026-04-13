package ccamanager.command;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.DuplicateEventException;
import ccamanager.exceptions.InvalidEventName;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to create and add a new Event to the Event manager
 */
public class AddEventCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddEventCommand.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String eventName;
    private String ccaName;
    private String dateTime;

    public AddEventCommand(String eventName, String ccaName, String dateTime) {
        assert eventName != null && !eventName.isBlank() : "Event name cannot be null or empty";
        assert ccaName != null && !ccaName.isBlank() : "CCA name cannot be null or empty";
        assert dateTime != null && !dateTime.isBlank() : "Date/Time of Event cannot be null or empty";

        this.eventName = eventName;
        this.ccaName = ccaName;
        this.dateTime = dateTime;

        logger.log(Level.INFO, "Created AddEventCommand for {0} for the CCA {1}, during {2}",
                new Object[]{eventName, ccaName, dateTime});
    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            Cca cca = ccaManager.getCCAList().stream()
                    .filter(x -> x.getName().equalsIgnoreCase(ccaName))
                    .findFirst()
                    .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

            if (eventName.isBlank()) {
                throw new InvalidEventName("Please enter a valid event name.");
            }

            validateDate(dateTime);

            eventManager.addEvent(eventName, cca, dateTime);
            ui.showMessage("Event added: " + eventName + " for the CCA " + ccaName + ", during " + dateTime);

        } catch (CcaNotFoundException | DuplicateEventException | InvalidEventName | IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private void validateDate(String dateTime) {
        try {
            LocalDate.parse(dateTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Please enter a valid date in YYYY-MM-DD format.");
        }
    }
}

