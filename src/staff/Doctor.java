/**
 * Doctor.java
 * ICS4U Hospital Management System — Staff Module
 * Ferdinand Zhang
 * June 2, 2026
 *
 * Concrete staff role for physicians: patient assignment, appointments,
 * payroll, diagnoses, prescriptions, and surgical referrals.
 */

package staff;

import appointment.Appointment;
import patient.Patient;
import shared.Date;

public class Doctor extends Staff {

    private static final double FLAT_ANNUAL_SALARY = 200_000.0;
    private static final int PAY_PERIODS_PER_YEAR = 26;

    private String licenseNumber;
    private Patient[] patientsAssigned;
    private double consultationFee;
    private int maxPatients;

    /**
     * Constructs a doctor with shared staff attributes and role-specific fields.
     *
     * @param staffID         unique staff identifier
     * @param name            full name
     * @param experience      years of experience
     * @param specialization  medical specialization area
     * @param offDays         scheduled off-day dates
     * @param schedule        personal appointment schedule
     * @param licenseNumber   medical licence number
     * @param consultationFee fee per consultation
     * @param maxPatients     maximum patient capacity
     */
    public Doctor(String staffID, String name, int experience, String specialization,
            String[] offDays, Appointment[] schedule, String licenseNumber,
            double consultationFee, int maxPatients) {
        super(staffID, name, experience, specialization, offDays, schedule);
        this.licenseNumber = licenseNumber;
        this.consultationFee = consultationFee;
        this.maxPatients = maxPatients;
        this.patientsAssigned = new Patient[maxPatients];
    }

    // Linear search — count assigned patients, then append up to maxPatients
    @Override
    public void assignPatients(Patient[] patients) {
        if (patients == null) {
            return;
        }

        int count = 0;
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] != null) {
                count++;
            }
        }

        for (int i = 0; i < patients.length; i++) {
            if (patients[i] == null) {
                continue;
            }
            if (count >= maxPatients) {
                System.out.println("Error: doctor at capacity.");
                return;
            }
            patientsAssigned[count] = patients[i];
            count++;
        }
    }

    // Linear search — conflict check, then insert into schedule
    @Override
    public void addAppointment(Appointment appt) {
        if (appt == null) {
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
                return;
            }
        }
        System.out.println("Error: schedule at capacity.");
    }

    // Arithmetic — flat annual salary divided by pay periods per year
    @Override
    public double calculatePay() {
        return FLAT_ANNUAL_SALARY / PAY_PERIODS_PER_YEAR;
    }

    // Loop + string build — day-by-day patient list with appointment times
    @Override
    public String getSchedule() {
        StringBuilder sb = new StringBuilder();
        Appointment[] schedule = getSortedScheduleAppointments();
        Date lastDate = null;

        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                continue;
            }

            Date apptDate = schedule[i].getDate();
            if (lastDate == null || apptDate.compareTo(lastDate) != 0) {
                if (lastDate != null) {
                    sb.append("\n");
                }
                sb.append(apptDate.toString()).append(":\n");
                lastDate = apptDate;
            }

            Patient p = schedule[i].getPatient();
            String patientLabel = "Unknown patient";
            if (p != null) {
                patientLabel = p.getFirstName() + " " + p.getLastName()
                        + " (ID: " + p.getPatientID() + ")";
            }

            sb.append("  ")
                    .append(formatTime(schedule[i].getTime()))
                    .append(" — ")
                    .append(patientLabel)
                    .append("\n");
        }

        if (sb.length() == 0) {
            return "No scheduled appointments.";
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Doctor\n");
        sb.append(getStaffID()).append("\n");
        sb.append(getName()).append("\n");
        sb.append(getExperience()).append("\n");
        sb.append(getSpecialization()).append("\n");
        sb.append(formatOffDaysForFile()).append("\n");
        sb.append(licenseNumber).append("\n");
        sb.append(consultationFee).append("\n");
        sb.append(maxPatients).append("\n");
        sb.append(":");
        return sb.toString();
    }

    /**
     * Records a diagnosis on the patient record via addDiagnoses on the patient.
     *
     * @param p          patient to diagnose
     * @param diagnosis  diagnosis text to add
     */
    public void diagnosePatient(Patient p, String diagnosis) {
        if (p == null) {
            return;
        }
        p.addDiagnoses(diagnosis);
    }

    /**
     * Prescribes medication after checking for allergy conflicts.
     *
     * @param p       patient receiving the prescription
     * @param med     medication name
     * @param dosage  prescribed dosage
     */
    public void prescribeMedication(Patient p, String med, String dosage) {
        if (p == null) {
            return;
        }
        if (p.checkAllergyConflict(med)) {
            System.out.println("Warning: allergy conflict for medication " + med + ".");
            return;
        }
        p.addMedication(med, dosage);
    }

    /**
     * Refers an assigned patient to a surgeon and updates assignments via setAssignedStaff on the patient.
     *
     * @param p patient to refer
     * @param s surgeon receiving the referral
     */
    public void referPatient(Patient p, Surgeon s) {
        if (p == null || s == null) {
            return;
        }

        if (!isPatientAssigned(p)) {
            System.out.println("Error: patient not assigned to this doctor.");
            return;
        }

        if (!s.addReferral(p.getPatientID())) {
            System.out.println("Error: could not add referral to surgeon.");
            return;
        }

        s.assignPatients(new Patient[] { p });
        removePatientFromAssigned(p);
        p.setAssignedStaff(s);
    }

    /** @return medical licence number */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * @param licenseNumber medical licence number to set
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    /**
     * Returns a defensive copy of assigned patients.
     *
     * @return copy of the patients assigned to this doctor
     */
    public Patient[] getPatientsAssigned() {
        Patient[] copy = new Patient[patientsAssigned.length];
        for (int i = 0; i < patientsAssigned.length; i++) {
            copy[i] = patientsAssigned[i];
        }
        return copy;
    }

    /** @return fee per consultation */
    public double getConsultationFee() {
        return consultationFee;
    }

    /**
     * @param consultationFee fee per consultation to set
     */
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    /** @return maximum patient capacity */
    public int getMaxPatients() {
        return maxPatients;
    }

    /**
     * @param maxPatients maximum patient capacity to set
     */
    public void setMaxPatients(int maxPatients) {
        this.maxPatients = maxPatients;
        Patient[] resized = new Patient[maxPatients];
        int limit = Math.min(patientsAssigned.length, maxPatients);
        for (int i = 0; i < limit; i++) {
            resized[i] = patientsAssigned[i];
        }
        patientsAssigned = resized;
    }

    private boolean isPatientAssigned(Patient p) {
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == p) {
                return true;
            }
        }
        return false;
    }

    private void removePatientFromAssigned(Patient p) {
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == p) {
                for (int j = i; j < patientsAssigned.length - 1; j++) {
                    patientsAssigned[j] = patientsAssigned[j + 1];
                }
                patientsAssigned[patientsAssigned.length - 1] = null;
                return;
            }
        }
    }

}
