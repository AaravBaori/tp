package ccamanager.storage;

import ccamanager.enumerations.CcaLevel;
import ccamanager.exceptions.DuplicateCcaException;
import ccamanager.exceptions.DuplicateEventException;
import ccamanager.exceptions.DuplicateResidentException;
import ccamanager.exceptions.InvalidCcaLevelException;
import ccamanager.exceptions.EventNotFoundException;
import ccamanager.exceptions.ResidentAlreadyInCcaException;
import ccamanager.exceptions.ResidentAlreadyInEventException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Cca;
import ccamanager.model.Event;
import ccamanager.model.Resident;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StorageManager — handles all reading and writing of persistent data to text files.
 * <p>
 * File layout:
 *   data/ccas.txt               — one CCA per line
 *   data/residents.txt          — one resident per line
 *   data/events.txt             — one event per line
 *   data/memberships.txt        — Resident <-> CCA join table (points + isExco flag)
 *   data/event_attendance.txt   — Resident <-> Event join table
 * <p>
 * Column separator: pipe ( | ). Literal pipes inside values are escaped as \|
 * Literal backslashes are escaped as \\ so the two never interfere.
 * <p>
 * Load order is critical — see load() for explanation.
 */
public class StorageManager {

    private static final Logger LOGGER = Logger.getLogger(StorageManager.class.getName());

    private static final String DATA_DIR        = "data";
    private static final String CCA_FILE        = DATA_DIR + "/ccas.txt";
    private static final String RESIDENT_FILE   = DATA_DIR + "/residents.txt";
    private static final String EVENT_FILE      = DATA_DIR + "/events.txt";
    private static final String MEMBER_FILE     = DATA_DIR + "/memberships.txt";
    private static final String ATTENDANCE_FILE = DATA_DIR + "/event_attendance.txt";

    private static final String SEP       = "|";
    private static final String SEP_REGEX = "(?<!\\\\)\\|";

    // =========================================================================
    // SAVE — full snapshot on every call, overwrites existing files
    // =========================================================================

    /**
     * Persists all in-memory state to disk. Overwrites existing files completely.
     * Should be called by CcaLedger after every mutating command.
     */
    public void save(CcaManager ccaManager,
                     ResidentManager residentManager,
                     EventManager eventManager) throws IOException {
        ensureDataDir();
        saveCcas(ccaManager);
        saveResidents(residentManager);
        saveEvents(eventManager);
        saveMemberships(residentManager);
        saveEventAttendance(eventManager);
    }

    /**
     * Writes ccas.txt
     * Format: name|level
     * Example: Basketball|HIGH
     */
    private void saveCcas(CcaManager cm) throws IOException {
        try (BufferedWriter w = openWriter(CCA_FILE)) {
            for (Cca cca : cm.getCCAList()) {
                w.write(escape(cca.getName()) + SEP + cca.getLevel().name());
                w.newLine();
            }
        }
    }

    /**
     * Writes residents.txt
     * Format: name|matricNumber
     * Example: Alice Tan|A1234567X
     */
    private void saveResidents(ResidentManager rm) throws IOException {
        try (BufferedWriter w = openWriter(RESIDENT_FILE)) {
            for (Resident r : rm.getResidentList()) {
                w.write(escape(r.getName()) + SEP + escape(r.getMatricNumber()));
                w.newLine();
            }
        }
    }

    /**
     * Writes events.txt
     * Format: eventName|ccaName|eventDate
     * Example: AGM|Basketball|2025-04-01
     */
    private void saveEvents(EventManager em) throws IOException {
        try (BufferedWriter w = openWriter(EVENT_FILE)) {
            for (Event e : em.getEventList()) {
                w.write(escape(e.getEventName()) + SEP
                        + escape(e.getCca().getName()) + SEP
                        + escape(e.getEventDate()));
                w.newLine();
            }
        }
    }

