package patient;

import appointment.Appointment;
import appointment.EmergencyVisit;
import appointment.RoutineCheckup;
import appointment.Surgery;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import shared.Date;
import staff.Staff;

/**
 * File: Patient.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 2, 2026
 * Description: This class manages the patients in a hopsital.
 */

public class PatientManager {
    private Patient[] patients; // Array to store patient records
    private int numPatients; // Number of patients currently stored
    private int maxPatients; // Maximum capacity of the patients array
    public final static Date CUR_DATE = new Date (2026, 06, 04);
    public final static int CUR_TIME = 1200;

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
     * Returns the array of patients managed by this PatientManager
     * @return the patients array
     */
    public Patient[] getPatients() {
        return patients;
    }

    /**
     * Returns the number of patients currently stored
     * @return the number of current patients
     */
    public int getNumPatients() {
        return numPatients;
    }

    /**
     * Returns the maximum capacity of the patients array
     * @return the maximum number of patients
     */
    public int getMaxPatients() {
        return maxPatients;
    }

    /**
     * Sets the array of patients managed by this PatientManager
     * @param patients the patients array to set
     */
    public void setPatients(Patient[] patients) {
        this.patients = patients;
    }

    /**
     * Sets the number of patients currently stored
     * @param numPatients the number of patients to set
     */
    public void setNumPatients(int numPatients) {
        this.numPatients = numPatients;
    }

