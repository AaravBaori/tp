# User Guide

## Introduction

{Give a product intro}

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features

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
> add-resident-to-cca <matric number> <cca name> <points>
> add-resident-to-event <matric number> <event name> <cca name>
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