    /**
     * Writes memberships.txt — the Resident <-> CCA join table.
     * Format: matricNumber|ccaName|points|isExco
     * Example: A1234567X|Basketball|50|false
     * <p>
     * We iterate residents so each resident's per-CCA point value is
     * naturally available from resident.getPoints(). The isExco flag
     * is resolved by checking whether the resident appears in cca.getExcos().
     */
    private void saveMemberships(ResidentManager rm) throws IOException {
        try (BufferedWriter w = openWriter(MEMBER_FILE)) {
            for (Resident r : rm.getResidentList()) {
                for (int i = 0; i < r.getCcas().size(); i++) {
                    Cca     cca    = r.getCcas().get(i);
                    int     pts    = r.getPoints().get(i);
                    boolean isExco = cca.getExcos().contains(r);
                    w.write(escape(r.getMatricNumber()) + SEP
                            + escape(cca.getName()) + SEP
                            + pts + SEP
                            + isExco);
                    w.newLine();
                }
            }
        }
    }

    /**
     * Writes event_attendance.txt — the Resident <-> Event join table.
     * Format: matricNumber|eventName|ccaName
     * Example: A1234567X|AGM|Basketball
     * <p>
     * ccaName is stored alongside eventName because event names are not
     * guaranteed to be unique across different CCAs (both could have "AGM").
     * Together they form a composite FK back to events.txt.
     */
    private void saveEventAttendance(EventManager em) throws IOException {
        try (BufferedWriter w = openWriter(ATTENDANCE_FILE)) {
            for (Event e : em.getEventList()) {
                for (Resident r : e.getParticipants()) {
                    w.write(escape(r.getMatricNumber()) + SEP
                            + escape(e.getEventName()) + SEP
                            + escape(e.getCca().getName()));
                    w.newLine();
                }
            }
        }
    }

    // =========================================================================
    // LOAD — reconstruct full in-memory state from disk
    // =========================================================================

    /**
     * Loads all persisted data from disk into the managers.
     * Safe to call on first run when no data files exist yet.
     * <p>
     * Order is critical because later files reference earlier ones as FKs:
     *   1. ccas.txt          — no dependencies
     *   2. residents.txt     — no dependencies
     *   3. events.txt        — FK: ccaName → ccas.txt
     *   4. memberships.txt   — FK: matricNumber → residents.txt, ccaName → ccas.txt
     *   5. event_attendance  — FK: matricNumber → residents.txt,
     *                              (eventName + ccaName) → events.txt
     */
    public void load(CcaManager ccaManager,
                     ResidentManager residentManager,
                     EventManager eventManager) throws IOException, EventNotFoundException,
            ResidentAlreadyInEventException {
        ensureDataDir();
        loadCcas(ccaManager);
        loadResidents(residentManager);
        loadEvents(ccaManager, eventManager);
        loadMemberships(ccaManager, residentManager);
        loadEventAttendance(residentManager, eventManager);
    }

    /**
     * Loads ccas.txt → populates CcaManager.
     * Format: name|level
     */
    private void loadCcas(CcaManager cm) throws IOException {
        Path p = Path.of(CCA_FILE);
        if (!Files.exists(p)) {
            return;
        }
        int lineNum = 0;
        for (String line : Files.readAllLines(p)) {
            lineNum++;
            if (line.isBlank()) {
                continue;
            }
            String[] f = line.split(SEP_REGEX, 2);
            if (f.length < 2) {
                LOGGER.log(Level.WARNING, "ccas.txt line {0}: too few fields — skipping: {1}",
                        new Object[]{lineNum, line});
                continue;
            }
            CcaLevel level;
            try {
                level = CcaLevel.valueOf(f[1].trim());
            } catch (IllegalArgumentException e) {
                // Skip rather than load with UNKNOWN since addCCA rejects UNKNOWN anyway
                LOGGER.log(Level.WARNING, "ccas.txt line {0}: unknown level ''{1}'' — skipping",
                        new Object[]{lineNum, f[1].trim()});
                continue;
            }
            try {
                cm.addCCA(unescape(f[0]), level);
            } catch (DuplicateCcaException | InvalidCcaLevelException e) {
                LOGGER.log(Level.WARNING, "ccas.txt line {0}: could not add CCA — skipping: {1}",
                        new Object[]{lineNum, e.getMessage()});
            }
        }
    }

