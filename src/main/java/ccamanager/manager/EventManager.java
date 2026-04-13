package ccamanager.manager;

import ccamanager.exceptions.DuplicateEventException;
import ccamanager.exceptions.EventNotFoundException;
import ccamanager.model.Cca;
import ccamanager.model.Event;
import ccamanager.model.Resident;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {

    private static final Logger logger = Logger.getLogger(EventManager.class.getName());
    private final ArrayList<Event> events;


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
            logger.log(Level.WARNING, "Event already exists: " + eventName + " for " + cca.getName() + " on " +
                    eventDate);
            throw new DuplicateEventException("Event '" + eventName + "' already exists for this " +
                    "CCA on this date.");
        }

        events.add(new Event(eventName, cca, eventDate));
        logger.log(Level.INFO, "Successfully added event: {0}", eventName);
    }



    public void addResidentToEvent(String eventName, Cca cca, Resident resident) throws EventNotFoundException {
        Event event = events.stream()
                .filter(x -> x.getEventName()
                        .equalsIgnoreCase(eventName) && x.getCca().getName().equalsIgnoreCase(cca.getName()))
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException("Event " + eventName +
                        " not found for CCA " + cca.getName() + "."));
        
        event.addResident(resident);
        logger.log(Level.INFO, "Successfully added resident {0} to event {1}",
                new Object[]{resident.getName(), eventName});
    }

    public ArrayList<Event> getEventList() {
        return events;
    }

    public ArrayList<Event> viewMyEvents(String marticNumber){
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for(Event event: events){
            ArrayList<Resident> participants = event.getParticipants();
            for(Resident resident:participants){
                if(resident.getMatricNumber().equalsIgnoreCase(marticNumber)){
                    matchingEvents.add(event);
                    break;
                }
            }
        }

        return matchingEvents;
    }

    public ArrayList<Event> viewCcaEvents(String ccaName){
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for(int i =0;i< events.size();i++){
            Cca cca = events.get(i).getCca();
            if(cca.getName().equalsIgnoreCase(ccaName)){
                matchingEvents.add(events.get(i));
            }
        }
        return matchingEvents;
    }

    public Event findByNameAndCca(String eventName, String ccaName) {
        return events.stream()
                .filter(e -> e.getEventName().equalsIgnoreCase(eventName)
                        && e.getCca().getName().equalsIgnoreCase(ccaName))
                .findFirst().orElse(null);
    }
    
    public void removeEventsByCca(String ccaName) {
        events.removeIf(event -> event.getCca().getName().equalsIgnoreCase(ccaName));
    }
}

