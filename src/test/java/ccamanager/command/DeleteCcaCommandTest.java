package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import ccamanager.model.Cca;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeleteCcaCommandTest {

    @Test
    public void execute_deleteCca_success() {

        CcaManager ccaManager = new CcaManager();
        ResidentManager residentManager = new ResidentManager();
        Ui ui = new Ui();

        AddCcaCommand addCca = new AddCcaCommand("Basketball");

        addCca.execute(ccaManager, residentManager, ui);

        DeleteCcaCommand deleteCca = new DeleteCcaCommand("Basketball");

        deleteCca.execute(ccaManager, residentManager, ui);

        boolean found = false;

        for (Cca cca : ccaManager.getCCAList()) {
            if (cca.getName().equals("Basketball")) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }

    @Test
    public void execute_deleteCca_invalidName() {

        CcaManager ccaManager = new CcaManager();
        ResidentManager residentManager = new ResidentManager();
        Ui ui = new Ui();

        DeleteCcaCommand deleteCca = new DeleteCcaCommand("Basketball");
        deleteCca.execute(ccaManager, residentManager, ui);

        assertEquals("The CCA " + "Basketball" + " does not exist, please enter a valid " +
                "CCA name.", ui.getLastMessage());

    }
}

