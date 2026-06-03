package patient;

import appointment.Appointment;
import shared.Date;
import staff.Staff;

public abstract class Patient {
    private int patientID; // unique ID of the patient
    private String firstName; // first name of the patient
    private String lastName; // last name of the patient
    private Date dateOfBirth; // date of birth of the patient
    private String ward; // ward the patient is assigned to
    private String address; // home address of the patient
    private int phoneNum; // phone number of the patient
    private int numOHIP; // OHIP number of the patient
    private Date dateRegistered; // date the patient was registered in the hospital
    private char gender; // gender of the patient
    private int emergencyContactPhoneNumber; // phone number of the patient's emergency contact
    private Staff assignedStaff; // staff member assigned to the patient
    private String[] diagnosis; // diagnosis of the patient
    private Medication[] medications; // medications prescribed to the patient
    private String[] allergies; // allergies of the patient
    private String[] medicalHistory; // medical history of the patient
    private String[] familyHistory; // family medical history of the patient
    private Appointment[] pastAppointments; // past appointments of the patient
    private Appointment[] upcomingAppointments; // upcoming appointments of the patient

    /**
     * Constructor for the Patient class
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
     */
    public Patient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum,
                    int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName; 
        this.dateOfBirth = dateOfBirth;
        this.ward = ward;
        this.address = address;
        this.phoneNum = phoneNum;
        this.numOHIP = numOHIP;
        this.dateRegistered = dateRegistered;
        this.gender = gender;
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
        this.assignedStaff = assignedStaff;
    }

    /**
     * Determines the index of the specified medication name in the medications array
     * @param medName the name of the medication
     * @return the index in the medications array
     */
    public int getIndexOfMedicationByName (String medName) {
        for (int i = 0; i < medications.length; i++) {
            if (medications[i] != null && medName.equals(medications[i].getMedName())) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Adds the name and dosage of the prescribed medication into medications
     * @param medName the name of the medciation
     * @param dosage the dosage of the medication
     */
    public void addMedication (String medName, String dosage) {
        medications[medications.length] = new Medication(medName, dosage);
    }

    /**
     * Deletes the Medication from medications specified by the medication name
     * @param medName the name of the medication to be deleted
     * @return boolean if the medication is found
     */
    public boolean deleteMedication (String medName) {
        int index = getIndexOfMedicationByName(medName);

        if (index != -1) {
            for (int i = index; i < medications.length - 1; i++) {
                medications[i] = medications[i + 1];
            }
            medications[medications.length - 1] = null;
            return true;
        }

        return false;
    }

    /**
     * Updates the name and dosage of the specified medication
     * @param medName the name of the medication to be updated
     * @param newMedName the new name of the medication
     * @param newDosage the new dosage of the medication
     * @return boolean if the medication is found and updated
     */
    public boolean updateMedication (String medName, String newMedName, String newDosage) {
        int index = getIndexOfMedicationByName(medName);

        if (index != -1) {
            medications[index] = new Medication(newMedName, newDosage);
            return true;
        }

        return false;
    }

    /**
     * Determines the index of the specified allergy name in the allergies array
     * @param allergy the name of the allergy
     * @return the index in the allergies array
     */
    public int getIndexOfAllergyByName (String allergy) {
        for (int i = 0; i < allergies.length; i++) {
            if (allergies[i] != null && allergy.equals(allergies[i])) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Adds the name of the prescribed allergy into allergies
     * @param allergy the name of the allergy
     */
    public void addAllergy (String allergy) {
        allergies[allergies.length] = allergy;
    }

    /**
     * Deletes the allergy from allergies specified by the allergy name
     * @param allergy the name of the allergy to be deleted
     * @return boolean if the allergy is found
     */
    public boolean deleteAllergy (String allergy) {
        int index = getIndexOfAllergyByName(allergy);

        if (index != -1) {
            for (int i = index; i < allergies.length - 1; i++) {
                allergies[i] = allergies[i + 1];
            }
            allergies[allergies.length - 1] = null;
            return true;
        }

        return false;
    }

    /**
     * Updates the name of the specified allergy
     * @param allergy the name of the allergy to be updated
     * @param newAllergy the new name of the allergy
     * @return boolean if the allergy is found and updated
     */
    public boolean updateAllergy (String allergy, String newAllergy) {
        int index = getIndexOfAllergyByName(allergy);

        if (index != -1) {
            allergies[index] = newAllergy;
            return true;
        }

        return false;
    }

    /**
     * Checks if the specified medication name conflicts with any of the patient's allergies
     * @param medName the name of the medication to be checked for allergy conflict
     * @return boolean if the patient has an allergy conflict
     */
    public boolean checkAllergyConflict (String medName) {
        int index = getIndexOfAllergyByName(medName);

        return index != -1;
    }
}
