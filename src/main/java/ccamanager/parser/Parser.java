package ccamanager.parser;

import ccamanager.command.*;

/**
 * Parser — reads raw user input and returns the appropriate Command object.
 **/

public class Parser {

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
            if (parts.length < 2 || parts[1].isBlank()) {
                return new UnknownCommand("Usage: add-cca <cca name>");
            }
            return new AddCcaCommand(getCcaName(input));

        case "view-cca":
            return new ViewCcaCommand();

        case "delete-cca":
            if (parts.length < 2 || parts[1].isBlank()) {
                return new UnknownCommand("Usage: delete-cca <cca name>");
            }

            return new DeleteCcaCommand(getCcaName(input));

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

    /**
     * Extracts out the name of the CCA from the command given
     * @param input input text given by the user
     * @return CCA name as a string
     */
    String getCcaName(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            return "";
        }
        return parts[1];
    }
}
