package ccamanager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a hall resident.
 * Plain data model — fields only, no business logic.
 */
public class Resident {

    private String name;
    private String matricNumber;
    private ArrayList<Cca> ccaRegisteredIn = new ArrayList<Cca>();
    private ArrayList<Integer> points = new ArrayList<Integer>();

    /**
     * @param name the resident's full name, e.g. "John Tan"
     */
    public Resident(String name,String matricNumber) {
        assert name != null : "Resident name should not be null";
        assert matricNumber != null : "Matric number should not be null";
        this.name = name;
        this.matricNumber=matricNumber;
    }

    /**
     * @return the resident's name
     */
    public String getName() {
        assert name != null : "Resident name should not be null";
        return name;
    }

    /**
     * @param name the resident's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds residents to the CCA they registered for, defaults to 0 points
     * @param cca CCA to add residents in
     */
    public void addCcaToResident(Cca cca) {
        ccaRegisteredIn.add(cca);
        points.add(0);
    }

    /**
     * Adds residents to the CCA they registered for, with points
     * @param cca CCA object the resident is registered with
     * @param pointsEarned Number of points resident scored for the CCA
     */
    public void addCcaToResident(Cca cca, int pointsEarned) {
        assert cca != null : "CCA should not be null";
        assert pointsEarned >= 0 : "Points earned should be non-negative";
        assert ccaRegisteredIn != null : "CCA list should be initialized";
        assert points != null : "Points list should be initialized";

        ccaRegisteredIn.add(cca);
        points.add(pointsEarned);
    }

    /**
     * @return unique matric number of each student
     */
    public String getMatricNumber() {
        return matricNumber;
    }

    public ArrayList<Cca> getCcas(){
        return ccaRegisteredIn;
    }

    public ArrayList<Integer> getPoints(){
        return points;
    }

    /**
     * Returns a hashmap with the CCAs and the corresponding number of points that the resident has
     * @return a hashmap with the CCAs and the corresponding number of points that the resident has
     */
    public HashMap<Cca, Integer> getCcaPoints(){
        HashMap<Cca, Integer> map = new HashMap<>();
        for (int i = 0; i < ccaRegisteredIn.size(); i++) {
            map.put(ccaRegisteredIn.get(i), points.get(i));
        }
        return map;
    }

    /**
     * Returns an integer that is the sum of all points the resident have
     * @return aan integer that is the sum of all points the resident have
     */
    public int getTotalPoints() {
        int totalPoints=0;
        for(int i=0;i<ccaRegisteredIn.size();i++){
            totalPoints+= points.get(i);
        }
        return  totalPoints;
    }

    @Override
    public String toString() {
        return name + " | " + matricNumber;
    }
}
