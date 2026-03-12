package ccamanager.model;

import java.util.ArrayList;

/**
 * Represents a Co-Curricular Activity (CCA).
 * Plain data model — fields only, no business logic.
 * Add new fields (e.g. description, category) here if needed; logic goes in CcaManager.
 */
public class Cca {

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
        this.name = name;
    }

    /**
     * Add resident to a CCA
     * @param resident the resident to be added
     */
    public void addResidentToCca(Resident resident) {
        registeredResidents.add(resident);
    }

    /**
     * @return the CCA name as a string
     */
    @Override
    public String toString() {
        return name;
    }
}
