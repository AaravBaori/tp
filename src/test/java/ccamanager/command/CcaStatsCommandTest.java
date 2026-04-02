package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Resident;
import ccamanager.parser.Parser;
import ccamanager.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CcaStatsCommandTest {
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
    void avgPoints_success() {
        Command addCcaBasketball = parser.parse("add-cca Basketball; HIGH");
        addCcaBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaFootball = parser.parse("add-cca Football; MEDIUM");
        addCcaFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJohn = parser.parse("add-resident John; 1234");
        addResidentJohn.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJames = parser.parse("add-resident James; 4321");
        addResidentJames.execute(ccaManager, residentManager, eventManager, ui);
        Command addJohnToBasketball = parser.parse("add-resident-to-cca 1234; Basketball; 9");
        addJohnToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToBasketball = parser.parse("add-resident-to-cca 4321; Basketball; 8");
        addJamesToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        assert ccas.size() == 2 : "There should be 2 CCAs.";
        assert residentManager.getResidentList().size() == 2 :  "There should be 2 residents.";
        String result = CcaStatsCommand.avgPoints(ccas).toString();
        assertTrue(result.equals("{Basketball(HIGH): 2 residents=8.5, Football(MEDIUM): 0 residents=0.0}") ||
                result.equals("{Football(MEDIUM): 0 residents=0.0, Basketball(HIGH): 2 residents=8.5}"));
    }

    @Test
    void avgPoints_noCcas_failure() {
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        assert ccas.isEmpty() : "There should be no CCAs.";
        boolean caughtException = false;
        try {
            HashMap<Cca, Double> avgPoints = CcaStatsCommand.avgPoints(ccas);
        } catch (IllegalArgumentException e) {
            caughtException = true;
        }
        assertTrue(caughtException);
    }

    @Test
    void mostPopularCcas_success() {
        Command addCcaBasketball = parser.parse("add-cca Basketball; HIGH");
        addCcaBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaFootball = parser.parse("add-cca Football; MEDIUM");
        addCcaFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaTennis = parser.parse("add-cca Tennis; LOW");
        addCcaTennis.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJohn = parser.parse("add-resident John; 1234");
        addResidentJohn.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJames = parser.parse("add-resident James; 4321");
        addResidentJames.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJane = parser.parse("add-resident Jane; 5678");
        addResidentJane.execute(ccaManager, residentManager, eventManager, ui);
        Command addJohnToBasketball = parser.parse("add-resident-to-cca 1234; Basketball; 9");
        addJohnToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToFootball = parser.parse("add-resident-to-cca 4321; Football; 9");
        addJamesToFootball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJaneToTennis = parser.parse("add-resident-to-cca 5678; Tennis; 8");
        addJaneToTennis.execute(ccaManager, residentManager, eventManager, ui);
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        assert ccas.size() == 3 : "There should be 3 CCAs.";
        assert residentManager.getResidentList().size() == 3 : "There should be 3 residents.";
        HashMap<Cca, Double> avgPoints = CcaStatsCommand.avgPoints(ccas);
        String result = CcaStatsCommand.mostPopularCcas(avgPoints).toString();
        assertTrue(result.equals("[Basketball(HIGH): 1 residents, Football(MEDIUM): 1 residents]") ||
                result.equals("[Football(MEDIUM): 1 residents, Basketball(HIGH): 1 residents]"));
    }

    @Test
    void mostPopularCcas_noCcas_failure() {
        avgPoints_noCcas_failure(); // mostPopularCcas() calls averagePoints() anyway
    }

    @Test
    void mostActiveResidents_success() {
        Command addCcaBasketball = parser.parse("add-cca Basketball; HIGH");
        addCcaBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addCcaTennis = parser.parse("add-cca Tennis; LOW");
        addCcaTennis.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJohn = parser.parse("add-resident John; 1234");
        addResidentJohn.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJames = parser.parse("add-resident James; 4321");
        addResidentJames.execute(ccaManager, residentManager, eventManager, ui);
        Command addResidentJane = parser.parse("add-resident Jane; 5678");
        addResidentJane.execute(ccaManager, residentManager, eventManager, ui);
        Command addJohnToBasketball = parser.parse("add-resident-to-cca 1234; Basketball; 9");
        addJohnToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJamesToBasketball = parser.parse("add-resident-to-cca 4321; Basketball; 8");
        addJamesToBasketball.execute(ccaManager, residentManager, eventManager, ui);
        Command addJaneToTennis = parser.parse("add-resident-to-cca 5678; Tennis; 8");
        addJaneToTennis.execute(ccaManager, residentManager, eventManager, ui);
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        assert ccas.size() == 2 :  "There should be 2 CCAs.";
        assert residentManager.getResidentList().size() == 3 :  "There should be 3 residents.";
        String result = CcaStatsCommand.mostActiveResidents(ccas).toString();
        assertTrue(result.equals("{Tennis(LOW): 1 residents=Jane | 5678, Basketball(HIGH): 2 residents=John | 1234}")
                || result.equals("{Basketball(HIGH): 2 residents=John | 1234, Tennis(LOW): 1 residents=Jane | 5678}"));
    }

    @Test
    void mostActiveResidents_noCcas_failure() {
        ArrayList<Cca> ccas = ccaManager.getCCAList();
        assert ccas.isEmpty() : "There should be no CCAs.";
        boolean caughtException = false;
        try {
            HashMap<Cca, Resident> avgPoints = CcaStatsCommand.mostActiveResidents(ccas);
        } catch (IllegalArgumentException e) {
            caughtException = true;
        }
        assertTrue(caughtException);
    }
}
