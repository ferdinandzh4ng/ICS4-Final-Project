/**
 * Surgeon.java
 * ICS4U Hospital Management System — Staff Module
 * Ferdinand Zhang
 * June 6, 2026
 *
 * Concrete staff role for surgeons: referral-based patient assignment, OR
 * scheduling, per-procedure compensation, and surgical outcome tracking.
 */

package staff;

import appointment.Appointment;
import patient.Patient;

public class Surgeon extends Staff {

    private static final double FLAT_ANNUAL_SALARY = 200_000.0;
    private static final int PAY_PERIODS_PER_YEAR = 26;
    private static final int MAX_ASSIGNED_PATIENTS = 20;
    private static final int MAX_REFERRALS = 50;
    private static final int MAX_OR_BOOKINGS = 20;
    private static final int MAX_SURGERY_OUTCOMES = 100;

    private int operatingRoom;
    private int surgeriesCompleted;
    private String specialtyArea;
    private double surgeryFeePerProcedure;
    private Patient[] patientsAssigned;
    private int[] referralList;
    private int referralCount;
    private int[] orRooms;
    private String[] orDates;
    private double[] orTimes;
    private int orBookingCount;
    private boolean[] surgeryOutcomes;
    private int outcomeCount;
    private boolean orScheduleFailed;

    /**
     * Constructs a surgeon with shared staff attributes and role-specific fields.
     *
     * @param staffID                unique staff identifier
     * @param name                   full name
     * @param experience             years of experience
     * @param specialization         medical specialization area
     * @param offDays                scheduled off-day dates
     * @param schedule               personal appointment/shift schedule
     * @param operatingRoom          assigned operating room number
     * @param surgeriesCompleted     total surgeries performed to date
     * @param specialtyArea          surgical specialty area
     * @param surgeryFeePerProcedure fee earned per completed procedure
     */
    public Surgeon(String staffID, String name, int experience, String specialization,
            String[] offDays, Appointment[] schedule, int operatingRoom,
            int surgeriesCompleted, String specialtyArea, double surgeryFeePerProcedure) {
        super(staffID, name, experience, specialization, offDays, schedule);
        this.operatingRoom = operatingRoom;
        this.surgeriesCompleted = surgeriesCompleted;
        this.specialtyArea = specialtyArea;
        this.surgeryFeePerProcedure = surgeryFeePerProcedure;
        this.patientsAssigned = new Patient[MAX_ASSIGNED_PATIENTS];
        this.referralList = new int[MAX_REFERRALS];
        this.referralCount = 0;
        this.orRooms = new int[MAX_OR_BOOKINGS];
        this.orDates = new String[MAX_OR_BOOKINGS];
        this.orTimes = new double[MAX_OR_BOOKINGS];
        this.orBookingCount = 0;
        this.surgeryOutcomes = new boolean[MAX_SURGERY_OUTCOMES];
        this.outcomeCount = 0;
    }

    // Binary search — match patients against sorted referral list before assigning
    @Override
    public void assignPatients(Patient[] patients) {
        if (patients == null) {
            return;
        }

        for (int i = 0; i < patients.length; i++) {
            if (patients[i] == null) {
                continue;
            }

            int patientID = patients[i].getPatientID();
            int index = binarySearchReferral(patientID, 0, referralCount - 1);
            if (index > -1) {
                addPatientToAssigned(patients[i]);
            }
        }
    }

