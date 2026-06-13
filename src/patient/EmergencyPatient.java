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
    public static final double ER_DAILY_RATE = 500.00;

    private int arrivalTime; // time of arrival at the hospital
    private Date dayIn; // Date of admission to the hospital
    private Date dayOut; // Date of discharge from the hospital
    private String presentingComplaint; // the main reason for the patient's visit to the emergency department
    private String arrivalType; // the mode of arrival (e.g., ambulance, walk-in, etc.)
    private String status; // the current status of the patient (e.g., waiting, being treated, discharged, etc.)

    /**
     * Constructor for creating an EmergencyPatient object with all fields.
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
     * @param arrivalTime The arrival time at the hospital.
     * @param dayIn The admission date.
     * @param dayOut The discharge date.
     * @param presentingComplaint The main reason for the visit.
     * @param arrivalType The mode of arrival.
     * @param status The current status of the patient.
     */
    public EmergencyPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, long phoneNum, String numOHIP, Date dateRegistered, char gender, long emergencyContactPhoneNumber, Staff assignedStaff, int arrivalTime, Date dayIn, Date dayOut, String presentingComplaint, String arrivalType, String status) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        this.arrivalTime = arrivalTime;
        this.dayIn = dayIn;
        this.dayOut = dayOut;
        this.presentingComplaint = presentingComplaint;
        this.arrivalType = arrivalType;
        this.status = status;
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
        status = "Discharged";

        Appointment todayAppt = getApptByDateUpcoming(PatientManager.CUR_DATE);
        if (todayAppt != null) {
            addToHistory(todayAppt);
        }

        calculateBill();

        if (followUp.equals("checkup")) {
            scheduleNextRoutineCheckup();
            return true;
        } else if (followUp.equals("surgery")) {
            scheduleNextSurgery();
            return true;
        } else if (followUp.equals("none")) {
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
        Appointment todayAppt = getApptByDatePast(PatientManager.CUR_DATE);
        Doctor doctor = null;
        if (todayAppt != null) {
            doctor = getFollowUpDoctor(todayAppt);
        }
        RoutineCheckup newAppt = new RoutineCheckup(
            (int)(Math.random() * 1000) + 9000,
            this,
            doctor != null ? new Staff[]{doctor} : null,
            PatientManager.CUR_DATE.addDays(7),
            9.0,
            0.5,
            0.0,
            Appointment.STATUS_SCHEDULED,
            1,
            doctor
        );
        addUpcomingAppointment(newAppt);
    }

    /**
     * Schedules a surgery appointment
     */
    @Override
    public void scheduleNextSurgery() {
        staff.Surgeon placeholderSurgeon = new staff.Surgeon(
            "TBD", "TBD", 0, "General",
            new String[0], new Appointment[0],
            1, 0, "General", 0.0
        );
        Staff[] surgeryStaff = new Staff[]{placeholderSurgeon};
        Surgery newAppt = new Surgery(
            (int)(Math.random() * 1000) + 9000,
            this,
            surgeryStaff,
            PatientManager.CUR_DATE.addDays(7),
            9.0,
            2.0,
            0.0,
            Appointment.STATUS_SCHEDULED,
            1,
            "none",
            0.0,
            "General",
            null
        );
        addUpcomingAppointment(newAppt);
    }
    
    /**
     * Returns the admitted date display
     * @return String the admitted date display
     */
    public String getAdmittedDateDisplay() {
        if (dayIn != null) {
            return dayIn.toISODateString();
        } else {
            return "N/A";
        }
    }

    @Override
    public double calculateBill() {
        double total = calculateTotalCost();
        if (dayIn != null) {
            Date endDate;
            if (dayOut != null && dayOut.getYear() != 0) {
                endDate = dayOut;
            } else {
                endDate = PatientManager.CUR_DATE;
            }
            int days = 0;
            Date current = dayIn;
            while (current.compareTo(endDate) < 0) {
                days++;
                current = current.addDays(1);
            }
            if (days < 1) {
                days = 1;
            }
            total += days * ER_DAILY_RATE;
        }
        if ("Critical".equals(status)) {
            total *= 1.5;
        }
        return total;
    }

    /**
     * Returns a string representation of the emergency patient.
     * @return A string containing the patient's information.
     */
    @Override
    public String toString () {
        String dayOutStr;
        if (dayOut == null || dayOut.getYear() == 0) {
            dayOutStr = "N/A";
        } else {
            dayOutStr = dayOut.toString();
        }
        return super.toString() + "\nArrival Time: " + arrivalTime + "\nDay In: " + dayIn
                + "\nDay Out: " + dayOutStr + "\nPresenting Complaint: " + presentingComplaint
                + "\nArrival Type: " + arrivalType + "\nStatus: " + status;
    }
}
