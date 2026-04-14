package ccamanager.command;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.ResidentAlreadyInEventException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Event;
import ccamanager.model.Resident;
import ccamanager.storage.StorageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StorageManagerTest {

    private static final Path DATA_DIR      = Path.of("data");
    private static final Path CCA_FILE      = DATA_DIR.resolve("ccas.txt");
    private static final Path RESIDENT_FILE = DATA_DIR.resolve("residents.txt");
    private static final Path EVENT_FILE    = DATA_DIR.resolve("events.txt");
    private static final Path MEMBER_FILE   = DATA_DIR.resolve("memberships.txt");
    private static final Path ATTEND_FILE   = DATA_DIR.resolve("event_attendance.txt");

    private StorageManager storageManager;
    private CcaManager ccaManager;
    private ResidentManager residentManager;
    private EventManager eventManager;

    @BeforeEach
    void setUp() throws IOException {
        storageManager  = new StorageManager();
        ccaManager      = new CcaManager();
        residentManager = new ResidentManager();
        eventManager    = new EventManager();
        deleteDataFiles();
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteDataFiles();
    }

    private void deleteDataFiles() throws IOException {
        for (Path p : List.of(CCA_FILE, RESIDENT_FILE, EVENT_FILE, MEMBER_FILE, ATTEND_FILE)) {
            Files.deleteIfExists(p);
        }
    }

    @Test
    void load_noFilesExist_doesNotThrow() {
        assertDoesNotThrow(() ->
                storageManager.load(ccaManager, residentManager, eventManager)
        );
    }

    @Test
    void load_noFilesExist_managersRemainEmpty() throws Exception, ResidentAlreadyInEventException {
        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(0, ccaManager.getCCAList().size());
        assertEquals(0, residentManager.getResidentList().size());
        assertEquals(0, eventManager.getEventList().size());
    }

    @Test
    void saveAndLoad_singleCca_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);

        storageManager.save(ccaManager, residentManager, eventManager);
        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals(1, loaded.getCCAList().size());
        assertEquals("Basketball", loaded.getCCAList().get(0).getName());
        assertEquals(CcaLevel.HIGH, loaded.getCCAList().get(0).getLevel());
    }

    @Test
    void saveAndLoad_multipleCcas_allRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        ccaManager.addCCA("Chess", CcaLevel.LOW);
        ccaManager.addCCA("Dance", CcaLevel.MEDIUM);

        storageManager.save(ccaManager, residentManager, eventManager);
        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals(3, loaded.getCCAList().size());
    }

    @Test
    void saveAndLoad_allCcaLevels_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Alpha", CcaLevel.HIGH);
        ccaManager.addCCA("Beta",  CcaLevel.MEDIUM);
        ccaManager.addCCA("Gamma", CcaLevel.LOW);

        storageManager.save(ccaManager, residentManager, eventManager);
        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals(CcaLevel.HIGH,   loaded.getCCAList().get(0).getLevel());
        assertEquals(CcaLevel.MEDIUM, loaded.getCCAList().get(1).getLevel());
        assertEquals(CcaLevel.LOW,    loaded.getCCAList().get(2).getLevel());
    }

    @Test
    void saveAndLoad_ccaNameWithPipe_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Hall | Drama", CcaLevel.LOW);

        storageManager.save(ccaManager, residentManager, eventManager);
        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals("Hall | Drama", loaded.getCCAList().get(0).getName());
    }

    @Test
    void saveAndLoad_ccaNameWithBackslash_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Back\\Slash", CcaLevel.LOW);

        storageManager.save(ccaManager, residentManager, eventManager);
        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals("Back\\Slash", loaded.getCCAList().get(0).getName());
    }

    @Test
    void saveAndLoad_singleResident_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        residentManager.addResident("Alice Tan", "A1234567X");

        storageManager.save(ccaManager, residentManager, eventManager);
        ResidentManager loaded = new ResidentManager();
        storageManager.load(new CcaManager(), loaded, new EventManager());

        assertEquals(1, loaded.getResidentList().size());
        assertEquals("Alice Tan", loaded.getResidentList().get(0).getName());
        assertEquals("A1234567X", loaded.getResidentList().get(0).getMatricNumber());
    }

    @Test
    void saveAndLoad_multipleResidents_allRestored() throws Exception, ResidentAlreadyInEventException {
        residentManager.addResident("Alice Tan",  "A1234567X");
        residentManager.addResident("Bob Lim",    "B7654321Y");
        residentManager.addResident("Charlie Ng", "C1111111Z");

        storageManager.save(ccaManager, residentManager, eventManager);
        ResidentManager loaded = new ResidentManager();
        storageManager.load(new CcaManager(), loaded, new EventManager());

        assertEquals(3, loaded.getResidentList().size());
    }

    @Test
    void saveAndLoad_residentNameWithPipe_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        residentManager.addResident("O'Brien | Jr", "A9999999Z");

        storageManager.save(ccaManager, residentManager, eventManager);
        ResidentManager loaded = new ResidentManager();
        storageManager.load(new CcaManager(), loaded, new EventManager());

        assertEquals("O'Brien | Jr", loaded.getResidentList().get(0).getName());
    }

    @Test
    void saveAndLoad_singleEvent_restoredCorrectly() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca cca = ccaManager.getCCAList().get(0);
        eventManager.addEvent("AGM", cca, "2026-04-01");

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager   loadedCca   = new CcaManager();
        EventManager loadedEvent = new EventManager();
        storageManager.load(loadedCca, new ResidentManager(), loadedEvent);

        assertEquals(1, loadedEvent.getEventList().size());
        assertEquals("AGM", loadedEvent.getEventList().get(0).getEventName());
        assertEquals("Basketball", loadedEvent.getEventList().get(0).getCca().getName());
        assertEquals("2026-04-01", loadedEvent.getEventList().get(0).getEventDate());
    }

    @Test
    void saveAndLoad_multipleEvents_allRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca cca = ccaManager.getCCAList().get(0);
        eventManager.addEvent("AGM",      cca, "2026-04-01");
        eventManager.addEvent("Training", cca, "2026-05-10");
        eventManager.addEvent("Finals",   cca, "2026-06-20");

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager   loadedCca   = new CcaManager();
        EventManager loadedEvent = new EventManager();
        storageManager.load(loadedCca, new ResidentManager(), loadedEvent);

        assertEquals(3, loadedEvent.getEventList().size());
    }

    @Test
    void saveAndLoad_sameEventName_differentCcas() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        ccaManager.addCCA("Chess",      CcaLevel.LOW);
        eventManager.addEvent("AGM", ccaManager.getCCAList().get(0), "2026-04-01");
        eventManager.addEvent("AGM", ccaManager.getCCAList().get(1), "2026-04-01");

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager   loadedCca   = new CcaManager();
        EventManager loadedEvent = new EventManager();
        storageManager.load(loadedCca, new ResidentManager(), loadedEvent);

        assertEquals(2, loadedEvent.getEventList().size());
    }

    @Test
    void saveAndLoad_membershipWithPoints_pointsRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca cca = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        Resident alice = residentManager.getResidentList().get(0);
        alice.addCcaToResident(cca, 80);
        cca.addResidentToCca(alice);

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      loadedCca      = new CcaManager();
        ResidentManager loadedResident = new ResidentManager();
        storageManager.load(loadedCca, loadedResident, new EventManager());

        assertEquals(80, loadedResident.getResidentList().get(0).getTotalPoints());
    }

    @Test
    void saveAndLoad_membershipZeroPoints_zeroRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Chess", CcaLevel.LOW);
        Cca cca = ccaManager.getCCAList().get(0);

        residentManager.addResident("Bob", "B1234567X");
        Resident bob = residentManager.getResidentList().get(0);
        bob.addCcaToResident(cca, 0);
        cca.addResidentToCca(bob);

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      loadedCca      = new CcaManager();
        ResidentManager loadedResident = new ResidentManager();
        storageManager.load(loadedCca, loadedResident, new EventManager());

        assertEquals(0, loadedResident.getResidentList().get(0).getTotalPoints());
    }

    @Test
    void saveAndLoad_residentInMultipleCcas_allRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        ccaManager.addCCA("Chess",      CcaLevel.LOW);
        Cca basketball = ccaManager.getCCAList().get(0);
        Cca chess      = ccaManager.getCCAList().get(1);

        residentManager.addResident("Alice", "A1234567X");
        Resident alice = residentManager.getResidentList().get(0);
        alice.addCcaToResident(basketball, 60);
        alice.addCcaToResident(chess, 40);
        basketball.addResidentToCca(alice);
        chess.addResidentToCca(alice);

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      loadedCca      = new CcaManager();
        ResidentManager loadedResident = new ResidentManager();
        storageManager.load(loadedCca, loadedResident, new EventManager());

        assertEquals(100, loadedResident.getResidentList().get(0).getTotalPoints());
        assertEquals(2,   loadedResident.getResidentList().get(0).getCcas().size());
    }

    @Test
    void saveAndLoad_eventAttendance_participantRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        Cca cca = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1234567X");
        Resident alice = residentManager.getResidentList().get(0);

        eventManager.addEvent("AGM", cca, "2026-04-01");
        Event agm = eventManager.getEventList().get(0);
        agm.addResidentToEvent(alice);

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      loadedCca      = new CcaManager();
        ResidentManager loadedResident = new ResidentManager();
        EventManager    loadedEvent    = new EventManager();
        storageManager.load(loadedCca, loadedResident, loadedEvent);

        assertEquals(1, loadedEvent.getEventList().get(0).getParticipants().size());
        assertEquals("A1234567X",
                loadedEvent.getEventList().get(0).getParticipants().get(0).getMatricNumber());
    }

    @Test
    void saveAndLoad_multipleParticipants_allRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Dance", CcaLevel.MEDIUM);
        Cca cca = ccaManager.getCCAList().get(0);

        residentManager.addResident("Alice", "A1111111X");
        residentManager.addResident("Bob",   "B2222222Y");
        residentManager.addResident("Carol", "C3333333Z");

        eventManager.addEvent("Showcase", cca, "2026-07-01");
        Event showcase = eventManager.getEventList().get(0);
        for (Resident r : residentManager.getResidentList()) {
            showcase.addResidentToEvent(r);
        }

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      loadedCca      = new CcaManager();
        ResidentManager loadedResident = new ResidentManager();
        EventManager    loadedEvent    = new EventManager();
        storageManager.load(loadedCca, loadedResident, loadedEvent);

        assertEquals(3, loadedEvent.getEventList().get(0).getParticipants().size());
    }

    @Test
    void load_malformedCcaLine_skippedGracefully() throws Exception {
        Files.createDirectories(DATA_DIR);
        Files.writeString(CCA_FILE, "OnlyOnePart\nBasketball|HIGH\n");

        assertDoesNotThrow(() ->
                storageManager.load(ccaManager, residentManager, eventManager)
        );
        assertEquals(1, ccaManager.getCCAList().size());
        assertEquals("Basketball", ccaManager.getCCAList().get(0).getName());
    }

    @Test
    void load_unknownCcaLevel_lineSkipped() throws Exception, ResidentAlreadyInEventException {
        Files.createDirectories(DATA_DIR);
        Files.writeString(CCA_FILE, "BadClub|LEGENDARY\nChess|LOW\n");

        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(1, ccaManager.getCCAList().size());
        assertEquals("Chess", ccaManager.getCCAList().get(0).getName());
    }

    @Test
    void load_malformedResidentLine_skippedGracefully() throws Exception {
        Files.createDirectories(DATA_DIR);
        Files.writeString(RESIDENT_FILE, "NoMatricHere\nAlice|A1234567X\n");

        assertDoesNotThrow(() ->
                storageManager.load(ccaManager, residentManager, eventManager)
        );
        assertEquals(1, residentManager.getResidentList().size());
    }

    @Test
    void load_blankLinesInFiles_ignoredGracefully() throws Exception, ResidentAlreadyInEventException {
        Files.createDirectories(DATA_DIR);
        Files.writeString(CCA_FILE, "\nBasketball|HIGH\n\n");

        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(1, ccaManager.getCCAList().size());
    }

    @Test
    void load_eventWithUnknownCca_lineSkipped() throws Exception, ResidentAlreadyInEventException {
        Files.createDirectories(DATA_DIR);
        Files.writeString(CCA_FILE,   "Basketball|HIGH\n");
        Files.writeString(EVENT_FILE, "AGM|GhostCCA|2026-04-01\nTraining|Basketball|2026-05-01\n");

        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(1, eventManager.getEventList().size());
        assertEquals("Training", eventManager.getEventList().get(0).getEventName());
    }



    @Test
    void load_duplicateCcaInFile_secondSkipped() throws Exception, ResidentAlreadyInEventException {
        Files.createDirectories(DATA_DIR);
        Files.writeString(CCA_FILE, "Basketball|HIGH\nBasketball|HIGH\n");

        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(1, ccaManager.getCCAList().size());
    }

    @Test
    void load_duplicateResidentInFile_secondSkipped() throws Exception, ResidentAlreadyInEventException {
        Files.createDirectories(DATA_DIR);
        Files.writeString(RESIDENT_FILE, "Alice|A1234567X\nAlice Clone|A1234567X\n");

        storageManager.load(ccaManager, residentManager, eventManager);

        assertEquals(1, residentManager.getResidentList().size());
        assertEquals("Alice", residentManager.getResidentList().get(0).getName());
    }


    @Test
    void save_calledTwice_secondSaveOverwritesFirst() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager fresh = new CcaManager();
        storageManager.save(fresh, residentManager, eventManager);

        CcaManager loaded = new CcaManager();
        storageManager.load(loaded, new ResidentManager(), new EventManager());

        assertEquals(0, loaded.getCCAList().size());
    }

    @Test
    void saveAndLoad_fullState_everythingRestored() throws Exception, ResidentAlreadyInEventException {
        ccaManager.addCCA("Basketball", CcaLevel.HIGH);
        ccaManager.addCCA("Chess",      CcaLevel.LOW);
        Cca basketball = ccaManager.getCCAList().get(0);
        Cca chess      = ccaManager.getCCAList().get(1);

        residentManager.addResident("Alice", "A1111111X");
        residentManager.addResident("Bob",   "B2222222Y");
        Resident alice = residentManager.getResidentList().get(0);
        Resident bob   = residentManager.getResidentList().get(1);

        alice.addCcaToResident(basketball, 50);
        basketball.addResidentToCca(alice);
        bob.addCcaToResident(chess, 30);
        chess.addResidentToCca(bob);

        eventManager.addEvent("Finals",   basketball, "2026-06-01");
        eventManager.addEvent("Training", basketball, "2026-05-01");
        Event finals = eventManager.getEventList().get(0);
        finals.addResidentToEvent(alice);

        storageManager.save(ccaManager, residentManager, eventManager);

        CcaManager      lCca      = new CcaManager();
        ResidentManager lResident = new ResidentManager();
        EventManager    lEvent    = new EventManager();
        storageManager.load(lCca, lResident, lEvent);

        assertEquals(2, lCca.getCCAList().size());
        assertEquals(2, lResident.getResidentList().size());
        assertEquals(2, lEvent.getEventList().size());

        Resident lAlice = lResident.findByMatric("A1111111X");
        assertNotNull(lAlice);
        assertEquals(50, lAlice.getTotalPoints());

        Resident lBob = lResident.findByMatric("B2222222Y");
        assertNotNull(lBob);
        assertEquals(30, lBob.getTotalPoints());

        Event lFinals = lEvent.findByNameAndCca("Finals", "Basketball");
        assertNotNull(lFinals);
        assertEquals(1, lFinals.getParticipants().size());
        assertEquals("A1111111X",
                lFinals.getParticipants().get(0).getMatricNumber());
    }
}
