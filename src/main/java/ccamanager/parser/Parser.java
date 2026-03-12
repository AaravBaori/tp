package ccamanager.parser;

import ccamanager.command.AddCcaCommand;
import ccamanager.command.AddResidentCommand;
import ccamanager.command.AddResidentToCcaCommand;
import ccamanager.command.Command;
import ccamanager.command.DeleteCcaCommand;
import ccamanager.command.ExitCommand;
import ccamanager.command.UnknownCommand;
import ccamanager.command.ViewCcaCommand;
import ccamanager.command.ViewResidentCommand;

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

        String[] parts = input.split(" ");
        String commandWord = parts[0].toLowerCase();

        switch (commandWord) {
        case "add-cca":
            return new AddCcaCommand(getCcaName(input));

        case "view-cca":
            return new ViewCcaCommand();

        case "delete-cca":
            return new DeleteCcaCommand(getCcaName(input));

        case "bye":
            return new ExitCommand();

        case "view-resident":
            return new ViewResidentCommand();
        case "add-resident":
            return new AddResidentCommand(parts[1],parts[2]);
        case "add-resident-to-cca":
            return new AddResidentToCcaCommand(parts[1],parts[2],parts[3]);
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
