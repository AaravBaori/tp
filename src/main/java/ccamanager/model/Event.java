package ccamanager.model;

import java.util.ArrayList;

import ccamanager.exceptions.EventNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

import ccamanager.exceptions.ResidentAlreadyInEventException;

public class Event {
    private static final Logger logger = Logger.getLogger(Event.class.getName());

    private final String eventName;
    private final Cca cca;
    private final String eventDate;
    private ArrayList<Resident> participants;

    public Event(String eventName, Cca cca, String eventDate) {
        assert eventName != null && !eventName.isBlank() : "Event name should not be null or blank";
        assert cca != null : "CCA should not be null";
        assert eventDate != null && !eventDate.isBlank() : "Event date should not be null or blank";

        this.eventName = eventName;
        this.cca = cca;
        this.eventDate = eventDate;
        participants = new ArrayList<Resident>();
    }

    public void addResidentToEvent(Resident resident) throws EventNotFoundException, ResidentAlreadyInEventException {
        logger.log(Level.INFO, "Attempted to add " + resident.getMatricNumber()
                + " to " + eventName);

        assert participants != null : "Registered residents list should be initialized";
        assert resident != null : "Resident should not be null";

        boolean alreadyIn = participants.stream()
                .anyMatch(x -> x.getMatricNumber().equals(resident.getMatricNumber()));
        if (alreadyIn) {
            logger.log(Level.WARNING, "Resident " + resident.getMatricNumber()
                    + " already exists in CCA " + eventName );
            throw new ResidentAlreadyInEventException("Resident " + resident.getName()
                    + " is already a member of " + this.eventName + ".");
        }

        participants.add(resident);
        logger.log(Level.INFO, "Successfully added resident {0} to event {1}",
                new Object[]{resident.getName(), eventName});
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public Cca getCca() {
        return cca;
    }

    public ArrayList<Resident> getParticipants() {
        return participants;
    }

    @Override
    public String toString() {
        return "Event :" + eventName + " of the Cca " + cca + ". Held on " + eventDate + ".";
    }
}
