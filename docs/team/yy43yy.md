# Yi Yang - Project Portfolio Page

## Project: CCAManager
CCAManager is a CLI-based application designed for Hall Leaders to manage Resident CCA records, track event participation, and analyze performance points efficiently.

---

## Summary of Contributions
### 1. Delete Resident Feature
* **Functional Implementation:** Implemented the `DeleteResidentCommand` to allow users to remove residents from the system using their matric number.
* **Validation Logic:** Ensured proper error handling for non-existent residents and empty resident lists.
* **Data Consistency:** Updated the system state after deletion so that resident records are removed cleanly.

### 2. View Points Feature
* **Functional Implementation:** Implemented the `ViewPointsCommand` to display each resident’s total CCA points together with a detailed breakdown of points earned from each CCA.
* **Logic:** Used resident point and CCA records to calculate and display total points accurately.
* **UI Formatting:** Structured the output clearly with separators and breakdown sections to improve readability for users.


### 3. View CCA Events Feature
* **Functional Implementation:** Implemented the `ViewCcaEventsCommand` to allow users to view all events associated with a specified CCA.
* **Filtering Logic:** Ensured only events relevant to the given CCA are displayed.
* **Error Handling:** Handled cases where no matching events exist or where the requested CCA has no recorded events.


### 4. View My Events Feature
* **Functional Implementation:** Implemented the `ViewMyEventsCommand` to allow a resident to view all events they are participating in.
* **Logic:** Integrated resident and event information to retrieve the correct list of events linked to a resident.
* **User-Centric Design:** Presented the information in a straightforward and personalized format for easier interpretation.

### 5. Update Points Feature
* **Functional Implementation:** Implemented the `UpdateCcaPointCommand` to allow users to update the number of points a resident has for a specific CCA.
* **Validation Logic:** Ensured that input points are non-negative and that both the resident and CCA exist before performing the update.
* **Encapsulation:** Delegated point update logic to the `Resident.updatePoint(...)` method to maintain data consistency and proper separation of concerns.


### 6. Sort Points Feature
* **Functional Implementation:** Implemented the `SortPointsCommand` to sort residents based on their total CCA points.
* **Sorting Logic:** Used Java’s `Comparator` to arrange residents in descending order of total points.
* **Usability Improvement:** Enabled Hall Leaders to quickly identify top-performing residents through a sorted view of points.

### 7. Testing & Quality Assurance
* **Unit Testing:** Wrote JUnit tests for `DeleteResidentCommand`, `ViewPointsCommand`, `ViewCcaEventsCommand`, and `ViewMyEventsCommand`.
* **Edge Case Coverage:** Tested scenarios such as empty lists, invalid inputs, and non-existent records to ensure robustness.
* **Quality Assurance:** Verified that implemented features behaved correctly and consistently with the project requirements.

---


## Contributions to the User Guide
* Wrote instructions for **View Points** (`view-points`).
* Wrote instructions for **View CCA Events** (`view-cca-events`).
* Wrote instructions for **View My Events** (`view-my-events`).
* Wrote instructions for **Sort Points** (`sort-points`).
* * Wrote instructions for **Sort Points** (`edit-points`).

## Contributions to the Developer Guide
* Documented the implementation details for `DeleteResidentCommand`, `ViewPointsCommand`, `ViewCcaEventsCommand`, `UpdateCcaPointCommand`, `ViewMyEventsCommand`, and `SortPointsCommand`.
* Contributed explanations of the logic and design considerations behind resident point viewing and event viewing features.
* Helped document command execution behavior and feature interactions within the application.

---

## Contributions to the Team Project

### Feature Development
* Took ownership of multiple user-facing commands related to resident management, point tracking, and event viewing.
* Focused on implementing features that improve data retrieval and presentation for end users.

### Code Quality & Collaboration
* Ensured that implemented features were consistent with the project’s overall architecture and coding style.
* Collaborated with teammates to integrate features across the `ResidentManager`, `CcaManager`, and `EventManager`.

---

## Summary of Contributions
* Implemented key resident-related features including resident deletion, point viewing, event viewing, and point sorting.
* Contributed unit tests to improve the reliability of these features.
* Improved the usability of CCAManager by enhancing how resident and event data are presented to users.
