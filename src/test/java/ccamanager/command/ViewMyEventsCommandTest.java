package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewMyEventsCommandTest {

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
    void execute_viewMyEvents_residentNotFound() {
        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = ui.getLastMessage();
        // Updated to match your exact UI output
        assertTrue(output.contains("not registered as resident"),
                "Expected a 'not registered' error but got: " + output);
    }

    @Test
    void execute_viewMyEvents_noEvents() {
        new AddResidentCommand("John Doe", "A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = ui.getLastMessage();
        // Updated to match your exact UI output
        assertTrue(output.contains("There is no event for you!"),
                "Expected a 'no event' message but got: " + output);
    }

    @Test
    void execute_viewMyEvents_oneMatchingEvent() {
        // Setup
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John Doe", "A1234567X").execute(ccaManager, residentManager, eventManager, ui);
        new AddEventCommand("Training", "Basketball", "2026-04-03").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToEventCommand("A1234567X", "Training", "Basketball").execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = ui.getLastMessage();

        // I added the "Actual output: " string so if it fails, you can see EXACTLY why!
        assertTrue(output.contains("Training"), "Output should contain the event name. Actual output: [" + output + "]");
        assertTrue(output.contains("2026-04-03"), "Output should contain the date. Actual output: [" + output + "]");
    }

    @Test
    void execute_viewMyEvents_multipleMatchingEvents() {
        // Setup
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John Doe", "A1234567X").execute(ccaManager, residentManager, eventManager, ui);

        new AddEventCommand("Training", "Basketball", "2026-04-03").execute(ccaManager, residentManager, eventManager, ui);
        new AddEventCommand("Game", "Basketball", "2026-04-04").execute(ccaManager, residentManager, eventManager, ui);

        new AddResidentToEventCommand("A1234567X", "Training", "Basketball").execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToEventCommand("A1234567X", "Game", "Basketball").execute(ccaManager, residentManager, eventManager, ui);

        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = ui.getLastMessage();

        // I added the "Actual output: " string here too
        assertTrue(output.contains("Training"), "Output should contain the first event 'Training'. Actual: [" + output + "]");
        assertTrue(output.contains("Game"), "Output should contain the second event 'Game'. Actual: [" + output + "]");
    }
}