    /**
     * Loads residents.txt → populates ResidentManager.
     * Format: name|matricNumber
     */
    private void loadResidents(ResidentManager rm) throws IOException {
        Path p = Path.of(RESIDENT_FILE);
        if (!Files.exists(p)) {
            return;
        }
        int lineNum = 0;
        for (String line : Files.readAllLines(p)) {
            lineNum++;
            if (line.isBlank()) {
                continue;
            }
            String[] f = line.split(SEP_REGEX, 2);
            if (f.length < 2) {
                LOGGER.log(Level.WARNING, "residents.txt line {0}: too few fields — skipping: {1}",
                        new Object[]{lineNum, line});
                continue;
            }
            try {
                rm.addResident(unescape(f[0]), unescape(f[1]));
            } catch (DuplicateResidentException e) {
                LOGGER.log(Level.WARNING, "residents.txt line {0}: duplicate resident — skipping: {1}",
                        new Object[]{lineNum, e.getMessage()});
            }
        }
    }

    /**
     * Loads events.txt → populates EventManager.
     * Format: eventName|ccaName|eventDate
     * Rows whose CCA FK cannot be resolved are skipped with a warning.
     */
    private void loadEvents(CcaManager cm, EventManager em) throws IOException {
        Path p = Path.of(EVENT_FILE);
        if (!Files.exists(p)) {
            return;
        }
        int lineNum = 0;
        for (String line : Files.readAllLines(p)) {
            lineNum++;
            if (line.isBlank()) {
                continue;
            }
            String[] f = line.split(SEP_REGEX, 3);
            if (f.length < 3) {
                LOGGER.log(Level.WARNING, "events.txt line {0}: too few fields — skipping: {1}",
                        new Object[]{lineNum, line});
                continue;
            }
            Cca cca = cm.findByName(unescape(f[1]));
            if (cca == null) {
                LOGGER.log(Level.WARNING, "events.txt line {0}: unknown CCA ''{1}'' — skipping",
                        new Object[]{lineNum, f[1]});
                continue;
            }
            try {
                em.addEvent(unescape(f[0]), cca, unescape(f[2]));
            } catch (DuplicateEventException e) {
                LOGGER.log(Level.WARNING, "events.txt line {0}: duplicate event — skipping: {1}",
                        new Object[]{lineNum, e.getMessage()});
            }
        }
    }

