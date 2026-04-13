package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteResidentCommandTest {

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
    public void execute_deleteResident_success() throws Exception {

        residentManager.addResident("John Doe", "A1234567X");
        DeleteResidentCommand command = new DeleteResidentCommand("A1234567X");

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(0, residentManager.getResidentList().size());
        assertEquals("Resident deleted: John Doe", ui.getLastMessage());

    }

    @Test
    public void execute_deleteResident_emptyList() {
        DeleteResidentCommand command = new DeleteResidentCommand("A1234567X");

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(0, residentManager.getResidentList().size());
        assertEquals("Resident with A1234567X does not exist", ui.getLastMessage());

    }

    @Test
    public void execute_deleteResident_invalidMatric() throws Exception {
        residentManager.addResident("John Doe", "A1234567X");
        DeleteResidentCommand command = new DeleteResidentCommand("A7654321Y");

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(1, residentManager.getResidentList().size());
        assertEquals("Resident with matric number A7654321Y does not exist", ui.getLastMessage());
    }
}
