package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.InvalidPointsException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewMyEventsCommandTest {

    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;
    private Ui ui;

    // This is our secret terminal recorder
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        ccaManager = new CcaManager();
        residentManager = new ResidentManager();
        eventManager = new EventManager();
        ui = new Ui();

        // Hijack the console output before each test starts
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Give the console back to Java after each test finishes
        System.setOut(originalOut);
    }

    @Test
    void execute_viewMyEvents_residentNotFound() {
        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = ui.getLastMessage();
        // Updated to match your exact UI output
        assertTrue(output.contains("not found"),
                "Expected a 'not found' error but got: " + output);
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
    void execute_viewMyEvents_oneMatchingEvent() throws InvalidPointsException {
        // Setup
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John Doe", "A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui);

        // ---> NEW FIX: Add John to the CCA first! <---
        new AddResidentToCcaCommand("A1234567X", "Basketball", "0")
                .execute(ccaManager, residentManager, eventManager, ui);

        new AddEventCommand("Training", "Basketball", "2026-04-03")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToEventCommand("A1234567X", "Training", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);

        // Clear out the setup logs so we ONLY test the ViewMyEventsCommand output
        outContent.reset();

        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = outContent.toString();

        assertTrue(output.contains("Training"),
                "Output should contain the event name. Actual output: [" + output + "]");
        assertTrue(output.contains("2026-04-03"),
                "Output should contain the date. Actual output: [" + output + "]");
    }

    @Test
    void execute_viewMyEvents_multipleMatchingEvents() throws InvalidPointsException {
        // Setup
        new AddCcaCommand("Basketball", CcaLevel.HIGH).execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentCommand("John Doe", "A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui);

        // ---> NEW FIX: Add John to the CCA first! <---
        new AddResidentToCcaCommand("A1234567X", "Basketball", "0")
                .execute(ccaManager, residentManager, eventManager, ui);

        new AddEventCommand("Training", "Basketball", "2026-04-03")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddEventCommand("Game", "Basketball", "2026-04-04")
                .execute(ccaManager, residentManager, eventManager, ui);

        new AddResidentToEventCommand("A1234567X", "Training", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);
        new AddResidentToEventCommand("A1234567X", "Game", "Basketball")
                .execute(ccaManager, residentManager, eventManager, ui);

        outContent.reset();

        assertDoesNotThrow(() -> new ViewMyEventsCommand("A1234567X")
                .execute(ccaManager, residentManager, eventManager, ui));

        String output = outContent.toString();

        assertTrue(
                output.contains("Training"),
                "Output should contain the first event 'Training'. Actual: [" + output + "]");
        assertTrue(
                output.contains("Game"),
                "Output should contain the second event 'Game'. Actual: [" + output + "]");
    }
}
