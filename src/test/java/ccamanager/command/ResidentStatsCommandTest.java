package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Resident;
import ccamanager.parser.Parser;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResidentStatsCommandTest {
    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;
    private Ui ui = new Ui();
    private Parser parser =  new Parser();

    @BeforeEach
    void setUp() {
        ccaManager = new CcaManager();
        residentManager = new ResidentManager();
        eventManager = new EventManager();
    }

    @Test
    void totalPoints_success() {
        Command addCcaBasketball = parser.parse("add-cca Basketball HIGH");
        addCcaBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaFootball = parser.parse("add-cca Football MEDIUM");
        addCcaFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJohn = parser.parse("add-resident John 1234");
        addResidentJohn.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJames = parser.parse("add-resident James 4321");
        addResidentJames.execute(ccaManager, residentManager, eventManager, ui);
        Command addJohnToBasketball = parser.parse("add-resident-to-cca 1234 Basketball 9");
        addJohnToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToBasketball = parser.parse("add-resident-to-cca 4321 Basketball 8");
        addJamesToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToFootball = parser.parse("add-resident-to-cca 4321 Football 9");
        addJamesToFootball.execute(ccaManager, residentManager, eventManager, ui);
        ArrayList<Resident> residents = residentManager.getResidentList();
        assert residents.size() == 2 : "There should be 2 residents.";
        assert ccaManager.getCCAList().size() == 2 : "There should be 2 CCAs.";
        HashMap<Resident, Integer> expectedTotalPoints = new HashMap<>();
        expectedTotalPoints.put(new Resident("James", "4321"), 17);
        expectedTotalPoints.put(new Resident("John", "1234"), 9);
        assertEquals(expectedTotalPoints.toString(), ResidentStatsCommand.totalPoints(residents).toString());
    }

    @Test
    void totalPoints_noResidents_failure() {
        ArrayList<Resident> residents = residentManager.getResidentList();
        assert residents.isEmpty() : "There should be no residents.";
        boolean caughtException = false;
        try {
            HashMap<Resident, Integer> totalPoints = ResidentStatsCommand.totalPoints(residents);
        } catch (IllegalArgumentException e) {
            caughtException = true;
        }
        assertTrue(caughtException);
    }

    @Test
    void mostActiveResidents_success() {
        Command addCcaBasketball = parser.parse("add-cca Basketball HIGH");
        addCcaBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaFootball = parser.parse("add-cca Football MEDIUM");
        addCcaFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaTennis = parser.parse("add-cca Tennis LOW");
        addCcaTennis.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJohn = parser.parse("add-resident John 1234");
        addResidentJohn.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJames = parser.parse("add-resident James 4321");
        addResidentJames.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJane = parser.parse("add-resident Jane 5678");
        addResidentJane.execute(ccaManager, residentManager, eventManager, ui);
        Command addJohnToBasketball = parser.parse("add-resident-to-cca 1234 Basketball 9");
        addJohnToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToFootball = parser.parse("add-resident-to-cca 4321 Football 9");
        addJamesToFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJaneToTennis = parser.parse("add-resident-to-cca 5678 Tennis 8");
        addJaneToTennis.execute(ccaManager, residentManager, eventManager, ui);
        ArrayList<Resident> residents = residentManager.getResidentList();
        assert residents.size() == 3 :  "There should be 3 residents.";
        assert ccaManager.getCCAList().size() == 3 :  "There should be 3 CCAs.";
        HashMap<Resident, Integer> totalPoints = ResidentStatsCommand.totalPoints(residents);
        ArrayList<Resident> expectedMostActiveResidents = new ArrayList<>();
        expectedMostActiveResidents.add(new Resident("John", "1234"));
        expectedMostActiveResidents.add(new Resident("James", "4321"));
        assertEquals(expectedMostActiveResidents.toString(),
                ResidentStatsCommand.mostActiveResidents(totalPoints).toString());
    }

    @Test
    void mostActiveResidents_noResidents_failure() {
        totalPoints_noResidents_failure(); // mostActiveResidents() calls totalPoints() anyway
    }
}