    /**
     * Sets the maximum capacity of the patients array
     * @param maxPatients the maximum number of patients to set
     */
    public void setMaxPatients(int maxPatients) {
        this.maxPatients = maxPatients;
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
     * Loads patient records from the specified file.
     * The file format is: PatientType|patientID|firstName|lastName|dateOfBirth|ward|address|phoneNum|numOHIP|dateRegistered|gender|emergencyContactPhoneNumber
     * Date values use YYYY-MM-DD.
     * @param fileName the patient file path
     * @return true if load succeeds, false otherwise
     */
    public boolean loadPatientInfo(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            patients = new Patient[maxPatients];
            numPatients = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                Patient patient = parsePatientLine(line);
                if (patient != null && numPatients < maxPatients) {
                    patients[numPatients++] = patient;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves patient records to the specified file.
     * @param fileName the patient file path
     * @return true if save succeeds, false otherwise
     */
    public boolean savePatientInfo(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < numPatients; i++) {
                Patient patient = patients[i];
                if (patient == null) {
                    continue;
                }
                String line = String.join("|",
                    patient.getClass().getSimpleName(),
                    Integer.toString(patient.getPatientID()),
                    patient.getFirstName(),
                    patient.getLastName(),
                    formatDate(patient.getDateOfBirth()),
                    patient.getWard(),
                    patient.getAddress(),
                    Integer.toString(patient.getPhoneNum()),
                    Integer.toString(patient.getNumOHIP()),
                    formatDate(patient.getDateRegistered()),
                    Character.toString(patient.getGender()),
                    Integer.toString(patient.getEmergencyContactPhoneNumber())
                );
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Loads appointment records from the specified file and assigns them to matching patients.
     * The file format is: patientID|appointmentType|apptID|date|time|duration|cost|status|extra...
     * @param fileName the appointment file path
     * @return true if load succeeds, false otherwise
     */
    public boolean loadPatientAppts(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 8) {
                    continue;
                }
                int patientID = Integer.parseInt(parts[0]);
                int patientIndex = searchPatientIndexByID(patientID);
                if (patientIndex == -1) {
                    continue;
                }
                Patient patient = patients[patientIndex];
                Appointment appt = parseAppointmentLine(parts, patient);
                if (appt != null) {
                    patient.addAppointment(appt);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves all patients' upcoming and past appointment records to the specified file.
     * @param fileName the appointment file path
     * @return true if save succeeds, false otherwise
     */
    public boolean savePatientAppts(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < numPatients; i++) {
                Patient patient = patients[i];
                if (patient == null) {
                    continue;
                }

                Appointment[] upcoming = patient.getUpcomingAppointments();
                if (upcoming != null) {
                    for (Appointment appt : upcoming) {
                        if (appt != null) {
                            writer.write(serializeAppointmentLine(patient.getPatientID(), appt));
                            writer.newLine();
                        }
                    }
                }

                Appointment[] past = patient.getPastAppointments();
                if (past != null) {
                    for (Appointment appt : past) {
                        if (appt != null) {
                            writer.write(serializeAppointmentLine(patient.getPatientID(), appt));
                            writer.newLine();
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Parses a patient record line from the patient file.
     * @param line a pipe-delimited patient record line
     * @return a Patient instance if parsing succeeds, null otherwise
     */
    private Patient parsePatientLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 12) {
            return null;
        }

        String type = parts[0];
        int patientID = Integer.parseInt(parts[1]);
        String firstName = parts[2];
        String lastName = parts[3];
        Date dateOfBirth = parseDate(parts[4]);
        String ward = parts[5];
        String address = parts[6];
        int phoneNum = Integer.parseInt(parts[7]);
        int numOHIP = Integer.parseInt(parts[8]);
        Date dateRegistered = parseDate(parts[9]);
        char gender = parts[10].isEmpty() ? ' ' : parts[10].charAt(0);
        int emergencyContactPhoneNumber = Integer.parseInt(parts[11]);

        switch (type) {
            case "InPatient":
                return new InPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null);
            case "OutPatient":
                return new OutPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null);
            case "EmergencyPatient":
                return new EmergencyPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null);
            default:
                return null;
        }
    }

    /**
     * Parses an appointment record line from the appointment file.
     * @param parts the split appointment fields from a pipe-delimited line
     * @param patient the patient to associate with the appointment
     * @return an Appointment instance if parsing succeeds, null otherwise
     */
    private Appointment parseAppointmentLine(String[] parts, Patient patient) {
        if (parts.length < 8 || patient == null) {
            return null;
        }

        String type = parts[1];
        int apptID = Integer.parseInt(parts[2]);
        Date date = parseDate(parts[3]);
        double time = Double.parseDouble(parts[4]);
        double duration = Double.parseDouble(parts[5]);
        double cost = Double.parseDouble(parts[6]);
        String status = parts[7];

        switch (type) {
            case "RoutineCheckup":
                int clinicRoomNum = parts.length > 8 ? Integer.parseInt(parts[8]) : 0;
                return new RoutineCheckup(apptID, patient, null, date, time, duration, cost, status, clinicRoomNum, null);
            case "Surgery":
                int operatingRoomNum = parts.length > 8 ? Integer.parseInt(parts[8]) : 0;
                String anaesthesiaType = parts.length > 9 ? parts[9] : "";
                double anaesthesiaDose = parts.length > 10 ? Double.parseDouble(parts[10]) : 0.0;
                String surgeryType = parts.length > 11 ? parts[11] : "";
                String preOpInstructions = parts.length > 12 ? parts[12] : "";
                return new Surgery(apptID, patient, null, date, time, duration, cost, status, operatingRoomNum, anaesthesiaType, anaesthesiaDose, surgeryType, preOpInstructions);
            case "EmergencyVisit":
                int emergencyRoomNum = parts.length > 8 ? Integer.parseInt(parts[8]) : 0;
                int urgencyIdx = parts.length > 9 ? Integer.parseInt(parts[9]) : 0;
                return new EmergencyVisit(apptID, patient, null, date, time, duration, cost, status, emergencyRoomNum, urgencyIdx);
            default:
                return null;
        }
    }

    private String serializeAppointmentLine(int patientID, Appointment appt) {
        StringBuilder builder = new StringBuilder();
        builder.append(patientID).append("|")
               .append(appt.getClass().getSimpleName()).append("|")
               .append(appt.getApptID()).append("|")
               .append(formatDate(appt.getDate())).append("|")
               .append(appt.getTime()).append("|")
               .append(appt.getDuration()).append("|")
               .append(appt.getCost()).append("|")
               .append(appt.getStatus());

        if (appt instanceof RoutineCheckup) {
            RoutineCheckup routine = (RoutineCheckup) appt;
            builder.append("|").append(routine.getRoomNum()).append("|").append("0");
        } else if (appt instanceof Surgery) {
            Surgery surgery = (Surgery) appt;
            builder.append("|")
                   .append(surgery.getRoomNum()).append("|")
                   .append(surgery.getAnaesthesiaType()).append("|")
                   .append(surgery.getAnaesthesiaDose()).append("|")
                   .append(surgery.getType()).append("|")
                   .append("");
        } else if (appt instanceof EmergencyVisit) {
            EmergencyVisit emergency = (EmergencyVisit) appt;
            builder.append("|")
                   .append(emergency.getRoomNum()).append("|")
                   .append(emergency.getUrgencyIdx());
        }

        return builder.toString();
    }

    /**
     * Parses a date string from the file into a Date object.
     * @param token the date token in YYYY-MM-DD format
     * @return a Date object representing the parsed date
     */
    private Date parseDate(String token) {
        String[] parts = token.split("-");
        if (parts.length != 3) {
            return new Date(0, 0, 0);
        }
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return new Date(year, month, day);
    }

    /**
     * Formats a Date for file output in YYYY-MM-DD format.
     * @param date the Date object to format
     * @return the formatted date string
     */
    private String formatDate(Date date) {
        return date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
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
     * Searches for the patient ID of a patient by first and last name
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @return the patient ID if found, -1 otherwise
     */
    public int searchPatientIDByName(String firstName, String lastName) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].equalsName(firstName, lastName)) {
                return patients[i].getPatientID();
            }
        }
        return -1;
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
     * Searches for a patient by their ID using binary search
     * @param patientID the ID of the patient to search for
     * @param bottom the lower bound of the search range
     * @param top the upper bound of the search range
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByID (int patientID, int bottom, int top) {
        sortByPatientID();

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
     * Sorts the patients array by patient ID using a bubble sort
     */
    public void sortByPatientID() {
        boolean sorted;
        
        if (numPatients <= 1) {
            return;
        }

        for (int upperBound = numPatients - 1; upperBound > 0; upperBound--) {
            sorted = true;
            for (int j = 0; j < upperBound; j++) {
                if (patients[j].getPatientID() > patients[j + 1].getPatientID()) {
                    Patient temp = patients[j];
                    patients[j] = patients[j + 1];
                    patients[j + 1] = temp;
                    sorted = false;
                }
            }
            if (sorted) {
                break;
            }
        }
    }

    /**
     * Sorts the patients array by date registered using an insertion sort
     */
    public void sortByDateRegistered() {
        if (numPatients <= 1) {
            return;
        }

        for (int i = 1; i < numPatients; i++) {
            Patient itemToCompare = patients[i];
            int blankIndex = i;

            while (blankIndex > 0
                    && patients[blankIndex - 1].getDateRegistered().compareTo(itemToCompare.getDateRegistered()) > 0) {
                patients[blankIndex] = patients[blankIndex - 1];
                blankIndex--;
            }

            patients[blankIndex] = itemToCompare;
        }
    }

    /**
     * Sorts the patients array by ward registered using an insertion sort
     */
    public void sortByWard () {
        if (numPatients <= 1) {
            return;
        }

        for (int upperBound = numPatients - 1; upperBound > 0; upperBound--) {
            int maxIndex = 0;
            for (int j = 1; j <= upperBound; j++) {
                if (patients[j].getWard().compareToIgnoreCase(patients[maxIndex].getWard()) > 0) {
                    maxIndex = j;
                }
            }
            if (maxIndex != upperBound) {
                Patient temp = patients[maxIndex];
                patients[maxIndex] = patients[upperBound];
                patients[upperBound] = temp;
            }
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
     * Updates a patient by their ID
     * @param patientID the ID of the patient to update
     * @param updated the updated Patient information stored in a Patient object
     * @return boolean if the patient is successfully updated
     */
    public boolean updatePatient (int patientID, Patient updated) {
        int index = searchPatientIndexByID(patientID);
        if (index == -1) {
            return false;
        }

        patients[index] = updated;
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
     * Deletes a diagnosis to a patient
     * @param patientID the ID of the patient
     * @param diagnosis the diagnosis to delete
     * @return boolean if the diagnosis is successfully deleted
     */
    public boolean deleteDiagnosis (int patientID, String diagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false; // Patient not found
        }

        return patient.deleteDiagnoses(diagnosis);
    }

    /**
     * Updates a diagnosis to a patient
     * @param patientID the ID of the patient
     * @param orgDiagnosis the original diagnosis to change
     * @param newDiagnosis the updated diagnosis
     * @return boolean if the diagnosis is successfully deleted
     */
    public boolean deleteDiagnosis (int patientID, String orgDiagnosis, String newDiagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.updateDiagnoses(orgDiagnosis, newDiagnosis);
    }

    /**
     * Adds an appointment to a patient
     * @param patientID the ID of the patient
     * @param newAppt the appointment to be added
     * @return boolean if the appointment is successfully added
     */
    public boolean addAppointment (int patientID, Appointment newAppt) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.addAppointment(newAppt);
        return true;
    }

    /**
     * Deletes an appointment to a patient
     * @param patientID the ID of the patient
     * @param toDelete the appointment to delete
     * @return boolean if the appointment is successfully deleted
     */
    public boolean deleteAppointment (int patientID, Appointment toDelete) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.deleteAppointment(toDelete);
    }

    /**
     * Updates an appointment to a patient
     * @param patientID the ID of the patient
     * @param orgAppt the original appointment
     * @param newAppt the new appointment
     * @return boolean if the appointment is successfully deleted
     */
    public boolean updateAppointment (int patientID, Appointment orgAppt, Appointment newAppt) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.updateAppointment(orgAppt, newAppt);
    }

    /**
     * Adds a medication to a patient
     * @param patientID the ID of the patient
     * @param medName the name of the medication
     * @param dosage the dosage of the medication
     * @return boolean if the medication is successfully added
     */
    public boolean addMedication (int patientID, String medName, String dosage) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.addMedication(medName, dosage);
        return true;
    }

    /**
     * Deletes a medication to a patient
     * @param patientID the ID of the patient
     * @param medName the name of the medication
     * @return boolean if the medication is successfully deleted
     */
    public boolean deleteMedication (int patientID, String medName) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.deleteMedication(medName);
    }

