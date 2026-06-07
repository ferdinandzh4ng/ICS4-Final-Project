package patient;

public class PatientManager {
    private Patient[] patients; // Array to store patient records
    private int numPatients; // Number of patients currently stored
    private int maxPatients; // Maximum capacity of the patients array

    /**
     * Constructor for the PatientManager class
     * @param maxPatients the maximum number of patients that can be stored
     */
    public PatientManager(int maxPatients) {
        this.maxPatients = maxPatients;
        patients = new Patient[maxPatients];
        numPatients = 0;
    }

    /**
     * Searches for a patient by their ID
     * @param patientID the ID of the patient to search for
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByID(int patientID) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].getPatientID() == patientID) {
                return patients[i];
            }
        }
        return null; // Return null if patient not found
    }
}
