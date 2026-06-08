package patient;

import appointment.Appointment;
import appointment.RoutineCheckup;
import appointment.Surgery;
import shared.Date;
import staff.Doctor;
import staff.Staff;

/**
 * File: InPatient.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 6, 2026
 * Description: This class represents an in patient in a hospital.
 */

public class InPatient extends Patient {
    private Date dayIn; // Date of admission to the hospital
    private Date dayOut; // Date of discharge from the hospital
    private boolean hospitalBed; // If a hospital bed is assigned to a patient
    private String[] vitalsLog; // An array to store the patient's vital signs recorded during their stay
    private String[] medicationsAdministered; // An array to store the medications administered to the patient during their stay

    /**
     * Constructor for the InPatient class
     * @param patientID to be assigned to the patient
     * @param firstName to be assigned to the patient
     * @param lastName to be assigned to the patient
     * @param dateOfBirth to be assigned to the patient
     * @param ward to be assigned to the patient
     * @param address to be assigned to the patient
     * @param phoneNum to be assigned to the patient
     * @param numOHIP to be assigned to the patient
     * @param dateRegistered to be assigned to the patient
     * @param gender to be assigned to the patient
     * @param emergencyContactPhoneNumber to be assigned to the patient
     * @param assignedStaff to be assigned to the patient
     * @param dayIn the date of admission to the hospital
     */
    public InPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        this.dayIn = null;
        this.dayOut = null;
        this.hospitalBed = false;
        vitalsLog = new String[100]; // Assuming a maximum of 100 entries for vital signs
        medicationsAdministered = new String[100]; // Assuming a maximum of 100 entries
    }

    /**
     * Gets the date that the patient entered the hospital
     * @return the day in
     */
    public Date getDayIn() {
        return dayIn;
    }

    /**
     * Gets the date that the patient left the hospital
     * @return the day out
     */
    public Date getDayOut() {
        return dayOut;
    }

    /**
     * Gets the hospital bed number assigned to the patient
     * @return the hospital bed number
     */
    public boolean getHospitalBed() {
        return hospitalBed;
    }

    /**
     * Gets the patient's vitals log
     * @return the vitals log
     */
    public String[] getVitalsLog() {
        return vitalsLog;
    }

    /**
     * Gets the patient's medication administered list
     * @return the medications administered
     */
    public String[] getMedicationsAdministered() {
        return medicationsAdministered;
    }

    /**
     * Sets the date that the patient entered the hospital
     * @param the day in
     */
    public void setDayIn(Date dayIn) {
        this.dayIn = dayIn;
    }

    /**
     * Sets the date that the patient left the hospital
     * @param dayOut the day out
     */
    public void setDayOut (Date dayOut) {
        this.dayOut = dayOut;
    }

    /**
     * Sets whether the patient currently has a hospital bed assigned
     * @param hospitalBed true if a bed is assigned, false otherwise
     */
    public void setHospitalBed(boolean hospitalBed) {
        this.hospitalBed = hospitalBed;
    }
    
    /**
     * Returns a string representation of the InPatient object, including the patient's information and hospital stay details
     * @return a string representation of the InPatient object
     */
    @Override
    public String toString () {
        return super.toString() + "\nDay In: " + dayIn + "\nDay Out: " + dayOut + "\nHospital Bed Number: " + hospitalBed;
    }

    /**
     * Records the patient's vital signs, including heart rate and blood pressure, and stores them in the vitals log
     * @param heartRate the patient's heart rate to be recorded
     * @param bloodPressure the patient's blood pressure to be recorded
     */
    public void recordVitals (double heartRate, double bloodPressure) {
        vitalsLog[vitalsLog.length] = "Date: " + PatientManager.CUR_DATE + ", Heart Rate: " + heartRate + ", Blood Pressure: " + bloodPressure;
    }

    /**
     * Logs the medications administered to the patient, including the medication name and the date it was administered, and stores them in the medications log
     * @param medName the name of the medication administered to the patient
     * @param dosage the dosage of the medication administered to the patient
     */
    public void logMedicationsAdministered (Medication med) {
        medicationsAdministered[medicationsAdministered.length] = "Date: " + PatientManager.CUR_DATE + ", Medication: " + med.getMedName() + ", Dosage: " + med.getDosage();
    }

    /**
     * Checks in the patient
     * @return true if the patient is checked in successfully, false otherwise
     */
    @Override
    public boolean checkIn() {
        Appointment set = getApptByDateUpcoming(PatientManager.CUR_DATE);
        if (set != null) {
            hospitalBed = true;
            dayIn = PatientManager.CUR_DATE;
            return true;
        }
        return false;
    }

    /**
     * Checks in the patient with a specific date
     * @param dayIn the date when the patient is checked in
     * @return true if the patient is checked in successfully, false otherwise
     */
    public boolean checkIn(Date dayIn) {
        hospitalBed = true;
        if (dayIn.isValid()) {
            this.dayIn = dayIn;
            return true;
        }

        return false;
    }

    /**
     * Checks out the patient from the hospital
     * @param followUp the type of follow-up care the patient will receive
     * @return true if the patient is successfully checked out, false otherwise
     */
    @Override
    public boolean checkOut(String followUp) {
        dayOut = PatientManager.CUR_DATE;
        hospitalBed = false;

        if (followUp.equals("checkup")) {
            scheduleNextRoutineCheckup();
            return true;
        } else if (followUp.equals("surgery")) {
            scheduleNextSurgery();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Schedules a routine checkup
     */
    @Override
    public void scheduleNextRoutineCheckup() {
        Appointment completed = getApptByDatePast(PatientManager.CUR_DATE);
        Doctor mainDoctorPlaceholder = new Doctor();
        Appointment newAppt = new RoutineCheckup(
            completed.getApptID() + 1,
            completed.getPatient(),
            completed.getStaffList(),
            PatientManager.CUR_DATE.addDays(1),
            completed.getTime(),
            completed.getDuration(),
            completed.getCost(),
            "future",
            1,
            mainDoctorPlaceholder);
        boolean validated = false;
        int dayCounter = 2;

        while (!validated) {
            if (newAppt.validateBooking()) {
                validated = true;
            } else {
                newAppt.setDate(PatientManager.CUR_DATE.addDays(dayCounter));
                dayCounter++;
            }
        }

        addUpcomingAppointment(newAppt);
    }

    /**
     * Shedules a surgery appointment
     */
    @Override
    public void scheduleNextSurgery () {
        Appointment completed = getApptByDatePast(PatientManager.CUR_DATE);
        Appointment newAppt = new Surgery(
            completed.getApptID() + 1,
            completed.getPatient(),
            completed.getStaffList(),
            PatientManager.CUR_DATE.addDays(1),
            completed.getTime(),
            completed.getDuration(),
            completed.getCost(),
            "future",
            1,
            "none",
            0.0,
            "general",
            null
        );
        boolean validated = false;
        int dayCounter = 2;

        while (!validated) {
            if (newAppt.validateBooking()) {
                validated = true;
            } else {
                newAppt.setDate(PatientManager.CUR_DATE.addDays(dayCounter));
                dayCounter++;
            }
        }

        addUpcomingAppointment(newAppt);
    }
}
