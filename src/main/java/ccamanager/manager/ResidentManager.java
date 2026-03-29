package ccamanager.manager;


import ccamanager.exceptions.ResidentNotFoundException;
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

    /**
     * Return the resident that have the matching matric number
     * @param matricNumber matric number of that resident
     * @return Resident
     */
    public Resident matchingResident(String matricNumber){
        for(Resident resident : residents){
            if(resident.getMatricNumber().equalsIgnoreCase(matricNumber)) {
                return resident;
            }
        }
        return null;
    }

    public String nameGivenMatricNumber(String matricNumber){
        Resident resident= this.matchingResident(matricNumber);
        return resident.getName();
    }

    /**
     * Delete a resident
     * @param matricNumber Name of the CCA
     * @throws ResidentNotFoundException Exception if invalid CCA name is given
     */
    public void deleteResident(String matricNumber) throws ResidentNotFoundException {

        String residentName = this.nameGivenMatricNumber(matricNumber);
        logger.log(Level.INFO,"Attempted to delete resident: {0}", residentName);

        assert residents != null : "ccaList should be initialized";
        assert residentName != null : "CCA name should not be null";
        assert !residentName.isBlank() : "CCA name should not be blank";

        for (int i = 0; i < residents.size(); i++) {
            if (residents.get(i).getMatricNumber().equalsIgnoreCase(matricNumber)) {
                int oldSize = residents.size();
                residents.remove(i);
                assert residents.size() == oldSize - 1 : "CCA list size should decrease by 1 after deletion";

                logger.log(Level.INFO, "Successfully deleted resident: {0}", residentName);
                return;
            }
        }
        logger.log(Level.WARNING, "Failed to delete resident: {0}. (Not an exisitng resident).", residentName);
        throw new ResidentNotFoundException(residentName + " does not exist, please enter a valid CCA name.");
    }
}
