package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddResidentCommandTest {

    @Test
    public void execute_addResident_success() {
        CcaManager ccaManager = new CcaManager();
        ResidentManager residentManager = new ResidentManager();
        Ui ui = new Ui();

        Command addResidentCommand = new AddResidentCommand("Veer","A12345");
        addResidentCommand.execute(ccaManager, residentManager, ui);

        boolean found = false;

        for (Resident resident : residentManager.getResidentList()) {
            if (resident.getName().equals("Veer") & resident.getMatricNumber().equals("A12345")) {
                found = true;
                break;
            }
        }

        assertTrue(found);


    }
}
