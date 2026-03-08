package ccamanager.manager;

import ccamanager.model.Resident;

import java.util.ArrayList;

/**
 * Handles adding, deleting, viewing and giving CCA points to residents.
 */
public class ResidentManager {
    private ArrayList<Resident> residents;

    public ResidentManager() {
        residents = new ArrayList<>();
    }

    // TODO: Implement adding (Aarav), deleting (Yi Yang) and giving CCA points (Smeet) to residents.

    /**
     * Return the list of all the residents
     * @return List of residents
     */
    public ArrayList<Resident> getResidentList() {
        return this.residents;
    }
}
