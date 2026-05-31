# Hospital Management System — API Reference

> **Packages:** `staff` · `patient` · `appointment` · `shared`  
> **Entry point:** `HospitalRunner` (default package)

---

## Table of Contents

1. [Overview](#overview)
2. [Data File Formats](#data-file-formats)
3. [Shared Module](#shared-module)
4. [Staff Module](#staff-module)
5. [Patient Module](#patient-module)
6. [Appointment Module](#appointment-module)

---

## Overview

The system is organized into three modules connected by object references and file I/O:

| Module | Package | Manager Class | Data Files |
|--------|---------|---------------|------------|
| Staff | `staff` | `StaffManager` | `data/staff.txt` |
| Patient | `patient` | `PatientManager` | `data/patients.txt`, `data/patient_appointments.txt` |
| Appointment | `appointment` | `ApptManager` | `data/appointments.txt` |

All domain classes use **private fields** with getters/setters. Abstract base classes define shared behaviour; subclasses override type-specific methods.

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

Colon-separated records. First line is entry count.

```
<count>
<type: InPatient | OutPatient | EmergencyPatient>
<patientID>
<firstName>
<lastName>
<dateOfBirth>
<ward>
<address>
<phoneNum>
<numOHIP>
<dateRegistered>
<gender>
<emergencyContactPhoneNumber>
<diagnoses: semicolon-separated, or NONE>
<allergies: semicolon-separated, or NONE>
<medicalHistory: semicolon-separated, or NONE>
<familyHistory: semicolon-separated, or NONE>
<type-specific fields...>
:
```

| Type | Additional Fields |
|------|-------------------|
| **InPatient** | `dayIn`, `dayOut` (or `NONE`), `hospitalBedNum` |
| **OutPatient** | `appointmentTimingMonths` |
| **EmergencyPatient** | `arrivalTime`, `dayIn`, `dayOut` (or `NONE`), `presentingComplaint`, `arrivalType`, `status` |

---

### `patient_appointments.txt`

Links appointment IDs to patients.

```
<patientID>
<past | upcoming>
<apptID>
:
```

---

### `appointments.txt`

One comma-separated record per line.

| Type | Format |
|------|--------|
| **Checkup** | `Checkup,<apptID>,<patientID>,<date>,<time>,<status>,<clinicRoomNum>,<doctorID>` |
| **Surgery** | `Surgery,<apptID>,<patientID>,<date>,<time>,<status>,<ORnum>,<surgeryType>,<anaesthesiaType>,<anaesthesiaDose>,<preOpInstructions>,<surgeonID>,<nurseIDs...>` |
| **Emergency** | `Emergency,<apptID>,<patientID>,<date>,<time>,<status>,<ERnum>,<urgencyIdx>,<doctorID>,<nurseIDs...>` |

- **Date format:** `YYYYMMDD`
- **Time format:** `hh.mm` (24-hour, stored as `double`)

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
| `Date(String dateStr)` | Substring extraction | Extract chars 0–4 as year, 5–7 as month, 8–10 as day; convert each to `int` |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `toString()` | `String` | Switch-case | Convert month `int` to month name; combine with day and year |
| `compareTo(Date other)` | `int` | Sequential comparison | Compare month, then day; return difference or `0` if equal |

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
| `equals(Staff other)` | `boolean` | Direct comparison | Return `false` if `other` is null; compare `staffID` strings |
| `hasTimeConflict(Date date, double time)` | `boolean` | Linear search | Loop through `schedule`; return `true` if same date and time found |
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
| `diagnosePatient(Patient p, String diagnosis)` | `void` | Delegation | Call `p.addDiagnosis(diagnosis)` |
| `prescribeMedication(Patient p, String med, String dosage)` | `void` | Delegation + guard | Call `p.checkAllergyConflict(med)`; if conflict print warning and return; else `p.addMedication(med, dosage)` |
| `referPatient(Patient p, Surgeon s)` | `void` | Validation + delegation | Verify patient assigned to this doctor; call `s.assignPatients()`; remove from `patientsAssigned`; call `p.updateAssignedStaff(s)` |

---

### `staff.Nurse` extends `Staff`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `ward` | `String` | Assigned ward (e.g. `"ICU"`, `"ER"`) |
| `shiftType` | `String` | `"Day"`, `"Night"`, or `"Rotating"` |
| `hourlyRate` | `double` | Pay per hour |
| `hoursWorkedThisWeek` | `int` | Hours worked this week |

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
| `administerMedication(Patient p, String med)` | `void` | Delegation + guard | Call `p.hasMedication(med)`; if not prescribed print warning and return; else `p.logMedicationAdministered(med)` |
| `monitorVitals(Patient p, double heartRate, double bp)` | `void` | Delegation | Call `p.recordVitals(heartRate, bp)` |
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
| `assignPatients(Patient[])` | `void` | Binary search | For each patient, binary search sorted referral list; if found (index > −1), add to assigned list |
| `addAppointment(Appointment)` | `void` | Recursive OR conflict check | Call `scheduleOR()` to verify no conflict; if clear, add to appointment list |
| `calculatePay()` | `double` | Arithmetic | Return `surgeriesCompleted × surgeryFeePerProcedure + base salary` |
| `getSchedule()` | `String` | Loop + string build | Surgical calendar: OR number, procedure, patient per slot |
| `toString()` | `String` | String formatting | Formatted surgeon info |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `performSurgery(Patient p, String procedureName)` | `void` | Delegation + increment | Call `p.addMedicalHistory(procedureName)`; increment `surgeriesCompleted` |
| `scheduleOR(int room, String date, String time, int index)` | `void` | **Recursive** | Base case: index equals total booked slots — reserve and return; if conflict at index print error; else recurse with `index + 1` |
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
| `dateOfBirth` | `String` | Date of birth |
| `ward` | `String` | Hospital ward |
| `address` | `String` | Home address |
| `phoneNum` | `int` | Phone number |
| `numOHIP` | `int` | 10-digit OHIP number |
| `dateRegistered` | `String` | Registration date |
| `gender` | `char` | `'M'` or `'F'` |
| `emergencyContactPhoneNumber` | `int` | Emergency contact |
| `assignedStaff` | `Staff` | Responsible staff member |
| `diagnoses` | `String[]` | Diagnosis list |
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
| `checkOut()` | `boolean` | Abstract — subclass-specific | Check out, bill, schedule follow-up |
| `calculateBill()` | `double` | Abstract — subclass-specific | Compute total bill |
| `scheduleNextAppointment()` | `void` | Abstract — subclass-specific | Schedule follow-up appointment |

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
| `addAppointment(Appointment a)` | `void` | Insert at end | Add to end of `upcomingAppointments` |
| `updateAppointment(Appointment org, Appointment newA)` | `boolean` | Index-find-then-modify | Find in upcoming by ID; if −1 return `false`; replace at index |
| `deleteAppointment(Appointment a)` | `boolean` | Index-find-then-modify | Find in upcoming by ID; remove and left-shift |
| `addDiagnosis(String diagnosis)` | `void` | Insert at end | Add to end of `diagnoses` |
| `deleteDiagnosis(String diagnosis)` | `boolean` | Linear search + left shift | Search and remove/replace |
| `updateDiagnosis(String org, String newD)` | `boolean` | Linear search + replace | Search and replace diagnosis |
| `updateAssignedStaff(Staff assigned)` | `void` | Direct assignment | Set `assignedStaff` to `assigned` |
| `isValidOHIP(int numOHIP)` | `boolean` | String length check | Convert to `String`; return `true` if length equals 10 |
| `equalsName(String firstName, String lastName)` | `boolean` | Case-insensitive compare | Compare both names case-insensitively |
| `wasSeenByStaff(Staff assigned)` | `boolean` | Direct comparison | Compare `assignedStaff` reference |
| `equalsDateRegistered(String date)` | `boolean` | String comparison | Compare `dateRegistered` to given date |
| `compareToDateRegistered(Patient other)` | `int` | Sequential comparison | Split dates into year/month/day; compare year, then month, then day |
| `hasSameHospitalBed(Patient other)` | `boolean` | Type check + compare | If either not `InPatient` return `false`; cast and compare `hospitalBedNum` |
| `logMedicationAdministered(String med)` | `void` | Insert at end | Combine med with current date/time; add to end of log |
| `addToHistory(Appointment a)` | `void` | Index-find-then-modify | Find in upcoming; remove with left-shift; add to end of past |
| `scheduleNextAppointment(String date)` | `void` | Direct scheduling | Create appointment with given date; call `addAppointment()` |
| `recordVitals(double heartRate, double bp)` | `void` | Insert at end | Combine vitals with timestamp; append to log |
| *getters/setters* | — | Accessor / mutator | Standard accessors for all fields |

---

### `patient.InPatient` extends `Patient`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `dayIn` | `String` | Admission date |
| `dayOut` | `String` | Discharge date (`null` if admitted) |
| `hospitalBedNum` | `int` | Assigned bed number |
| `vitalsLog` | `String[]` | Timestamped vitals entries |
| `medicationsAdministered` | `String[]` | Timestamped medication log |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `checkIn()` | `boolean` | Direct assignment | Set `dayIn` to current date; return `true` |
| `checkIn(String date)` | `boolean` | Format validation | Validate `YYYY-MM-DD`; if invalid return `false`; set `dayIn` |
| `checkOut()` | `boolean` | Sequential operations | Set `dayOut` to current date; set `hospitalBedNum` to −1; call `calculateBill()` and `scheduleNextAppointment()` |
| `calculateBill()` | `double` | Loop + arithmetic | Sum `calculateCost()` on past appointments; add daily room rate × days between `dayIn` and `dayOut` |
| `scheduleNextAppointment()` | `void` | Date arithmetic | Split `dayOut`; add 1–2 days; create appointment; call `addAppointment()` |
| `toString()` | `String` | String formatting | All patient info + `dayIn`, `dayOut`, `hospitalBedNum` |

#### Unique Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `assignHospitalBed(int bedNum)` | `boolean` | Direct assignment | Set `hospitalBedNum` to `bedNum`; return `true` |
| `transferWard(String newWard)` | `boolean` | Direct assignment | Update `ward` field |
| `availableBed(int bedNum)` | `boolean` | **Linear search** | Loop all patients; for each `InPatient`, if bed number matches return `false`; return `true` after loop |
| `recordVitals(double heartRate, double bp)` | `void` | Insert at end | Combine vitals with date/time; add to end of `vitalsLog` |

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
| `checkOut()` | `boolean` | Sequential operations | Call `calculateBill()` and `scheduleNextAppointment()`; return `true` |
| `calculateBill()` | `double` | Loop + sum | Initialize total to 0; loop past appointments adding `calculateCost()` |
| `scheduleNextAppointment()` | `void` | Date arithmetic | Add `appointmentTimingMonths` to current date; create appointment; call `addAppointment()` |
| `toString()` | `String` | String formatting | All base patient fields |

---

### `patient.EmergencyPatient` extends `Patient`

#### Additional Fields

| Field | Type | Description |
|-------|------|-------------|
| `arrivalTime` | `String` | Time of arrival |
| `dayIn` | `String` | Date of arrival |
| `dayOut` | `String` | Date of discharge |
| `presentingComplaint` | `String` | Reason for visit |
| `arrivalType` | `String` | Mode of arrival (e.g. `"Ambulance"`) |
| `status` | `String` | `"Awaiting Triage"`, `"In Treatment"`, `"Stable"`, `"Critical"`, `"Discharged"` |

#### Overridden Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `checkIn()` | `boolean` | Direct assignment | Set `arrivalTime`, `dayIn`, status to `"Awaiting Triage"`; return `true` |
| `checkIn(String arrivalTime, String arrivalType)` | `boolean` | Direct assignment | Set provided fields; set `dayIn` and status to `"Awaiting Triage"` |
| `checkIn(String arrivalTime, String arrivalType, String complaint)` | `boolean` | Direct assignment | Set all provided fields; set `dayIn` and status to `"Awaiting Triage"` |
| `checkOut()` | `boolean` | Sequential operations | Set `dayOut`; set status to `"Discharged"`; call `calculateBill()` and `scheduleNextAppointment()` |
| `calculateBill()` | `double` | Loop + arithmetic + multiplier | Sum appointment costs; add ER daily rate × days; apply status-based surcharge |
| `scheduleNextAppointment()` | `void` | Date arithmetic | Split `dayOut`; add 1 day; create appointment; call `addAppointment()` |
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
| `registerPatient(...)` | `void` | Bounds check + insert | If at capacity return; determine subclass; create object; insert at `patients[numPatients]`; increment count |
| `deletePatient(int patientID)` | `boolean` | Recursive binary search + left shift | Call `searchPatientByPatientID()`; if null return `false`; remove, shift left, decrement count |
| `updatePatient(int patientID, ...)` | `boolean` | Search + update | Find patient by ID; call setters; return result |
| `addDiagnosis(Patient, String)` | `boolean` | Delegation | Call `toUpdate.addDiagnosis()` |
| `deleteDiagnosis(Patient, String)` | `boolean` | Delegation | Call `toUpdate.deleteDiagnosis()` |
| `updateDiagnosis(Patient, String org, String newD)` | `boolean` | Delegation | Call `toUpdate.updateDiagnosis()` |
| `addAppointment(Appointment a)` | `boolean` | Delegation | Call patient's `addAppointment()` |
| `deleteAppointment(Appointment a)` | `boolean` | Delegation | Call patient's `deleteAppointment()` |
| `updateAppointment(Appointment org, Appointment newA)` | `boolean` | Delegation | Call patient's `updateAppointment()` |
| `addMedication(Patient, String med, String dosage)` | `boolean` | Delegation | Call `addMedication()` on patient |
| `deleteMedication(Patient, String med)` | `boolean` | Delegation | Call `deleteMedication()` on patient |
| `updateMedication(Patient, String, String newMed, String newDosage)` | `boolean` | Delegation | Call `updateMedication()` on patient |
| `addAllergy(Patient, String allergy)` | `boolean` | Delegation | Call `addAllergy()` on patient |
| `deleteAllergy(Patient, String allergy)` | `boolean` | Delegation | Call `deleteAllergy()` on patient |
| `updateAllergy(Patient, String org, String newA)` | `boolean` | Delegation | Call `updateAllergy()` on patient |
| `addMedicalHistory(Patient, String entry)` | `boolean` | Delegation | Call `addMedicalHistory()` on patient |
| `deleteMedicalHistory(Patient, String entry)` | `boolean` | Delegation | Call `deleteMedicalHistory()` on patient |
| `addFamilyHistory(Patient, String entry)` | `boolean` | Delegation | Call `addFamilyHistory()` on patient |
| `deleteFamilyHistory(Patient, String entry)` | `boolean` | Delegation | Call `deleteFamilyHistory()` on patient |
| `updateAssignedStaffForPatient(Patient, Staff)` | `boolean` | Delegation | Call `updateAssignedStaff()` on patient |
| `loadPatientInfo(String fileName)` | `void` | **File I/O** | Parse type tag and fields; create subclass; call `registerPatient()` or equivalent |
| `savePatientInfo(String fileName)` | `void` | **File I/O** | Write each patient via `toString()` |
| `loadPatientAppts(String fileName)` | `void` | **File I/O** | For each record, find patient by ID; call `addAppointment()` or `addToHistory()` by tag |
| `savePatientAppts(String fileName)` | `void` | **File I/O** | Write patient appointment associations |
| `checkInPatient(Patient)` | `void` | Delegation | Call `checkIn()` on patient |
| `checkOutPatient(Patient)` | `void` | Delegation | Call `checkOut()` on patient |
| `calculateBill(Patient)` | `double` | Delegation | Call `calculateBill()` on patient |
| `scheduleNextAppointmentForPatient(Patient, Appointment)` | `void` | Delegation | Schedule follow-up on patient |
| `listAllPatients()` | `void` | Loop + print | Loop and print all patients |
| `viewAppointments(Patient)` | `void` | Loop + print | Print patient's appointments |
| `logMedicationAdministered(Patient, String med)` | `boolean` | Delegation | Call `logMedicationAdministered()` on patient |
| `addToHistory(Patient, Appointment a)` | `void` | Delegation | Call `addToHistory()` on patient |
| `updateStatus(EmergencyPatient, String status)` | `void` | Delegation | Call `updateStatus()` on emergency patient |
| `searchPatientByName(String firstName, String lastName)` | `Patient` | **Sequential search** | Loop patients; call `equalsName()`; return match or `null` |
| `searchPatientByPatientID(int patientID)` | `Patient` | **Recursive binary search** | Set low/high; base case low > high return `null`; mid compare; recurse left or right |
| `sortByDateEntered()` | `void` | **Insertion sort** | Loop from index 1; store key; shift right while key compares greater; insert key |
| `sortByWard()` | `void` | **Selection sort** | Outer loop sets `minIndex`; inner finds alphabetically smallest ward; swap |
| `sortByPatientID()` | `void` | **Bubble sort** | Outer and inner loops; swap adjacent if patient IDs out of order |

---

## Appointment Module

### `appointment.Appointment` *(abstract)*

Base class for all appointments.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `apptID` | `int` | Unique identifier |
| `patient` | `Patient` | Patient for this appointment |
| `staff` | `Staff[]` | Assigned staff members |
| `date` | `String` | Date (`YYYYMMDD`) |
| `time` | `double` | Time (`hh.mm`, 24-hour) |
| `duration` | `double` | Duration in hours |
| `cost` | `double` | Computed cost |
| `status` | `String` | `"future"`, `"done"`, `"cancelled"`, `"no-show"` |

#### Abstract Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `calculateCost()` | `double` | Abstract — subclass-specific | Compute appointment cost |
| `validateBooking()` | `boolean` | Abstract — subclass-specific | Validate booking requirements |
| `assignStaff()` | `void` | Abstract — subclass-specific | Assign staff to appointment |
| `toString()` | `String` | Abstract — subclass-specific | Formatted appointment info |

#### Concrete Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `cancel()` | `void` | Direct assignment | Set status to `"cancelled"` |
| `reschedule(String newDate, double newTime)` | `boolean` | Validate-and-revert | Store old values; set new date/time; call `validateBooking()`; revert if invalid |
| `equals(Appointment other)` | `boolean` | Nested loop (conflict check) | Compare date/time; if same room return `true`; nested loop compare staff arrays |
| `markDone()` | `void` | Delegation | Set status to `"complete"`; call `patient.addToHistory(this)` |
| *getters/setters* | — | Accessor / mutator | Standard accessors; each subclass implements `getRoomNum()` for conflict checks |

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
| `assignStaff(Doctor d)` | `void` | Direct assignment | Set `mainDoctor`; assign `d` to `staff[0]` |
| `assignClinicRoom()` | `void` | **Linear search** | Iterate room roster; find first unallocated room; assign to `clinicRoomNum` |
| `markNoShow()` | `void` | Direct assignment + arithmetic | Set status to `"NoShow"`; add no-show fee to cost |
| `estimateDuration(String reasonForVisit)` | `double` | Keyword scan | Initialize base duration; check reason for keywords that add time; return total |
| `toString()` | `String` | String formatting | Base info + `clinicRoomNum` |

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
| `calculateCost()` | `double` | Arithmetic | Base fee + type/anaesthesia surcharges + hourly rate × duration |
| `validateBooking()` | `boolean` | Direct comparison | Extract surgeon from `staff[0]`; compare specialty to `type` |
| `assignStaff(Surgeon surgeon, Nurse[] nurses)` | `void` | Loop + assignment | Assign surgeon to `staff[0]`; loop nurses into `staff[1..n]` |
| `assignOperatingRoom(int ORnum)` | `void` | Linear search | Check preferred OR free; if occupied loop alternatives until free found |
| `givePreOpInstructions()` | `void` | Conditional assignment | Evaluate `type`; set `preOpInstructions` to matching guidelines |
| `estimateDuration(int experience)` | `double` | Arithmetic | Set baseline by type; adjust by surgeon experience factor |
| `calculateNursesNeeded()` | `int` | Conditional | Return 3 if urgency ≥ 4; 2 if urgency == 3; else 1 |
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
| `calculateCost()` | `double` | Arithmetic | Base fee + medication costs + staff duration fees |
| `validateBooking()` | `boolean` | Conditional check | Return `true` if `emergencyRoomNum > 0`, else `false` |
| `assignStaff(Doctor d, int urgencyIdx)` | `void` | Direct assignment | Assign `d` to `staff[0]` |
| `autoAssignNurse(Nurse n)` | `void` | **Linear search** | Loop `staff` from index 1; find first null slot; assign nurse |
| `urgentAssignStaff(Doctor d, Nurse[] nurses)` | `void` | Loop + assignment | Clear staff array; assign doctor at `[0]`; loop nurses into `[1..n]` |
| `markStabilized(boolean isStable)` | `void` | Conditional decrement | If `isStable`, decrement `urgencyIdx` |
| `assignEmergencyRoom()` | `int` | **Linear search** | Scan ER bay roster; find first unoccupied bay; set and return number |
| `estimateDuration()` | `double` | Conditional arithmetic | Initialize to `0.5 × urgencyIdx`; add 1.5 h if urgency ≥ 2; add 2 h if urgency ≥ 4 |
| `toString()` | `String` | String formatting | Base info + ER room number and urgency index |

---

### `appointment.ApptManager`

Central controller for all appointments.

#### Fields

| Field | Type | Description |
|-------|------|-------------|
| `apptList` | `Appointment[]` | All appointments |
| `apptNum` | `int` | Current appointment count |
| `maxAppts` | `int` | Array capacity |

#### Methods

| Method | Returns | Algorithm | Description |
|--------|---------|-----------|-------------|
| `addAppt(Appointment a)` | `boolean` | Validation + insert | Call `validateBooking()`; call `isSlotConflict()`; insert at `apptList[apptNum]`; increment count |
| `cancelAppt(int apptID)` | `boolean` | Recursive search + left shift | Call `searchByID()`; call `cancel()`; find index; shift left; decrement count |
| `rescheduleAppt(int apptID, String newDate, double newTime)` | `boolean` | Recursive search + validate-and-revert | Find by ID; store old values; call `reschedule()`; check conflict; revert on failure |
| `isSlotConflict(Appointment checkAppt)` | `boolean` | **Linear search** | Loop `apptList`; skip cancelled and same ID; call `equals()`; return `true` on conflict |
| `searchByDate(String date)` | `Appointment` | **Linear search** | Loop appointments; return first matching date |
| `searchByPatientAndDate(int patientID, String date, int idx)` | `Appointment` | **Recursive** | Base case: idx ≥ `apptNum` return `null`; if match return it; else recurse with `idx + 1` |
| `searchByID(int apptID, int idx)` | `Appointment` | **Recursive** | Base case: idx ≥ `apptNum` return `null`; if ID matches return appointment; else recurse |
| `sortByDate()` | `void` | **Selection sort** | Outer loop sets `minIndex`; inner compares dates via `compareTo()`; swap minimum |
| `sortByDate(String date)` | `void` | **Selection sort** | Selection sort appointments up to given date |
| `sortByPatientThenDate()` | `void` | **Bubble sort** | Compare adjacent patient IDs; if equal compare dates; swap if out of order |
| `loadFromFile(String filename)` | `boolean` | **File I/O** | Read each line; split by comma; instantiate matching subclass; add to `apptList` |
| `saveToFile(String filename)` | `boolean` | **File I/O** | Loop `apptList`; format and write each record |
| `viewDailySchedule(String date)` | `void` | Loop + print | Print header; loop and print `toString()` for matching date |
| `viewUpcomingAppointments(int patientID)` | `void` | Loop + print | Loop; print appointments matching patient ID with status `"future"` |
| `runCostSummary(String date)` | `double` | Loop + sum | Initialize total; loop; if date matches add `calculateCost()`; return total |