    // Conflict check, then recursive OR booking — reserve OR slot only when schedule is clear
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
        int slotIndex = -1;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                slotIndex = i;
                break;
            }
        }
        if (slotIndex == -1) {
            System.out.println("Error: schedule at capacity.");
            return;
        }

        String date = appt.getDate().toISODateString();
        orScheduleFailed = false;
        scheduleOR(operatingRoom, date, formatTime(appt.getTime()), 0);

        if (orScheduleFailed) {
            return;
        }

        schedule[slotIndex] = appt;
    }

    // Arithmetic — per-procedure fees plus base salary for the pay period
    @Override
    public double calculatePay() {
        double baseSalary = FLAT_ANNUAL_SALARY / PAY_PERIODS_PER_YEAR;
        return (surgeriesCompleted * surgeryFeePerProcedure) + baseSalary;
    }

    // Loop + string build — surgical calendar with OR, procedure, and patient
    @Override
    public String getSchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("OR ").append(operatingRoom)
                .append(" | Specialty: ").append(specialtyArea).append("\n");

        Appointment[] schedule = getScheduleSlots();
        boolean hasAppointments = false;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                continue;
            }
            hasAppointments = true;

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
                    .append(" | OR ")
                    .append(operatingRoom)
                    .append(" | ")
                    .append(getProcedureLabel(schedule[i]))
                    .append(" | ")
                    .append(patientLabel)
                    .append("\n");
        }

        if (!hasAppointments) {
            sb.append("No scheduled surgeries.\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Surgeon\n");
        sb.append(getStaffID()).append("\n");
        sb.append(getName()).append("\n");
        sb.append(getExperience()).append("\n");
        sb.append(getSpecialization()).append("\n");
        sb.append(formatOffDaysForFile()).append("\n");
        sb.append(operatingRoom).append("\n");
        sb.append(surgeriesCompleted).append("\n");
        sb.append(specialtyArea).append("\n");
        sb.append(surgeryFeePerProcedure).append("\n");
        sb.append(":");
        return sb.toString();
    }

    /**
     * Performs a surgical procedure and records it on the patient history.
     *
     * @param p              patient receiving surgery
     * @param procedureName  name of the surgical procedure
     */
    public void performSurgery(Patient p, String procedureName) {
        performSurgery(p, procedureName, true);
    }

    /**
     * Performs a surgical procedure, records the outcome, and updates history.
     *
     * @param p              patient receiving surgery
     * @param procedureName  name of the surgical procedure
     * @param successful     whether the procedure completed successfully
     */
    public void performSurgery(Patient p, String procedureName, boolean successful) {
        if (p == null) {
            return;
        }
        p.addMedicalHistory(procedureName);
        surgeriesCompleted++;
        if (outcomeCount < surgeryOutcomes.length) {
            surgeryOutcomes[outcomeCount] = successful;
            outcomeCount++;
        }
    }

    /**
     * Adds a patient ID to the sorted referral list (called by Doctor.referPatient).
     *
     * @param patientID referred patient identifier
     * @return true if the referral was added or already present; false if the list is full
     */
    public boolean addReferral(int patientID) {
        int index = binarySearchReferral(patientID, 0, referralCount - 1);
        if (index > -1) {
            return true;
        }

        if (referralCount >= referralList.length) {
            System.out.println("Error: referral list at capacity.");
            return false;
        }

        int insertIndex = referralCount;
        for (int i = 0; i < referralCount; i++) {
            if (patientID < referralList[i]) {
                insertIndex = i;
                break;
            }
        }

        for (int i = referralCount; i > insertIndex; i--) {
            referralList[i] = referralList[i - 1];
        }
        referralList[insertIndex] = patientID;
        referralCount++;
        return true;
    }

    /**
     * Recursively checks OR bookings and reserves a slot when no conflict exists.
     *
     * @param room  operating room number
     * @param date  booking date in YYYY-MM-DD format
     * @param time  booking time as HH:MM string
     * @param index current index in the OR booking list
     */
    public void scheduleOR(int room, String date, String time, int index) {
        if (index >= orBookingCount) {
            if (orBookingCount >= MAX_OR_BOOKINGS) {
                System.out.println("Error: OR booking list at capacity.");
                orScheduleFailed = true;
                return;
            }
            orRooms[orBookingCount] = room;
            orDates[orBookingCount] = date;
            orTimes[orBookingCount] = parseTime(time);
            orBookingCount++;
            return;
        }

        if (orRooms[index] == room
                && orDates[index] != null
                && orDates[index].equals(date)
                && orTimes[index] == parseTime(time)) {
            System.out.println("Error: OR scheduling conflict.");
            orScheduleFailed = true;
            return;
        }

        scheduleOR(room, date, time, index + 1);
    }

    /**
     * Computes the percentage of surgeries recorded as successful.
     *
     * @return success rate as a percentage, or 0.0 when no outcomes are recorded
     */
    public double getSuccessRate() {
        if (outcomeCount == 0) {
            return 0.0;
        }

        // Loop + count — tally successful outcomes against total recorded surgeries
        int successful = 0;
        for (int i = 0; i < outcomeCount; i++) {
            if (surgeryOutcomes[i]) {
                successful++;
            }
        }
        return ((double) successful / outcomeCount) * 100.0;
    }

    /** @return assigned operating room number */
    public int getOperatingRoom() {
        return operatingRoom;
    }

    /**
     * @param operatingRoom assigned operating room number to set
     */
    public void setOperatingRoom(int operatingRoom) {
        this.operatingRoom = operatingRoom;
    }

    /** @return total surgeries completed */
    public int getSurgeriesCompleted() {
        return surgeriesCompleted;
    }

    /**
     * @param surgeriesCompleted total surgeries completed to set
     */
    public void setSurgeriesCompleted(int surgeriesCompleted) {
        this.surgeriesCompleted = surgeriesCompleted;
    }

    /** @return surgical specialty area */
    public String getSpecialtyArea() {
        return specialtyArea;
    }

    /**
     * @param specialtyArea surgical specialty area to set
     */
    public void setSpecialtyArea(String specialtyArea) {
        this.specialtyArea = specialtyArea;
    }

    /** @return fee per surgical procedure */
    public double getSurgeryFeePerProcedure() {
        return surgeryFeePerProcedure;
    }

    /**
     * @param surgeryFeePerProcedure fee per surgical procedure to set
     */
    public void setSurgeryFeePerProcedure(double surgeryFeePerProcedure) {
        this.surgeryFeePerProcedure = surgeryFeePerProcedure;
    }

    /**
     * Returns a defensive copy of assigned patients.
     *
     * @return copy of patients assigned to this surgeon
     */
    public Patient[] getPatientsAssigned() {
        Patient[] copy = new Patient[patientsAssigned.length];
        for (int i = 0; i < patientsAssigned.length; i++) {
            copy[i] = patientsAssigned[i];
        }
        return copy;
    }

    private int binarySearchReferral(int patientID, int low, int high) {
        if (referralCount == 0 || low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (referralList[mid] == patientID) {
            return mid;
        }
        if (patientID < referralList[mid]) {
            return binarySearchReferral(patientID, low, mid - 1);
        }
        return binarySearchReferral(patientID, mid + 1, high);
    }

    private boolean addPatientToAssigned(Patient p) {
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == p) {
                return true;
            }
        }
        for (int i = 0; i < patientsAssigned.length; i++) {
            if (patientsAssigned[i] == null) {
                patientsAssigned[i] = p;
                return true;
            }
        }
        System.out.println("Error: surgeon patient list at capacity.");
        return false;
    }

    private String getProcedureLabel(Appointment appt) {
        return "Surgery (Appt #" + appt.getApptID() + ")";
    }

    private double parseTime(String time) {
        if (time == null || !time.contains(":")) {
            return 0.0;
        }
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours + (minutes / 100.0);
    }

}
