# User Guide

## Introduction

This is a CLI based CCAManager tool. It aids the Hall Leader/CCA Leader to maintain and analyze CCA records of the CCA participants.

---


## Quick Start

Follow the steps below to set up and run the application:

### Prerequisites
- Ensure that you have **Java 17** installed on your system.
- You can verify your Java version by running:
  ```
  java -version
  ```
- If the version is not Java 17, install it before proceeding.

---

### Download the Application
1. Go to the project repository:  
   https://github.com/AY2526S2-CS2113-W13-1/tp
2. Navigate to the **Releases** section.
3. Download the latest `.jar` file.

---

### Running the Application
1. Open a terminal/command prompt.
2. Navigate to the folder containing the `.jar` file.
3. Run the following command:
   ```
   java -jar CCAledger.jar
   ```

---

### First Run
- The application will start in the terminal.
- You can begin entering commands immediately.
- Use the following command to see available commands:
  ```
  help
  ```

---

### Notes
- Ensure that the `.jar` file is in the correct directory before running.
- If the application does not start, re-check your Java version and installation.
- All data will be stored locally in the same directory as the `.jar` file.

---

# Index

- [CCA Commands](#cca-commands)
- [Event Commands](#event-commands)
- [Resident Commands](#resident-commands)
- [EXCO Commands](#exco-commands)
- [Statistics Commands](#statistics-commands)
- [General Commands](#general-commands)
- [Command Summary (Cheat Sheet)](#command-summary)

---

# Features

---

## CCA Commands

### Adding CCA

Add a new CCA with its priority. Priority can be HIGH/MEDIUM/LOW/UNKNOWN

Format: `add-cca <cca name>; <level>`

Example :

```
> add-cca Basketball; HIGH
_________________________________________________________________________________
 CCA added: Basketball (HIGH)
_________________________________________________________________________________
```

---

### Viewing CCA

View all the CCAs

Format: `view-cca`

Example :

```
> view-cca
_________________________________________________________________________________
Here is the complete list of all the CCAs :
1. Basketball (HIGH): 0 residents
2. ComputingClub (MEDIUM): 0 residents
3. Dance (LOW): 0 residents
_________________________________________________________________________________
```

---

### Deleting CCA

Delete any existing CCA

Format: `delete-cca <cca name>`

Example :

```
> delete-cca Basketball
_________________________________________________________________________________
 CCA deleted: Basketball
_________________________________________________________________________________
```

---

### Viewing Events for a CCA: `view-cca-events`

View all events belonging to a specific CCA.

Format: view-cca-events <cca name>

Example:

```
> view-cca-events Basketball
_________________________________________________________________________________
Here are the events:
1. training date: 05/06/2025
2. finals date: 12/12/2025
_________________________________________________________________________________
```
---

### Adding Resident to a CCA: `add-resident-to-cca`

Add an existing resident to a CCA and assign their points for participation.

Format: `add-resident-to-cca <matric number> <cca name> <points>`

Example:
```
> add-resident-to-cca A1234567B; Basketball; 10
_________________________________________________________________________________
 Resident John | A1234567B was added to CCA: Basketball with 10 points.
_________________________________________________________________________________
```

---

## Event Commands

### Adding Event

Adding an event to any CCA

Format: `add-event <event name>; <cca name>; <date>`

Example :

```
> add-event Practice-Week1; Dance; 29/3/26
_________________________________________________________________________________
 Event added: Practice-Week1 for the CCA Dance, during 29/3/26
_________________________________________________________________________________
```

---

### Adding Resident to an Event

Residents who take part or attend an event need to be added to the event

Format: `add-resident-to-event <matric number>; <event namee>; <cca name>`

Example :

```
> add-resident-to-event A123; Practice-Week1; Dance
_________________________________________________________________________________
 Successfully added Veer to event Practice-Week1 under CCA Dance.
_________________________________________________________________________________
```

---


### Adding Resident to a CCA: `add-resident-to-cca`

Add an existing resident to a CCA and assign their points for participation.

Format: `add-resident-to-cca <matric number>; <cca name>; <points>`

Example:
```
> add-resident-to-cca A1234567B; Basketball; 10
_________________________________________________________________________________
 Resident John | A1234567B was added to CCA: Basketball with 10 points.
_________________________________________________________________________________
```

---

### Viewing My Events: `view-my-events`

View all events that a resident is participating in.

Format: view-my-events <matric number>

Example:

```
> view-my-events A1234567B
Hi John, here are your events:
_________________________________________________________________________________
1. Practice-Week1 | Basketball | 29/3/26
2. Orientation | ComputingClub | 2/4/26
_________________________________________________________________________________
```

---

## Resident Commands

### Adding Resident: : `add-resident`

Add a new resident into the system.

Format: `add-resident <resident name>; <matric number>`

Example:

```
> add-resident Ramesh; A1234567B
_________________________________________________________________________________
Resident added: Ramesh A1234567B
_________________________________________________________________________________
```

---

### Viewing residents: `view-resident`

Displays all the residents in the system.

Format: `view-resident`

Example:

`> view-resident`

```
_________________________________________________________________________________
Here is the complete list of all the residents :
1. John | 1234
2. James | 4321
_________________________________________________________________________________
```

---

### Deleting Resident: `delete-resident`

Delete an existing resident from the system.

Format: delete-resident <matric number>

Example:

```
> delete-resident A1234567B
_________________________________________________________________________________
Resident deleted: John
_________________________________________________________________________________
```

---

### Viewing Points: `view-points`

View the CCA points of all residents.

Format: view-points

Example:

```
> view-points
_________________________________________________________________________________
Here are the points of each resident:
1. jiawen | 12345: 10 points
_________________________________________________________________________________
Here is the breakdown:
floorball 10
_________________________________________________________________________________
2. Si Zhao | 123: 5 points
_________________________________________________________________________________
Here is the breakdown:
floorball 5
_________________________________________________________________________________
3. yy | A1: 0 point
_________________________________________________________________________________```
```
---

### Viewing Points(sorted): `sort-points`

View the CCA points of all residents in a sorted manager.

Format: sort-points

Example:

```
> sort-points
_________________________________________________________________________________
 Residents sorted by total points in descending order.
_________________________________________________________________________________
_________________________________________________________________________________
Here are the points of each resident:
1. jiawen | 12345: 10 points
_________________________________________________________________________________
Here is the breakdown:
floorball 10
_________________________________________________________________________________
2. Si Zhao | 123: 5 points
_________________________________________________________________________________
Here is the breakdown:
floorball 5
_________________________________________________________________________________
3. yy | A1: 0 point
_________________________________________________________________________________```
```
---

## EXCO Commands

### Viewing EXCO members of a CCA: `view-exco`

Displays the list of Executive Committee (EXCO) members for a specific CCA.

Format: `view-exco <cca name>`

Example:

```
> view-exco Basketball
_________________________________________________________________________________
Here is the complete list of all the EXCOs :
1. Aarav | A0310652R
_________________________________________________________________________________
```

---

### Promoting/Assigning a resident to EXCO: `add-exco-to-cca`

Assigns a resident as an Executive Committee member for a specific CCA.

Format: `add-exco-to-cca <matric number>; <cca name>`

Example:

```
_________________________________________________________________________________
> add-exco-to-cca A0310652R Basketball
_________________________________________________________________________________
 Resident Aarav | A0310652R was added as an EXCO to CCA: Basketball
_________________________________________________________________________________
> view-exco Basketball
_________________________________________________________________________________
Here is the complete list of all the EXCOs :
1. Aarav | A0310652R
_________________________________________________________________________________
```

---

## Statistics Commands

### Viewing per-CCA statistics: `cca-stats`

Displays the average points and the most active resident for each CCA, as well as the most popular CCAs based on average points.

Format: `cca-stats`

Example:

```
> cca-stats
_________________________________________________________________________________
Average points and most active resident per CCA:
1. Football(HIGH): 1 residents, average points: 4.0, most active: John | 1234
2. Basketball(HIGH): 2 residents, average points: 6.0, most active: James | 4321

Most popular CCAs:
1. Basketball(HIGH): 2 residents, average points: 6.0
_________________________________________________________________________________
```

---

### Viewing per-resident statistics: `resident-stats`

Displays the total points for each resident and the most active residents across all CCAs.

Format: `resident-stats`

Example:

```
> resident-stats
_________________________________________________________________________________
Total points for each resident:
1. John | 1234, total points: 9
2. James | 4321, total points: 7

Most active residents across all CCAs:
1. John | 1234, total points: 9
_________________________________________________________________________________
```

---

## General Commands

### Viewing all the available commands: `help`

Displays a list of all available commands and their usage.

Format: `help`

Example:

```
_________________________________________________________________________________
 --- CCA Manager Help Menu ---

[CCA Management]
> add-cca <name>; <level (HIGH, MEDIUM, LOW, UNKNOWN)>
> delete-cca <name>
> view-cca
> add-exco-to-cca <matric> ; <cca name>
> view-exco
> cca-stats

[Resident Management]
> add-resident <name>; <matric>
> delete-resident <matric>
> view-resident
> add-resident-to-cca <matric>; <cca name>; <points>
> view-points
> resident-stats

[Event Management]
> add-event <name>; <cca name>; <date time>
> add-resident-to-event <matric>; <event name>; <cca name>
> view-cca-events <cca name>
> view-my-events <matric>

[General]
> help
> bye
_________________________________________________________________________________
```

---

# Command Summary

```
add-cca <cca name>; <level>
view-cca
delete-cca <cca name>

add-event <event name>; <cca name>; <date>
view-cca-events <cca name>
add-resident-to-event <matric number>; <event name>; <cca name>
view-my-events <matric number>

add-resident <name>; <matric number>
view-resident
delete-resident <matric number>
view-points

view-exco <cca name>
add-exco-to-cca <matric number>; <cca name>

cca-stats
resident-stats

help
bye
```
