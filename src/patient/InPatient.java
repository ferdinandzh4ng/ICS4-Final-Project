package patient;

import appointment.Appointment;
import shared.Date;
import staff.Staff;

public class InPatient extends Patient {
    private String dayIn; // Date of admission to the hospital
    private String dayOut; // Date of discharge from the hospital
    private int hospitalBedNumber; // The hospital bed number assigned to the patient
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
    public InPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff, String dayIn) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        this.dayIn = dayIn;
        this.dayOut = null;
        this.hospitalBedNumber = 0;
        vitalsLog = new String[100]; // Assuming a maximum of 100 entries for vital signs
        medicationsAdministered = new String[100]; // Assuming a maximum of 100 entries
    }

    /**
     * Gets the hospital bed number assigned to the patient
     * @return the hospital bed number
     */
    public int getHospitalBedNumber() {
        return hospitalBedNumber;
    }

    /**
     * Sets the hospital bed number assigned to the patient
     * @param hospitalBedNumber the hospital bed number to be assigned to the patient
     */
    public void setHospitalBedNumber(int hospitalBedNumber) {
        this.hospitalBedNumber = hospitalBedNumber;
    }

    /**
     * Checks if the patient is assigned to a specific hospital bed
     * @param bedNumber the hospital bed number to check
     * @return true if the patient is assigned to the specified bed, false otherwise
     */
    public boolean equalsBed (int bedNumber) {
        return this.hospitalBedNumber == bedNumber;
    }
    
    /**
     * Returns a string representation of the InPatient object, including the patient's information and hospital stay details
     * @return a string representation of the InPatient object
     */
    public String toString () {
        return super.toString() + "\nDay In: " + dayIn + "\nDay Out: " + dayOut + "\nHospital Bed Number: " + hospitalBedNumber;
    }
}
