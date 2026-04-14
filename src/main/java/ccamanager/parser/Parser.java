package ccamanager.parser;

import ccamanager.command.AddCcaCommand;
import ccamanager.command.AddEventCommand;
import ccamanager.command.AddExcoToCcaCommand;
import ccamanager.command.AddResidentCommand;
import ccamanager.command.AddResidentToCcaCommand;
import ccamanager.command.AddResidentToEventCommand;
import ccamanager.command.CcaStatsCommand;
import ccamanager.command.Command;
import ccamanager.command.DeleteCcaCommand;
import ccamanager.command.DeleteResidentCommand;
import ccamanager.command.ExitCommand;
import ccamanager.command.HelpCommand;
import ccamanager.command.ResidentStatsCommand;
import ccamanager.command.UnknownCommand;
import ccamanager.command.ViewCcaCommand;
import ccamanager.command.ViewCcaEventsCommand;
import ccamanager.command.ViewCcaExcoCommand;
import ccamanager.command.ViewMyEventsCommand;
import ccamanager.command.ViewPointsCommand;
import ccamanager.command.ViewResidentCommand;
import ccamanager.command.SortPointsCommand;
import ccamanager.command.UpdateCcaPointCommand;
import ccamanager.command.ViewResidentInCcaCommand;
import ccamanager.enumerations.CcaLevel;

import ccamanager.exceptions.InvalidPointsException;

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

        String trimmedInput = input.trim();
        int firstSpace = trimmedInput.indexOf(" ");

        // Handle single-word commands (no arguments)
        if (firstSpace == -1) {
            String commandWord = trimmedInput.toLowerCase();
            switch (commandWord) {
            case "help":
                return new HelpCommand();
            case "bye":
                return new ExitCommand();
            case "view-cca":
                return new ViewCcaCommand();
            case "view-resident":
                return new ViewResidentCommand();
            case "view-points":
                return new ViewPointsCommand();
            case "cca-stats":
                return new CcaStatsCommand();
            case "resident-stats":
                return new ResidentStatsCommand();
            case "sort-points":
                return new SortPointsCommand();
            default:
                return new UnknownCommand();
            }
        }

        String commandWord = trimmedInput.substring(0, firstSpace).toLowerCase();
        String rawArgs = trimmedInput.substring(firstSpace + 1).trim();

        String[] args = rawArgs.split(";");

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }

        assert args.length > 0 : "Non-blank input should produce at least one token";

        switch (commandWord) {
        case "add-cca":
            if (args.length < 2 || args[0].isBlank() || args[1].isBlank()) {
                return new UnknownCommand("Usage: add-cca <cca name>; <level>");
            }
            String name = args[0];
            CcaLevel level = getCcaLevel(args[1]);
            return new AddCcaCommand(name, level);

        case "view-exco":
            if (args.length < 1 || args[0].isBlank()){
                return new UnknownCommand("Usage: view-exco <cca-name>");
            }
            return new ViewCcaExcoCommand(args[0]);

        case "delete-cca":
            if (args.length < 1 || args[0].isBlank()) {
                return new UnknownCommand("Usage: delete-cca <cca name>");
            }
            return new DeleteCcaCommand(args[0]);

        case "add-event":
            if (args.length < 3) {
                return new UnknownCommand("Usage: add-event <event name>; <cca name>; <date time>");
            }
            return new AddEventCommand(args[0], args[1], args[2]);

        case "add-resident":
            if (args.length < 2) {
                return new UnknownCommand("Usage: add-resident <name>; <matric number>");
            }
            if (args[0].isBlank()) {
                return new UnknownCommand("Resident name cannot be empty.");
            }
            if (args[1].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty.");
            }
            return new AddResidentCommand(args[0], args[1]);

        case "delete-resident":
            if (args[0].isBlank()) {
                return new UnknownCommand("Resident name cannot be empty.");
            }
            return new DeleteResidentCommand(args[0]);

        case "add-resident-to-event":
            if (args.length < 3) {
                return new UnknownCommand("Usage: add-resident-to-event <matric>; <event>; <cca>");
            }
            if (args[0].isBlank()) {
                return new UnknownCommand("Student number cannot be empty");
            }
            if (args[1].isBlank()) {
                return new UnknownCommand("Event name cannot be empty");
            }
            if (args[2].isBlank()) {
                return new UnknownCommand("CCA Name cannot be empty.");
            }
            return new AddResidentToEventCommand(args[0], args[1], args[2]);

        case "add-resident-to-cca":
            if (args.length < 3) {
                return new UnknownCommand("Usage: add-resident-to-cca <matric>; <cca>; <points>");
            }
            if (args[0].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty.");
            }
            if (args[1].isBlank()) {
                return new UnknownCommand("CCA name cannot be empty.");
            }
            if (args[2].isBlank()) {
                return new UnknownCommand("Points cannot be empty.");
            }
            try {
                return new AddResidentToCcaCommand(args[0], args[1], args[2]);
            } catch (InvalidPointsException e) {
                return new UnknownCommand(e.getMessage());
            }

        case "add-exco-to-cca":
            if (args.length < 2) {
                return new UnknownCommand("Usage: add-exco-to-cca <matric number>; <cca name>");
            }
            if (args[0].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty.");
            }
            if (args[1].isBlank()) {
                return new UnknownCommand("CCA name cannot be empty.");
            }
            return new AddExcoToCcaCommand(args[0], args[1]);

        case "view-cca-events":
            if (args[0].isBlank()) {
                return new UnknownCommand("CCA name cannot be empty.");
            }
            return new ViewCcaEventsCommand(args[0]);

        case "view-my-events":
            if (args[0].isBlank()) {
                return new UnknownCommand("Resident matric number cannot be empty.");
            }
            return new ViewMyEventsCommand(args[0]);
        case "update-point":
            if (args.length < 3) {
                return new UnknownCommand("Usage: add-resident-to-event <matric>; <event>; <cca>");
            }
            if (args[0].isBlank()) {
                return new UnknownCommand("Matric number cannot be empty");
            }
            if (args[1].isBlank()) {
                return new UnknownCommand("CCA name cannot be empty");
            }
            if (args[2].isBlank()) {
                return new UnknownCommand("Point cannot be empty.");
            }
            return new UpdateCcaPointCommand(args[0],args[1],args[2]);

        case "view-resident-in-cca":
            if(args[0].isBlank()){
                return new UnknownCommand("Cca name cannot be empty");
            }
            return new ViewResidentInCcaCommand(args[0]);
        default:
            // This captures cases like "help" (if not caught above) or completely unknown words
            return parseSingleWordFallback(commandWord);
        }
    }

    private Command parseSingleWordFallback(String commandWord) {
        switch (commandWord) {
        case "help": return new HelpCommand();
        case "bye": return new ExitCommand();
        case "view-cca": return new ViewCcaCommand();
        case "view-resident": return new ViewResidentCommand();
        case "view-points": return new ViewPointsCommand();
        case "cca-stats": return new CcaStatsCommand();
        case "resident-stats": return new ResidentStatsCommand();
        default: return new UnknownCommand();
        }
    }

    /**
     * Extracts out the name of the CCA from the command given
     * @param ccaLevel input level given by the user
     * @return CCA name as a string
     */
    private static CcaLevel getCcaLevel(String ccaLevel){
        try {
            return CcaLevel.valueOf(ccaLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid CCA level entered: " + ccaLevel);
        }
        return CcaLevel.UNKNOWN;
    }
}
