package ccamanager.manager;

import ccamanager.exceptions.CcaNotFoundException;
import ccamanager.model.Cca;
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
    public void addCCA(String ccaName) throws DuplicateCcaException {
        assert ccaList != null : "ccaList should be initialized";
        boolean isDuplicate = ccaList.stream()
                .anyMatch(x -> x.getName().equalsIgnoreCase(ccaName));
        if (isDuplicate) {
            throw new DuplicateCcaException("CCA " + ccaName + " already exists.");
        }
        ccaList.add(new Cca(ccaName));
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
     * Delete a CCA from the CCA list
     * @param ccaName Name of the CCA
     * @throws CcaNotFoundException Exception if invalid CCA name is given
     */
    public void deleteCca(String ccaName) throws CcaNotFoundException {
        for (int i = 0; i < ccaList.size(); i++) {
            if (ccaList.get(i).getName().equals(ccaName)) {
                ccaList.remove(i);
                logger.log(Level.INFO, "Successfully deleted CCA: {0}", ccaName);
                return;
            }
        }
        logger.log(Level.WARNING, "Failed to delete CCA: {0}. Not found in list.", ccaName);
        throw new CcaNotFoundException("The CCA " + ccaName + " does not exist, please enter a valid CCA name.");
    }
}
