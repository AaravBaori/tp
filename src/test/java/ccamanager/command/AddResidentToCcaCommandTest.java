package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.manager.EventManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddResidentToCcaCommandTest {

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
    void execute_addResidentToCca_success() {
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Resident John | A1234567B was added to CCA: Basketball with 10 points.",
                ui.getLastMessage());
    }

    @Test
    void execute_addResidentToCca_ccaNotFound() {
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Basketball not found.", ui.getLastMessage());
    }

    @Test
    void execute_addResidentToCca_residentNotFound() {
        new AddCcaCommand("Basketball", CcaLevel.MEDIUM).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("A1234567B not found.", ui.getLastMessage());
    }

    @Test
    void execute_addResidentToCca_alreadyInCca() {
        new AddCcaCommand("Basketball", CcaLevel.LOW).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Resident John is already a member of Basketball.", ui.getLastMessage());
    }
}
