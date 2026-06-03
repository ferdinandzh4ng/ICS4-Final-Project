package patient;

import appointment.Appointment;
import shared.Date;
import staff.Staff;

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
        presentingComplaint = "";
        arrivalType = "";
        status = "not checked-in";
    }

    /**
     * Sets the status of the emergency patient.
     * @param status The new status for the patient.
     */
    public void setStatus (String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the emergency patient.
     * @return A string containing the patient's information.
     */
    public String toString () {
        return super.toString() + "\nArrival Time: " + arrivalTime + "\nDay In: " + dayIn + "\nDay Out: " + dayOut + "\nPresenting Complaint: " + presentingComplaint + "\nArrival Type: " + arrivalType + "\nStatus: " + status;
}
