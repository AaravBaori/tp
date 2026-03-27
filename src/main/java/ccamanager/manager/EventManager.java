package ccamanager.manager;

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


    public void addEvent(String eventName, Cca cca, String eventDate) throws DuplicateResidentException {
        boolean isDuplicate = events.stream()
                .anyMatch(x -> x.getEventName().equalsIgnoreCase(eventName));
        if (isDuplicate) {
            logger.log(Level.WARNING,"Resident with the same matric number " + eventName + " already exists.");
            throw new DuplicateResidentException("Resident with matric number " + eventName + " already exists.");
        }
        events.add(new Event(eventName, cca, eventDate));
        logger.log(Level.INFO, "Successfully added resident: {0}", eventName);
    }
    
    public ArrayList<Event> getEventList() {
        return events;
    }
}
