package patient;

import appointment.Appointment;
import shared.Date;
import staff.Staff;

public class InPatient extends Patient {
    private Date dayIn; // Date of admission to the hospital
    private Date dayOut; // Date of discharge from the hospital
    private int hospitalBedNumber; // The hospital bed number assigned to the patient
    private String[] vitalsLog; // An array to store the patient's vital signs recorded during their stay
    private String[] medicationsAdministered; // An array to store the medications administered to the patient during their stay

    public static final Date TODAY = new Date();

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
        this.hospitalBedNumber = -1;
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

    /**
     * Records the patient's vital signs, including heart rate and blood pressure, and stores them in the vitals log
     * @param heartRate the patient's heart rate to be recorded
     * @param bloodPressure the patient's blood pressure to be recorded
     */
    public void recordVitals (double heartRate, double bloodPressure) {
        vitalsLog[vitalsLog.length] = "Date: " + TODAY + ", Heart Rate: " + heartRate + ", Blood Pressure: " + bloodPressure;
    }

    /**
     * Logs the medications administered to the patient, including the medication name and the date it was administered, and stores them in the medications log
     * @param medName the name of the medication administered to the patient
     * @param dosage the dosage of the medication administered to the patient
     */
    public void logMedicationsAdministered (Medication med) {
        medicationsAdministered[medicationsAdministered.length] = "Date: " + TODAY + ", Medication: " + med.getMedName() + ", Dosage: " + med.getDosage();
    }

    /**
     * Checks in the patient
     * @return true if the patient is checked in successfully, false otherwise
     */
    @Override
    public boolean checkIn() {
        int availableBedNum = PatientManager.findAvailableBed();

        if (hospitalBedNumber != 0) {
            hospitalBedNumber = availableBedNum;
            dayIn = TODAY;
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
        int availableBedNum = PatientManager.findAvailableBed();

        if (hospitalBedNumber != 0) {
            hospitalBedNumber = availableBedNum;
            if (dayIn.isValid()) {
                this.dayIn = dayIn;
                return true;
            }
        }

        return false;
    }

    // find out how to determine the appointment to move or smth
    /*public boolean checkOut() {
        dayOut = TODAY;
        hospitalBedNumber = -1;
        scheduleNextAppointment();
        calculateBill();

    }*/

    /*public double calculateBill() {
        double total = 0.0;
        int totalDays = dayOut.daysBetween(dayIn);

        total += totalDays * ROOM_RATE;
    }*/

    /*public void scheduleNextAppointment () {
        
    }*/
}
