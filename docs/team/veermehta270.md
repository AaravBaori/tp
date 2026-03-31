# Project Portfolio: Veer Mehta

## Project: CCAManager
CCAManager is a CLI-based application designed for Hall Leaders to manage Resident CCA records, track event participation, and analyze performance points efficiently.

---

## Summary of Contributions

### 1. Core Feature: Event Management System
* **Functional Implementation:** Developed the `EventManager` and `Event` classes to handle the scheduling and tracking of hall activities.
* **Key Commands:** Implemented `AddEventCommand` and `AddResidentToEventCommand`, enabling the association of residents with specific CCA events.
* **Logic :** Integrated Java Streams for duplicate detection (checking name, CCA, and date) and attendance validation.

### 2. CCA Management
* **Management Logic:** Built the `CcaManager` to support adding and deleting CCAs.
* **CCA object** Built the `Cca` class to represent each CCA to which students can be added and events can be assigned to.
* **Key Commands** Implemented `AddCcaCommand`, `ViewCcaCommand` and `ViewCcaCommand`, enabling complete management of the Cca object, givng the user an ability to create, view and delete CCAs

### 3. Software Architecture & Defensive Programming
* **Command Pattern:** Refactored the `Parser` and `Command` hierarchy to ensure the codebase remains extensible and decoupled.
* **Error Handling:** Implemented custom exceptions (`CcaNotFoundException`, `DuplicateEventException` etc) to ensure proper error handling.
* **Logging:** Integrated `java.util.logging` throughout the manager classes to track system state and help in debugging.

### 4. Testing & Quality Assurance
* **Unit Testing:** Wrote extensive JUnit tests for `AddCcaCommand`, `DeleteCcaCommand`, and `AddResidentCommand` to verify edge cases, such as case-insensitive duplicate handling.

---

## Contributions to the User Guide
* Wrote instructions for **CCA Management** (`add-cca`, `view-cca`, `delete-cca`).
* Wrote instructions for **Event Management** (`add-event`, `add-resident-to-event`).


## Contributions to the Developer Guide
* Created **Sequence Diagrams** for the `add-cca`, `view-cca`, `delete-cca`, `add-event` and `add-resident-to-event` operations to visualize object interactions.
* Documented the **Design Considerations** for using the Command Pattern versus direct invocation, highlighting benefits for maintainability.

---

## Contributions to the Team Project 

### Provided the Idea of Overall Architecture
* Proposed to have a **Command Pattern** architecture. This architecture has benefits of :
  * **Decoupling** : The object that invokes a command (`Parser`) does not need to know anything about how command is executed. 
  * **Single Responsibility Principle** : Instead of having a big parser with huge switch statemnt containing all logic, each logic unit now has its own file. This helps in debugging and makes code readble.
### Leading the Team and followups
* Took responsibility of organizing the weekly meetings on google meet. 
* Planned the meeting agenda in advance 
* Provided reminders for deadlines to peers.
