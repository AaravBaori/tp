package ccamanager.model;

import java.util.ArrayList;

import ccamanager.exceptions.ResidentAlreadyInCcaException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a Co-Curricular Activity (CCA).
 * Plain data model — fields only, no business logic.
 * Add new fields (e.g. description, category) here if needed; logic goes in CcaManager.
 */
public class Cca {
    private static final Logger logger = Logger.getLogger(Cca.class.getName());

    /**
     * The display name of this CCA.
     */
    private String name;
    private ArrayList<Resident> registeredResidents = new ArrayList<Resident>();


    /**
     * @param name the name of the CCA, e.g. "Basketball"
     */
    public Cca(String name) {
        this.name = name;
    }

    /**
     * @return the CCA name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the new CCA name
     */
    public void setName(String name) {
        assert name != null : "CCA name should not be null";
        this.name = name;
    }

    /**
     * Add resident to a CCA
     * @param resident the resident to be added
     */
    public void addResidentToCca(Resident resident) throws ResidentAlreadyInCcaException {

        logger.log(Level.INFO, "Attempted to add " + resident.getMatricNumber() + " to " + name );

        assert registeredResidents != null : "Registered residents list should be initialized";
        assert resident != null : "Resident should not be null";

        boolean alreadyIn = registeredResidents.stream()
                .anyMatch(x -> x.getMatricNumber().equals(resident.getMatricNumber()));
        if (alreadyIn) {
            logger.log(Level.WARNING, "Resident " + resident.getMatricNumber() + " already exists in CCA " + name );
            throw new ResidentAlreadyInCcaException("Resident " + resident.getName()
                    + " is already a member of " + this.name + ".");
        }
        registeredResidents.add(resident);
        logger.log(Level.INFO, "Resident " + resident.getMatricNumber() + " added successfully to the CCA " + name);
    }

    /**
     * @return the CCA name as a string
     */
    @Override
    public String toString() {
        return name;
    }
}
