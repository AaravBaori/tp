package ccamanager.parser;

import ccamanager.command.AddCcaCommand;
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

        String[] parts = input.split(" ", 2);
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

        default:
            return new UnknownCommand();
        }
    }

    String getCcaName(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            return "";
        }
        return parts[1];
    }

}
