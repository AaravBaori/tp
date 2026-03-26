package ccamanager.parser;

import ccamanager.command.AddCcaCommand;
import ccamanager.command.AddResidentCommand;
import ccamanager.command.AddResidentToCcaCommand;
import ccamanager.command.ExitCommand;
import ccamanager.command.Command;
import ccamanager.command.UnknownCommand;
import ccamanager.command.DeleteCcaCommand;
import ccamanager.command.ViewCcaCommand;
import ccamanager.command.ViewResidentCommand;
import ccamanager.command.ViewPointsCommand;
import ccamanager.enumerations.CcaLevel;
import ccamanager.model.Cca;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parser — reads raw user input and returns the appropriate Command object.
 **/

public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses the user's input string and returns the matching Command.
     *
     * @param input the raw input string from the user
     * @return the appropriate Command object to execute
     */
    public Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new UnknownCommand();
        }



        String[] parts = input.trim().split("\\s+");
        String commandWord = parts[0].toLowerCase();

        assert parts.length > 0 : "Non-blank input should produce at least one token";



        switch (commandWord) {
        case "add-cca":
            if (parts.length < 3 || parts[1].isBlank() || parts[2].isBlank()) {
                return new UnknownCommand("Usage: add-cca <cca name> <level>");
            }
            String name = parts[1];
            CcaLevel level = getCcaLevel(parts[2]);
            return new AddCcaCommand(name, level);

        case "view-cca":
            return new ViewCcaCommand();

        case "delete-cca":
            if (parts.length < 2 || parts[1].isBlank()) {
                return new UnknownCommand("Usage: delete-cca <cca name>");
            }

            return new DeleteCcaCommand(parts[1]);

        case "bye":
            return new ExitCommand();

        case "view-resident":
            return new ViewResidentCommand();

        case "add-resident":
            if (parts.length < 3) {
                return new UnknownCommand("Usage: add-resident <name> <matric number>");
            }
            if (parts[1].isBlank()) {
                return new UnknownCommand("Resident name cannot be empty.");
            }
            if (parts[2].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty.");
            }
            return new AddResidentCommand(parts[1], parts[2]);

        case "add-resident-to-cca":
            if (parts.length < 4) {
                return new UnknownCommand("Usage: add-resident-to-cca <matric number> <cca name> <points>");
            }
            if (parts[1].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty.");
            }
            if (parts[2].isBlank()) {
                return new UnknownCommand("CCA name cannot be empty.");
            }
            if (parts[3].isBlank()) {
                return new UnknownCommand("Points cannot be empty.");
            }
            return new AddResidentToCcaCommand(parts[1], parts[2], parts[3]);
        case "view-points":
            return new ViewPointsCommand();
        default:
            return new UnknownCommand();
        }
    }

    static CcaLevel getCcaLevel(String ccaLevel){
        try {
            return CcaLevel.valueOf(ccaLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle case where user typed "super-high" or something invalid
            logger.log(Level.WARNING, "Invalid CCA level entered: " + ccaLevel);
        }
        return CcaLevel.UNKNOWN;
    }
}
