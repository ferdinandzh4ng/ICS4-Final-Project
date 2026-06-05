package patient;

import shared.Date;
import staff.Staff;

public class PatientManager {
    private Patient[] patients; // Array to store patient records
    private int numPatients; // Number of patients currently stored
    private int maxPatients; // Maximum capacity of the patients array
    public final static Date CUR_DATE = new Date (2026, 06, 04);

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
        return searchPatientByID(patientID, 0, numPatients - 1);
    }

    /**
     * Searches for the index of a patient by their ID
     * @param patientID the ID of the patient to search for
     * @return the index of the patient if found, -1 otherwise
     */
    public int searchPatientIndexByID(int patientID) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].getPatientID() == patientID) {
                return i; // Patient found, return index
            }
        }
        return -1; // Patient not found
    }

    /**
     * Registers a new in-patient
     * @param patientID the ID of the patient
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param ward the ward where the patient will be admitted
     * @param address the address of the patient
     * @param phoneNum the phone number of the patient
     * @param numOHIP the OHIP number of the patient
     * @param dateRegistered the date when the patient was registered
     * @param gender the gender of the patient
     * @param emergencyContactPhoneNumber the phone number of the emergency contact
     * @param assignedStaff the staff member assigned to the patient
     * @return boolean if the patient is successfully registered
     */
    public boolean registerInPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }

        Patient newPatient = new InPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        patients[numPatients] = newPatient;
        numPatients++;
        return true;
    }

    /**
     * Registers a new out-patient
     * @param patientID the ID of the patient
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param ward the ward where the patient will be admitted
     * @param address the address of the patient
     * @param phoneNum the phone number of the patient
     * @param numOHIP the OHIP number of the patient
     * @param dateRegistered the date when the patient was registered
     * @param gender the gender of the patient
     * @param emergencyContactPhoneNumber the phone number of the emergency contact
     * @param assignedStaff the staff member assigned to the patient
     * @return boolean if the patient is successfully registered
     */
    public boolean registerOutPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }

        Patient newPatient = new OutPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        patients[numPatients] = newPatient;
        numPatients++;
        return true;
    }

    /**
     * Registers a new emergency patient
     * @param patientID the ID of the patient
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param ward the ward where the patient will be admitted
     * @param address the address of the patient
     * @param phoneNum the phone number of the patient
     * @param numOHIP the OHIP number of the patient
     * @param dateRegistered the date when the patient was registered
     * @param gender the gender of the patient
     * @param emergencyContactPhoneNumber the phone number of the emergency contact
     * @param assignedStaff the staff member assigned to the patient
     * @return boolean if the patient is successfully registered
     */
    public boolean registerEmergencyPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }

        Patient newPatient = new EmergencyPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        patients[numPatients] = newPatient;
        numPatients++;
        return true;
    }

    /**
     * Searches for a patient by their ID using binary search
     * @param patientID the ID of the patient to search for
     * @param bottom the lower bound of the search range
     * @param top the upper bound of the search range
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByID (int patientID, int bottom, int top) {
        if (bottom > top) {
            return null;
        }

        int mid = (bottom + top) / 2;
        if (patients[mid].getPatientID() == patientID) {
            return patients[mid]; // Patient found
        } else if (patients[mid].getPatientID() < patientID) {
            return searchPatientByID(patientID, mid + 1, top); // Search in the upper half
        } else {
            return searchPatientByID(patientID, bottom, mid - 1); // Search in the lower half
        }
    }

    /**
     * Deletes a patient by their ID
     * @param patientID the ID of the patient to delete
     * @return boolean if the patient is successfully deleted
     */
    public boolean deletePatient (int patientID) {
        int index = searchPatientIndexByID(patientID);
        if (index == -1) {
            return false; // Patient not found
        }

        // Shift all patients after the found patient one position to the left
        for (int i = index; i < numPatients - 1; i++) {
            patients[i] = patients[i + 1];
        }
        patients[numPatients - 1] = null;
        numPatients--;
        return true;
    }

    /**
     * Adds a diagnosis to a patient
     * @param patientID the ID of the patient
     * @param diagnosis the diagnosis to add
     * @return boolean if the diagnosis is successfully added
     */
    public boolean addDiagnosis (int patientID, String diagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false; // Patient not found
        }

        patient.addDiagnoses(diagnosis);
        return true;
    }

    /**
     * Finds an available hospital bed number for an in-patient.
     * @return the available bed number, or -1 if no bed is available
     */
    public int findAvailableBed() {
        boolean[] usedBeds = new boolean[maxPatients];

        // fill usedBeds
        for (int i = 0; i < numPatients; i++) {
            if (patients[i] != null && patients[i] instanceof InPatient) {
                int bed = ((InPatient) patients[i]).getHospitalBedNumber();
                if (bed >= 0 && bed < maxPatients) {
                    usedBeds[bed] = true;
                }
            }
        }

        // find first unused bed
        for (int bed = 0; bed < maxPatients; bed++) {
            if (!usedBeds[bed]) {
                return bed;
            }
        }

        return -1;
    }
}