    /**
     * Updates a medication to a patient
     * @param patientID the ID of the patient
     * @param medName the name of the original medication
     * @param newMed the name of the updated medication
     * @param newDosage the dosage of the updated medication
     * @return boolean if the medication is successfully updated
     */
    public boolean updateMedication (int patientID, String medName, String newMed, String newDosage) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.updateMedication(medName, newMed, newDosage);
    }

    /**
     * Adds an allergy to a patient
     * @param patientID the ID of the patient
     * @param newAllergy the allergy to be added
     * @return boolean if the allergy is successfully added
     */
    public boolean addAllergy (int patientID, String newAllergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.addAllergy(newAllergy);
        return true;
    }

    /**
     * Deletes an allergy to a patient
     * @param patientID the ID of the patient
     * @param allergy the allergy to be deleted
     * @return boolean if the allergy is successfully deleted
     */
    public boolean deleteAllergy (int patientID, String allergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.deleteAllergy(allergy);
    }

    /**
     * Updates an allergy to a patient
     * @param patientID the ID of the patient
     * @param orgAllergy the original allergy
     * @param newAllergy the updated allergy
     * @return boolean if the allergy is successfully updated
     */
    public boolean updateAllergy (int patientID, String orgAllergy, String newAllergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.updateAllergy(orgAllergy, newAllergy);
    }

    /**
     * Adds medical history to a patient
     * @param patientID the ID of the patient
     * @param medHistory the medical history to add
     * @return boolean if the medical history to successfully added
     */
    public boolean addMedicalHistory (int patientID, String medHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.addMedicalHistory(medHistory);
        return true;
    }

    /**
     * Deletes medical history to a patient
     * @param patientID the ID of the patient
     * @param medHistory the medical history to delete
     * @return boolean if the medical history to successfully deleted
     */
    public boolean deleteMedicalHistory (int patientID, String medHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.deleteMedicalHistory(medHistory);
    }

    /**
     * Adds family history to a patient
     * @param patientID the ID of the patient
     * @param medHistory the family history to add
     * @return boolean if the family history to successfully added
     */
    public boolean addFamilyHistory (int patientID, String famHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.addFamilyHistory(famHistory);
        return true;
    }

    /**
     * Deletes family history to a patient
     * @param patientID the ID of the patient
     * @param medHistory the family history to delete
     * @return boolean if the family history to successfully deleted
     */
    public boolean deleteFamilyHistory (int patientID, String famHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        return patient.deleteFamilyHistory(famHistory);
    }

    /**
     * Updates assgiend staff for a patient
     * @param patientID the ID of the patient
     * @param assigned the Staff to be assigned
     * @return boolean if the staff was successfully assigned
     */
    public boolean updateAssignedStaffForPatient(int patientID, Staff assigned) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.setAssignedStaff(assigned);
        return true;
    }

    /**
     * Checks in patient
     * @param patientID the ID of the patient
     * @return boolean if the patient was successfully checked in
     */
    public boolean checkInPatient (int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.checkIn();
        return true;
    }

    /**
     * Checks out patient
     * @param patientID the ID of the patient
     * @param followUp the type of follow up appointment
     * @return boolean if the patient was successfully checked in
     */
    public boolean checkOutPatient (int patientID, String followUp) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return false;
        }

        patient.checkOut(followUp);
        return true;
    }

    /**
     * Returns the total cost for a patient
     * @param patientID the ID of the patient
     * @return double the total cost of the bill
     */
    public double calculateTotalCostForPatient (int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) {
            return -1;
        }

        return patient.calculateTotalCost();
    }

    /**
     * Set the status of an emergency patient
     * @param patientID the ID of the patient
     * @param status the status of the patient
     * @return boolean if the status is successfully changed
     */
    public boolean setEmergencyPatientStatus (int patientID, String status) {
        Patient patient = searchPatientByID(patientID);
        if (patient != null && patient instanceof EmergencyPatient) {
            ((EmergencyPatient)patient).setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Returns a string containing information about all the patients
     * @return String the information of the patients
     */
    public String listAllPatients () {
        String patientString = "";

        for (int i = 0; i < numPatients; i++) {
            patientString += patients[i].toString();
            patientString += "\n";
        }

        return patientString;
    }

    /**
     * Returns a string containing information about all appointments of a patient
     * @param patientID the ID of the patient
     * @return String the information of the appointments
     */
    public String listAppointmentsForPatient (int patientID) {
        Patient patient = searchPatientByID(patientID);
        String apptString = "";

        if (patient == null) {
            return null;
        }
        
        apptString += "Past appointments: \n";
        Appointment[] pastAppts = patient.getPastAppointments();
        for (int i = 0; i < pastAppts.length; i++) {
            apptString += pastAppts[i].toString();
            apptString += "\n";
        }

        apptString += "\nUpcoming appointments: \n";
        Appointment[] upcomingAppts = patient.getUpcomingAppointments();
        for (int i = 0; i < upcomingAppts.length; i++) {
            apptString += upcomingAppts[i].toString();
            apptString += "\n";
        }

        return apptString;
    }

    /**
     * Adds an appointment to history
     * @param patientID the ID of the patient
     * @param appt the appointment to be added to history
     * @return boolean if the appointment is successfully added to history
     */
    public boolean addtoHistory (int patientID, Appointment appt) {
        Patient patient = searchPatientByID(patientID);

        if (patient == null) {
            return false;
        }
        
        patient.addToHistory(appt);
        return true;
    }

    /**
     * Logs a medication that was administered
     * @param patientID the ID of the patient
     * @param med the medication administered
     * @return boolean if the medication is successfully logged
     */
    public boolean logMedicationAdministeredForPatient (int patientID, Medication med) {
        Patient patient = searchPatientByID(patientID);

        if (patient != null && patient instanceof InPatient) {
            ((InPatient)patient).logMedicationsAdministered(med);
            return true;
        }
        return false;
    }

    /**
     * Records vitals for a patient
     * @param patientID the ID of the patient
     * @param heartRate the heart rate of the patient
     * @param bloodPressure the blood pressure of the patient
     * @return boolean if the vitals are successfully logged
     */
    public boolean recordVitalsForPatient (int patientID, double heartRate, double bloodPressure) {
        Patient patient = searchPatientByID(patientID);

        if (patient != null && patient instanceof InPatient) {
            ((InPatient)patient).recordVitals(heartRate, bloodPressure);
            return true;
        }
        return false;
    }

}
