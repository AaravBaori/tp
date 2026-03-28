package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.manager.EventManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddResidentCommandTest {

    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;
    private Ui ui;

    @BeforeEach
    void setUp() {
        ccaManager = new CcaManager();
        residentManager = new ResidentManager();
        eventManager = new EventManager();
        ui = new Ui();
    }

    @Test
    void execute_addResident_success() {
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals(1, residentManager.getResidentList().size());
        assertEquals("John", residentManager.getResidentList().get(0).getName());
    }

    @Test
    void execute_addDuplicateResident_showsError() {
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals(1, residentManager.getResidentList().size());
        assertEquals("Resident with matric number A1234567B already exists.", ui.getLastMessage());
    }

    @Test
    void execute_addDuplicateMatricDifferentName_showsError() {
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("Jane", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals(1, residentManager.getResidentList().size());
        assertEquals("Resident with matric number A1234567B already exists.", ui.getLastMessage());
    }
}
