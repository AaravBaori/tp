package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.manager.EventManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCcaCommandTest {

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
    void execute_deleteCca_success() {
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new DeleteCcaCommand("Basketball").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals(0, ccaManager.getCCAList().size());
        assertEquals("CCA deleted: Basketball", ui.getLastMessage());
    }

    @Test
    void execute_deleteCca_invalidName() {
        new DeleteCcaCommand("Basketball").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("The CCA Basketball does not exist, please enter a valid CCA name.",
                ui.getLastMessage());
    }

    @Test
    void execute_deleteCca_emptyList() {
        new DeleteCcaCommand("Basketball").execute(ccaManager, residentManager, eventManager, ui);
        assertEquals(0, ccaManager.getCCAList().size());
        assertEquals("The CCA Basketball does not exist, please enter a valid CCA name.",
                ui.getLastMessage());
    }
}
