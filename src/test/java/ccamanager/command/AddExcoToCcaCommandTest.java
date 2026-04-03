package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.manager.EventManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddExcoToCcaCommandTest {
    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;
    private Ui ui;

    @BeforeEach
    void setup(){
        this.ccaManager = new CcaManager();
        this.residentManager = new ResidentManager();
        this.eventManager = new EventManager();
        this.ui = new Ui();
    }

    @Test
    void executeAddExcoToCcasuccess(){
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddExcoToCcaCommand("A1234567B", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Resident John | A1234567B was added as an EXCO to CCA: Basketball",
                ui.getLastMessage());
    }

    @Test
    void executeAddExcoToCcaCommandccaNotFound(){
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddExcoToCcaCommand("A1234568B", "Football")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Football not found.", ui.getLastMessage());
    }

    @Test
    void executeAddExcoToCcaresidentNotFound(){
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddExcoToCcaCommand("A1234568A", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("A1234568A not found.", ui.getLastMessage());
    }

    @Test
    void executeAddExcoToCcaResidentAlreadyInExco(){
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddExcoToCcaCommand("A1234567B", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddExcoToCcaCommand("A1234567B", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);
        assertEquals("Resident John is already a EXCO of Basketball.", ui.getLastMessage());
    }
}
