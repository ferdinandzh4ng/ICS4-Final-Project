# Hospital Management System

A Java application for managing hospital operations across staff, patients, and appointments. The system supports role-based staff workflows, patient medical records, and scheduling with conflict detection and persistent file storage.

---

## Features

### Staff Management
- Manage doctors, nurses, and surgeons with role-specific attributes and behaviour
- Assign patients by capacity, ward, or surgical referral
- Track schedules and prevent double-booking via time-conflict checks
- Run payroll with role-specific compensation (salary, hourly + overtime, per-procedure fees)
- Search, sort, and persist staff records to file

### Patient & Medical Records
- Register and manage in-patients, out-patients, and emergency patients
- Maintain diagnoses, allergies, medications, and medical/family history
- Check patients in and out with type-specific billing
- Track appointment history (past and upcoming) per patient
- Validate OHIP numbers and detect medication–allergy conflicts

### Appointments & Scheduling
- Book routine checkups, surgeries, and emergency visits
- Assign staff teams based on availability, ward, and urgency
- Detect room and staff scheduling conflicts
- Reschedule, cancel, and mark appointments complete
- Generate daily schedules and cost summaries by date

---

## Architecture

The application is divided into three modules that communicate through shared object references and file I/O:

| Module | Package | Responsibility |
|--------|---------|----------------|
| Staff | `staff` | Staff records, scheduling, payroll, and team assignment |
| Patient | `patient` | Patient records, medical data, check-in/out, and billing |
| Appointment | `appointment` | Booking, conflict detection, and appointment lifecycle |

Shared utilities live in the `shared` package (e.g. `Date` for date parsing and comparison across all modules).

---

## Project Structure

```
ICS4-Final-Project/
├── data/
│   ├── staff.txt
│   ├── patients.txt
│   ├── patient_appointments.txt
│   └── appointments.txt
└── src/
    ├── HospitalRunner.java
    ├── shared/
    │   └── Date.java
    ├── staff/
    │   ├── Staff.java
    │   ├── Doctor.java
    │   ├── Nurse.java
    │   ├── Surgeon.java
    │   └── StaffManager.java
    ├── patient/
    │   ├── Patient.java
    │   ├── InPatient.java
    │   ├── OutPatient.java
    │   ├── EmergencyPatient.java
    │   ├── Medication.java
    │   └── PatientManager.java
    └── appointment/
        ├── Appointment.java
        ├── RoutineCheckup.java
        ├── Surgery.java
        ├── EmergencyVisit.java
        └── ApptManager.java
```

---

## Getting Started

**Requirements:** Java JDK 8 or later

From the project root:

```bash
javac src/HospitalRunner.java src/shared/*.java src/staff/*.java src/patient/*.java src/appointment/*.java
java -cp src HospitalRunner
```

---

## Data Storage

All records are persisted as plain-text files in `data/`:

| File | Contents |
|------|----------|
| `staff.txt` | Staff records tagged by role (Doctor, Nurse, Surgeon) |
| `patients.txt` | Patient records tagged by type (InPatient, OutPatient, EmergencyPatient) |
| `patient_appointments.txt` | Past and upcoming appointment IDs linked to each patient |
| `appointments.txt` | Appointment records (Checkup, Surgery, Emergency) |

Each manager class handles loading from and saving to its corresponding file(s).

---

## Class Hierarchy

### Staff

```
Staff (abstract)
├── Doctor      — diagnoses, prescriptions, referrals
├── Nurse       — ward care, vitals, medication administration
└── Surgeon     — surgical procedures, OR scheduling

StaffManager    — CRUD, search, sort, payroll, availability queries
```

### Patient

```
Patient (abstract)
├── InPatient           — admitted patients with bed assignment and vitals log
├── OutPatient          — clinic visits with recurring appointment intervals
└── EmergencyPatient    — ER arrivals with triage status and urgency tracking

Medication      — prescribed medication entry
PatientManager  — CRUD, search, sort, check-in/out, file I/O
```

### Appointment

```
Appointment (abstract)
├── RoutineCheckup  — clinic visits with assigned doctor and room
├── Surgery         — OR procedures with surgical team and anaesthesia
└── EmergencyVisit  — ER visits with urgency-based staffing

ApptManager       — booking, cancellation, rescheduling, reporting
```

---

## Module Interactions

```
┌─────────────┐     assign staff      ┌──────────────┐
│  Appointment │ ◄──────────────────► │    Staff     │
│   Manager    │     check conflicts   │   Manager    │
└──────┬──────┘                       └──────┬───────┘
       │                                     │
       │  book / cancel / complete           │  diagnose / prescribe /
       ▼                                     ▼  administer / operate
┌─────────────┐
│   Patient   │
│   Manager   │
└─────────────┘
```

- **Staff → Patient:** Doctors update diagnoses and prescriptions; nurses record vitals and administer medication; surgeons log procedures to medical history.
- **Staff → Appointment:** Each staff member maintains a personal schedule; the staff manager resolves available nurses and doctors when booking teams.
- **Appointment → Patient:** Creating, updating, or completing an appointment syncs the patient's appointment history.
- **Appointment → Staff:** New bookings validate staff availability and room occupancy before confirmation.
