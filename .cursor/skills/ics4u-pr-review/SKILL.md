---
name: ics4u-pr-review
description: >-
  Reviews pull requests and Java code for the ICS4U Hospital Management System
  against Ontario Grade 12 Computer Science curriculum expectations and the
  project design specification. Use when reviewing PRs, code changes, merge
  requests, or when the user asks for ICS4U, OOP, or curriculum-aligned code review.
---

# ICS4U PR Review & Coding Practices

Review code against **Ontario ICS4U curriculum expectations** and the **Hospital Management System design spec** (`docs/API.md`).

## Review Workflow

1. Read the PR diff and identify which subsystem(s) changed (`staff`, `patient`, `appointment`, `shared`).
2. Cross-check changed classes and methods against `docs/API.md`.
3. Evaluate against the checklist below.
4. Post structured feedback using the output template.

## Output Template

```markdown
## PR Review Summary
**Subsystem(s):** staff | patient | appointment | shared
**Verdict:** Approve | Request Changes | Needs Discussion

### Critical (must fix)
- ...

### Curriculum & Design (should fix)
- ...

### Suggestions (optional)
- ...

### What's done well
- ...
```

Use severity labels: **Critical** (blocks merge), **Should fix** (curriculum/spec gap), **Suggestion** (style/clarity).

---

## ICS4U Curriculum Checklist

### A1 — Data Types & Expressions
- [ ] Correct use of primitives, `String`, `char`, `double`, arrays
- [ ] One-dimensional arrays of objects (not `ArrayList` unless explicitly approved)
- [ ] Valid type casting in inheritance hierarchies
- [ ] String comparisons use `.equals()`, not `==`

### A2 — Modular Programming
- [ ] Code split across packages: `staff`, `patient`, `appointment`, `shared`
- [ ] Each class in its own file, filename matches class name
- [ ] Reusable design via encapsulation, inheritance, method overriding, polymorphism
- [ ] Manager classes (`StaffManager`, `PatientManager`, `ApptManager`) handle collections; domain classes handle entity behaviour

### A3 — Algorithms & File I/O
- [ ] **File I/O:** `loadFromFile` / `saveToFile` read and write external text files correctly using buffer
- [ ] **Required searches:** linear search AND recursive search present somewhere in the project
- [ ] **Required sorts:** selection sort, bubble sort, AND insertion sort each used at least once
- [ ] **Recursion:** at least one meaningful recursive method (e.g. `findStaffByID`, `scheduleOR`, `searchByID`)
- [ ] Array insert/delete uses left-shift pattern, not orphaned slots
- [ ] Edge cases handled: empty arrays, full arrays, not-found returns (`null`, `-1`, `false`)

| Required Algorithm | Expected Location |
|--------------------|-------------------|
| Selection sort | `StaffManager.sortStaff()`, `PatientManager.sortByWard()`, `ApptManager.sortByDate()` |
| Bubble sort | `StaffManager.sortStaffByExp()`, `PatientManager.sortByPatientID()`, `ApptManager.sortByPatientThenDate()` |
| Insertion sort | `PatientManager.sortByDateEntered()` |
| Linear search | `StaffManager.findStaff()`, `ApptManager.searchByDate()` |
| Binary search | `PatientManager.searchPatientByPatientID()` |
| Recursion | `StaffManager.findStaffByID()`, `Surgeon.scheduleOR()`, `ApptManager.searchByID()` |

### C1 — Modular Design (OOP)
- [ ] Abstract base classes: `Staff`, `Patient`, `Appointment`
- [ ] Concrete subclasses override abstract methods with **different** behaviour (polymorphism)
- [ ] All fields **private**; access only through getters/setters
- [ ] Array getters return **defensive copies** where specified (e.g. `getOffDays()`)
- [ ] No business logic in `HospitalRunner` — runner delegates to managers
- [ ] Cross-subsystem calls follow the interaction map in `docs/API.md`

