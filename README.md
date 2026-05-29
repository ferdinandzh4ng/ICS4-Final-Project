# Hospital Management System

ICS4U Final Project — Stage 2 Design Implementation

A Java-based hospital management system organized into three independent subsystems that share data through file I/O and cross-subsystem references. Each subsystem uses inheritance and polymorphism to model its domain.

**Team:** Ferdinand · Caroline · Ida

---

## Subsystems

| Subsystem | Package | Owner | Description |
|-----------|---------|-------|-------------|
| **A — Medical Staff Management** | `staff` | Ferdinand | Doctors, nurses, and surgeons; scheduling, payroll, and staff assignment |
| **B — Patient & Medical Records** | `patient` | Caroline | In-patients, out-patients, and emergency patients; diagnoses, medications, and billing |
| **C — Appointments & Scheduling** | `appointment` | Ida | Routine checkups, surgeries, and emergency visits; booking and conflict detection |

---

## Project Structure

```
ICS4-Final-Project/
├── data/                          # Input / output text files
│   ├── staff.txt
│   ├── patients.txt
│   ├── patient_appointments.txt
│   └── appointments.txt
└── src/
    ├── HospitalRunner.java        # Program entry point
    ├── shared/
    │   └── Date.java              # Shared date helper (used by all subsystems)
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

## Compile & Run

From the project root:

```bash
# Compile all source files
javac src/HospitalRunner.java src/shared/*.java src/staff/*.java src/patient/*.java src/appointment/*.java

# Run the program
java -cp src HospitalRunner
```

---

## Data Files

All persistent data is stored in plain-text files under `data/`:

| File | Subsystem | Purpose |
|------|-----------|---------|
| `staff.txt` | A | Staff records (Doctor, Nurse, Surgeon) |
| `patients.txt` | B | Patient records (InPatient, OutPatient, EmergencyPatient) |
| `patient_appointments.txt` | B | Past and upcoming appointments per patient |
| `appointments.txt` | C | All appointment records (Checkup, Surgery, Emergency) |

---

## Class Hierarchy

### Subsystem A — Staff

```
Staff (abstract)
├── Doctor
├── Nurse
└── Surgeon

StaffManager
```

### Subsystem B — Patient

```
Patient (abstract)
├── InPatient
├── OutPatient
└── EmergencyPatient

Medication
PatientManager
```

### Subsystem C — Appointment

```
Appointment (abstract)
├── RoutineCheckup
├── Surgery
└── EmergencyVisit

ApptManager
```

---

## Cross-Subsystem Interactions

- **Staff → Patient:** Doctors diagnose and prescribe; nurses administer medication and record vitals; surgeons perform procedures and update medical history.
- **Staff → Appointment:** Staff members hold personal schedules; `StaffManager` provides available nurses and doctors for appointment booking.
- **Appointment → Patient:** Booking, cancelling, rescheduling, and completing appointments update each patient's appointment history.
- **Appointment → Staff:** Appointments reference assigned staff and use conflict checking to prevent double-booking.
