---
name: ics4u-java-implement
description: >-
  Implements Java code for the ICS4U Hospital Management System following
  Ontario Grade 12 Computer Science curriculum expectations, OOP design patterns,
  and the project API spec. Use when writing, implementing, or scaffolding
  classes, methods, file I/O, search/sort algorithms, or when the user asks
  for ICS4U-compliant Java code.
---

# ICS4U Java Implementation

Write code that satisfies **Ontario ICS4U curriculum expectations** and matches **`docs/API.md`** exactly — method names, algorithms, and file formats.

For reviewing finished code, use the `ics4u-pr-review` skill instead.

---

## Implementation Workflow

1. **Identify scope** — which package and class (`staff`, `patient`, `appointment`, `shared`).
2. **Read `docs/API.md`** — confirm fields, method signatures, and required algorithm for each method.
3. **Read existing code** in that package — match naming, style, and patterns already in use.
4. **Implement** using the patterns below; do not deviate from the spec algorithm.
5. **Verify** — private fields, `@Override` on overrides, edge cases, compiles cleanly.

---

## Project Structure Rules

```
src/
├── HospitalRunner.java       # Entry point only — no business logic
├── shared/Date.java
├── staff/                    # Ferdinand's subsystem
├── patient/                  # Caroline's subsystem
└── appointment/              # Ida's subsystem
```

- One public class per file; filename = class name.
- Package declaration at top: `package staff;` etc.
- Use **arrays**, not `ArrayList`, unless the team explicitly agrees otherwise.
- Cross-package references use imports: `import patient.Patient;`, `import shared.Date;`

---

## Class Writing Patterns

### Abstract Base Class

```java
package staff;

import patient.Patient;
import appointment.Appointment;

public abstract class Staff {
    private String name;
    private String staffID;
    // ... all fields private

    public Staff(String staffID, String name, /* ... */) {
        this.staffID = staffID;
        this.name = name;
        // initialize every field
    }

    // abstract methods — no body
    public abstract void assignPatients(Patient[] patients);
    public abstract void addAppointment(Appointment appt);
    public abstract double calculatePay();
    public abstract String getSchedule();
    public abstract String toString();

    // shared concrete method
    public boolean hasTimeConflict(Date date, double time) {
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] != null
                    && schedule[i].getDate().compareTo(date) == 0
                    && schedule[i].getTime() == time) {
                return true;
            }
        }
        return false;
    }

    // getters/setters for every field
    public String getStaffID() { return staffID; }
    public void setStaffID(String staffID) { this.staffID = staffID; }

    // defensive copy for array getters when spec requires it
    public String[] getOffDays() {
        String[] copy = new String[offDays.length];
        for (int i = 0; i < offDays.length; i++) {
            copy[i] = offDays[i];
        }
        return copy;
    }
}
```

### Subclass Override

```java
public class Doctor extends Staff {
    private int maxPatients;
    private Patient[] patientsAssigned;

    public Doctor(/* params */) {
        super(/* parent params */);
        this.maxPatients = maxPatients;
        this.patientsAssigned = new Patient[maxPatients];
    }

    @Override
    public void assignPatients(Patient[] patients) {
        int count = 0;
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] != null) count++;
        }
        for (int i = 0; i < patients.length; i++) {
            if (count >= maxPatients) {
                System.out.println("Error: doctor at capacity.");
                return;
            }
            patientsAssigned[count] = patients[i];
            count++;
        }
    }

    @Override
    public String toString() {
        return "Doctor: " + getName() + " (" + getStaffID() + ")";
    }
}
```

**Rules:**
- Every field `private`; never expose mutable arrays directly.
- `@Override` on every overridden method.
- Polymorphism via overrides — avoid long `instanceof` chains when a virtual method suffices.
- Put shared logic in the abstract base; put role-specific logic only in subclasses.

---

## Required Algorithm Templates

Always implement the algorithm named in `docs/API.md` for that method. Label it in a brief comment:

```java
// Selection sort by name
public void sortStaff() { ... }
```

### Linear Search

```java
public Staff findStaff(String name) {
    for (int i = 0; i < staffCount; i++) {
        if (staffArray[i].getName().equals(name)) {
            return staffArray[i];
        }
    }
    return null;
}
```

### Recursive Linear Search

```java
public Staff findStaffByID(String id, int index) {
    if (index >= staffCount) return null;
    if (staffArray[index].getStaffID().equals(id)) return staffArray[index];
    return findStaffByID(id, index + 1);
}
```

### Binary Search (array must be sorted by search key first)

```java
public Patient searchPatientByPatientID(int patientID) {
    return searchPatientByPatientID(patientID, 0, numPatients - 1);
}

private Patient searchPatientByPatientID(int id, int low, int high) {
    if (low > high) return null;
    int mid = (low + high) / 2;
    if (patients[mid].getPatientID() == id) return patients[mid];
    if (id < patients[mid].getPatientID())
        return searchPatientByPatientID(id, low, mid - 1);
    return searchPatientByPatientID(id, mid + 1, high);
}
```

### Selection Sort

```java
public void sortStaff() {
    for (int i = 0; i < staffCount - 1; i++) {
        int minIndex = i;
        for (int j = i + 1; j < staffCount; j++) {
            if (staffArray[j].getName().compareTo(staffArray[minIndex].getName()) < 0) {
                minIndex = j;
            }
        }
        if (minIndex != i) {
            Staff temp = staffArray[i];
            staffArray[i] = staffArray[minIndex];
            staffArray[minIndex] = temp;
        }
    }
}
```

