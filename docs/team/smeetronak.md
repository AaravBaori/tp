# Project Portfolio: Smeet Ronak

## Project: CCAManager
CCAManager is a CLI-based application designed for Hall Leaders to manage Resident CCA records, track event participation, and analyze performance points efficiently.

## Summary of Contributions

### Enhancements Implemented

**1. Exception Handling & Error Messaging**
* Defined custom exception classes to handle invalid inputs and illegal operations (e.g., referencing non-existent residents or CCAs), ensuring the application never crashes on bad input.
* Ensured all commands return clear, user-friendly error messages by catching exceptions at the appropriate level across all command classes.

**2. Input Parsing**
* Modified the `Parser` class to handle edge cases such as missing arguments, extra whitespace, and unrecognised commands gracefully without crashing.
* Designed the parser to be easily extensible, allowing teammates to add new commands with minimal changes to existing logic.

**3. Data Storage & Persistence**
* Implemented the persistence layer that saves and restores all application data — residents, CCAs, events, and participation records — across sessions.
* Handled edge cases such as missing or corrupted save files, with the storage layer kept separate from business logic.

**4. CCA–Resident Association**
* Implemented `add-resident-to-cca`, including duplicate validation and error messaging. Assisted teammates in implementing other commands consistently with established patterns.

### Contributions to the User Guide
* Verified existing commands for accuracy and corrected incorrect command documentation throughout the UG.
* Added documentation for missing commands, including `add-resident-to-cca`, `delete-cca`, and others.

### Contributions to the Developer Guide
* Documented the **Storage Architecture** — class responsibilities, serialisation/deserialisation, and interface with the rest of the application.
* Documented the **Exception Architecture** — the custom exception hierarchy, when each is thrown, and how errors propagate and surface to the user.

### Contributions to Team-Based Tasks
* Reviewed and merged a significant number of PRs throughout the project.
* Tracked, triaged, and resolved bugs and issues as they arose, keeping the team unblocked.
* Helped teammates stay on top of deadlines and milestones.

### Contributions Beyond the Project Team
* Responded to peer questions in the course forum when possible.