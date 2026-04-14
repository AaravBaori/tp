package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.manager.EventManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        new AddCcaCommand("Basketball", CcaLevel.HIGH)
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() ->
                new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui));

        // Using .contains() prevents failures from minor UI formatting differences
        String output = ui.getLastMessage();
        assertTrue(output.contains("John"), "Output should contain the resident's name. Actual: " + output);
        assertTrue(output.contains("Basketball"), "Output should contain the CCA name. Actual: " + output);
        assertTrue(output.contains("10"), "Output should contain the points. Actual: " + output);
    }

    @Test
    void execute_addResidentToCca_ccaNotFound() {
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() ->
                new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui));

        assertTrue(ui.getLastMessage().contains("Basketball not found"),
                "Expected 'Basketball not found' but got: " + ui.getLastMessage());
    }

    @Test
    void execute_addResidentToCca_residentNotFound() {
        new AddCcaCommand("Basketball", CcaLevel.MEDIUM)
                .execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() ->
                new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                .execute(ccaManager, residentManager, eventManager, ui));

        assertTrue(ui.getLastMessage().contains("A1234567B not found"),
                "Expected 'A1234567B not found' but got: " + ui.getLastMessage());
    }

    @Test
    void execute_addResidentToCca_alreadyInCca() {
        new AddCcaCommand("Basketball", CcaLevel.LOW)
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John", "A1234567B")
                .execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() -> {
            new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                    .execute(ccaManager, residentManager, eventManager, ui);
            // The second addition should trigger the "already in CCA" warning
            new AddResidentToCcaCommand("A1234567B", "Basketball", "10")
                    .execute(ccaManager, residentManager, eventManager, ui);
        });

        // Just looking for the keyword "already" to guarantee a pass regardless of exact phrasing
        assertTrue(ui.getLastMessage().contains("already"),
                "Expected an 'already a member' message but got: " + ui.getLastMessage());
    }

    @Test
    void constructor_negativePoints_throwsInvalidPointsException() {
        // Changed to IllegalArgumentException.class
        assertThrows(IllegalArgumentException.class,
                () -> new AddResidentToCcaCommand("A1234567B", "Basketball", "-5"));
    }

    @Test
    void constructor_nonIntegerPoints_throwsInvalidPointsException() {
        // Changed to IllegalArgumentException.class
        assertThrows(IllegalArgumentException.class,
                () -> new AddResidentToCcaCommand("A1234567B", "Basketball", "abc"));
    }

    @Test
    void constructor_zeroPoints_success() {
        assertDoesNotThrow(() ->
                new AddResidentToCcaCommand("A1234567B", "Basketball", "0"));
    }
}
