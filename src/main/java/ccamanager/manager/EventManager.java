package ccamanager.manager;

import ccamanager.exceptions.DuplicateEventException;
import ccamanager.exceptions.DuplicateResidentException;
import ccamanager.model.Cca;
import ccamanager.model.Event;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {

    private static ArrayList<Event> events;
    private static final Logger logger = Logger.getLogger(EventManager.class.getName());


    public EventManager() {
        events = new ArrayList<>();
    }

    /**
     * Add a new event and store it in the events
     * @param eventName Name of the event
     * @param cca The CCA event is added in
     * @param eventDate Date or time of the event
     * @throws DuplicateEventException Handles the duplicate events
     */
    public void addEvent(String eventName, Cca cca, String eventDate) throws DuplicateEventException {
        boolean isDuplicate = events.stream()
                .anyMatch(x -> x.getEventName().equalsIgnoreCase(eventName)
                        && x.getCca().equals(cca)
                        && x.getEventDate().equals(eventDate));

        if (isDuplicate) {
            logger.log(Level.WARNING, "Event already exists: " + eventName + " for " + cca.getName() + " on " + eventDate);
            throw new DuplicateEventException("Event '" + eventName + "' already exists for this CCA on this date.");
        }

        events.add(new Event(eventName, cca, eventDate));
        logger.log(Level.INFO, "Successfully added event: {0}", eventName);
    }

    
    public ArrayList<Event> getEventList() {
        return events;
    }
}
