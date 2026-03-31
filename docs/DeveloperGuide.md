# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

### Overall Architecture

CcaLedger follows a **layered, command-driven architecture** modelled after the se-edu AddressBook Level-2 design. Each layer has a single responsibility and communicates only with its immediate neighbours.

![highLevel-architecture.png](images/highLevel-architecture.png)

The diagram above shows the six layers and their relationships. The table below summarises each layer's role.

| Layer | Key Classes | Responsibility |
|-------|-------------|----------------|
| Entry Point | `Main` | Instantiates `CcaLedger` and calls `run()`. |
| Orchestration | `CcaLedger` | Owns the main loop; coordinates all components. |
| UI & Parsing | `Ui`, `Parser` | Handles console I/O; translates input into `Command` objects. |
| Command | `Command` and subclasses | Encapsulates a single user-facing operation. |
| Managers | `CcaManager`, `ResidentManager`, `EventManager` | Holds and mutates application state. |
| Domain Model | `Cca`, `Resident`, `Event`, `CcaLevel` | Plain data objects with no business logic. |

**How a command executes (happy path):**

1. `Ui.readInput()` reads a line from the user.
2. `Parser.parse(input)` inspects the string and returns the appropriate `Command` subclass.
3. `CcaLedger` calls `command.execute(ccaManager, residentManager, eventManager, ui)`.
4. The command calls the relevant manager method(s) and prints feedback via `Ui`.
5. If `command.isExit()` returns `true`, the loop terminates.

**Key design rules:**
- Domain objects (`Cca`, `Resident`, `Event`) have no outward dependencies.
- Managers never reference `Ui`, `Parser`, or `Command`.
- Commands receive all dependencies as method parameters in `execute()` — they store nothing.
- `Command` subclasses are only ever instantiated inside `Parser.parse()`.

---

# CCA Manager


## Add CCA Command

### Overview

The `add-cca` command adds a new CCA to the system.

Format:
`add-cca <cca name> <level>`

---

### Implementation

The `add-cca` command is implemented using the Command pattern.

- The `Parser` creates an `AddCcaCommand` object from user input.
- `AddCcaCommand.execute()` calls `CcaManager.addCCA(...)`.
- If the CCA already exists, a `DuplicateCcaException` is thrown and handled.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   try {
      ccaManager.addCCA(ccaName, ccaLevel);
      ui.showMessage("CCA added: " + ccaName + "(" + ccaLevel + ")");
   } catch (DuplicateCcaException | InvalidCcaLevelException e) {
      ui.showError(e.getMessage());
   }
}
```
### Sequeunce Diagram
![Add CCA Sequence Diagram](images/add-cca.png)

### Design Considerations

- Command pattern is used to separate parsing and execution.
- Exception handling is used to manage duplicate CCA cases cleanly

### Alternatives Considered
1. Direct Invocation from Parser to Manager
   Approach: Parser directly calls `CcaManager.addCCA(...)`
   Rejected because:
- Violates separation of concerns
- Makes Parser overly complex
- Reduces extensibility



## View CCA Command

### Overview

The `view-cca` command displays the list of all CCAs stored in the system.

Format:
`view-cca`

---

### Implementation

The `view-cca` command retrieves and displays all CCAs.

- The `Parser` creates a `ViewCcaCommand` object.
- `ViewCcaCommand.execute()` calls `CcaManager.getCCAList()`.
- The retrieved list is passed to `Ui.showCcaList(...)` for display.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
    ArrayList<Cca> ccaList = ccaManager.getCCAList();
    ui.showCcaList(ccaList);
}
```

### Sequence Diagram 
![Add CCA Sequence Diagram](images/view-cca.png)

## View Resident Command

### Overview

The `view-resident` command displays the list of all residents stored in the system.

Format:
`view-resident`

---

### Implementation

The `view-resident` command retrieves and displays all residents.

- The `Parser` creates a `ViewResidentCommand` object.
- `ViewResidentCommand.execute()` calls `ResidentManager.getResidentList()`.
- The retrieved list is passed to `Ui.showResidentList(...)` for display.

