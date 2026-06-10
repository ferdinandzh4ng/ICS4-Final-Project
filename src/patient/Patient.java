package patient;

import appointment.Appointment;
import shared.Date;
import staff.Doctor;
import staff.Staff;

/**
 * File: Patient.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 2, 2026
 * Description: This class represents a patient in a hospital.
 */

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
        if (numOHIP < 0 || !isValidOHIP(numOHIP)) {
            this.numOHIP = 0;
        }
        this.dateRegistered = dateRegistered;
        this.gender = gender;
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
        this.assignedStaff = assignedStaff;
        diagnosis = new String[100]; // Assuming a maximum of 100 entries for diagnosis
        medications = new Medication[100]; // Assuming a maximum of 100 entries for medications
        allergies = new String[100]; // Assuming a maximum of 100 entries for allergies
        medicalHistory = new String[100]; // Assuming a maximum of 100 entries for medical
        familyHistory = new String[100]; // Assuming a maximum of 100 entries for family history
        pastAppointments = new Appointment[100]; // Assuming a maximum of 100 entries for past
        upcomingAppointments = new Appointment[100]; // Assuming a maximum of 100 entries for upcoming appointments
    }

    /**
     * Returns the ID of the patient
     * @return the patient ID
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * Returns the date when the patient was registered
     * @return the date registered
     */
    public Date getDateRegistered() {
        return dateRegistered;
    }

    /**
     * Returns the first name of the patient
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the patient
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the date of birth of the patient
     * @return the date of birth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Returns the ward of the patient
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the address of the patient
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the phone number of the patient
     * @return the phone number
     */
    public int getPhoneNum() {
        return phoneNum;
    }

    /**
     * Returns the OHIP number of the patient
     * @return the OHIP number
     */
    public int getNumOHIP() {
        return numOHIP;
    }

    /**
     * Returns the gender of the patient
     * @return the gender
     */
    public char getGender() {
        return gender;
    }

    /**
     * Returns the emergency contact phone number of the patient
     * @return the emergency contact phone number
     */
    public int getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber;
    }

    /**
     * Returns the staff member assigned to the patient
     * @return the assigned staff member
     */
    public Staff getAssignedStaff() {
        return assignedStaff;
    }

    /**
     * Returns the patient's diagnosis list
     * @return the diagnosis array
     */
    public String[] getDiagnosis() {
        return diagnosis;
    }

    /**
     * Returns the patient's medication list
     * @return the medications array
     */
    public Medication[] getMedications() {
        return medications;
    }

    /**
     * Returns the patient's allergy list
     * @return the allergies array
     */
    public String[] getAllergies() {
        return allergies;
    }

    /**
     * Returns the patient's medical history
     * @return the medical history array
     */
    public String[] getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * Returns the patient's family history
     * @return the family history array
     */
    public String[] getFamilyHistory() {
        return familyHistory;
    }

    /**
     * Returns the patient's past appointments
     * @return the past appointments array
     */
    public Appointment[] getPastAppointments() {
        return pastAppointments;
    }

    /**
     * Returns the patient's upcoming appointments
     * @return the upcoming appointments array
     */
    public Appointment[] getUpcomingAppointments() {
        return upcomingAppointments;
    }

    /**
     * Sets the first name of the patient
     * @param firstName the first name to be assigned to the patient
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the patient
     * @param lastName the last name to be assigned to the patient
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the date of birth of the patient
     * @param dateOfBirth the date of birth to be assigned to the patient
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the address of the patient
     * @param address the address to be assigned to the patient
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the phone number of the patient
     * @param phoneNum the phone number to be assigned to the patient
     */
    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * Sets the OHIP number of the patient
     * @param numOHIP the OHIP number to be assigned to the patient
     */
    public void setNumOHIP(int numOHIP) {
        this.numOHIP = numOHIP;
    }

    /**
     * Sets the date registered of the patient
     * @param dateRegistered the date registered to be assigned to the patient
     */
    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    /**
     * Sets the gender of the patient
     * @param gender the gender to be assigned to the patient
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Sets the emergency contact phone number of the patient
     * @param emergencyContactPhoneNumber the emergency contact phone number to be assigned to the patient
     */
    public void setEmergencyContactPhoneNumber(int emergencyContactPhoneNumber) {
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
    }

    /**
     * Sets the staff member assigned to the patient
     * @param assignedStaff the staff member to be assigned to the patient
     */
    public void setAssignedStaff (Staff assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    /**
     * Sets the ward assigned to the patient
     * @param ward the ward to be assigned to the patient
     */
    public void setWard (String ward) {
        this.ward = ward;
    }

    /**
     * Determines the index of the specified medication name in the medications array
     * @param medName the name of the medication
     * @return the index in the medications array
     */
    private int getIndexOfMedicationByName (String medName) {
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
    private int getIndexOfAllergyByName (String allergy) {
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

    /**
     * Adds a medical history entry for the patient
     * @param history the medical history entry to be added
     */
    public void addMedicalHistory (String history) {
        this.medicalHistory[this.medicalHistory.length] = history;
    }

    /**
     * Deletes a medical history entry for the patient
     * @param history the medical history entry to be deleted
     * @return boolean if the entry is found and deleted
     */
    public boolean deleteMedicalHistory (String history) {
        int index = -1;
        boolean found = false;

        for (int i = 0; i < medicalHistory.length && !found; i++) {
            if (medicalHistory[i] != null && history.equals(medicalHistory[i])) {
                index = i;
                found = true;
            }
        }

        if (index != -1) {
            for (int i = index; i < medicalHistory.length - 1; i++) {
                medicalHistory[i] = medicalHistory[i + 1];
            }
            medicalHistory[medicalHistory.length - 1] = null;
            return true;
        }

        return false;
    }

    /**
     * Adds a family history entry for the patient
     * @param familyHistory the family history entry to be added
     */
    public void addFamilyHistory (String history) {
        this.familyHistory[this.familyHistory.length] = history;
    }

    /**
     * Deletes a family history entry for the patient
     * @param history the family history entry to be deleted
     * @return boolean if the entry is found and deleted
     */
    public boolean deleteFamilyHistory (String history) {
        int index = -1;
        boolean found = false;

        for (int i = 0; i < familyHistory.length && !found; i++) {
            if (familyHistory[i] != null && history.equals(familyHistory[i])) {
                index = i;
                found = true;
            }
        }

        if (index != -1) {
            for (int i = index; i < familyHistory.length - 1; i++) {
                familyHistory[i] = familyHistory[i + 1];
            }
            familyHistory[familyHistory.length - 1] = null;
            return true;
        }

        return false;
    }

    /**
     * Determines the index of the specified appointment ID in the pastAppointments array
     * @param apptID the ID of the appointment
     * @return the index in the pastAppointments array
     */
    public int getIndexOfApptByIDPast (int apptID) {
        for (int i = 0; i < pastAppointments.length; i++) {
            if (pastAppointments[i] != null && apptID == pastAppointments[i].getApptID()) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Determines the index of the specified appointment ID in the upcomingAppointments array
     * @param apptID the ID of the appointment
     * @return the index in the upcomingAppointments array
     */
    public int getIndexOfApptByIDUpcoming (int apptID) {
        for (int i = 0; i < upcomingAppointments.length; i++) {
            if (upcomingAppointments[i] != null && apptID == upcomingAppointments[i].getApptID()) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Determines the Appointment of the specified Date in the pastAppointments array
     * @param dateToFind the Date to find the appointment
     * @return the Appointment on the date
     */
    public Appointment getApptByDatePast (Date dateToFind) {
        for (int i = 0; i < pastAppointments.length; i++) {
            if (pastAppointments[i] != null && dateToFind.equals(pastAppointments[i].getDate())) {
                return pastAppointments[i];
            }
        }

        return null;
    }

    /**
     * Determines the Appointment of the specified Date in the upcomingAppointments array
     * @param dateToFind the Date to find the appointment
     * @return the Appointment on the date
     */
    public Appointment getApptByDateUpcoming (Date dateToFind) {
        for (int i = 0; i < upcomingAppointments.length; i++) {
            if (upcomingAppointments[i] != null && dateToFind.equals(upcomingAppointments[i].getDate())) {
                return upcomingAppointments[i];
            }
        }

        return null;
    }

    /**
     * Returns the first doctor from the completed appointment staff list.
     * If no doctor exists in the staff list, returns a placeholder doctor.
     * @param completed the completed appointment used to find a doctor
     * @return the doctor for the follow-up appointment
     */
    public Doctor getFollowUpDoctor(Appointment completed) {
        if (completed != null) {
            Staff[] staffList = completed.getStaffList();
            if (staffList != null) {
                for (Staff staff : staffList) {
                    if (staff instanceof Doctor) {
                        return (Doctor) staff;
                    }
                }
            }
        }

        return new Doctor(
            "0000",
            "Placeholder Doctor",
            0,
            "General",
            new String[0],
            new Appointment[0],
            "N/A",
            0.0,
            0);
    }

    /**
     * Adds the specified appointment to the pastAppointments or upcomingAppointments array depending on the status of the appointment
     * @param appt the appointment to be added
     */
    public void addAppointment (Appointment appt) {
        if (appt.getStatus().equals("future")) {
            for (int i = 0; i < upcomingAppointments.length; i++) {
                if (upcomingAppointments[i] == null) {
                    upcomingAppointments[i] = appt;
                    return;
                }
            }
        } else if (appt.getStatus().equals("past")) {
            for (int i = 0; i < pastAppointments.length; i++) {
                if (pastAppointments[i] == null) {
                    pastAppointments[i] = appt;
                    return;
                }
            }
        }
    }

    /**
     * Deletes the specified appointment from the pastAppointments or upcomingAppointments array depending on the status of the appointment
     * @param appt the appointment to be deleted
     * @return boolean if the appointment is found and deleted
     */
    public boolean deleteAppointment (Appointment appt) {
        int indexPast = getIndexOfApptByIDPast(appt.getApptID());
        int indexUpcoming = getIndexOfApptByIDUpcoming(appt.getApptID());

        if (indexPast != -1) {
            for (int i = indexPast; i < pastAppointments.length - 1; i++) {
                pastAppointments[i] = pastAppointments[i + 1];
            }
            pastAppointments[pastAppointments.length - 1] = null;
            return true;
        } else if (indexUpcoming != -1) {
            for (int i = indexUpcoming; i < upcomingAppointments.length - 1; i++) {
                upcomingAppointments[i] = upcomingAppointments[i + 1];
            }
            upcomingAppointments[upcomingAppointments.length - 1] = null;
            return true;
        }

        return false;
    }

    /**
     * Updates the specified appointment in the pastAppointments or upcomingAppointments array depending on the status of the appointment
     * @param orgAppointment the original appointment
     * @param newAppointment the new appointment
     * @return boolean if the appointment is found and updated
     */
    public boolean updateAppointment (Appointment orgAppointment, Appointment newAppointment) {
        int indexPast = getIndexOfApptByIDPast(orgAppointment.getApptID());
        int indexUpcoming = getIndexOfApptByIDUpcoming(orgAppointment.getApptID());

        if (indexPast != -1) {
            pastAppointments[indexPast] = newAppointment;
            return true;
        } else if (indexUpcoming != -1) {
            upcomingAppointments[indexUpcoming] = newAppointment;
            return true;
        }

        return false;
    }

    /**
     * Adds a diagnosis entry for the patient
     * @param diagnosis the diagnosis entry to be added
     */
    public void addDiagnoses (String diagnosis) {
        this.diagnosis[this.diagnosis.length] = diagnosis;
    }

    /**
     * Deletes a diagnosis entry for the patient
     * @param diagnosis the diagnosis entry to be deleted
     * @return boolean if the diagnosis is found and deleted
     */
    public boolean deleteDiagnoses (String diagnosis) {
        int index = -1;
        boolean found = false;

        for (int i = 0; i < this.diagnosis.length && !found; i++) {
            if (this.diagnosis[i] != null && diagnosis.equals(this.diagnosis[i])) {
                index = i;
                found = true;
            }
        }

        if (index != -1) {
            for (int i = index; i < this.diagnosis.length - 1; i++) {
                this.diagnosis[i] = this.diagnosis[i + 1];
            }
            this.diagnosis[this.diagnosis.length - 1] = null;
            return true;
        }

        return false;
    }
    
    /**
     * Updates a diagnosis entry for the patient
     * @param diagnosis the original diagnosis entry
     * @param newDiagnosis the new diagnosis entry
     * @return boolean if the diagnosis is found and updated
     */
    public boolean updateDiagnoses (String diagnosis, String newDiagnosis) {
        int index = -1;
        boolean found = false;

        for (int i = 0; i < this.diagnosis.length && !found; i++) {
            if (this.diagnosis[i] != null && diagnosis.equals(this.diagnosis[i])) {
                index = i;
                found = true;
            }
        }

        if (index != -1) {
            this.diagnosis[index] = newDiagnosis;
            return true;
        }

        return false;
    }

    /**
     * Checks if the provided OHIP number is valid
     * @param numOHIP the OHIP number to be validated
     * @return boolean if the OHIP number is valid
     */
    public static boolean isValidOHIP (int numOHIP) {
        String numOHIPStr = Integer.toString(numOHIP);
        
        return numOHIPStr.length() == 10;
    }

    /**
     * Checks if the provided first name and last name match the patient's first name and last name
     * @param firstName the first name to be compared with the patient's first name
     * @param lastName the last name to be compared with the patient's last name
     * @return boolean if the provided first name and last name match the patient's first name and last name
     */
    public boolean equalsName (String firstName, String lastName) {
        return this.firstName.equals(firstName) && this.lastName.equals(lastName);
    }

    /**
     * Checks if the patient has been seen by the specified staff member in any of their past or upcoming appointments
     * @param staff the staff member to be checked if they have seen the patient
     * @return boolean if the patient has been seen by the specified staff member in any of their past or upcoming appointments
     */
    public boolean wasSeenByStaff (Staff staff) {
        for (int i = 0; i < pastAppointments.length; i++) {
            if (pastAppointments[i] != null && pastAppointments[i].getStaffList().equals(staff)) {
                return true;
            }
        }

        for (int i = 0; i < upcomingAppointments.length; i++) {
            if (upcomingAppointments[i] != null && upcomingAppointments[i].getStaffList().equals(staff)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Moves the specified appointment from the upcomingAppointments array to the pastAppointments array
     * @param appt the appointment to be moved to the patient's history
     * @return boolean if the appointment is found in the upcomingAppointments array and moved to the pastAppointments array
     */
    public boolean addToHistory (Appointment appt) {
        int index = getIndexOfApptByIDUpcoming(appt.getApptID());

        if (index != -1) {
            deleteAppointment(appt);
            for (int i = 0; i < pastAppointments.length; i++) {
                if (pastAppointments[i] == null) {
                    pastAppointments[i] = appt;
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Returns the age of the patient
     * @return the age of the patient
     */
    public int getAge () {
        return dateOfBirth.yearDiff(PatientManager.CUR_DATE);
    }

    /**
     * Compares the date registered with another patient
     * @param other the other patient
     * @return the comparison result
     */
    public int compareToDateRegistered (Patient other) {
        return this.dateRegistered.compareTo(other.getDateRegistered());
    }

    /**
     * Returns a string representation of the patient
     * @return String the string representation of the patient
     */
    @Override
    public String toString () {
        return "Patient ID: " + patientID + "\nName: " + firstName + " " + lastName + "\nDate of Birth: " + dateOfBirth.toString() +
                "\nWard: " + ward + "\nAddress: " + address + "\nPhone Number: " + phoneNum + "\nOHIP Number: " + numOHIP +
                "\nDate Registered: " + dateRegistered.toString() + "\nGender: " + gender + "\nEmergency Contact Phone Number: " + 
                emergencyContactPhoneNumber;
                // + "\nAssigned Staff: " + assignedStaff.getName();
    }

    /**
     * Adds an appoingment to upcomingAppoinments
     * @param appt The appointment to be added
     */
    public void addUpcomingAppointment(Appointment appt) {
        for (int i = 0; i < upcomingAppointments.length; i++) {
            if (upcomingAppointments[i] == null) {
                upcomingAppointments[i] = appt;
                return;
            }
        }
    }

    /**
     * Calculates the total cost of all the patient's past appointments
     * @return double the total cost
     */
    public double calculateTotalCost() {
        double total = 0.0;
        
        for (int i = 0; i < pastAppointments.length; i++) {
            total += pastAppointments[i].calculateCost();
        }

        return total;
    }

    /**
     * Checks in the patient to the hospital and returns true if the patient is successfully checked in
     * @return boolean if the patient is successfully checked in
     */
    public abstract boolean checkIn();

    /**
     * Checks out the patient from the hospital and returns true if the patient is successfully checked out 
     * @return boolean if the patient is successfully checked out
     */
    public abstract boolean checkOut(String followUp);

    /**
     * Schedules the next routine checkup for the patient
     */
    public abstract void scheduleNextRoutineCheckup();

    /**
     * Schedules the next surgery for the patient
     */
    public abstract void scheduleNextSurgery();
}
