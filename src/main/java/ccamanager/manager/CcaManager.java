package ccamanager.manager;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.exceptions.InvalidCcaLevelException;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import java.util.logging.Level;
import java.util.logging.Logger;

import ccamanager.exceptions.DuplicateCcaException;

import java.util.ArrayList;


public class CcaManager {
    private static final Logger logger = Logger.getLogger(CcaManager.class.getName());
    private ArrayList<Cca> ccaList =  new ArrayList<>();


    /**
     * Creates and adds the CCA to CCAList
     * @param ccaName Name of the CCA
     */
    public void addCCA(String ccaName, CcaLevel ccaLevel) throws DuplicateCcaException, InvalidCcaLevelException {
        logger.log(Level.INFO,"Attempting to add CCA: " + ccaName + "(" + ccaLevel + ")");

        assert !ccaName.isBlank() : "CCA name should not be blank";
        assert !ccaLevel.toString().isBlank() : "CCA level should not be blank";
        assert ccaList != null : "ccaList should be initialized";

        boolean isDuplicate = ccaList.stream()
                .anyMatch(x -> x.getName().equalsIgnoreCase(ccaName));
        if (isDuplicate) {
            logger.log(Level.WARNING,"Failed to add CCA (duplicate) :" + ccaName);
            throw new DuplicateCcaException("CCA " + ccaName + " already exists.");
        }
        if(ccaLevel == CcaLevel.UNKNOWN){
            throw new InvalidCcaLevelException("Could not add CCA: Invalid level provided.");
        }

        int oldSize = ccaList.size();
        ccaList.add(new Cca(ccaName, ccaLevel));
        assert ccaList.size() == oldSize + 1 : "CCA list size should increase by 1 after adding";

        logger.log(Level.INFO, "Successfully added CCA: {0}", ccaName);
    }

    /**
     * Return the list of all the CCAs
     * @return List of CCAs
     */
    public ArrayList<Cca> getCCAList() {
        assert ccaList != null : "ccaList should be initialized";
        return ccaList;
    }

    /**
     * Removes a resident from all CCAs they are registered in,
     * including from the exco list if applicable.
     * Should be called before deleting a resident to avoid stale references.
     *
     * @param resident the resident to remove from all CCAs
     */
    public void removeResidentFromAllCcas(Resident resident) {
        assert resident != null : "Resident should not be null";
        for (Cca cca : ccaList) {
            cca.getResidents().remove(resident);
            cca.getExcos().remove(resident);
        }
    }

    /**
     * Delete a CCA from the CCA list
     * @param ccaName Name of the CCA
     * @throws CcaNotFoundException Exception if invalid CCA name is given
     */
    public void deleteCca(String ccaName) throws CcaNotFoundException {

        logger.log(Level.INFO,"Attempted to delete CCA: {0}", ccaName);

        assert ccaList != null : "ccaList should be initialized";
        assert ccaName != null : "CCA name should not be null";
        assert !ccaName.isBlank() : "CCA name should not be blank";

        for (int i = 0; i < ccaList.size(); i++) {
            if (ccaList.get(i).getName().equalsIgnoreCase(ccaName)) {
                int oldSize = ccaList.size();
                ccaList.remove(i);
                assert ccaList.size() == oldSize - 1 : "CCA list size should decrease by 1 after deletion";

                logger.log(Level.INFO, "Successfully deleted CCA: {0}", ccaName);
                return;
            }
        }
        logger.log(Level.WARNING, "Failed to delete CCA: {0}. (Not an exisitng CCA).", ccaName);
        throw new CcaNotFoundException("The CCA " + ccaName + " does not exist, please enter a valid CCA name.");
    }

    public Cca findByName(String name) {
        return ccaList.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
