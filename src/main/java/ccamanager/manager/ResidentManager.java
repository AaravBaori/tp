package ccamanager.manager;

import ccamanager.model.Resident;

import ccamanager.exceptions.DuplicateResidentException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles adding, deleting, viewing and giving CCA points to residents.
 */
public class ResidentManager {
    private static final Logger logger = Logger.getLogger(ResidentManager.class.getName());
    private static ArrayList<Resident> residents;

    public ResidentManager() {
        residents = new ArrayList<>();
    }

    /**
     * Create a new resident object
     * @param residentName name of the resident
     * @param matricNumber unique matric number for the resident
     */
    public void addResident(String residentName, String matricNumber) throws DuplicateResidentException {
        boolean isDuplicate = residents.stream()
                .anyMatch(x -> x.getMatricNumber().equalsIgnoreCase(matricNumber));
        if (isDuplicate) {
            logger.log(Level.WARNING,"Resident with the same matric number " + matricNumber + " already exists.");
            throw new DuplicateResidentException("Resident with matric number " + matricNumber + " already exists.");
        }
        residents.add(new Resident(residentName, matricNumber));
        logger.log(Level.INFO, "Successfully added resident: {0}", residentName);
    }


    /**
     * Return the list of all the residents
     * @return List of residents
     */
    public ArrayList<Resident> getResidentList() {
        return residents;
    }
}
