# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

# CCA Manager


## Add CCA Command

### Overview

The `add-cca` command adds a new CCA to the system.

Format:
`add-cca <cca name>`

---

### Implementation

The `add-cca` command is implemented using the Command pattern.

- The `Parser` creates an `AddCcaCommand` object from user input.
- `AddCcaCommand.execute()` calls `CcaManager.addCCA(...)`.
- If the CCA already exists, a `DuplicateCcaException` is thrown and handled.

```java
@Override
public void execute(CcaManager ccaManager, ResidentManager residentManager, Ui ui) {
    try {
        ccaManager.addCCA(ccaName);
        ui.showMessage("CCA added: " + ccaName);
    } catch (DuplicateCcaException e) {
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
- `CcaStatsCommand.mostActiveResidents()` find the most active member of each CCA by taking the resident with the most points for that CCA.
- If there are no CCAs in the first place, `CcaStatsCommand.execute()`passes a message to the user through `Ui.showMessage()`. Otherwise, it passes the above information to `Ui.showCcaStats()` for display.

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

private static HashMap<Cca, Double> avgPoints(ArrayList<Cca> ccas) throws IllegalArgumentException {
   if (ccas.isEmpty()) {
      throw new IllegalArgumentException();
   }
   HashMap<Cca, Double> avgPoints = new HashMap<>();
   for (Cca cca : ccas) {
      ArrayList<Resident> registeredResidents = cca.getRegisteredResidents();
      double totalPoints = 0;
      for (Resident resident : registeredResidents) {
         totalPoints += resident.getCcaMap().get(cca);
      }
      double avg = totalPoints / registeredResidents.size();
      avgPoints.put(cca, avg);
   }
   return avgPoints;
}

private static Cca mostPopularCca(HashMap<Cca, Double> avgPoints) throws IllegalArgumentException {
   if  (avgPoints.isEmpty()) {
      throw  new IllegalArgumentException();
   }
   Cca mostPopularCca = null;
   for (Cca cca : avgPoints.keySet()) {
      if (mostPopularCca == null) {
         mostPopularCca = cca;
      } else if (avgPoints.get(cca) > avgPoints.get(mostPopularCca)) {
         mostPopularCca = cca;
      }
   }
   return mostPopularCca;
}

private static HashMap<Cca, Resident> mostActiveResidents(ArrayList<Cca> ccas) throws IllegalArgumentException {
   if (ccas.isEmpty()) {
      throw new IllegalArgumentException();
   }
   HashMap<Cca, Resident> mostActiveResidents = new HashMap<>();
   for (Cca cca : ccas) {
      ArrayList<Resident> registeredResidents = cca.getRegisteredResidents();
      Resident mostActiveResident = null;
      for (Resident resident : registeredResidents) {
         if (mostActiveResident == null) {
            mostActiveResident = resident;
         } else if (resident.getCcaMap().get(cca) > mostActiveResident.getCcaMap().get(cca)) {
            mostActiveResident = resident;
         }
      }
      mostActiveResidents.put(cca, mostActiveResident);
   }
   return mostActiveResidents;
}
```
### Sequence Diagram
![Add CCA Statistics Sequence Diagram](images/cca-stats.png)

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