```java
@Override
 public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
     ArrayList<Resident> residentList = residentManager.getResidentList();
     ui.showResidentList(residentList);
 }
```
### Sequence Diagram
![Add View Resident Sequence Diagram](images/view-resident.png)

## View Points Command

### Overview

The view-points command displays the CCA points for all residents in the system.

Format:
view-points

---

### Implementation

The view-points command retrieves and displays CCA points for all residents.

- The Parser creates a ViewPointsCommand object.
- ViewPointsCommand.execute() calls ResidentManager.getResidentList().
- The retrieved list is passed to Ui.showCcaPoints(...) for display.
```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
ArrayList<Resident> residentList = residentManager.getResidentList();
ui.showCcaPoints(residentList);
}
```

### Design Considerations

- Command pattern is used to separate parsing and execution.
- Reuses ResidentManager.getResidentList() to retrieve resident data, keeping the manager layer lean.


## Delete CCA Command

### Overview

The `delete-cca` command removes an existing CCA from the system.

Format:
`delete-cca <cca name>`

---

### Implementation

The `delete-cca` command is implemented using the Command pattern.

- The `Parser` creates a `DeleteCcaCommand` object from user input.
- `DeleteCcaCommand.execute()` calls `CcaManager.deleteCca(...)`.
- If the CCA does not exist, a `CcaNotFoundException` is thrown and handled.
```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
    try {
        ccaManager.deleteCca(ccaName);
        ui.showMessage("CCA deleted: " + ccaName);
    } catch (CcaNotFoundException e) {
        ui.showMessage(e.getMessage());
    }
}
```

### Sequence Diagram
![delete-cca.png](images/delete-cca.png)

### Design Considerations

- Command pattern is used to separate parsing and execution.
- Exception handling is used to manage non-existent CCA cases cleanly.

### Alternatives Considered
1. Direct Invocation from Parser to Manager  
   Approach: Parser directly calls `CcaManager.deleteCca(...)`  
   Rejected because:
    - Violates separation of concerns
    - Makes Parser overly complex
    - Reduces extensibility

## Add Event Command

### Overview

The `add-event` command adds a new event under a specified CCA.

Format:
`add-event <event name> <cca name> <date/time>`

---

### Implementation

The `add-event` command is implemented using the Command pattern.

- The `Parser` creates an `AddEventCommand` object from user input.
- `AddEventCommand.execute()` retrieves the corresponding CCA from `CcaManager`.
- The event is added using `EventManager.addEvent(...)`.
- If the CCA does not exist, a `CcaNotFoundException` is thrown and handled.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   try {
      Cca cca = ccaManager.getCCAList().stream()
              .filter(x -> x.getName().equals(ccaName))
              .findFirst()
              .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

      eventManager.addEvent(eventName, cca, dateTime);

      ui.showMessage("Event added: " + eventName + " for the CCA " + ccaName + ", during " + dateTime);

   } catch (CcaNotFoundException e) {
      ui.showError(e.getMessage());
   }
}
```
### Sequence Diagram
![add-event.png](images/add-event.png)


## Add Resident to Event Command

### Overview

The `add-resident-to-event` command adds an existing resident to a specific event under a CCA.

Format:
`add-resident-to-event <matric number> <event name> <cca name>`

---

### Implementation

The command is implemented using the Command pattern.

- The `Parser` creates an `AddResidentToEventCommand`.
- The command retrieves the `Resident` from `ResidentManager`.
- The corresponding `Cca` is retrieved from `CcaManager`.
- The resident is added to the event using `EventManager.addResidentToEvent(...)`.
- Exceptions are thrown if the resident, CCA, or event does not exist.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   try {
      Resident resident = residentManager.getResidentList().stream()
              .filter(r -> r.getMatricNumber().equalsIgnoreCase(matricNumber))
              .findFirst()
              .orElseThrow(() -> new ResidentNotFoundException(...));

      Cca cca = ccaManager.getCCAList().stream()
              .filter(c -> c.getName().equalsIgnoreCase(ccaName))
              .findFirst()
              .orElseThrow(() -> new CcaNotFoundException(...));

      eventManager.addResidentToEvent(eventName, cca, resident);

      ui.showMessage(...);

   } catch (ResidentNotFoundException | CcaNotFoundException | EventNotFoundException e) {
      ui.showError(e.getMessage());
   }
}
```

