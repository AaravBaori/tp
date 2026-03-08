package ccamanager.manager;

import ccamanager.model.Cca;

import java.util.ArrayList;

public class CcaManager {
    private ArrayList<Cca> CCAList =  new ArrayList<>();

    public void addCCA(String ccaName) {
        Cca cca = new Cca(ccaName);
        CCAList.add(cca);
    }
}
