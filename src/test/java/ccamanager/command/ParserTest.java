package ccamanager.command;


import ccamanager.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ParserTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void parse_addCca_success() {
        assertInstanceOf(AddCcaCommand.class, parser.parse("add-cca Basketball HIGH"));
    }

    @Test
    void parse_addCca_missingName() {
        assertInstanceOf(UnknownCommand.class, parser.parse("add-cca"));
    }

    @Test
    void parse_viewCca_success() {
        assertInstanceOf(ViewCcaCommand.class, parser.parse("view-cca"));
    }

    @Test
    void parse_deleteCca_success() {
        assertInstanceOf(DeleteCcaCommand.class, parser.parse("delete-cca Basketball"));
    }

    @Test
    void parse_deleteCca_missingName() {
        assertInstanceOf(UnknownCommand.class, parser.parse("delete-cca"));
    }

    @Test
    void parse_bye_success() {
        assertInstanceOf(ExitCommand.class, parser.parse("bye"));
    }

    @Test
    void parse_viewResident_success() {
        assertInstanceOf(ViewResidentCommand.class, parser.parse("view-resident"));
    }

    @Test
    void parse_addResident_success() {
        assertInstanceOf(AddResidentCommand.class, parser.parse("add-resident John A1234567B"));
    }

    @Test
    void parse_addResident_missingMatric() {
        assertInstanceOf(UnknownCommand.class, parser.parse("add-resident John"));
    }

    @Test
    void parse_addResident_missingAll() {
        assertInstanceOf(UnknownCommand.class, parser.parse("add-resident"));
    }

    @Test
    void parse_addResidentToCca_success() {
        assertInstanceOf(AddResidentToCcaCommand.class,
                parser.parse("add-resident-to-cca A1234567B Basketball 10"));
    }

    @Test
    void parse_addResidentToCca_missingPoints() {
        assertInstanceOf(UnknownCommand.class,
                parser.parse("add-resident-to-cca A1234567B Basketball"));
    }

    @Test
    void parse_addResident_blankName() {
        Command command = parser.parse("add-resident  A123");

        assertInstanceOf(UnknownCommand.class, command);
    }


    @Test
    void parse_unknownCommand() {
        assertInstanceOf(UnknownCommand.class, parser.parse("gibberish"));
    }

    @Test
    void parse_emptyInput() {
        assertInstanceOf(UnknownCommand.class, parser.parse(""));
    }

    @Test
    void parse_nullInput() {
        assertInstanceOf(UnknownCommand.class, parser.parse(null));
    }
}

