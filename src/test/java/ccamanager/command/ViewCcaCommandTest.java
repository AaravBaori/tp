package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.manager.CcaManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewCcaCommandTest {

    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private StubUi stubUi;

    @BeforeEach
    public void setUp() {
        ccaManager = new CcaManager();
        residentManager = new ResidentManager();
        stubUi = new StubUi();
    }

    @Test
    public void execute_emptyCcaList_showsEmptyList() {
        ViewCcaCommand command = new ViewCcaCommand();

        command.execute(ccaManager, residentManager, stubUi);

        assertTrue(stubUi.wasShowCcaListCalled);
        assertEquals(0, stubUi.capturedList.size());
    }

    @Test
    public void execute_populatedCcaList_showsPopulatedList() {
        ccaManager.getCCAList().add(new Cca("Basketball", CcaLevel.HIGH));
        ccaManager.getCCAList().add(new Cca("Chess Club", CcaLevel.LOW));

        ViewCcaCommand command = new ViewCcaCommand();

        command.execute(ccaManager, residentManager, stubUi);

        assertTrue(stubUi.wasShowCcaListCalled);
        assertEquals(2, stubUi.capturedList.size());
        assertEquals(CcaLevel.HIGH, stubUi.capturedList.get(0).getLevel());
    }

    private static class StubUi extends Ui {
        boolean wasShowCcaListCalled = false;
        ArrayList<Cca> capturedList = null;

        @Override
        public void showCcaList(ArrayList<Cca> ccaList) {
            this.wasShowCcaListCalled = true;
            this.capturedList = ccaList;
        }
    }
}
