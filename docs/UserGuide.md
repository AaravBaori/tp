# User Guide

## Introduction

This is a CLI based CCAManager tool. It aids the Hall Leader/CCA Leader to maintain and analyze CCA records of the
CCA participants.

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
1. Down the latest version of `CCAManager` from [here](http://link.to/duke).

## Features

### Adding CCA

Add a new CCA with its priority

Format: `add-resident <Resident Name> <Student Number>`

Example :

```
> add-cca Basketball HIGH
_________________________________________________________________________________
 CCA added: Basketball(HIGH)
_________________________________________________________________________________
```

### Viewing CCA

View all the CCAs

Format: `view-cca`

Example :

```
> view-cca
_________________________________________________________________________________
Here is the complete list of all the CCAs :
1. Basketball(HIGH): 0 residents
2. ComputingClub(MEDIUM): 0 residents
3. Dance(LOW): 0 residents
_________________________________________________________________________________
```
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

### Adding Event

Adding an event to any CCA

Format: `add-event <event name> <cca name> <date>`

Example :

```
> add-event Practice-Week1 Dance 29/3/26
_________________________________________________________________________________
 Event added: Practice-Week1 for the CCA Dance, during 29/3/26
_________________________________________________________________________________
```

### Adding Resident to an Event

Residents who take part or attend an event need to be added to the event

Format: `add-resident-to-event <matric number> <event namee> <cca name>`

Example :

```
> add-resident-to-event A123 Practice-Week1 Dance
_________________________________________________________________________________
 Successfully added Veer to event Practice-Week1 under CCA Dance.
_________________________________________________________________________________
```





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

### Viewing per-CCA statistics: `cca-stats`
Displays the average points and the most active resident for each CCA, as well as the most popular CCAs based on average points.

Format: `cca-stats`

Example:

`> cca-stats`

```
_________________________________________________________________________________
Average points and most active resident per CCA:
1. Football(HIGH): 1 residents, average points: 4.0, most active: John | 1234
2. Basketball(HIGH): 2 residents, average points: 6.0, most active: James | 4321

Most popular CCAs:
1. Basketball(HIGH): 2 residents, average points: 6.0
_________________________________________________________________________________
```

### Viewing per-resident statistics: `resident-stats`
Displays the total points for each resident and the most active residents across all CCAs.

Format: `resident-stats`

Example:

`> resident-stats`

```
_________________________________________________________________________________
Total points for each resident:
1. John | 1234, total points: 9
2. James | 4321, total points: 7

Most active residents across all CCAs:
1. John | 1234, total points: 9
_________________________________________________________________________________
```

### Viewing EXCO members of a CCA: `view-exco`
Displays the list of Executive Committee (EXCO) members for a specific CCA.

Format: `view-exco <cca name>`

Example:

`> view-exco Basketball`

```

> view-exco Basketball
_________________________________________________________________________________
Here is the complete list of all the EXCOs :
1. Aarav | A0310652R
_________________________________________________________________________________

```

### Promoting/Assigning a resident to EXCO: `add-exco-to-cca`
Assigns a resident as an Executive Committee member for a specific CCA.

Format: `add-exco-to-cca <matric number> <cca name>`

Example:

`> add-exco-to-cca A0123456G Basketball`

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

### Viewing all the available commands: `help`
Displays a list of all available commands and their usage.

Format: `help`

Example:

`> help`

```
_________________________________________________________________________________
 Here is a list of all commands:
> add-cca <cca name> <level (HIGH, MEDIUM, LOW or UNKNOWN)>
> view-cca
> delete-cca <cca name>
> add-event <event name> <cca name> <data time>
> add-resident <name> <matric number>
> view-resident
> view-exco <cca name>
> add-resident-to-cca <matric number> <cca name> <points>
> add-resident-to-event <matric number> <event name> <cca name>
> add-exco-to-cca <matric number> <cca name>
> view-points
> cca-stats
> resident-stats
> help
> bye
_________________________________________________________________________________
```

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
