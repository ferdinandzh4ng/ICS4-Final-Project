# Hospital Management System — API Reference

> **Packages:** `staff` · `patient` · `appointment` · `shared`  
> **Entry point:** `HospitalRunner` (default package)

---

## Table of Contents

1. [Overview](#overview)
2. [Sample Console Output](#sample-console-output)
3. [Data File Formats](#data-file-formats)
4. [Shared Module](#shared-module)
5. [Staff Module](#staff-module)
6. [Patient Module](#patient-module)
7. [Appointment Module](#appointment-module)

---

## Overview

The system is organized into three modules connected by object references and file I/O:

| Module | Package | Manager Class | Data Files |
|--------|---------|---------------|------------|
| Staff | `staff` | `StaffManager` | `data/staff.txt` |
| Patient | `patient` | `PatientManager` | `data/patients.txt`, `data/patient_appointments.txt` |
| Appointment | `appointment` | `ApptManager` | `data/appointments.txt` |

All domain classes use **private fields** with getters/setters. Abstract base classes define shared behaviour; subclasses override type-specific methods.

The program is driven by a **text-based menu** with numbered choices. Console output for key operations must match the formats in [Sample Console Output](#sample-console-output) (from the Stage 1 proposal).

---

## Sample Console Output

Reference output for `HospitalRunner` and manager display methods. User prompts use `>` for input. Dates shown to the user use `YYYY-MM-DD`; times use `hh.mm` (24-hour) or `HH:MM` in schedule views.

### Book a routine checkup

Triggered when booking via `ApptManager.addAppointment()` after selecting **Routine Checkup**.

```
--- Book Appointment ---
[1] Routine Checkup  [2] Surgery  [3] Emergency Visit
> 1
Patient ID: 1042
Date (YYYY-MM-DD): 2026-06-15
Time (e.g. 10.30): 10.30
Appointment booked successfully.
Appointment ID   : 5081
Patient          : Jane Smith (ID: 1042)
Staff            : Dr. Chen (Doctor)
Date / Time      : 2026-06-15 at 10:30
Clinic Room      : 3
Est. Duration    : 30 min
Cost             : $150.00
```

| Line | Source |
|------|--------|
| Appointment ID | `appt.getApptID()` |
| Patient | `patient.getFirstName()` + `getLastName()`, `getPatientID()` |
| Staff | Assigned doctor name and role |
| Date / Time | Appointment date; time formatted as `HH:MM` |
| Clinic Room | `RoutineCheckup.getClinicRoomNum()` (or equivalent) |
| Est. Duration | `RoutineCheckup.estimateDuration()` converted to minutes |
| Cost | `calculateCost()` formatted as currency |

---

### Search staff by specialty and minimum experience

Triggered by `StaffManager.findStaff(String specialty, int exp)`.

```
--- Search Staff by Specialty and Experience ---
Specialty: Cardiology
Minimum years of experience: 5
Results (2 found):
[1] Dr. Alan Chen  -- Cardiology, 12 years, License: ON-4821
[2] Dr. Sara Mehta -- Cardiology, 7 years, License: ON-3307
```

Each result line: staff name, specialization, experience, and doctor `licenseNumber` where applicable.

---

### Sort patients by ward

Triggered by `PatientManager.sortByWardThenPatientID()` then listing patients.

```
--- Patients Sorted by Ward ---
Ward         Name           ID    Type        Admitted
Cardiology   Smith, Jane    1042  InPatient   2026-06-10
ICU          Brown, Tom     0987  Emergency   2026-06-09
Pediatrics   Doe, Emily     1105  InPatient   2026-06-11
```

| Column | Source |
|--------|--------|
| Ward | `patient.getWard()` |
| Name | `lastName, firstName` |
| ID | `getPatientID()` (zero-padded if desired) |
| Type | Subclass name (`InPatient`, `OutPatient`, `EmergencyPatient`) |
| Admitted | `patient.getAdmittedDateDisplay()` (`dayIn` for inpatients/emergency; `dateRegistered` otherwise) |

---

### Patient check-out and bill (InPatient)

Triggered by `PatientManager.checkOutPatient(int patientID, String followUp)` on an `InPatient`.

```
--- Check Out: Jane Smith (ID: 1042) ---
Ward: Cardiology
Days admitted: 3
Room fee (3 days x $250.00)     = $750.00
Appointment fees                = $195.00
----------------------------------------
Total Bill                      = $945.00
Checked out successfully.
Day out        : 2026-06-13
Next appointment: 2026-07-15
```

| Line | Source |
|------|--------|
| Days admitted | `InPatient.getDaysAdmitted()` |
| Room fee | `InPatient.getRoomFee()` (`DAILY_ROOM_RATE` × days) |
| Appointment fees | `InPatient.getAppointmentFees()` (sum of past appointment costs) |
| Total Bill | `InPatient.calculateBill()` |
| Day out | `dayOut` after checkout |
| Next appointment | First upcoming appointment date after follow-up scheduling |

---

### View daily schedule

Triggered by `ApptManager.viewDailySchedule(Date date)` (or equivalent display logic in `HospitalRunner`).

```
--- Daily Schedule: 2026-06-15 ---
08:00  #5079  Surgery          John Lee (1042)   OR 2   Dr. Park
10:30  #5081  Routine Checkup  Jane Smith (1042) Rm 3   Dr. Chen
13:00  #5082  Emergency Visit  Tom Brown (0987)  ER 1   Dr. Yuen
```

Each row (sorted by time): time (`HH:MM`), appointment ID, type label, patient name and ID, room/OR/ER identifier, primary staff name. `HospitalRunner` uses polymorphic display methods on each appointment subclass.

| Column | Source |
|--------|--------|
| Time | `time` field formatted as `HH:MM` |
| `#` + ID | `getApptID()` |
| Type | `getTypeLabel()` (`Routine Checkup`, `Surgery`, `Emergency Visit`) |
| Patient | Name and ID from linked `Patient` |
| Location | `getLocationLabel()` (`Rm n` / `OR n` / `ER n`) |
| Staff | Primary assigned staff member name |

---

### Success criteria (UI behaviour)

From the Stage 1 proposal — output and menus should satisfy:

- All required operations (add, delete, modify, search, sort) are available through the menu-driven interface
- The system prevents staff and patients from being double-booked
- Searches return accurate results for the given criteria
- Sorting produces correct order for each sort method
- The interface clearly communicates action options
- Invalid inputs are handled without crashes (print an error message and re-prompt or return)

---

## Data File Formats

### `staff.txt`

Colon-separated records. First line is entry count.

```
<count>
<role: Doctor | Nurse | Surgeon>
<staffID>
<name>
<experience>
<specialization>
<offDays: comma-separated YYYY-MM-DD, or NONE>
<role-specific fields...>
:
```

| Role | Additional Fields |
|------|-------------------|
| **Doctor** | `licenseNumber`, `consultationFee`, `maxPatients` |
| **Nurse** | `ward`, `shiftType`, `hourlyRate`, `hoursWorkedThisWeek` |
| **Surgeon** | `operatingRoom`, `surgeriesCompleted`, `specialtyArea`, `surgeryFeePerProcedure` |

---

### `patients.txt`

Pipe-delimited records (one patient per line). Lines starting with `#` or blank lines are skipped.

```
<type>|<patientID>|<firstName>|<lastName>|<dateOfBirth>|<ward>|<address>|<phoneNum>|<numOHIP>|<dateRegistered>|<gender>|<emergencyContactPhoneNumber>
```

- **Type values:** `InPatient`, `OutPatient`, `EmergencyPatient`
- **Date format:** `YYYY-MM-DD`

---

### `patient_appointments.txt`

Pipe-delimited appointment links (one record per line). Lines starting with `#` or blank lines are skipped.

```
<patientID>|<appointmentType>|<apptID>|<date>|<time>|<duration>|<cost>|<status>|<type-specific fields...>
```

| Type | Additional Fields |
|------|-------------------|
| **RoutineCheckup** | `clinicRoomNum`, `doctorID placeholder` |
| **Surgery** | `operatingRoomNum`, `anaesthesiaType`, `anaesthesiaDose`, `surgeryType`, `preOpInstructions` |
| **EmergencyVisit** | `emergencyRoomNum`, `urgencyIdx` |

- **Date format:** `YYYY-MM-DD`

---

### `appointments.txt`

First line is entry count. Each subsequent line is one comma-separated record.

**Common fields (all types):** `type,apptID,patientID,date,time,status,roomNum,duration,cost`

| Type | Additional Fields |
|------|-------------------|
| **RoutineCheckup** | `doctorID` |
| **Surgery** | `surgeryType,anaesthesiaType,anaesthesiaDose,preOpInstructions,surgeonID,nurseIDs...` |
| **EmergencyVisit** | `urgencyIdx` |

- **Date format:** `YYYYMMDD`
- **Time format:** `hh.mm` (24-hour, stored as `double`)
- **Status values:** `"Scheduled"`, `"Done"`, `"Cancelled"`, `"No Show"` (see `Appointment` status constants)

---

## Shared Module

### `shared.Date`

Calendar date helper used across all modules.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `year` | `int` | Four-digit year |
| `month` | `int` | Month (1–12) |
| `day` | `int` | Day of month |

#### Constructors

| Signature | Algorithm | Description |
|-----------|-----------|-------------|
| `Date(int year, int month, int day)` | Direct assignment | Assign each parameter to its corresponding field |
| `Date(String dateStr)` | Multi-format parse | Accepts `YYYY-MM-DD`, `YYYYMMDD`, or `"Month day, year"` display format; empty/null → zero date |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `toString()` | `String` | Switch-case | Convert month `int` to month name; combine with day and year |
| `toISODateString()` | `String` | String formatting | Return `YYYY-MM-DD` for file I/O, off-days, and OR booking records |
| `compareTo(Date other)` | `int` | Sequential comparison | Compare year, then month, then day; return difference or `0` if equal |
| `addDays(int daysToAdd)` | `Date` | Date arithmetic | Return a new `Date` offset by the given number of days |
| `isValid()` | `boolean` | Bounds check | Return `true` if year/month/day form a valid calendar date |
| `equals(Object other)` | `boolean` | Field comparison | Compare year, month, and day |

---

## Staff Module

### `staff.Staff` *(abstract)*

Base class for all hospital staff.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `name` | `String` | Full name |
| `staffID` | `String` | Unique identifier |
| `experience` | `int` | Years of experience |
| `specialization` | `String` | Medical specialization area |
| `offDays` | `String[]` | Scheduled off-day dates |
| `schedule` | `Appointment[]` | Personal appointment/shift schedule |

#### Abstract Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `assignPatients(Patient[] patients)` | `void` | Abstract — subclass-specific | Assign patients (role-specific logic) |
| `addAppointment(Appointment appt)` | `void` | Abstract — subclass-specific | Add appointment to schedule |
| `calculatePay()` | `double` | Abstract — subclass-specific | Compute pay for current period |
| `getSchedule()` | `String` | Abstract — subclass-specific | Formatted schedule string |
| `toString()` | `String` | Abstract — subclass-specific | Formatted staff info |

#### Concrete Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `takeOffDay(String date)` | `void` | Format validation + linear search | Validate `YYYY-MM-DD`; check duplicate in `offDays`; add to next available slot |
| `equals(Object other)` | `boolean` | Type check + delegation | Delegate to `equals(Staff other)` |
| `equals(Staff other)` | `boolean` | Direct comparison | Return `false` if `other` is null; compare `staffID` strings |
| `hashCode()` | `int` | String hash | Return `staffID.hashCode()` |
| `hasTimeConflict(Date date, double time)` | `boolean` | Linear search | Loop through `schedule`; return `true` if same date and time found |
| `formatTime(double time)` | `String` | String formatting | *(protected)* Format stored `hh.mm` time as `HH:MM` for schedule display |
| `formatOffDaysForFile()` | `String` | Loop + string build | *(protected)* Comma-separate `offDays` as `YYYY-MM-DD`, or `NONE` if empty |
| *getters/setters* | — | Accessor / mutator | Standard accessors for all fields; `getOffDays()` returns defensive copy |

---

### `staff.Doctor` extends `Staff`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `licenseNumber` | `String` | Medical licence number |
| `patientsAssigned` | `Patient[]` | Currently assigned patients |
| `consultationFee` | `double` | Fee per consultation |
| `maxPatients` | `int` | Maximum patient capacity |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `assignPatients(Patient[])` | `void` | Linear search | Count current patients; for each patient, if count ≥ `maxPatients` print error and return; else add and increment |
| `addAppointment(Appointment)` | `void` | Linear search (conflict check) | Loop existing appointments; if date/time overlap print error and return; else add to list |
| `calculatePay()` | `double` | Arithmetic | Return flat annual salary ÷ number of pay periods per year |
| `getSchedule()` | `String` | Loop + string build | Day-by-day patient list with appointment times |
| `toString()` | `String` | String formatting | Formatted doctor info |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `diagnosePatient(Patient p, String diagnosis)` | `void` | Delegation | Call `p.addDiagnoses(diagnosis)` |
| `prescribeMedication(Patient p, String med, String dosage)` | `void` | Delegation + guard | Call `p.checkAllergyConflict(med)`; if conflict print warning and return; else `p.addMedication(med, dosage)` |
| `referPatient(Patient p, Surgeon s)` | `void` | Validation + delegation | Verify patient assigned to this doctor; call `s.addReferral(patientID)`; call `s.assignPatients()`; remove from `patientsAssigned`; call `p.setAssignedStaff(s)` |

---

### `staff.Nurse` extends `Staff`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `ward` | `String` | Assigned ward (e.g. `"ICU"`, `"ER"`) |
| `shiftType` | `String` | `"Day"`, `"Night"`, or `"Rotating"` |
| `hourlyRate` | `double` | Pay per hour |
| `hoursWorkedThisWeek` | `int` | Hours worked this week |
| `patientsAssigned` | `Patient[]` | Ward-matched patients tracked for schedule display |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `assignPatients(Patient[])` | `void` | Linear search (ward filter) | For each patient, if ward matches this nurse's ward, add to assigned list |
| `addAppointment(Appointment)` | `void` | Validation + insert | If duration > 12 hours print error and return; add to list; update `hoursWorkedThisWeek` |
| `calculatePay()` | `double` | Conditional arithmetic | If hours ≤ 40: `hourlyRate × hours`; else: `(hourlyRate × 40) + (hourlyRate × 1.5 × overtime hours)` |
| `getSchedule()` | `String` | Loop + string build | Shift blocks with ward and assigned patients |
| `toString()` | `String` | String formatting | Formatted nurse info |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `administerMedication(Patient p, String med)` | `void` | Linear search + delegation | Search `p.getMedications()` for `med`; if not found print warning and return; else if `InPatient`, call `logMedicationsAdministered(Medication)` |
| `monitorVitals(Patient p, double heartRate, double bp)` | `void` | Delegation | If `InPatient`, call `recordVitals(heartRate, bloodPressure)` |
| `switchShift(String newShift)` | `void` | Validation + loop | Validate shift is `"Day"`, `"Night"`, or `"Rotating"`; set shift; clear conflicting appointments; recalculate availability |

---

### `staff.Surgeon` extends `Staff`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `operatingRoom` | `int` | Assigned OR number |
| `surgeriesCompleted` | `int` | Total surgeries performed |
| `specialtyArea` | `String` | e.g. `"Cardiac"`, `"Orthopedic"` |
| `surgeryFeePerProcedure` | `double` | Fee per surgery |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `assignPatients(Patient[])` | `void` | Binary search | For each patient, binary search sorted referral list; if found (index > −1), add to assigned list; skip patients not yet referred |
| `addAppointment(Appointment)` | `void` | Conflict check + recursive OR booking | Call `hasTimeConflict()`; verify schedule capacity; call `scheduleOR()` with `date.toISODateString()`; if clear, add to appointment list |
| `calculatePay()` | `double` | Arithmetic | Return `surgeriesCompleted × surgeryFeePerProcedure + base salary` |
| `getSchedule()` | `String` | Loop + string build | Surgical calendar: OR number, procedure, patient per slot |
| `toString()` | `String` | String formatting | Formatted surgeon info |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `performSurgery(Patient p, String procedureName)` | `void` | Delegation + increment | Call `performSurgery(p, procedureName, true)` |
| `performSurgery(Patient p, String procedureName, boolean successful)` | `void` | Delegation + increment | Call `p.addMedicalHistory(procedureName)`; increment `surgeriesCompleted`; record outcome for `getSuccessRate()` |
| `addReferral(int patientID)` | `boolean` | Insertion (sorted) | Binary search for duplicate; insert patient ID into sorted `referralList`; return `false` if at capacity |
| `scheduleOR(int room, String date, String time, int index)` | `void` | **Recursive** | Base case: index equals total booked slots — reserve and return; if conflict at index print error; else recurse with `index + 1`. `date` must be `YYYY-MM-DD` |
| `getSuccessRate()` | `double` | Loop + count | Count total and successful outcomes; if total is 0 return `0.0`; else return `(successful / total) × 100` |

---

### `staff.StaffManager`

Central controller for all staff records.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `staffArray` | `Staff[]` | Polymorphic staff storage |
| `staffCount` | `int` | Current number of staff |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `addStaff(Staff s)` | `void` | Bounds check + insert | If at capacity print error; set `staffArray[staffCount]` to `s`; increment count |
| `removeStaff(String id)` | `boolean` | Linear search + left shift | Loop array; on ID match shift elements left, null last slot, decrement count, return `true`; else return `false` |
| `updateStaff(String id, ...)` | `boolean` | Recursive search + update | Call `findStaffByID(id, 0)`; if null return `false`; call setters on result; return `true` |
| `findStaff(String name)` | `Staff` | **Linear search** | For each staff, if name equals return it; else return `null` |
| `findStaff(String specialty, int exp)` | `Staff[]` | **Linear scan** (two-criteria) | Create results array; for each staff, if specialization matches AND experience ≥ exp, add to results |
| `findStaffByID(String id, int index)` | `Staff` | **Recursive** linear search | Base case: index equals `staffCount` return `null`; if ID matches return staff; else recurse with `index + 1` |
| `sortStaff()` | `void` | **Selection sort** | Outer loop sets `minIndex`; inner loop finds alphabetically smallest name; swap into position |
| `sortStaffByExp()` | `void` | **Bubble sort** | Outer loop repeats `staffCount − 1` times; inner loop swaps by experience descending, then name A–Z on tie |
| `checkShifts(Staff s)` | `String` | Delegation | Return `s.getSchedule()` |
| `addShift(String name, Appointment appt)` | `void` | Linear search + delegation | Find staff by name; call `addAppointment(appt)` |
| `removeShift(String name, Appointment appt)` | `boolean` | Linear search + remove | Find staff by name; remove appointment from schedule |
| `runPayroll()` | `double` | Loop (polymorphic) | Initialize total to 0; for each staff call `calculatePay()` and add; return total |
| `loadFromFile(String filename)` | `void` | **File I/O** | Open file; parse role tag and fields per record; construct subtype; call `addStaff()`; close file |
| `saveToFile(String filename)` | `void` | **File I/O** | Open for writing; for each staff call `toString()` and write; close file |
| `getAvailableNurses(String ward, Date date, double time, int count)` | `Nurse[]` | Linear search | Loop `staffArray`; skip non-nurses and wrong ward; call `hasTimeConflict()`; collect until count met or return `null` |
| `getTriageNurse(Date date, double time)` | `Nurse` | Delegation | Call `getAvailableNurses("ER", date, time, 1)`; return first result or `null` |
| `getTraumaDoctor(int urgencyIdx, Date date, double time)` | `Doctor` | Linear search | Set `needsSenior = (urgencyIdx ≥ 4)`; loop doctors with ER specialization; skip if busy; pick senior (exp ≥ 10) or any available |

---

## Patient Module

### `patient.Patient` *(abstract)*

Base class for all patients.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `patientID` | `int` | Unique identifier |
| `firstName`, `lastName` | `String` | Patient name |
| `dateOfBirth` | `Date` | Date of birth |
| `ward` | `String` | Hospital ward |
| `address` | `String` | Home address |
| `phoneNum` | `long` | Phone number |
| `numOHIP` | `int` | 10-digit OHIP number |
| `dateRegistered` | `Date` | Registration date |
| `gender` | `char` | `'M'` or `'F'` |
| `emergencyContactPhoneNumber` | `long` | Emergency contact |
| `assignedStaff` | `Staff` | Responsible staff member |
| `diagnosis` | `String[]` | Diagnosis list |
| `medications` | `Medication[]` | Prescribed medications |
| `allergies` | `String[]` | Known allergies |
| `medicalHistory` | `String[]` | Medical history entries |
| `familyHistory` | `String[]` | Family history entries |
| `pastAppointments` | `Appointment[]` | Completed appointments |
| `upcomingAppointments` | `Appointment[]` | Scheduled appointments |

#### Abstract Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `toString()` | `String` | Abstract — subclass-specific | Full patient info |
| `checkIn()` | `boolean` | Abstract — subclass-specific | Check patient in |
| `checkOut(String followUp)` | `boolean` | Abstract — subclass-specific | Check out, bill, schedule follow-up (`"checkup"`, `"surgery"`, or other) |
| `calculateBill()` | `double` | Abstract — subclass-specific | Compute total bill |
| `scheduleNextRoutineCheckup()` | `void` | Abstract — subclass-specific | Schedule a follow-up routine checkup |
| `scheduleNextSurgery()` | `void` | Abstract — subclass-specific | Schedule a follow-up surgery |

#### Concrete Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `getAge()` | `int` | Date arithmetic | Split DOB and current date into components; subtract total days; integer divide by 365 |
| `getIndexOfMedicationByName(String medName)` | `int` | **Linear search** | Search `medications` for matching name; return index or −1 |
| `addMedication(String medName, String dosage)` | `void` | Insert at end | Create `Medication` object; add to end of `medications` array |
| `deleteMedication(String medName)` | `boolean` | Index-find-then-modify | Call `getIndexOfMedicationByName()`; if −1 return `false`; remove and left-shift |
| `updateMedication(String medName, String newMed, String newDosage)` | `boolean` | Index-find-then-modify | Find index; if −1 return `false`; replace with new `Medication` |
| `checkAllergyConflict(String medName)` | `boolean` | Index-find-then-modify | Call `getIndexOfAllergyByName(medName)`; return `true` if index > −1 |
| `getIndexOfAllergyByName(String allergy)` | `int` | **Linear search** (case-insensitive) | Search `allergies`; return index or −1 |
| `addAllergy(String allergy)` | `void` | Index-find-then-modify | Add to end of `allergies` array |
| `deleteAllergy(String allergy)` | `boolean` | Index-find-then-modify | Find index; remove and left-shift |
| `updateAllergy(String allergy, String newAllergy)` | `boolean` | Index-find-then-modify | Find index; replace entry |
| `addMedicalHistory(String entry)` | `void` | Insert at end | Add to end of `medicalHistory` |
| `deleteMedicalHistory(String entry)` | `boolean` | Linear search + left shift | Search and remove with left shift |
| `addFamilyHistory(String entry)` | `void` | Insert at end | Add to end of `familyHistory` |
| `deleteFamilyHistory(String entry)` | `boolean` | Linear search + left shift | Search and remove with left shift |
| `getIndexOfApptByIDPast(String apptID)` | `int` | **Linear search** | Search `pastAppointments` by ID; return index or −1 |
| `getIndexOfApptByIDUpcoming(String apptID)` | `int` | **Linear search** | Search `upcomingAppointments` by ID; return index or −1 |
| `addAppointment(Appointment a)` | `void` | Insert at end | Route to `upcomingAppointments` if status is `"future"` or `STATUS_SCHEDULED`; to `pastAppointments` if `"past"` or `STATUS_DONE` |
| `updateAppointment(Appointment org, Appointment newA)` | `boolean` | Index-find-then-modify | Find in upcoming by ID; if −1 return `false`; replace at index |
| `deleteAppointment(Appointment a)` | `boolean` | Index-find-then-modify | Find in upcoming by ID; remove and left-shift |
| `addDiagnoses(String diagnosis)` | `void` | Insert at end | Add to end of `diagnosis` array |
| `deleteDiagnoses(String diagnosis)` | `boolean` | Linear search + left shift | Search and remove diagnosis entry |
| `updateDiagnoses(String org, String newD)` | `boolean` | Linear search + replace | Search and replace diagnosis entry |
| `setAssignedStaff(Staff assigned)` | `void` | Direct assignment | Set `assignedStaff` to `assigned` |
| `isValidOHIP(int numOHIP)` | `boolean` | String length check | Convert to `String`; return `true` if length equals 10 |
| `equalsName(String firstName, String lastName)` | `boolean` | Case-insensitive compare | Compare both names case-insensitively |
| `wasSeenByStaff(Staff assigned)` | `boolean` | Direct comparison | Compare `assignedStaff` reference |
| `equalsDateRegistered(String date)` | `boolean` | String comparison | Compare `dateRegistered` to given date |
| `compareToDateRegistered(Patient other)` | `int` | Sequential comparison | Split dates into year/month/day; compare year, then month, then day |
| `hasSameHospitalBed(Patient other)` | `boolean` | Type check + compare | If either not `InPatient` return `false`; cast and compare `hospitalBedNum` |
| `addToHistory(Appointment a)` | `boolean` | Index-find-then-modify | Find in upcoming; remove with left-shift; add to end of past |
| `calculateTotalCost()` | `double` | Loop + sum | Sum `calculateCost()` on all non-null past appointments |
| `getAdmittedDateDisplay()` | `String` | Polymorphic | Return ISO admission date for listings; default is `dateRegistered` |
| *getters/setters* | — | Accessor / mutator | Standard accessors for all fields |

---

### `patient.InPatient` extends `Patient`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `dayIn` | `Date` | Admission date |
| `dayOut` | `Date` | Discharge date (`null` if admitted) |
| `hospitalBed` | `boolean` | Whether a hospital bed is assigned |
| `vitalsLog` | `String[]` | Timestamped vitals entries |
| `medicationsAdministered` | `String[]` | Timestamped medication log |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `checkIn()` | `boolean` | Linear search | Find today's upcoming appointment; set `dayIn` and assign bed |
| `checkIn(Date dayIn)` | `boolean` | Format validation | Validate date; set `dayIn` and assign bed |
| `checkOut(String followUp)` | `boolean` | Sequential operations | Set `dayOut`; call `calculateBill()`; schedule follow-up by type |
| `calculateBill()` | `double` | Loop + arithmetic | Return `getRoomFee()` + `getAppointmentFees()` |
| `getAdmittedDateDisplay()` | `String` | Conditional | Return `dayIn` as ISO string, or `"N/A"` |
| `scheduleNextRoutineCheckup()` | `void` | Date arithmetic | Create follow-up checkup 1–2 days after checkout |
| `scheduleNextSurgery()` | `void` | Date arithmetic | Create follow-up surgery 1–2 days after checkout |
| `toString()` | `String` | String formatting | All patient info + `dayIn`, `dayOut`, bed status |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `getDaysAdmitted()` | `int` | Date arithmetic | Days between `dayIn` and `dayOut` (or current date for preview) |
| `getRoomFee()` | `double` | Arithmetic | `getDaysAdmitted()` × `DAILY_ROOM_RATE` |
| `getAppointmentFees()` | `double` | Delegation | Call `calculateTotalCost()` on past appointments |
| `recordVitals(double heartRate, double bloodPressure)` | `void` | Insert at end | Combine vitals with date/time; add to end of `vitalsLog` |
| `logMedicationsAdministered(Medication med)` | `void` | Insert at end | Combine medication name, dosage, and date; add to end of `medicationsAdministered` |

---

### `patient.OutPatient` extends `Patient`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `appointmentTimingMonths` | `int` | Months until next appointment |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `checkIn()` | `boolean` | **Linear search** | Get current date; search `upcomingAppointments` for match today; return `false` if not found |
| `checkOut(String followUp)` | `boolean` | Sequential operations | Call `calculateBill()`; schedule follow-up by type; return `true` |
| `calculateBill()` | `double` | Loop + sum | Return `calculateTotalCost()` on past appointments |
| `scheduleNextRoutineCheckup()` | `void` | Date arithmetic | Create follow-up checkup after completed appointment |
| `scheduleNextSurgery()` | `void` | Date arithmetic | Create follow-up surgery after completed appointment |
| `toString()` | `String` | String formatting | All base patient fields |

---

### `patient.EmergencyPatient` extends `Patient`

#### Constants

| Constant | Value | Description |
|----------|-------|-------------|
| `ER_DAILY_RATE` | `500.00` | Daily emergency stay fee |

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `arrivalTime` | `int` | Time of arrival (24-hour integer, e.g. `1200`) |
| `dayIn` | `Date` | Date of arrival |
| `dayOut` | `Date` | Date of discharge |
| `presentingComplaint` | `String` | Reason for visit |
| `arrivalType` | `String` | Mode of arrival (e.g. `"Ambulance"`) |
| `status` | `String` | `"Awaiting triage"`, `"In Treatment"`, `"Stable"`, `"Critical"`, `"Discharged"` |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `checkIn()` | `boolean` | Direct assignment | Set `arrivalTime`, `dayIn`, status to `"Awaiting triage"`; return `true` |
| `checkIn(String arrivalType)` | `boolean` | Direct assignment | Set provided fields; set `dayIn` and status to `"Awaiting triage"` |
| `checkIn(String arrivalType, String complaint)` | `boolean` | Direct assignment | Set all provided fields; set `dayIn` and status to `"Awaiting triage"` |
| `checkOut(String followUp)` | `boolean` | Sequential operations | Set `dayOut`; set status to `"Discharged"`; call `calculateBill()`; schedule follow-up |
| `calculateBill()` | `double` | Loop + arithmetic + multiplier | Sum appointment costs; add `ER_DAILY_RATE` × days; apply 1.5× surcharge if status is `"Critical"` |
| `getAdmittedDateDisplay()` | `String` | Conditional | Return `dayIn` as ISO string, or `"N/A"` |
| `scheduleNextRoutineCheckup()` | `void` | Date arithmetic | Create follow-up checkup 1 day after checkout |
| `scheduleNextSurgery()` | `void` | Date arithmetic | Create follow-up surgery 1 day after checkout |
| `toString()` | `String` | String formatting | All fields + arrival info and status |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `updateStatus(String newStatus)` | `boolean` | Validation | Check against allowed status values; if invalid return `false`; else set and return `true` |

---

### `patient.Medication`

Helper class for a prescribed medication entry.

#### Fields

| Field | Type |
|-------|------|
| `medName` | `String` |
| `dosage` | `String` |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `equals(Object other)` | `boolean` | Direct comparison | Compare by `medName` |
| *getters/setters* | — | Accessor / mutator | Standard accessors |

---

### `patient.PatientManager`

Central controller for all patient records.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `patients` | `Patient[]` | All registered patients |
| `numPatients` | `int` | Current patient count |
| `maxPatients` | `int` | Array capacity |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `registerInPatient(...)` | `boolean` | Bounds check + insert | Validate OHIP; create `InPatient`; insert at end |
| `registerOutPatient(...)` | `boolean` | Bounds check + insert | Validate OHIP; create `OutPatient`; insert at end |
| `registerEmergencyPatient(...)` | `boolean` | Bounds check + insert | Validate OHIP; create `EmergencyPatient`; insert at end |
| `deletePatient(int patientID)` | `boolean` | Linear search + left shift | Find by ID; shift left; decrement count |
| `updatePatient(int patientID, Patient updated)` | `boolean` | Search + update | Replace patient record at found index |
| `addDiagnosis(int patientID, String)` | `boolean` | Delegation | Call `patient.addDiagnoses()` |
| `deleteDiagnosis(int patientID, String)` | `boolean` | Delegation | Call `patient.deleteDiagnoses()` |
| `updateDiagnosis(int patientID, String org, String newD)` | `boolean` | Delegation | Call `patient.updateDiagnoses()` |
| `addAppointment(int patientID, Appointment a)` | `boolean` | Delegation | Find patient; call `addAppointment()` |
| `deleteAppointment(int patientID, Appointment a)` | `boolean` | Delegation | Find patient; call `deleteAppointment()` |
| `updateAppointment(int patientID, Appointment org, Appointment newA)` | `boolean` | Delegation | Find patient; call `updateAppointment()` |
| `addMedication(int patientID, String med, String dosage)` | `boolean` | Delegation | Call `addMedication()` on patient |
| `deleteMedication(int patientID, String med)` | `boolean` | Delegation | Call `deleteMedication()` on patient |
| `updateMedication(int patientID, String, String newMed, String newDosage)` | `boolean` | Delegation | Call `updateMedication()` on patient |
| `addAllergy(int patientID, String allergy)` | `boolean` | Delegation | Call `addAllergy()` on patient |
| `deleteAllergy(int patientID, String allergy)` | `boolean` | Delegation | Call `deleteAllergy()` on patient |
| `updateAllergy(int patientID, String org, String newA)` | `boolean` | Delegation | Call `updateAllergy()` on patient |
| `addMedicalHistory(int patientID, String entry)` | `boolean` | Delegation | Call `addMedicalHistory()` on patient |
| `deleteMedicalHistory(int patientID, String entry)` | `boolean` | Delegation | Call `deleteMedicalHistory()` on patient |
| `addFamilyHistory(int patientID, String entry)` | `boolean` | Delegation | Call `addFamilyHistory()` on patient |
| `deleteFamilyHistory(int patientID, String entry)` | `boolean` | Delegation | Call `deleteFamilyHistory()` on patient |
| `updateAssignedStaffForPatient(int patientID, Staff)` | `boolean` | Delegation | Call `patient.setAssignedStaff()` |
| `loadPatientInfo(String fileName)` | `boolean` | **File I/O** | Parse pipe-delimited records; create subclass; insert into array |
| `savePatientInfo(String fileName)` | `boolean` | **File I/O** | Write each patient as pipe-delimited record |
| `loadPatientAppts(String fileName)` | `boolean` | **File I/O** | Parse appointment records; find patient by ID; call `addAppointment()` |
| `savePatientAppts(String fileName)` | `boolean` | **File I/O** | Write patient appointment associations |
| `checkInPatient(int patientID)` | `boolean` | Delegation | Call `checkIn()` on patient |
| `checkOutPatient(int patientID, String followUp)` | `boolean` | Delegation | Call `checkOut(followUp)` on patient |
| `calculateBill(int patientID)` | `double` | Delegation | Call `calculateBill()` on patient |
| `calculateTotalCostForPatient(int patientID)` | `double` | Delegation | Call `calculateTotalCost()` on patient |
| `listAllPatients()` | `String` | Loop + string build | Concatenate `toString()` for all patients |
| `listAppointmentsForPatient(int patientID)` | `String` | Loop + string build | Return past and upcoming appointment listings |
| `logMedicationAdministeredForPatient(int patientID, Medication med)` | `boolean` | Delegation | If `InPatient`, call `logMedicationsAdministered(med)` |
| `recordVitalsForPatient(int patientID, double heartRate, double bloodPressure)` | `boolean` | Delegation | If `InPatient`, call `recordVitals(heartRate, bloodPressure)` |
| `addtoHistory(int patientID, Appointment a)` | `boolean` | Delegation | Call `addToHistory()` on patient |
| `setEmergencyPatientStatus(int patientID, String status)` | `boolean` | Delegation | If `EmergencyPatient`, update status |
| `searchPatientByPatientID(int patientID)` | `Patient` | **Binary search** | Sort by ID; recurse on sorted `patients` array |
| `sortByDateEntered()` | `void` | **Insertion sort** | Sort patients by `dateRegistered` ascending |
| `sortByWardThenPatientID()` | `void` | **Selection sort** | Sort patients by ward name ascending, then patient ID |
| `sortByPatientID()` | `void` | **Bubble sort** | Sort patients by patient ID ascending |

---

## Appointment Module

### `appointment.Appointment` *(abstract)*

Base class for all appointments.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `apptID` | `int` | Unique identifier |
| `patient` | `Patient` | Patient for this appointment |
| `staffList` | `Staff[]` | Assigned staff members |
| `date` | `Date` | Appointment date |
| `time` | `double` | Time (`hh.mm`, 24-hour) |
| `duration` | `double` | Duration in hours |
| `cost` | `double` | Computed cost |
| `status` | `String` | `"Scheduled"`, `"Done"`, `"Cancelled"`, `"No Show"` |

#### Status Constants

| Constant | Value |
|----------|-------|
| `STATUS_SCHEDULED` | `"Scheduled"` |
| `STATUS_DONE` | `"Done"` |
| `STATUS_CANCELLED` | `"Cancelled"` |
| `STATUS_NO_SHOW` | `"No Show"` |

#### Abstract Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `calculateCost()` | `double` | Abstract — subclass-specific | Compute appointment cost |
| `validateBooking()` | `boolean` | Abstract — subclass-specific | Validate booking requirements |
| `getRoomNum()` | `int` | Abstract — subclass-specific | Return room/OR/ER number for conflict checks |
| `getTypeLabel()` | `String` | Abstract — subclass-specific | Human-readable type label for schedule display |
| `getLocationLabel()` | `String` | Abstract — subclass-specific | Human-readable location label (`Rm n`, `OR n`, `ER n`) |
| `toString()` | `String` | Abstract — subclass-specific | Formatted appointment info |

#### Concrete Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `cancel()` | `void` | Direct assignment | Set status to `STATUS_CANCELLED` |
| `reschedule(Date newDate, double newTime)` | `boolean` | Validate-and-revert | Store old values; set new date/time; call `validateBooking()`; revert if invalid |
| `assignStaff(Staff[] staffTeam)` | `void` | Array copy | Assign staff array (defensive copy) |
| `overlap(Object obj)` | `boolean` | Nested loop | Compare date/time overlap and shared staff members |
| `equals(Object obj)` | `boolean` | Direct comparison | Compare by `apptID` |
| `hashCode()` | `int` | Integer hash | Hash of `apptID` |
| `markDone()` | `void` | Delegation | Set status to `STATUS_DONE`; call `patient.addToHistory(this)` |
| `isActive()` | `boolean` | Status check | Return `false` if cancelled, done, or no-show |
| `toMinutes(double hhmm)` | `int` | **Static** formatting | Convert `hh.mm` double to total minutes |
| *getters/setters* | — | Accessor / mutator | Standard accessors for all fields |

---

### `appointment.RoutineCheckup` extends `Appointment`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `clinicRoomNum` | `int` | Assigned clinic room |
| `mainDoctor` | `Doctor` | Primary doctor |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `calculateCost()` | `double` | Direct return | Return flat routine checkup fee |
| `validateBooking()` | `boolean` | Conditional check | Return `false` if `clinicRoomNum ≤ 0` or `mainDoctor` is null; else `true` |
| `getTypeLabel()` | `String` | Direct return | Return `"Routine Checkup"` |
| `getLocationLabel()` | `String` | String formatting | Return `"Rm "` + `clinicRoomNum` |
| `assignStaff(Doctor d)` | `void` | Direct assignment | Set `mainDoctor`; assign `d` to `staffList[0]` |
| `assignClinicRoom(ApptManager manager)` | `int` | **Linear search** | Find first unoccupied clinic room; return room number or `-1` |
| `getMainDoctor()` | `Doctor` | Accessor | Return primary doctor |
| `markNoShow()` | `void` | Direct assignment + arithmetic | Set status to `STATUS_NO_SHOW`; set cost to no-show fee |
| `estimateDuration(String reasonForVisit)` | `double` | Keyword scan | Base 15 min; add time for known visit reasons; store as hours |
| `toString()` | `String` | String formatting | Base info + `clinicRoomNum` and doctor |

---

### `appointment.Surgery` extends `Appointment`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `anaesthesiaDose` | `double` | Anaesthesia dose |
| `anaesthesiaType` | `String` | Anaesthesia type (`null` if none) |
| `type` | `String` | Surgery type (must match surgeon specialty) |
| `operatingRoomNum` | `int` | OR number |
| `preOpInstructions` | `String` | Pre-op instructions (`null` if none) |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `calculateCost()` | `double` | Arithmetic | Return base surgery fee (`SURGERY_COST_BASE`) |
| `validateBooking()` | `boolean` | Direct comparison | Extract surgeon from `staffList[0]`; compare `specialtyArea` to `type` |
| `getTypeLabel()` | `String` | Direct return | Return `"Surgery"` |
| `getLocationLabel()` | `String` | String formatting | Return `"OR "` + `operatingRoomNum` |
| `assignStaff(Surgeon surgeon, Nurse[] nurses)` | `void` | Loop + assignment | Assign surgeon to `staffList[0]`; loop nurses into `[1..n]` |
| `assignOperatingRoom(ApptManager manager, int preferredOR)` | `int` | Linear search | Check preferred OR free; if occupied loop alternatives; return assigned room or `-1` |
| `getPreOpInstructions()` | `String` | Accessor | Return pre-op instructions string |
| `givePreOpInstructions()` | `void` | Conditional assignment | Set `preOpInstructions` based on `anaesthesiaType` |
| `estimateDuration()` | `double` | Arithmetic | Return stored duration |
| `toString()` | `String` | String formatting | Base info + OR number, anaesthesia dose and type |

---

### `appointment.EmergencyVisit` extends `Appointment`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `urgencyIdx` | `int` | Triage severity 1–5 (5 = most severe) |
| `emergencyRoomNum` | `int` | Assigned ER bay |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `calculateCost()` | `double` | Arithmetic | Base ER fee + duration × hourly rate |
| `validateBooking()` | `boolean` | Conditional check | Return `true` if `emergencyRoomNum > 0`, else `false` |
| `getTypeLabel()` | `String` | Direct return | Return `"Emergency Visit"` |
| `getLocationLabel()` | `String` | String formatting | Return `"ER "` + `emergencyRoomNum` |
| `assignStaff(Doctor d, int urgencyIdx)` | `void` | Direct assignment | Set `urgencyIdx`; assign `d` to `staffList[0]` |
| `assignStaff(Doctor d)` | `void` | Direct assignment | Assign `d` to `staffList[0]` |
| `autoAssignNurse(Nurse n)` | `void` | **Linear search** | Loop `staffList` from index 1; find first null slot; assign nurse |
| `urgentAssignStaff(Doctor d, Nurse[] nurses)` | `void` | Loop + assignment | Clear staff array; assign doctor at `[0]`; loop nurses into `[1..n]` |
| `markStabilized(boolean isStable)` | `void` | Conditional decrement | If `isStable` and `urgencyIdx > 1`, decrement `urgencyIdx` |
| `assignEmergencyRoom(ApptManager manager)` | `int` | **Linear search** | Scan ER bay roster; find first unoccupied bay; return number or `-1` |
| `calculateNursesNeeded()` | `int` | Conditional | Return 3 if urgency ≥ 4; 2 if urgency == 3; else 1 |
| `estimateDuration()` | `double` | Conditional arithmetic | Initialize to `0.5 × urgencyIdx`; add 1.5 h if urgency ≥ 2; add 2 h if urgency ≥ 4 |
| `toString()` | `String` | String formatting | Base info + ER room number and urgency index |

---

### `appointment.ApptManager`

Central controller for all appointments.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `appointments` | `Appointment[]` | All appointments |
| `numAppointments` | `int` | Current appointment count |
| `maxAppointments` | `int` | Array capacity |
| `staffManager` | `StaffManager` | Cross-module staff lookup |
| `patientManager` | `PatientManager` | Cross-module patient lookup |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `addAppointment(Appointment a)` | `boolean` | Validation + insert | Call `validateBooking()`; call `isSlotConflict()`; insert at end; increment count |
| `cancelAppointment(int apptID)` | `boolean` | Recursive search + left shift | Call `searchByID()`; call `cancel()`; shift left; decrement count |
| `rescheduleAppointment(int apptID, Date newDate, double newTime)` | `boolean` | Recursive search + validate-and-revert | Find by ID; call `reschedule()`; check conflict; revert on failure |
| `isSlotConflict(Appointment checkAppt)` | `boolean` | **Linear search** | Loop appointments; skip inactive/same ID; call `overlap()`; return `true` on conflict |
| `searchByPatientAndDate(int patientID, Date date, int idx)` | `Appointment` | **Recursive linear search** | Base case: idx ≥ count return `null`; if patient and date match return it; else recurse |
| `searchByID(int apptID, int idx)` | `Appointment` | **Recursive linear search** | Base case: idx ≥ count return `null`; if ID matches return appointment; else recurse |
| `sortByDate()` | `void` | **Selection sort** | Outer loop sets `minIndex`; inner compares dates via `compareTo()`; swap minimum |
| `sortByPatientThenDate()` | `void` | **Bubble sort** | Compare adjacent patient IDs; if equal compare dates; swap if out of order |
| `loadFromFile(String filename)` | `boolean` | **File I/O** | Read count + comma-separated records; instantiate matching subclass |
| `saveToFile(String filename)` | `boolean` | **File I/O** | Write count + common fields + subclass-specific fields per record |
| `viewDailySchedule(Date date)` | `void` | Loop + print | Print header; loop and print `toString()` for matching date |
| `viewUpcomingAppointments(int patientID)` | `void` | Loop + print | Print appointments matching patient ID with status `STATUS_SCHEDULED` |
| `runCostSummary(Date date)` | `double` | Loop + sum | Initialize total; loop; if date matches add `calculateCost()`; return total |
| `isRoomOccupied(Class<?> apptClass, int roomNum, Date date, double time, double duration)` | `boolean` | Linear search | Check whether a room of the given type is occupied at the given time |
