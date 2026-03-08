package ccamanager.parser;

import ccamanager.command.AddCcaCommand;
import ccamanager.command.Command;
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
            String ccaName = parts[1].replace("n/", "");
            return new AddCcaCommand(ccaName);

        case "view-cca":
            return new ViewCcaCommand();

        case "bye":
            return new ExitCommand();

        case "view-resident":
            return new ViewResidentCommand();

        default:
            return new UnknownCommand();
        }
    }

}
