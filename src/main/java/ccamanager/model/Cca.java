package ccamanager.model;

import java.util.ArrayList;

import ccamanager.exceptions.ResidentAlreadyInCcaException;

import java.util.logging.Level;
import java.util.logging.Logger;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.ResidentAlreadyInExcoException;

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
    private CcaLevel level;
    private ArrayList<Resident> registeredResidents = new ArrayList<Resident>();
    private ArrayList<Resident> excoMembers = new ArrayList<Resident>();


    /**
     * @param name the name of the CCA, e.g. "Basketball"
     * @param level the level of the CCA, e.g. "HIGH"
     */
    public Cca(String name, CcaLevel level) {

        this.name = name;
        this.level = level;
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
     * @return the CCA level
     */
    public CcaLevel getLevel() {
        return level;
    }

    public ArrayList<Resident> getResidents(){
        return this.registeredResidents;
    }

    public ArrayList<Resident> getExcos(){
        return this.excoMembers;
    }
    /**
     * @param level the level of the CCA
     */
    public void setLevel(CcaLevel level){
        assert level != null : "CCA level should not be null";
        this.level = level;
    }

    /**
     * Returns the registered residents
     * @return a list of residents enrolled in the CCA
     */
    public ArrayList<Resident> getRegisteredResidents() {
        return registeredResidents;
    }

    /**
     * Add resident to a CCA
     * @param resident the resident to be added
     */
    public void addResidentToCca(Resident resident) throws ResidentAlreadyInCcaException {

        logger.log(Level.INFO, "Attempted to add " + resident.getMatricNumber()
                + " to " + name );

        assert registeredResidents != null : "Registered residents list should be initialized";
        assert resident != null : "Resident should not be null";

        boolean alreadyIn = registeredResidents.stream()
                .anyMatch(x -> x.getMatricNumber().equals(resident.getMatricNumber()));
        if (alreadyIn) {
            logger.log(Level.WARNING, "Resident " + resident.getMatricNumber()
                    + " already exists in CCA " + name );
            throw new ResidentAlreadyInCcaException("Resident " + resident.getName()
                    + " is already a member of " + this.name + ".");
        }
        registeredResidents.add(resident);
        logger.log(Level.INFO, "Resident " + resident.getMatricNumber()
                + " added successfully to the CCA " + name);
    }

    public void addExcoToCca(Resident resident) throws ResidentAlreadyInExcoException, ResidentAlreadyInCcaException {

        logger.log(Level.INFO, "Attempted to add " + resident.getMatricNumber()
                + " as an EXCO to " + name );

        assert registeredResidents != null : "Registered residents list should be initialized";
        assert resident != null : "Resident should not be null";

        boolean alreadyInExco = excoMembers.stream()
                .anyMatch(x -> x.getMatricNumber()
                .equals(resident.getMatricNumber()));
        boolean alreadyIn = registeredResidents.stream()
                .anyMatch(x -> x.getMatricNumber()
                        .equals(resident.getMatricNumber()));
        if (alreadyInExco) {
            logger.log(Level.WARNING, "EXCO member " + resident.getMatricNumber()
                    + " already exists as an EXCO in CCA " + name );
            throw new ResidentAlreadyInExcoException("Resident " + resident.getName()
                    + " is already a EXCO of " + this.name + ".");
        }
        if (!alreadyIn) {
            this.addResidentToCca(resident);
            this.excoMembers.add(resident);
        }else{
            this.excoMembers.add(resident);
        }

        logger.log(Level.INFO, "Resident " + resident.getMatricNumber()
                + " added successfully as an EXCO of the CCA " + name);
    }

    /**
     * @return the CCA name as a string
     */
    @Override
    public String toString() {
        return this.name + "(" + this.level + "): " + this.registeredResidents.size() + " residents";
    }
}

