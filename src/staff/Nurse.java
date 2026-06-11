/**
 * Nurse.java
 * ICS4U Hospital Management System — Staff Module
 * Ferdinand Zhang
 * June 6, 2026
 *
 * Concrete staff role for nursing: ward-based patient assignment, shift
 * scheduling, hourly payroll with overtime, vitals monitoring, and medication
 * administration.
 */

package staff;

import appointment.Appointment;
import patient.InPatient;
import patient.Medication;
import patient.Patient;
import patient.InPatient;
import patient.Medication;

public class Nurse extends Staff {

    private static final int MAX_ASSIGNED_PATIENTS = 20;
    private static final double MAX_SHIFT_HOURS = 12.0;
    private static final double DAY_SHIFT_START = 7.0;
    private static final double DAY_SHIFT_END = 19.0;

    private String ward;
    private String shiftType;
    private double hourlyRate;
    private int hoursWorkedThisWeek;
    private Patient[] patientsAssigned;

    /**
     * Constructs a nurse with shared staff attributes and role-specific fields.
     *
     * @param staffID             unique staff identifier
     * @param name                full name
     * @param experience          years of experience
     * @param specialization      medical specialization area
     * @param offDays             scheduled off-day dates
     * @param schedule            personal appointment/shift schedule
     * @param ward                assigned hospital ward
     * @param shiftType           shift type ("Day", "Night", or "Rotating")
     * @param hourlyRate          pay per hour
     * @param hoursWorkedThisWeek hours worked in the current week
     */
    public Nurse(String staffID, String name, int experience, String specialization,
            String[] offDays, Appointment[] schedule, String ward, String shiftType,
            double hourlyRate, int hoursWorkedThisWeek) {
        super(staffID, name, experience, specialization, offDays, schedule);
        this.ward = ward;
        this.shiftType = shiftType;
        this.hourlyRate = hourlyRate;
        this.hoursWorkedThisWeek = hoursWorkedThisWeek;
        this.patientsAssigned = new Patient[MAX_ASSIGNED_PATIENTS];
    }

    // Linear search (ward filter) — assign patients whose ward matches this nurse's ward
    @Override
    public void assignPatients(Patient[] patients) {
        if (patients == null) {
            return;
        }

        for (int i = 0; i < patients.length; i++) {
            if (patients[i] == null) {
                continue;
            }
            if (patients[i].getWard() != null && patients[i].getWard().equals(ward)) {
                addPatientToAssigned(patients[i]);
            }
        }
    }