### Sequence Diagram

![add-resident-to-event.png](images/add-resident-to-event.png)

## Add Resident to Cca Command

### Overview

The `add-resident-to-cca` command adds an existing resident to a CCA.

Format:
`add-resident-to-cca <matric number> <cca name> <points>`

---

### Implementation

- The `Parser` creates a `AddResdientToCcaCommmand` object.
- The command retrieves the `Resident` from `ResidentManager`.
- The corresponding `Cca` is retrieved from `CcaManager`.
- The `Resident` is added to the Cca using `Cca.addResidentToCca(...)`.
- Exceptions are thrown if the resident or CCA does not exist.

```java
@Override
 public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
     try {
         Cca cca = ccaManager.getCCAList().stream()
                 .filter(x -> x.getName().equals(ccaName))
                 .findFirst()
                 .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));

         Resident resident = residentManager.getResidentList().stream()
                 .filter(x -> x.getMatricNumber().equals(matriculationNo))
                 .findFirst()
                 .orElseThrow(() -> new ResidentNotFoundException(matriculationNo + " not found."));

         cca.addResidentToCca(resident);
         resident.addCcaToResident(cca, pointsScored);

         ui.showMessage("Resident " + resident + " was added to CCA: " + cca.getName() +
                 " with " + pointsScored + " points.");

     } catch (CcaNotFoundException | ResidentNotFoundException | ResidentAlreadyInCcaException e) {
         ui.showError(e.getMessage());
     }
 }
```

### Sequence Diagram

![add-resident-to-cca.png](images/add-resident-to-cca.png)

## Add EXCO to Cca Command

### Overview

The `add-exco-to-cca` command adds an existing resident as an EXCO for the Cca.

Format:
`add-exco-to-cca <matric number> <cca name>`

---

### Implementation

- The `Parser` creates a `AddExcoToCcaCommand` object.
- The command retrieves the `Resident` from `ResidentManager`.
- The corresponding `Cca` is retrieved from `CcaManager`.
- The `Resident` is added to the Cca as an EXCO in the `excoResidents` arraylist.
- Exceptions are thrown if the resident or CCA does not exist.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   try {
      Cca cca = ccaManager.getCCAList().stream()
              .filter(x -> x.getName().equals(ccaName))
              .findFirst()
              .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));
   
      Resident resident = residentManager.getResidentList().stream()
              .filter(x -> x.getMatricNumber().equals(matriculationNo))
              .findFirst()
              .orElseThrow(() -> new ResidentNotFoundException(matriculationNo + " not found."));
   
      cca.addExcoToCca(resident);
      resident.addCcaToResident(cca);
   
      ui.showMessage("Resident " + resident + " was added as an EXCO to CCA: " + cca.getName());
   
   } catch (CcaNotFoundException | ResidentNotFoundException | ResidentAlreadyInCcaException e) {
      ui.showError(e.getMessage());
   }
}
```

## Sequence Diagram

![add-exco-to-cca.png](images/add-exco-to-cca.png)

## View all the EXCOs of a Cca

### Overview
The `view-exco` command display the list of EXCOs of an existing Cca.

Format:
`view-exco <cca name>`

## Implementation

- The `Parser` creates a `ViewCcaExco` object.
- The command retrieves the `CcaList` from `CcaManager`.
- It checks if the `Cca` input is a part of `CcaList`
- If no it throws a `CcaNotFoundException`.
- If yes, then is displays the `excoMembers` of a `Cca`

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   try {
      Cca cca = ccaManager.getCCAList().stream()
              .filter(x -> x.getName().equals(ccaName))
              .findFirst()
              .orElseThrow(() -> new CcaNotFoundException(ccaName + " not found."));
      ui.showExcoList(cca.getExcos());
   } catch (CcaNotFoundException e) {
      ui.showError(e.getMessage());
   }
}
```