### Bubble Sort

```java
public void sortByPatientID() {
    for (int pass = 0; pass < numPatients - 1; pass++) {
        for (int j = 0; j < numPatients - pass - 1; j++) {
            if (patients[j].getPatientID() > patients[j + 1].getPatientID()) {
                Patient temp = patients[j];
                patients[j] = patients[j + 1];
                patients[j + 1] = temp;
            }
        }
    }
}
```

### Insertion Sort

```java
public void sortByDateEntered() {
    for (int i = 1; i < numPatients; i++) {
        Patient key = patients[i];
        int j = i - 1;
        while (j >= 0 && patients[j].compareToDateRegistered(key) > 0) {
            patients[j + 1] = patients[j];
            j--;
        }
        patients[j + 1] = key;
    }
}
```

### Index-Find-Then-Modify (medications, allergies, appointments)

```java
public boolean deleteMedication(String medName) {
    int index = getIndexOfMedicationByName(medName);
    if (index == -1) return false;
    for (int i = index; i < medications.length - 1; i++) {
        medications[i] = medications[i + 1];
    }
    medications[medications.length - 1] = null;
    return true;
}
```

### Array Insert at End

```java
public void addStaff(Staff s) {
    if (staffCount >= staffArray.length) {
        System.out.println("Error: staff array at capacity.");
        return;
    }
    staffArray[staffCount] = s;
    staffCount++;
}
```

### Array Delete with Left Shift

```java
public boolean removeStaff(String id) {
    for (int i = 0; i < staffCount; i++) {
        if (staffArray[i].getStaffID().equals(id)) {
            for (int j = i; j < staffCount - 1; j++) {
                staffArray[j] = staffArray[j + 1];
            }
            staffArray[staffCount - 1] = null;
            staffCount--;
            return true;
        }
    }
    return false;
}
```

---

## File I/O Pattern

Use `BufferedReader` / `BufferedWriter` or `Scanner` / `PrintWriter`. Always close in `finally` or use try-with-resources.

```java
public void loadFromFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        int count = Integer.parseInt(reader.readLine());
        for (int i = 0; i < count; i++) {
            String role = reader.readLine();
            // parse shared fields, then role-specific fields
            // construct Doctor | Nurse | Surgeon
            // addStaff(staff);
            reader.readLine(); // consume trailing ":"
        }
    } catch (IOException e) {
        System.out.println("Error: could not read " + filename);
    } catch (NumberFormatException e) {
        System.out.println("Error: invalid file format in " + filename);
    }
}

public void saveToFile(String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        writer.println(staffCount);
        for (int i = 0; i < staffCount; i++) {
            writer.println(staffArray[i].toString());
        }
    } catch (IOException e) {
        System.out.println("Error: could not write " + filename);
    }
}
```

- `toString()` on domain classes must produce parseable output matching the file format in `docs/API.md`.
- Use relative paths: `"data/staff.txt"`, not absolute paths.

---

## ICS4U Coding Standards (When Writing)

### Naming
| Element | Convention | Example |
|---------|------------|---------|
| Class | PascalCase | `StaffManager`, `InPatient` |
| Method / variable | camelCase | `findStaffByID`, `staffCount` |
| Constant | UPPER_SNAKE_CASE | `MAX_PATIENTS` |
| Package | lowercase | `staff`, `patient` |

### Strings & Objects
- Compare strings with `.equals()`, never `==`.
- Compare chars with `==`.
- Check for null before calling methods on references.

### Validation at Method Entry
```java
public boolean checkIn(String date) {
    if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
        return false;
    }
    this.dayIn = date;
    return true;
}
```

### Error Handling
- User-facing errors: `System.out.println("Error: ...")` with a clear message.
- Recoverable failures: return `false`, `null`, or `-1` — do not throw unless necessary.
- Never empty catch blocks.

### Method Size & Clarity
- One responsibility per method.
- If a method exceeds ~30 lines, extract a private helper.
- Name private helpers descriptively: `getIndexOfMedicationByName`, not `search`.

---

## Subsystem Ownership

Stay in your assigned package unless integrating at a defined boundary:

| Owner | Package | Implement |
|-------|---------|-----------|
| Ferdinand | `staff` | `Staff`, `Doctor`, `Nurse`, `Surgeon`, `StaffManager` |
| Caroline | `patient` | `Patient`, subclasses, `Medication`, `PatientManager` |
| Ida | `appointment` | `Appointment`, subclasses, `ApptManager` |
| Shared | `shared` | `Date` |

When calling across packages, depend on **abstract types** where possible (`Staff`, `Patient`, `Appointment`), not concrete subclasses.

---

## Pre-Submit Checklist

Before marking implementation complete:

- [ ] All fields private with getters/setters
- [ ] Abstract methods implemented in every concrete subclass
- [ ] Algorithm matches `docs/API.md` (sort/search/recursion named in comment)
- [ ] Array operations use left-shift delete, not gaps
- [ ] Edge cases: empty array, full array, not found, null input
- [ ] `toString()` overridden for file persistence
- [ ] File format matches `docs/API.md`
- [ ] Compiles: `javac src/HospitalRunner.java src/shared/*.java src/staff/*.java src/patient/*.java src/appointment/*.java`
- [ ] No business logic added to `HospitalRunner`

---

## Additional Resources

- API spec (signatures + algorithms): `docs/API.md`
- Code review after implementation: use `ics4u-pr-review` skill
