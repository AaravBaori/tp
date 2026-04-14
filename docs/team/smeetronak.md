# Project Portfolio: Smeet Ronak

## Project: CCAManager

CCAManager is a CLI-based application designed for Hall Leaders to manage Resident CCA records, track event participation, and analyze performance points efficiently.

## Summary of Contributions

### Enhancements Implemented

#### 1. Exception Handling & Error Messaging
* **Custom Exceptions:** Defined and implemented custom exception classes to handle invalid inputs and illegal operations (e.g., referencing non-existent residents or CCAs), ensuring the application never crashes on bad input.
* **Consistent Error Messaging:** Ensured all commands return clear, user-friendly error messages by catching exceptions at the appropriate level across all command classes.
* **Completeness:** This forms a comprehensive defensive layer across the entire application — every command that could fail due to bad input is covered, making the application robust and production-ready.

#### 2. Input Parsing
* **Parser Design:** Modified the `Parser` class to handle edge cases such as missing arguments, extra whitespace, and unrecognised commands gracefully without crashing the application.
* **Extensibility:** Designed the parser to be easily extensible, allowing teammates to add new commands with minimal changes to existing parsing logic.

#### 3. Data Storage & Persistence
* **Persistence Layer:** Implemented the data storage system that saves and restores all application data — residents, CCAs, events, and participation records — across sessions.
* **Data Integrity:** Handled edge cases such as missing or corrupted save files, ensuring the application recovers gracefully without data loss.
* **Decoupled Design:** Kept the storage layer separate from business logic so that changes to either side do not cascade across the codebase.

#### 4. CCA–Resident Association
* **`add-resident-to-cca` Command:** Implemented the command to associate residents with specific CCAs, including validation to prevent duplicate associations and informative error messages for invalid inputs.
* Assisted teammates in implementing other commands, ensuring consistency with the parsing and exception-handling patterns established.

### Contributions to the User Guide
* Wrote the section on **Exception and Error Handling**, documenting all error messages users may encounter and how to resolve them.
* Wrote the section on **Data Storage**, explaining how application data is persisted across sessions and how to handle corrupted or missing save files.
* Contributed to the documentation of **CCA Management** commands, including `add-resident-to-cca`.

### Contributions to the Developer Guide
* **Storage Architecture:** Documented the design of the persistence layer, including class responsibilities, how data is serialised and deserialised, and how the storage layer interfaces with the rest of the application.
* **Exception Architecture:** Documented the custom exception hierarchy, explaining when each exception is thrown, how it propagates, and how it is caught and surfaced to the user.

### Contributions to Team-Based Tasks
* **PR Reviews & Merges:** Reviewed and merged a significant number of pull requests throughout the project, ensuring code quality and consistency with the established architecture.
* **Bug & Issue Management:** Actively tracked, triaged, and resolved issues and bugs as they arose, helping the team stay unblocked and maintain development momentum.
* **Deadline Tracking:** Helped teammates keep track of upcoming deadlines and milestones, proactively flagging tasks at risk of being delayed.

### Contributions Beyond the Project Team
* Helped out in the course forum when possible, responding to questions from peers facing similar implementation challenges.

