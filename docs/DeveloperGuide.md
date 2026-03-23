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
<img width="550" height="381" alt="AddCca" src="https://github.com/user-attachments/assets/6eed8ab5-d334-45ed-8d02-1769cb8416b6" />


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
