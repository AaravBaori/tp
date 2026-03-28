package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CommandTest {


    @Test
    void isExit_default_returnsFalse() {
        Command command = new Command() {
            @Override
            public void execute(CcaManager ccaManager, ResidentManager residentManager,
                                EventManager eventManager, Ui ui) {
            }
        };

        assertFalse(command.isExit());
    }

}



