package ccamanager;

import ccamanager.command.Command;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.parser.Parser;
import ccamanager.ui.Ui;

/**
 * CcaLedger — the orchestrator of the application.
 * <p>
 * Responsibilities:
 * - Owns the main run loop
 * - Wires together Parser, Ui, CcaManager, ResidentManager
 * - Calls Parser to get a Command, then calls command.execute()
 * <p>
 * DO NOT add business logic here. This class only coordinates.
 */
public class CcaLedger {

    private final CcaManager ccaManager;
    private final ResidentManager residentManager;
    private final EventManager eventManager;
    private final Ui ui;
    private final Parser parser;

    public CcaLedger() {
        this.ccaManager = new CcaManager();
        this.residentManager = new ResidentManager();
        this.eventManager = new EventManager();
        this.ui = new Ui();
        this.parser = new Parser();
    }

    /**
     * Starts the application loop.
     * Reads input → parses to Command → executes → repeats until exit.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readInput();
            Command command = parser.parse(input);
            command.execute(ccaManager, residentManager, eventManager, ui);
            isRunning = !command.isExit();
        }

        ui.showGoodbye();
    }
}