## Sequence Diagram

![view-exco.png](images/view-exco.png)

## CCA Statistics Command

### Overview

The `cca-stats` command displays the average points and most active member for each CCA as well as the most popular CCA based on the average points.

Format:
`cca-stats`

---

### Implementation

- The `Parser` creates a `CcaStatsCommand` object.
- `CcaStatsCommand.avgPoints()` computes the average points for each CCA.
- `CcaStatsCommand.mostPopularCca()` finds the most popular CCA by finding the CCA with the highest average points.
- `CcaStatsCommand.mostActiveResidents()` finds the most active member of each CCA by taking the resident with the most points for that CCA.
- If there are no CCAs in the first place, `CcaStatsCommand.execute()` passes a message to the user through `Ui.showMessage()`. Otherwise, it passes the above information to `Ui.showCcaStats()` for display.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   ArrayList<Cca> ccas = ccaManager.getCCAList();
   try {
      HashMap<Cca, Double> avgPoints = avgPoints(ccas);
      Cca mostPopularCca = mostPopularCca(avgPoints);
      HashMap<Cca, Resident> mostActiveResidents = mostActiveResidents(ccas);
      ui.showCcaStats(avgPoints, mostPopularCca, mostActiveResidents);
   } catch (IllegalArgumentException e) {
      ui.showMessage("There are no CCAs currently. Please add CCAs using add-cca command");
   }
}
```
### Sequence Diagram
![Add CCA Statistics Sequence Diagram](images/cca-stats.png)

## Resident Statistics Command

### Overview

The `resident-stats` command displays the total points for each resident and the most active residents across all CCAs

Format:
`resident-stats`

---

### Implementation

- The `Parser` creates a `ResidentStatsCommand` object.
- `ResidentStatsCommand.totalPoints()` computes the total points for each resident.
- `ResdientStatsCommand.mostActiveResidents()` finds the most active residents across all CCAs based on their total points.
- If there are no resdients in the first place, `ResidentStatsCommand.execute()`passes a message to the user through `Ui.showMessage()`. Otherwise, it passes the above information to `Ui.showResidentStats()` for display.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
   ArrayList<Resident> residents = residentManager.getResidentList();
   if (residents.isEmpty()) {
      ui.showMessage("There are no residents currently. Please add residents using add-resident command");
      return;
   }
   HashMap<Resident, Integer> totalPoints = totalPoints(residents);
   ArrayList<Resident> mostActiveResident = mostActiveResidents(totalPoints);
   ui.showResidentStats(totalPoints, mostActiveResident);
}
```
### Sequence Diagram
![Add Resident Statistics Sequence Diagram](images/resident-stats.png)

## Help Command

### Overview
The `help` command presents a list of all available commands and their usage.

Format:
`help`

### Implementation
The `help` command is implemented using the Command pattern.

- The `Parser` creates a `HelpCommand` object from user input.
- `HelpCommand.execute()` creates a string which is a list of all commands and their usage.
- It then passes the string to `Ui.showMessage()`.

```java
@Override
 public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
    String help = "Here is a list of all commands:\n" +
            "> add-cca <cca name> <level (HIGH, MEDIUM, LOW or UNKNOWN)>\n" +
            "> view-cca\n" +
            "> delete-cca <cca name>\n" +
            "> add-event <event name> <cca name> <data time>\n" +
            "> add-resident <name> <matric number>\n" +
            "> view-resident\n" +
            "> add-resident-to-cca <matric number> <cca name> <points>\n" +
            "> add-resident-to-event <matric number> <event name> <cca name>\n" +
            "> view-points\n" +
            "> cca-stats\n" +
            "> resident-stats\n" +
            "> help\n" +
            "> bye";
    ui.showMessage(help);
}
 ```

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