    // Validation + insert — reject shifts over 12 hours and update weekly hours
    @Override
    public void addAppointment(Appointment appt) {
        if (appt == null) {
            return;
        }

        if (appt.getDuration() > MAX_SHIFT_HOURS) {
            System.out.println("Error: shift duration exceeds 12 hours.");
            return;
        }

        if (hasTimeConflict(appt.getDate(), appt.getTime())) {
            System.out.println("Error: appointment time conflict.");
            return;
        }

        Appointment[] schedule = getScheduleSlots();
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                schedule[i] = appt;
                hoursWorkedThisWeek += (int) appt.getDuration();
                return;
            }
        }
        System.out.println("Error: schedule at capacity.");
    }

    // Conditional arithmetic — standard hours plus 1.5× overtime beyond 40 hours
    @Override
    public double calculatePay() {
        if (hoursWorkedThisWeek <= 40) {
            return hourlyRate * hoursWorkedThisWeek;
        }
        int overtimeHours = hoursWorkedThisWeek - 40;
        return (hourlyRate * 40) + (hourlyRate * 1.5 * overtimeHours);
    }

    // Loop + string build — shift blocks with ward and assigned patients
    @Override
    public String getSchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ward: ").append(ward).append(" | Shift: ").append(shiftType).append("\n");

        Appointment[] schedule = getScheduleSlots();
        boolean hasShifts = false;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                continue;
            }
            hasShifts = true;
            Patient p = schedule[i].getPatient();
            String patientLabel = "Unknown patient";
            if (p != null) {
                patientLabel = p.getFirstName() + " " + p.getLastName()
                        + " (ID: " + p.getPatientID() + ")";
            }
            sb.append("  ")
                    .append(schedule[i].getDate().toString())
                    .append(" ")
                    .append(formatTime(schedule[i].getTime()))
                    .append(" — ")
                    .append(patientLabel)
                    .append("\n");
        }
        if (!hasShifts) {
            sb.append("  No scheduled shifts.\n");
        }

        sb.append("Assigned patients:\n");
        boolean hasPatients = false;
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == null) {
                continue;
            }
            hasPatients = true;
            sb.append("  ")
                    .append(patientsAssigned[i].getFirstName())
                    .append(" ")
                    .append(patientsAssigned[i].getLastName())
                    .append(" (ID: ")
                    .append(patientsAssigned[i].getPatientID())
                    .append(")\n");
        }

        if (!hasPatients) {
            sb.append("  None\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nurse\n");
        sb.append(getStaffID()).append("\n");
        sb.append(getName()).append("\n");
        sb.append(getExperience()).append("\n");
        sb.append(getSpecialization()).append("\n");
        sb.append(formatOffDaysForFile()).append("\n");
        sb.append(ward).append("\n");
        sb.append(shiftType).append("\n");
        sb.append(hourlyRate).append("\n");
        sb.append(hoursWorkedThisWeek).append("\n");
        sb.append(":");
        return sb.toString();
    }

    /**
     * Administers medication after verifying it was prescribed for the patient.
     * Linear search through the patient's medications array; delegates to
     * logMedicationsAdministered on InPatient when found.
     *
     * @param p   patient receiving the medication
     * @param med medication name
     * @param m   dosage of medication
     */
    public void administerMedication(Patient p, String med, String dosage) {
        if (p == null) {
            return;
        }
<<<<<<< HEAD
        if (p.getIndexOfMedicationByName(med) == -1) {
=======
        Medication prescribed = findPrescribedMedication(p, med);
        if (prescribed == null) {
>>>>>>> origin/main
            System.out.println("Warning: medication " + med + " is not prescribed for this patient.");
            return;
        }
        if (p instanceof InPatient) {
<<<<<<< HEAD
            Medication m = new Medication(med, dosage);
            ((InPatient)p).logMedicationsAdministered(m);
=======
            ((InPatient) p).logMedicationsAdministered(prescribed);
>>>>>>> origin/main
        }
    }

    /**
     * Records a patient's vitals through delegation to recordVitals on InPatient.
     *
     * @param p         patient being monitored
     * @param heartRate heart rate reading
     * @param bp        blood pressure reading
     */
    public void monitorVitals(Patient p, double heartRate, double bp) {
        if (p == null) {
            return;
        }
        if (p instanceof InPatient) {
<<<<<<< HEAD
            ((InPatient)p).recordVitals(heartRate, bp);
=======
            ((InPatient) p).recordVitals(heartRate, bp);
>>>>>>> origin/main
        }
    }

    /**
     * Switches to a new shift type, clearing appointments that conflict with it.
     *
     * @param newShift target shift type ("Day", "Night", or "Rotating")
     */
    public void switchShift(String newShift) {
        if (newShift == null
                || (!newShift.equals("Day") && !newShift.equals("Night") && !newShift.equals("Rotating"))) {
            System.out.println("Error: invalid shift type. Use Day, Night, or Rotating.");
            return;
        }

        shiftType = newShift;

        // Validation + loop — remove appointments that conflict with the new shift
        Appointment[] schedule = getScheduleSlots();
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                continue;
            }
            if (conflictsWithShift(schedule[i].getTime(), newShift)) {
                hoursWorkedThisWeek -= (int) schedule[i].getDuration();
                if (hoursWorkedThisWeek < 0) {
                    hoursWorkedThisWeek = 0;
                }
                schedule[i] = null;
            }
        }
    }

    /** @return assigned hospital ward */
    public String getWard() {
        return ward;
    }

    /**
     * @param ward assigned hospital ward to set
     */
    public void setWard(String ward) {
        this.ward = ward;
    }

    /** @return shift type */
    public String getShiftType() {
        return shiftType;
    }

    /**
     * @param shiftType shift type to set
     */
    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    /** @return hourly pay rate */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * @param hourlyRate hourly pay rate to set
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /** @return hours worked this week */
    public int getHoursWorkedThisWeek() {
        return hoursWorkedThisWeek;
    }

    /**
     * @param hoursWorkedThisWeek hours worked this week to set
     */
    public void setHoursWorkedThisWeek(int hoursWorkedThisWeek) {
        this.hoursWorkedThisWeek = hoursWorkedThisWeek;
    }

    /**
     * Returns a defensive copy of assigned patients.
     *
     * @return copy of patients assigned to this nurse
     */
    public Patient[] getPatientsAssigned() {
        Patient[] copy = new Patient[patientsAssigned.length];
        for (int i = 0; i < patientsAssigned.length; i++) {
            copy[i] = patientsAssigned[i];
        }
        return copy;
    }

    /**
     * Linear search through a patient's medications array for a matching name.
     *
     * @param p       patient whose prescriptions to search
     * @param medName medication name to find
     * @return matching Medication object, or null if not prescribed
     */
    private Medication findPrescribedMedication(Patient p, String medName) {
        Medication[] meds = p.getMedications();
        for (int i = 0; i < meds.length; i++) {
            if (meds[i] != null && meds[i].getMedName().equals(medName)) {
                return meds[i];
            }
        }
        return null;
    }

    private void addPatientToAssigned(Patient p) {
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == p) {
                return;
            }
        }
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == null) {
                patientsAssigned[i] = p;
                return;
            }
        }
        System.out.println("Error: nurse patient list at capacity.");
    }

    private boolean conflictsWithShift(double time, String shift) {
        if (shift.equals("Rotating")) {
            return false;
        }
        boolean isDayHours = time >= DAY_SHIFT_START && time < DAY_SHIFT_END;
        if (shift.equals("Day")) {
            return !isDayHours;
        }
        return isDayHours;
    }

}
