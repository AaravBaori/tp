package ccamanager;

import ccamanager.command.Command;
import ccamanager.exceptions.EventNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInEventException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.parser.Parser;
import ccamanager.storage.StorageManager;
import ccamanager.ui.Ui;

import java.io.IOException;

/**
 * CcaLedger — the orchestrator of the application.
 * <p>
 * Responsibilities:
 * - Owns the main run loop
 * - Wires together Parser, Ui, CcaManager, ResidentManager, EventManager, StorageManager
 * - Calls Parser to get a Command, then calls command.execute()
 * - Saves to disk after every mutating command
 * <p>
 * DO NOT add business logic here. This class only coordinates.
 */
public class CcaLedger {

    private final CcaManager      ccaManager;
    private final ResidentManager residentManager;
    private final EventManager    eventManager;
    private final Ui              ui;
    private final Parser          parser;
    private final StorageManager  storage;

    public CcaLedger() {
        this.ccaManager      = new CcaManager();
        this.residentManager = new ResidentManager();
        this.eventManager    = new EventManager();
        this.ui              = new Ui();
        this.parser          = new Parser();
        this.storage         = new StorageManager();
    }

    /**
     * Starts the application loop.
     * Loads saved data → shows welcome → reads input → parses → executes →
     * saves (if mutating) → repeats until exit.
     */
    public void run() {
        // Restore persisted state before showing the welcome screen.
        // A missing data directory (first run) is handled gracefully inside load().
        try {
            storage.load(ccaManager, residentManager, eventManager);
        } catch (IOException e) {
            ui.showError("Failed to load saved data: " + e.getMessage()
                    + " — starting with empty state.");
        } catch (EventNotFoundException | ResidentAlreadyInEventException e) {
            throw new RuntimeException(e);
        }

        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning) {
            String  input   = ui.readInput();
            Command command = parser.parse(input);
            command.execute(ccaManager, residentManager, eventManager, ui);

            // Only write to disk when something could have changed.
            // View/stats commands override isReadOnly() to return true.
            if (!command.isReadOnly()) {
                try {
                    storage.save(ccaManager, residentManager, eventManager);
                } catch (IOException e) {
                    ui.showError("Failed to save data: " + e.getMessage());
                }
            }

            isRunning = !command.isExit();
        }

        ui.showGoodbye();
    }
}
