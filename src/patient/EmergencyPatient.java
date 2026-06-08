package patient;

import appointment.Appointment;
import appointment.RoutineCheckup;
import appointment.Surgery;
import shared.Date;
import staff.Doctor;
import staff.Staff;

/**
 * File: EmergencyPatient.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 4, 2026
 * Description: This class represents an emergency patient in a hospital.
 */

public class EmergencyPatient extends Patient {
    private int arrivalTime; // time of arrival at the hospital
    private Date dayIn; // Date of admission to the hospital
    private Date dayOut; // Date of discharge from the hospital
    private String presentingComplaint; // the main reason for the patient's visit to the emergency department
    private String arrivalType; // the mode of arrival (e.g., ambulance, walk-in, etc.)
    private String status; // the current status of the patient (e.g., waiting, being treated, discharged, etc.)

    /**
     * Constructor for creating an EmergencyPatient object.
     * @param patientID The unique identifier for the patient.
     * @param firstName The first name of the patient.
     * @param lastName The last name of the patient.
     * @param dateOfBirth The date of birth of the patient.
     * @param ward The ward to which the patient is assigned.
     * @param address The address of the patient.
     * @param phoneNum The phone number of the patient.
     * @param numOHIP The OHIP number of the patient.
     * @param dateRegistered The date when the patient was registered.
     * @param gender The gender of the patient.
     * @param emergencyContactPhoneNumber The phone number of the emergency contact.
     * @param assignedStaff The staff member assigned to the patient.
     */
    public EmergencyPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        arrivalTime = -1;
        dayIn = null;
        dayOut = null;
        presentingComplaint = "Unknown";
        arrivalType = "Unknown";
        status = "Not checked-in";
    }

    /**
     * Returns the arrival time of the emergency patient.
     * @return the arrival time
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the date the patient was admitted.
     * @return the date admitted
     */
    public Date getDayIn() {
        return dayIn;
    }

    /**
     * Returns the date the patient was discharged.
     * @return the discharge date
     */
    public Date getDayOut() {
        return dayOut;
    }

    /**
     * Returns the presenting complaint of the emergency patient.
     * @return the presenting complaint
     */
    public String getPresentingComplaint() {
        return presentingComplaint;
    }

    /**
     * Returns the arrival type of the emergency patient.
     * @return the arrival type
     */
    public String getArrivalType() {
        return arrivalType;
    }

    /**
     * Returns the current status of the emergency patient.
     * @return the patient status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the arrival time of the emergency patient.
     * @param arrivalTime the arrival time to set
     */
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Sets the date the patient was admitted.
     * @param dayIn the admission date to set
     */
    public void setDayIn(Date dayIn) {
        this.dayIn = dayIn;
    }

    /**
     * Sets the date the patient was discharged.
     * @param dayOut the discharge date to set
     */
    public void setDayOut(Date dayOut) {
        this.dayOut = dayOut;
    }

    /**
     * Sets the presenting complaint of the emergency patient.
     * @param presentingComplaint the presenting complaint to set
     */
    public void setPresentingComplaint(String presentingComplaint) {
        this.presentingComplaint = presentingComplaint;
    }

    /**
     * Sets the arrival type of the emergency patient.
     * @param arrivalType the arrival type to set
     */
    public void setArrivalType(String arrivalType) {
        this.arrivalType = arrivalType;
    }

    /**
     * Sets the status of the emergency patient.
     * @param status The new status for the patient.
     */
    public void setStatus (String status) {
        this.status = status;
    }

    /**
     * Checks in the patient
     * @return true if the patient is checked in successfully
     */
    @Override
    public boolean checkIn() {
        arrivalTime = PatientManager.CUR_TIME;
        dayIn = PatientManager.CUR_DATE;
        status = "Awaiting triage";
        return true;
    }

    /**
     * Checks in the patient
     * @param arrivalType the mode of arrival of the patient
     * @return true if the patient is checked in successfully, false otherwise
     */
    public boolean checkIn(String arrivalType) {
        arrivalTime = PatientManager.CUR_TIME;
        dayIn = PatientManager.CUR_DATE;
        status = "Awaiting triage";
        this.arrivalType = arrivalType;
        return true;
    }

    /**
     * Checks in the patient
     * @param arrivalType the mode of arrival of the patient
     * @param presentingComplaint the main reason for the patient's arrival at the hospital
     * @return true if the patient is checked in successfully, false otherwise
     */
    public boolean checkIn(String arrivalType, String presentingComplaint) {
        this.arrivalType = arrivalType;
        this.presentingComplaint = presentingComplaint;
        arrivalTime = PatientManager.CUR_TIME;
        dayIn = PatientManager.CUR_DATE;
        status = "Awaiting triage";
        return true;
    }

    /**
     * Checks out the patient from the hospital
     * @param followUp the type of follow-up care the patient will receive
     * @return true if the patient is successfully checked out, false otherwise
     */
    @Override
    public boolean checkOut(String followUp) {
        dayOut = PatientManager.CUR_DATE;

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
        Doctor mainDoctorPlaceholder = getFollowUpDoctor(completed);
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

    /**
     * Returns a string representation of the emergency patient.
     * @return A string containing the patient's information.
     */
    @Override
    public String toString () {
        return super.toString() + "\nArrival Time: " + arrivalTime + "\nDay In: " + dayIn + "\nDay Out: " + dayOut + "\nPresenting Complaint: " + presentingComplaint + "\nArrival Type: " + arrivalType + "\nStatus: " + status;
    }
}
