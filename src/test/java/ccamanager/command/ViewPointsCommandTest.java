package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.DuplicateCcaException;
import ccamanager.exceptions.DuplicateResidentException;
import ccamanager.exceptions.InvalidCcaLevelException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewPointsCommandTest {

    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;
    private Ui ui;
    private ViewPointsCommand command;

    @BeforeEach
    void setUp() {
        ccaManager = new CcaManager();
        residentManager = new ResidentManager();
        eventManager = new EventManager();
        ui = new Ui();
        command = new ViewPointsCommand();
    }


    @Test
    void isReadOnly_returnsTrue() {
        assertTrue(command.isReadOnly());
    }



    @Test
    void execute_noResidents_emptyListPassedToUi() {
        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(0, residentManager.getResidentList().size());
    }


    @Test
    void execute_singleResidentNoCcas_zeroTotalPoints() throws DuplicateResidentException {
        residentManager.addResident("Alice", "A1234567X");

        command.execute(ccaManager, residentManager, eventManager, ui);

        Resident alice = residentManager.getResidentList().get(0);
        assertEquals(0, alice.getTotalPoints());
        assertTrue(alice.getPoints().isEmpty());
    }


    @Test
    void execute_singleResidentWithCcaAndPoints_correctTotalPoints()
            throws DuplicateCcaException, InvalidCcaLevelException, DuplicateResidentException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca basketball = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        Resident alice = residentManager.getResidentList().get(0);
        alice.addCcaToResident(basketball, 80);

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(80, residentManager.getResidentList().get(0).getTotalPoints());
    }


    @Test
    void execute_residentWithMultipleCcas_totalPointsSummedCorrectly()
            throws DuplicateCcaException, InvalidCcaLevelException, DuplicateResidentException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        ccaManager.addCCA("Chess", CcaLevel.LOW);
        Cca basketball = ccaManager.getCCAList().get(0);
        Cca chess = ccaManager.getCCAList().get(1);

        residentManager.addResident("Alice", "A1234567X");
        Resident alice = residentManager.getResidentList().get(0);
        alice.addCcaToResident(basketball, 80);
        alice.addCcaToResident(chess, 20);

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(100, residentManager.getResidentList().get(0).getTotalPoints());
        assertEquals(2, residentManager.getResidentList().get(0).getPoints().size());
    }


    @Test
    void execute_multipleResidents_allPresentInList() throws DuplicateResidentException {
        residentManager.addResident("Alice", "A1234567X");
        residentManager.addResident("Bob", "B7654321Y");
        residentManager.addResident("Charlie", "C1111111Z");

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(3, residentManager.getResidentList().size());
    }

    @Test
    void execute_multipleResidentsWithPoints_eachPointsListIndependent()
            throws DuplicateCcaException, InvalidCcaLevelException, DuplicateResidentException {
        ccaManager.addCCA("Dance", CcaLevel.MEDIUM);
        Cca dance = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        residentManager.addResident("Bob", "B7654321Y");

        residentManager.getResidentList().get(0).addCcaToResident(dance, 60);
        residentManager.getResidentList().get(1).addCcaToResident(dance, 40);

        command.execute(ccaManager, residentManager, eventManager, ui);

        ArrayList<Resident> list = residentManager.getResidentList();
        assertEquals(60, list.get(0).getTotalPoints());
        assertEquals(40, list.get(1).getTotalPoints());
    }


    @Test
    void execute_duplicateMatricNumber_secondAddRejected() throws DuplicateResidentException {
        residentManager.addResident("Alice", "A1234567X");

        try {
            residentManager.addResident("Alice Clone", "A1234567X");
        } catch (DuplicateResidentException e) {
            // expected
        }

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(1, residentManager.getResidentList().size());
    }


    @Test
    void execute_doesNotMutateResidentList() throws DuplicateResidentException {
        residentManager.addResident("Alice", "A1234567X");
        residentManager.addResident("Bob", "B7654321Y");
        int sizeBefore = residentManager.getResidentList().size();

        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(sizeBefore, residentManager.getResidentList().size());
    }

    @Test
    void execute_doesNotMutateResidentPoints()
            throws DuplicateCcaException, InvalidCcaLevelException, DuplicateResidentException {
        ccaManager.addCCA("Dance", CcaLevel.MEDIUM);
        Cca dance = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        residentManager.getResidentList().get(0).addCcaToResident(dance, 75);

        int pointsBefore = residentManager.getResidentList().get(0).getTotalPoints();
        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(pointsBefore, residentManager.getResidentList().get(0).getTotalPoints());
    }


    @Test
    void execute_calledTwice_listAndPointsUnchanged()
            throws DuplicateCcaException, InvalidCcaLevelException, DuplicateResidentException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca basketball = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        residentManager.getResidentList().get(0).addCcaToResident(basketball, 50);

        command.execute(ccaManager, residentManager, eventManager, ui);
        command.execute(ccaManager, residentManager, eventManager, ui);

        assertEquals(1, residentManager.getResidentList().size());
        assertEquals(50, residentManager.getResidentList().get(0).getTotalPoints());
    }
}
