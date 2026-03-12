package ccamanager.manager;

import ccamanager.model.Resident;

import java.util.ArrayList;

/**
 * Handles adding, deleting, viewing and giving CCA points to residents.
 */
public class ResidentManager {
    private static ArrayList<Resident> residents;

    public ResidentManager() {
        residents = new ArrayList<>();
    }

    /**
     * Create a new resident object
     * @param residentName name of the resident
     * @param matricNumber unique matric number for the resident
     */
    public static void addResident(String residentName, String matricNumber) {
        Resident resident  = new Resident(residentName,matricNumber);
        residents.add(resident);
    }


    /**
     * Return the list of all the residents
     * @return List of residents
     */
    public ArrayList<Resident> getResidentList() {
        return this.residents;
    }
}