    /**
     * Loads memberships.txt → links Residents to CCAs with points and exco status.
     * Format: matricNumber|ccaName|points|isExco
     * <p>
     * Uses Resident.addCcaToResident(cca, points) to populate the resident side,
     * and Cca.addResidentToCca(resident) for the CCA side.
     * For exco members, we add directly to the exco list to avoid
     * double-registering them as regular members via addExcoToCca().
     */
    private void loadMemberships(CcaManager cm, ResidentManager rm) throws IOException {
        Path p = Path.of(MEMBER_FILE);
        if (!Files.exists(p)) {
            return;
        }
        int lineNum = 0;
        for (String line : Files.readAllLines(p)) {
            lineNum++;
            if (line.isBlank()) {
                continue;
            }
            String[] f = line.split(SEP_REGEX, 4);
            if (f.length < 4) {
                LOGGER.log(Level.WARNING, "memberships.txt line {0}: too few fields — skipping: {1}",
                        new Object[]{lineNum, line});
                continue;
            }

            Resident resident = rm.findByMatric(unescape(f[0]));
            Cca      cca      = cm.findByName(unescape(f[1]));

            if (resident == null) {
                LOGGER.log(Level.WARNING, "memberships.txt line {0}: unknown matric ''{1}'' — skipping",
                        new Object[]{lineNum, f[0]});
                continue;
            }
            if (cca == null) {
                LOGGER.log(Level.WARNING, "memberships.txt line {0}: unknown CCA ''{1}'' — skipping",
                        new Object[]{lineNum, f[1]});
                continue;
            }

            int points;
            try {
                points = Integer.parseInt(f[2].trim());
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "memberships.txt line {0}: invalid points ''{1}'' — skipping",
                        new Object[]{lineNum, f[2]});
                continue;
            }

            boolean isExco = Boolean.parseBoolean(f[3].trim());

            // Populate the Resident side (parallel ccas + points lists)
            resident.addCcaToResident(cca, points);

            // Populate the CCA side
            try {
                if (isExco) {
                    // Add to exco list directly, then register as member separately.
                    // We cannot call addExcoToCca() here because it calls addResidentToCca()
                    // internally, which would conflict with our explicit addResidentToCca() call
                    // and throw a duplicate exception.
                    cca.getExcos().add(resident);
                    cca.addResidentToCca(resident);
                } else {
                    cca.addResidentToCca(resident);
                }
            } catch (ResidentAlreadyInCcaException e) {
                LOGGER.log(Level.WARNING, "memberships.txt line {0}: duplicate membership — skipping: {1}",
                        new Object[]{lineNum, e.getMessage()});
            }
        }
    }

    /**
     * Loads event_attendance.txt → marks Residents as participants of Events.
     * Format: matricNumber|eventName|ccaName
     * <p>
     * (eventName + ccaName) forms a composite FK that uniquely identifies an event
     * because two CCAs may independently hold an event with the same name.
     */
    private void loadEventAttendance(ResidentManager rm, EventManager em) throws IOException, EventNotFoundException{
        Path p = Path.of(ATTENDANCE_FILE);
        if (!Files.exists(p)) {
            return;
        }
        int lineNum = 0;
        for (String line : Files.readAllLines(p)) {
            lineNum++;
            if (line.isBlank()) {
                continue;
            }
            String[] f = line.split(SEP_REGEX, 3);
            if (f.length < 3) {
                LOGGER.log(Level.WARNING, "event_attendance.txt line {0}: too few fields — skipping: {1}",
                        new Object[]{lineNum, line});
                continue;
            }

            Resident resident = rm.findByMatric(unescape(f[0]));
            Event    event    = em.findByNameAndCca(unescape(f[1]), unescape(f[2]));

            if (resident == null) {
                LOGGER.log(Level.WARNING,
                        "event_attendance.txt line {0}: unknown matric ''{1}'' — skipping",
                        new Object[]{lineNum, f[0]});
                continue;
            }
            if (event == null) {
                LOGGER.log(Level.WARNING,
                        "event_attendance.txt line {0}: unknown event ''{1}'' in CCA ''{2}'' — skipping",
                        new Object[]{lineNum, f[1], f[2]});
                continue;
            }

            try {
                event.addResidentToEvent(resident);
            } catch (EventNotFoundException | ResidentAlreadyInEventException e) {
                LOGGER.log(Level.WARNING,
                        "event_attendance.txt line {0}: could not add event_attendance — skipping: {1}",
                        new Object[]{lineNum, e.getMessage()});
            }
        }
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private void ensureDataDir() throws IOException {
        Files.createDirectories(Path.of(DATA_DIR));
    }

    /** Opens a writer that overwrites the file from scratch each time. */
    private BufferedWriter openWriter(String path) throws IOException {
        return new BufferedWriter(new FileWriter(path, false));
    }

    /**
     * Escapes backslashes first, then pipes, so round-tripping is exact.
     *   "Hall | Drama"  →  "Hall \| Drama"
     *   "back\slash"    →  "back\\slash"
     */
    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Reverses escape — unescape pipes first, then backslashes.
     */
    private String unescape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\|", "|").replace("\\\\", "\\");
    }
}