### C2 — Algorithm Analysis Readiness
- [ ] Search/sort method names or comments identify the algorithm used
- [ ] Recursive methods have clear base cases; no infinite recursion risk
- [ ] Conflict-check and validation methods have single, readable responsibilities

---

## Design Spec Compliance

Verify against `docs/API.md`:

### Staff (`staff` package)
- [ ] `Staff.hasTimeConflict()` prevents double-booking
- [ ] `Doctor` enforces `maxPatients`; `Nurse` filters by ward; `Surgeon` uses binary search on referrals
- [ ] `StaffManager.getAvailableNurses()`, `getTriageNurse()`, `getTraumaDoctor()` implemented for appointment module

### Patient (`patient` package)
- [ ] `Patient.isValidOHIP()` enforces 10-digit OHIP
- [ ] `checkAllergyConflict()` checked before prescribing
- [ ] Each patient subtype overrides `checkIn()`, `checkOut()`, `calculateBill()`, `scheduleNextAppointment()` differently
- [ ] `EmergencyPatient.updateStatus()` validates allowed status values

### Appointment (`appointment` package)
- [ ] `Appointment.equals()` detects room and staff conflicts
- [ ] `RoutineCheckup`, `Surgery`, `EmergencyVisit` each override `calculateCost()`, `validateBooking()`, `assignStaff()`
- [ ] `ApptManager.isSlotConflict()` called before booking

### File Formats
- [ ] `staff.txt`, `patients.txt`, `patient_appointments.txt`, `appointments.txt` match documented formats
- [ ] Colon-separated records (staff/patients) and comma-separated lines (appointments) parsed correctly

---

## Java Coding Standards (ICS4U)

### Naming
- Classes: `PascalCase` (`StaffManager`, `InPatient`)
- Methods/variables: `camelCase` (`findStaffByID`, `staffCount`)
- Constants: `UPPER_SNAKE_CASE` if used
- Packages: lowercase (`staff`, `patient`, `appointment`, `shared`)

### Structure
- One public class per file
- Constructor initializes all fields
- `toString()` overridden on every domain class for file output
- `@Override` annotation on all overridden methods

### Error Handling
- Validate inputs at method entry (null checks, format checks, bounds checks)
- Print clear error messages for user-facing failures; return `false`/`null` for recoverable errors
- Do not swallow exceptions silently; use try-catch around file I/O with meaningful messages

### What to Flag in Review

**Critical:**
- Public fields instead of private + accessors
- Missing abstract method overrides in subclasses
- Broken polymorphism (e.g. `instanceof` chains where override should suffice)
- Hardcoded file paths that break portability
- Logic that bypasses conflict checking or allergy validation

**Should fix:**
- Missing required algorithm from the spec table
- Method signature differs from `docs/API.md`
- Cross-subsystem call missing (e.g. `markDone()` not calling `addToHistory()`)
- Duplicate code across subclasses that belongs in the abstract base

**Suggestion:**
- Missing JavaDoc on public manager methods
- Magic numbers without named constants
- Overly long methods (> 30 lines) that could be decomposed
- Inconsistent date/time formatting

---

## PR Scope Guidance

When reviewing team PRs:

| Author area | Package | Key files |
|-------------|---------|-----------|
| Ferdinand | `staff` | `Staff`, `Doctor`, `Nurse`, `Surgeon`, `StaffManager` |
| Caroline | `patient` | `Patient`, `InPatient`, `OutPatient`, `EmergencyPatient`, `Medication`, `PatientManager` |
| Ida | `appointment` | `Appointment`, `RoutineCheckup`, `Surgery`, `EmergencyVisit`, `ApptManager` |
| Shared | `shared` | `Date` |

Flag changes that modify another member's subsystem without coordination. Integration touchpoints (imports across packages, shared `Date`, manager cross-calls) need extra scrutiny.

---

## Additional Resources

- API spec (signatures + algorithms): `docs/API.md`
- Writing new code: use `ics4u-java-implement` skill
- Ontario ICS4U expectations: encapsulation, inheritance, polymorphism, file I/O, search/sort/recursion (Curriculum 2008, Strand A & C)
