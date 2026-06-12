package patient;

import appointment.ApptManager;
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
 * File: PatientManager.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 2, 2026
 * Description: This class manages the patients in a hospital.
 */

public class PatientManager {
    private Patient[] patients;
    private int numPatients;
    private int maxPatients;
    public final static Date CUR_DATE;
    public final static int CUR_TIME;

    static {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        CUR_DATE = new Date(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        CUR_TIME = now.getHour() * 100 + now.getMinute();
    }

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
     * @param dayIn the date of admission to the hospital
     * @param dayOut the date of discharge from the hospital
     * @param hospitalBed whether a hospital bed is assigned
     * @return boolean if the patient is successfully registered
     */
    public boolean registerInPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, long phoneNum, int numOHIP, Date dateRegistered, char gender, long emergencyContactPhoneNumber, Staff assignedStaff, Date dayIn, Date dayOut, boolean hospitalBed) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }
        Patient newPatient = new InPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff, dayIn, dayOut, hospitalBed);
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
     * @param appointmentTimingMonths the number of months until the next appointment
     * @return boolean if the patient is successfully registered
     */
    public boolean registerOutPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, long phoneNum, int numOHIP, Date dateRegistered, char gender, long emergencyContactPhoneNumber, Staff assignedStaff, int appointmentTimingMonths) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }
        Patient newPatient = new OutPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff, appointmentTimingMonths);
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
     * @param arrivalTime the time of arrival at the hospital
     * @param dayIn the date of admission to the hospital
     * @param dayOut the date of discharge from the hospital
     * @param presentingComplaint the main reason for the visit
     * @param arrivalType the mode of arrival
     * @param status the current status of the patient
     * @return boolean if the patient is successfully registered
     */
    public boolean registerEmergencyPatient(int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, long phoneNum, int numOHIP, Date dateRegistered, char gender, long emergencyContactPhoneNumber, Staff assignedStaff, int arrivalTime, Date dayIn, Date dayOut, String presentingComplaint, String arrivalType, String status) {
        if (numPatients >= maxPatients || !Patient.isValidOHIP(numOHIP)) {
            return false;
        }
        Patient newPatient = new EmergencyPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff, arrivalTime, dayIn, dayOut, presentingComplaint, arrivalType, status);
        patients[numPatients] = newPatient;
        numPatients++;
        return true;
    }

    /**
     * Loads patient records from the specified file.
     * Each record has 12 base lines, plus type-specific lines,
     * followed by sentinel-terminated blocks for diagnoses, medications,
     * allergies, medical history, family history, and (InPatient only)
     * vitals log and medications administered.
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
                String[] patientLines = new String[12];
                patientLines[0] = line;
                for (int i = 1; i < 12; i++) {
                    String nextLine = reader.readLine();
                    if (nextLine == null) {
                        return false;
                    }
                    patientLines[i] = nextLine.trim();
                }

                String type = patientLines[0];
                Patient patient = null;

                if (type.equals("InPatient")) {
                    String dayInStr = reader.readLine();
                    if (dayInStr == null) { return false; }
                    String dayOutStr = reader.readLine();
                    if (dayOutStr == null) { return false; }
                    String hospitalBedStr = reader.readLine();
                    if (hospitalBedStr == null) { return false; }
                    Date dayIn = parseDate(dayInStr.trim());
                    Date dayOut = parseDate(dayOutStr.trim());
                    boolean hospitalBed = Boolean.parseBoolean(hospitalBedStr.trim());
                    Object[] typeData = {dayIn, dayOut, hospitalBed};
                    patient = parsePatientLines(patientLines, type, typeData);
                } else if (type.equals("OutPatient")) {
                    String appointmentTimingStr = reader.readLine();
                    if (appointmentTimingStr == null) { return false; }
                    int appointmentTiming = Integer.parseInt(appointmentTimingStr.trim());
                    Object[] typeData = {appointmentTiming};
                    patient = parsePatientLines(patientLines, type, typeData);
                } else if (type.equals("EmergencyPatient")) {
                    String arrivalTimeStr = reader.readLine();
                    if (arrivalTimeStr == null) { return false; }
                    String dayInStr = reader.readLine();
                    if (dayInStr == null) { return false; }
                    String dayOutStr = reader.readLine();
                    if (dayOutStr == null) { return false; }
                    String presentingComplaintStr = reader.readLine();
                    if (presentingComplaintStr == null) { return false; }
                    String arrivalTypeStr = reader.readLine();
                    if (arrivalTypeStr == null) { return false; }
                    String statusStr = reader.readLine();
                    if (statusStr == null) { return false; }
                    int arrivalTime = Integer.parseInt(arrivalTimeStr.trim());
                    Date dayIn = parseDate(dayInStr.trim());
                    Date dayOut = parseDate(dayOutStr.trim());
                    Object[] typeData = {arrivalTime, dayIn, dayOut, presentingComplaintStr.trim(), arrivalTypeStr.trim(), statusStr.trim()};
                    patient = parsePatientLines(patientLines, type, typeData);
                }

                // Read sentinel-terminated medical record blocks (all patient types)
                String[] loadedDiagnoses  = readUntilSentinel(reader, 20);
                String[] loadedMedLines   = readUntilSentinel(reader, 20);
                String[] loadedAllergies  = readUntilSentinel(reader, 20);
                String[] loadedMedHistory = readUntilSentinel(reader, 20);
                String[] loadedFamHistory = readUntilSentinel(reader, 20);

                if (patient != null && numPatients < maxPatients) {
                    for (String d : loadedDiagnoses) {
                        if (d != null) { patient.addDiagnoses(d); }
                    }
                    for (String m : loadedMedLines) {
                        if (m != null) {
                            int colon = m.indexOf(':');
                            if (colon != -1) {
                                patient.addMedication(m.substring(0, colon), m.substring(colon + 1));
                            }
                        }
                    }
                    for (String a : loadedAllergies) {
                        if (a != null) { patient.addAllergy(a); }
                    }
                    for (String h : loadedMedHistory) {
                        if (h != null) { patient.addMedicalHistory(h); }
                    }
                    for (String f : loadedFamHistory) {
                        if (f != null) { patient.addFamilyHistory(f); }
                    }

                    // InPatient-only: vitals log and medications administered
                    if (patient instanceof InPatient) {
                        InPatient ip = (InPatient) patient;
                        String[] loadedVitals    = readUntilSentinel(reader, 100);
                        String[] loadedMedsAdmin = readUntilSentinel(reader, 100);
                        String[] vitalsLog       = ip.getVitalsLog();
                        String[] medsAdmin       = ip.getMedicationsAdministered();
                        for (int j = 0; j < loadedVitals.length; j++) {
                            if (loadedVitals[j] != null && j < vitalsLog.length) {
                                vitalsLog[j] = loadedVitals[j];
                            }
                        }
                        for (int j = 0; j < loadedMedsAdmin.length; j++) {
                            if (loadedMedsAdmin[j] != null && j < medsAdmin.length) {
                                medsAdmin[j] = loadedMedsAdmin[j];
                            }
                        }
                    }

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
     * Each record has 12 base lines, plus type-specific lines,
     * followed by sentinel-terminated blocks for diagnoses, medications,
     * allergies, medical history, family history, and (InPatient only)
     * vitals log and medications administered.
     * @param fileName the patient file path
     * @return true if save succeeds, false otherwise
     */
    public boolean savePatientInfo(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < numPatients; i++) {
                Patient patient = patients[i];
                if (patient == null) { continue; }
                writer.write(patient.getClass().getSimpleName()); writer.newLine();
                writer.write(Integer.toString(patient.getPatientID())); writer.newLine();
                writer.write(patient.getFirstName()); writer.newLine();
                writer.write(patient.getLastName()); writer.newLine();
                writer.write(formatDate(patient.getDateOfBirth())); writer.newLine();
                writer.write(patient.getWard()); writer.newLine();
                writer.write(patient.getAddress()); writer.newLine();
                writer.write(Long.toString(patient.getPhoneNum())); writer.newLine();
                writer.write(Integer.toString(patient.getNumOHIP())); writer.newLine();
                writer.write(formatDate(patient.getDateRegistered())); writer.newLine();
                writer.write(Character.toString(patient.getGender())); writer.newLine();
                writer.write(Long.toString(patient.getEmergencyContactPhoneNumber())); writer.newLine();

                if (patient instanceof InPatient) {
                    InPatient ip = (InPatient) patient;
                    writer.write(formatDate(ip.getDayIn())); writer.newLine();
                    writer.write(formatDate(ip.getDayOut())); writer.newLine();
                    writer.write(Boolean.toString(ip.getHospitalBed())); writer.newLine();
                } else if (patient instanceof OutPatient) {
                    OutPatient op = (OutPatient) patient;
                    writer.write(Integer.toString(op.getAppointmentTimingMonths())); writer.newLine();
                } else if (patient instanceof EmergencyPatient) {
                    EmergencyPatient ep = (EmergencyPatient) patient;
                    writer.write(Integer.toString(ep.getArrivalTime())); writer.newLine();
                    writer.write(formatDate(ep.getDayIn())); writer.newLine();
                    writer.write(formatDate(ep.getDayOut())); writer.newLine();
                    writer.write(ep.getPresentingComplaint()); writer.newLine();
                    writer.write(ep.getArrivalType()); writer.newLine();
                    writer.write(ep.getStatus()); writer.newLine();
                }

                // Diagnoses
                String[] diagnoses = patient.getDiagnosis();
                if (diagnoses != null) {
                    for (String d : diagnoses) {
                        if (d != null) { writer.write(d); writer.newLine(); }
                    }
                }
                writer.write("---"); writer.newLine();

                // Medications (name:dosage)
                Medication[] medications = patient.getMedications();
                if (medications != null) {
                    for (Medication m : medications) {
                        if (m != null) { writer.write(m.getMedName() + ":" + m.getDosage()); writer.newLine(); }
                    }
                }
                writer.write("---"); writer.newLine();

                // Allergies
                String[] allergies = patient.getAllergies();
                if (allergies != null) {
                    for (String a : allergies) {
                        if (a != null) { writer.write(a); writer.newLine(); }
                    }
                }
                writer.write("---"); writer.newLine();

                // Medical history
                String[] medHistory = patient.getMedicalHistory();
                if (medHistory != null) {
                    for (String h : medHistory) {
                        if (h != null) { writer.write(h); writer.newLine(); }
                    }
                }
                writer.write("---"); writer.newLine();

                // Family history
                String[] famHistory = patient.getFamilyHistory();
                if (famHistory != null) {
                    for (String f : famHistory) {
                        if (f != null) { writer.write(f); writer.newLine(); }
                    }
                }
                writer.write("---"); writer.newLine();

                // InPatient-only: vitals log and medications administered
                if (patient instanceof InPatient) {
                    InPatient ip = (InPatient) patient;
                    String[] vitals = ip.getVitalsLog();
                    if (vitals != null) {
                        for (String v : vitals) {
                            if (v != null) { writer.write(v); writer.newLine(); }
                        }
                    }
                    writer.write("---"); writer.newLine();
                    String[] medsAdmin = ip.getMedicationsAdministered();
                    if (medsAdmin != null) {
                        for (String ma : medsAdmin) {
                            if (ma != null) { writer.write(ma); writer.newLine(); }
                        }
                    }
                    writer.write("---"); writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Replaces each patient's appointment lists with the canonical records from ApptManager.
     * @param apptManager loaded appointment manager
     */
    public void syncAppointmentsFromManager(ApptManager apptManager) {
        if (apptManager == null) { return; }
        for (int i = 0; i < numPatients; i++) {
            if (patients[i] != null) { patients[i].clearAppointments(); }
        }
        Appointment[] appts = apptManager.getAppointments();
        int count = apptManager.getNumAppointments();
        for (int i = 0; i < count; i++) {
            if (appts[i] != null && appts[i].getPatient() != null) {
                appts[i].getPatient().addAppointment(appts[i]);
            }
        }
    }

    /**
     * Loads appointment records from the specified file and assigns them to matching patients.
     * @param fileName the appointment file path
     * @return true if load succeeds, false otherwise
     */
    public boolean loadPatientAppts(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) { continue; }
                int patientID = Integer.parseInt(line);
                String apptType = reader.readLine();
                if (apptType == null) { continue; }
                apptType = apptType.trim();
                int patientIndex = searchPatientIndexByID(patientID);
                if (patientIndex == -1) {
                    skipAppointmentRecord(apptType, reader);
                    continue;
                }
                Patient patient = patients[patientIndex];
                Appointment appt = parseAppointmentLines(apptType, patient, reader);
                if (appt != null) { patient.addAppointment(appt); }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads lines from the reader into a String array until a "---" sentinel or EOF.
     * @param reader  the BufferedReader to read from
     * @param maxSize the maximum number of entries to store
     * @return array of read strings (trailing slots are null)
     */
    private String[] readUntilSentinel(BufferedReader reader, int maxSize) throws IOException {
        String[] result = new String[maxSize];
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("---")) { break; }
            if (count < maxSize) { result[count++] = line; }
        }
        return result;
    }

    /**
     * Consumes the remaining lines of an appointment record whose patient was not found.
     * @param apptType the appointment type string already read
     * @param reader   the BufferedReader to drain
     */
    private void skipAppointmentRecord(String apptType, BufferedReader reader) throws IOException {
        for (int i = 0; i < 6; i++) { reader.readLine(); }
        if (apptType.equals("RoutineCheckup")) {
            reader.readLine();
        } else if (apptType.equals("Surgery")) {
            reader.readLine(); reader.readLine(); reader.readLine(); reader.readLine(); reader.readLine();
        } else if (apptType.equals("EmergencyVisit")) {
            reader.readLine(); reader.readLine();
        }
    }

    /**
     * Parses patient record lines from the patient file.
     * @param patientLines array of 12 lines representing a patient record
     * @param type the type of patient
     * @param typeSpecificData additional data for type-specific fields
     * @return a Patient instance if parsing succeeds, null otherwise
     */
    private Patient parsePatientLines(String[] patientLines, String type, Object[] typeSpecificData) {
        if (patientLines.length < 12) { return null; }
        int patientID = Integer.parseInt(patientLines[1]);
        String firstName = patientLines[2];
        String lastName = patientLines[3];
        Date dateOfBirth = parseDate(patientLines[4]);
        String ward = patientLines[5];
        String address = patientLines[6];
        long phoneNum = Long.parseLong(patientLines[7]);
        int numOHIP = Integer.parseInt(patientLines[8]);
        Date dateRegistered = parseDate(patientLines[9]);
        char gender = patientLines[10].isEmpty() ? ' ' : patientLines[10].charAt(0);
        long emergencyContactPhoneNumber = Long.parseLong(patientLines[11]);

        switch (type) {
            case "InPatient":
                Date dayIn = (Date) typeSpecificData[0];
                Date dayOut = (Date) typeSpecificData[1];
                boolean hospitalBed = (boolean) typeSpecificData[2];
                return new InPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null, dayIn, dayOut, hospitalBed);
            case "OutPatient":
                int appointmentTimingMonths = (int) typeSpecificData[0];
                return new OutPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null, appointmentTimingMonths);
            case "EmergencyPatient":
                int arrivalTime = (int) typeSpecificData[0];
                Date emergDayIn = (Date) typeSpecificData[1];
                Date emergDayOut = (Date) typeSpecificData[2];
                String presentingComplaint = (String) typeSpecificData[3];
                String arrivalType = (String) typeSpecificData[4];
                String status = (String) typeSpecificData[5];
                return new EmergencyPatient(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, null, arrivalTime, emergDayIn, emergDayOut, presentingComplaint, arrivalType, status);
            default:
                return null;
        }
    }

    /**
     * Parses appointment record lines from the appointment file.
     * @param apptType the type of appointment
     * @param patient the patient to associate with the appointment
     * @param reader the BufferedReader to read additional lines
     * @return an Appointment instance if parsing succeeds, null otherwise
     */
    private Appointment parseAppointmentLines(String apptType, Patient patient, BufferedReader reader) throws IOException {
        if (patient == null) { return null; }
        String apptIDStr = reader.readLine();
        if (apptIDStr == null) { return null; }
        int apptID = Integer.parseInt(apptIDStr.trim());
        String dateStr = reader.readLine();
        if (dateStr == null) { return null; }
        Date date = parseDate(dateStr.trim());
        String timeStr = reader.readLine();
        if (timeStr == null) { return null; }
        double time = Double.parseDouble(timeStr.trim());
        String durationStr = reader.readLine();
        if (durationStr == null) { return null; }
        double duration = Double.parseDouble(durationStr.trim());
        String costStr = reader.readLine();
        if (costStr == null) { return null; }
        double cost = Double.parseDouble(costStr.trim());
        String status = reader.readLine();
        if (status == null) { return null; }
        status = status.trim();

        switch (apptType) {
            case "RoutineCheckup":
                String clinicRoomStr = reader.readLine();
                if (clinicRoomStr == null) { return null; }
                int clinicRoomNum = Integer.parseInt(clinicRoomStr.trim());
                return new RoutineCheckup(apptID, patient, null, date, time, duration, cost, status, clinicRoomNum, null);
            case "Surgery":
                String operatingRoomStr = reader.readLine();
                if (operatingRoomStr == null) { return null; }
                int operatingRoomNum = Integer.parseInt(operatingRoomStr.trim());
                String anaesthesiaType = reader.readLine();
                if (anaesthesiaType == null) { return null; }
                anaesthesiaType = anaesthesiaType.trim();
                String anaesthesiaDoseStr = reader.readLine();
                if (anaesthesiaDoseStr == null) { return null; }
                double anaesthesiaDose = Double.parseDouble(anaesthesiaDoseStr.trim());
                String surgeryType = reader.readLine();
                if (surgeryType == null) { return null; }
                surgeryType = surgeryType.trim();
                String preOpInstructions = reader.readLine();
                if (preOpInstructions == null) { return null; }
                preOpInstructions = preOpInstructions.trim();
                return new Surgery(apptID, patient, null, date, time, duration, cost, status, operatingRoomNum, anaesthesiaType, anaesthesiaDose, surgeryType, preOpInstructions);
            case "EmergencyVisit":
                String emergencyRoomStr = reader.readLine();
                if (emergencyRoomStr == null) { return null; }
                int emergencyRoomNum = Integer.parseInt(emergencyRoomStr.trim());
                String urgencyIdxStr = reader.readLine();
                if (urgencyIdxStr == null) { return null; }
                int urgencyIdx = Integer.parseInt(urgencyIdxStr.trim());
                return new EmergencyVisit(apptID, patient, null, date, time, duration, cost, status, emergencyRoomNum, urgencyIdx);
            default:
                return null;
        }
    }

    private void serializeAppointmentLines(int patientID, Appointment appt, BufferedWriter writer) throws IOException {
        writer.write(Integer.toString(patientID)); writer.newLine();
        writer.write(appt.getClass().getSimpleName()); writer.newLine();
        writer.write(Integer.toString(appt.getApptID())); writer.newLine();
        writer.write(formatDate(appt.getDate())); writer.newLine();
        writer.write(Double.toString(appt.getTime())); writer.newLine();
        writer.write(Double.toString(appt.getDuration())); writer.newLine();
        writer.write(Double.toString(appt.getCost())); writer.newLine();
        writer.write(appt.getStatus()); writer.newLine();
        if (appt instanceof RoutineCheckup) {
            writer.write(Integer.toString(((RoutineCheckup) appt).getRoomNum())); writer.newLine();
        } else if (appt instanceof Surgery) {
            Surgery surgery = (Surgery) appt;
            writer.write(Integer.toString(surgery.getRoomNum())); writer.newLine();
            writer.write(surgery.getAnaesthesiaType()); writer.newLine();
            writer.write(Double.toString(surgery.getAnaesthesiaDose())); writer.newLine();
            writer.write(surgery.getType()); writer.newLine();
            writer.write(""); writer.newLine();
        } else if (appt instanceof EmergencyVisit) {
            EmergencyVisit ev = (EmergencyVisit) appt;
            writer.write(Integer.toString(ev.getRoomNum())); writer.newLine();
            writer.write(Integer.toString(ev.getUrgencyIdx())); writer.newLine();
        }
    }

    /**
     * Parses a date string from the file into a Date object.
     * @param token the date token in YYYY-MM-DD format
     * @return a Date object representing the parsed date
     */
    private Date parseDate(String token) {
        String[] parts = token.split("-");
        if (parts.length != 3) { return new Date(0, 0, 0); }
        int year  = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day   = Integer.parseInt(parts[2]);
        if (year == 0 && month == 0 && day == 0) { return null; }
        return new Date(year, month, day);
    }

    /**
     * Formats a Date for file output in YYYY-MM-DD format.
     * @param date the Date object to format
     * @return the formatted date string
     */
    private String formatDate(Date date) {
        if (date == null) { return "0-0-0"; }
        return date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
    }

    /**
     * Searches for the index of a patient by their ID
     * @param patientID the ID of the patient to search for
     * @return the index of the patient if found, -1 otherwise
     */
    public int searchPatientIndexByID(int patientID) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].getPatientID() == patientID) { return i; }
        }
        return -1;
    }

    /**
     * Searches for the patient ID of a patient by first and last name
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @return the patient ID if found, -1 otherwise
     */
    public int searchPatientIDByName(String firstName, String lastName) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].equalsName(firstName, lastName)) { return patients[i].getPatientID(); }
        }
        return -1;
    }

    /**
     * Searches for a patient by their first and last name
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByName(String firstName, String lastName) {
        for (int i = 0; i < numPatients; i++) {
            if (patients[i].equalsName(firstName, lastName)) { return patients[i]; }
        }
        return null;
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
     * Searches for a patient by their ID (API alias used by ApptManager).
     * @param patientID the ID of the patient to search for
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByPatientID(int patientID) {
        return searchPatientByID(patientID);
    }

    /**
     * Searches for a patient by their ID using binary search
     * @param patientID the ID of the patient to search for
     * @param bottom the lower bound of the search range
     * @param top the upper bound of the search range
     * @return the patient if found, null otherwise
     */
    public Patient searchPatientByID(int patientID, int bottom, int top) {
        sortByPatientID();
        if (bottom > top) { return null; }
        int mid = (bottom + top) / 2;
        if (patients[mid].getPatientID() == patientID) {
            return patients[mid];
        } else if (patients[mid].getPatientID() < patientID) {
            return searchPatientByID(patientID, mid + 1, top);
        } else {
            return searchPatientByID(patientID, bottom, mid - 1);
        }
    }

    /**
     * Sorts the patients array by patient ID using a bubble sort
     */
    public void sortByPatientID() {
        boolean sorted;
        if (numPatients <= 1) { return; }
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
            if (sorted) { break; }
        }
    }

    /**
     * Sorts the patients array by date registered using an insertion sort.
     */
    public void sortByDateEntered() {
        if (numPatients <= 1) { return; }
        for (int i = 1; i < numPatients; i++) {
            Patient itemToCompare = patients[i];
            int blankIndex = i;
            while (blankIndex > 0 && patients[blankIndex - 1].getDateRegistered().compareTo(itemToCompare.getDateRegistered()) > 0) {
                patients[blankIndex] = patients[blankIndex - 1];
                blankIndex--;
            }
            patients[blankIndex] = itemToCompare;
        }
    }

    /**
     * Sorts the patients array by ward, then patient ID (selection sort).
     */
    public void sortByWardThenPatientID() {
        if (numPatients <= 1) { return; }
        for (int upperBound = numPatients - 1; upperBound > 0; upperBound--) {
            int maxIndex = 0;
            for (int j = 1; j <= upperBound; j++) {
                int wardComparison = patients[j].getWard().compareToIgnoreCase(patients[maxIndex].getWard());
                if (wardComparison > 0 || (wardComparison == 0 && patients[j].getPatientID() > patients[maxIndex].getPatientID())) {
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
    public boolean deletePatient(int patientID) {
        int index = searchPatientIndexByID(patientID);
        if (index == -1) { return false; }
        for (int i = index; i < numPatients - 1; i++) { patients[i] = patients[i + 1]; }
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
    public boolean updatePatient(int patientID, Patient updated) {
        int index = searchPatientIndexByID(patientID);
        if (index == -1) { return false; }
        patients[index] = updated;
        return true;
    }

    /**
     * Adds a diagnosis to a patient
     * @param patientID the ID of the patient
     * @param diagnosis the diagnosis to add
     * @return boolean if the diagnosis is successfully added
     */
    public boolean addDiagnosis(int patientID, String diagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addDiagnoses(diagnosis);
        return true;
    }

    /**
     * Deletes a diagnosis from a patient
     * @param patientID the ID of the patient
     * @param diagnosis the diagnosis to delete
     * @return boolean if the diagnosis is successfully deleted
     */
    public boolean deleteDiagnosis(int patientID, String diagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteDiagnosis(diagnosis);
    }

    /**
     * Updates a diagnosis for a patient
     * @param patientID the ID of the patient
     * @param orgDiagnosis the original diagnosis to change
     * @param newDiagnosis the updated diagnosis
     * @return boolean if the diagnosis is successfully updated
     */
    public boolean updateDiagnosis(int patientID, String orgDiagnosis, String newDiagnosis) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.updateDiagnosis(orgDiagnosis, newDiagnosis);
    }

    /**
     * Adds an appointment to a patient
     * @param patientID the ID of the patient
     * @param newAppt the appointment to be added
     * @return boolean if the appointment is successfully added
     */
    public boolean addAppointment(int patientID, Appointment newAppt) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addAppointment(newAppt);
        return true;
    }

    /**
     * Deletes an appointment from a patient
     * @param patientID the ID of the patient
     * @param toDelete the appointment to delete
     * @return boolean if the appointment is successfully deleted
     */
    public boolean deleteAppointment(int patientID, Appointment toDelete) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteAppointment(toDelete);
    }

    /**
     * Updates an appointment for a patient
     * @param patientID the ID of the patient
     * @param orgAppt the original appointment
     * @param newAppt the new appointment
     * @return boolean if the appointment is successfully updated
     */
    public boolean updateAppointment(int patientID, Appointment orgAppt, Appointment newAppt) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.updateAppointment(orgAppt, newAppt);
    }

    /**
     * Adds a medication to a patient
     * @param patientID the ID of the patient
     * @param medName the name of the medication
     * @param dosage the dosage of the medication
     * @return boolean if the medication is successfully added
     */
    public boolean addMedication(int patientID, String medName, String dosage) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { 
            return false; 
        }
        return patient.addMedication(medName, dosage);
    }

    /**
     * Deletes a medication from a patient
     * @param patientID the ID of the patient
     * @param medName the name of the medication
     * @return boolean if the medication is successfully deleted
     */
    public boolean deleteMedication(int patientID, String medName) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteMedication(medName);
    }

    /**
     * Updates a medication for a patient
     * @param patientID the ID of the patient
     * @param medName the name of the original medication
     * @param newMed the name of the updated medication
     * @param newDosage the dosage of the updated medication
     * @return boolean if the medication is successfully updated
     */
    public boolean updateMedication(int patientID, String medName, String newMed, String newDosage) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.updateMedication(medName, newMed, newDosage);
    }

    /**
     * Adds an allergy to a patient
     * @param patientID the ID of the patient
     * @param newAllergy the allergy to be added
     * @return boolean if the allergy is successfully added
     */
    public boolean addAllergy(int patientID, String newAllergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addAllergy(newAllergy);
        return true;
    }

    /**
     * Deletes an allergy from a patient
     * @param patientID the ID of the patient
     * @param allergy the allergy to be deleted
     * @return boolean if the allergy is successfully deleted
     */
    public boolean deleteAllergy(int patientID, String allergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteAllergy(allergy);
    }

    /**
     * Updates an allergy for a patient
     * @param patientID the ID of the patient
     * @param orgAllergy the original allergy
     * @param newAllergy the updated allergy
     * @return boolean if the allergy is successfully updated
     */
    public boolean updateAllergy(int patientID, String orgAllergy, String newAllergy) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.updateAllergy(orgAllergy, newAllergy);
    }

    /**
     * Adds medical history to a patient
     * @param patientID the ID of the patient
     * @param medHistory the medical history to add
     * @return boolean if the medical history is successfully added
     */
    public boolean addMedicalHistory(int patientID, String medHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addMedicalHistory(medHistory);
        return true;
    }

    /**
     * Deletes medical history from a patient
     * @param patientID the ID of the patient
     * @param medHistory the medical history to delete
     * @return boolean if the medical history is successfully deleted
     */
    public boolean deleteMedicalHistory(int patientID, String medHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteMedicalHistory(medHistory);
    }

    /**
     * Adds family history to a patient
     * @param patientID the ID of the patient
     * @param famHistory the family history to add
     * @return boolean if the family history is successfully added
     */
    public boolean addFamilyHistory(int patientID, String famHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addFamilyHistory(famHistory);
        return true;
    }

    /**
     * Deletes family history from a patient
     * @param patientID the ID of the patient
     * @param famHistory the family history to delete
     * @return boolean if the family history is successfully deleted
     */
    public boolean deleteFamilyHistory(int patientID, String famHistory) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.deleteFamilyHistory(famHistory);
    }

    /**
     * Updates assigned staff for a patient
     * @param patientID the ID of the patient
     * @param assigned the Staff to be assigned
     * @return boolean if the staff was successfully assigned
     */
    public boolean updateAssignedStaffForPatient(int patientID, Staff assigned) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.setAssignedStaff(assigned);
        return true;
    }

    /**
     * Checks in patient
     * @param patientID the ID of the patient
     * @return boolean if the patient was successfully checked in
     */
    public boolean checkInPatient(int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { 
            return false; 
        }
        return patient.checkIn();
    }

    /**
     * Checks out patient
     * @param patientID the ID of the patient
     * @param followUp the type of follow up appointment
     * @return boolean if the patient was successfully checked out
     */
    public boolean checkOutPatient(int patientID, String followUp) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        return patient.checkOut(followUp);
    }

    /**
     * Returns the total cost for a patient
     * @param patientID the ID of the patient
     * @return double the total cost of the bill
     */
    public double calculateTotalCostForPatient(int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return -1; }
        return patient.calculateTotalCost();
    }

    /**
     * Computes the total bill for a patient by delegating to the patient's calculateBill().
     * @param patientID the ID of the patient
     * @return the total bill, or -1 if the patient is not found
     */
    public double calculateBill(int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return -1; }
        return patient.calculateBill();
    }

    /**
     * Set the status of an emergency patient
     * @param patientID the ID of the patient
     * @param status the status of the patient
     * @return boolean if the status is successfully changed
     */
    public boolean setEmergencyPatientStatus(int patientID, String status) {
        Patient patient = searchPatientByID(patientID);
        if (patient != null && patient instanceof EmergencyPatient) {
            ((EmergencyPatient) patient).setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Returns a string containing information about all the patients
     * @return String the information of the patients
     */
    public String listAllPatients() {
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
    public String listAppointmentsForPatient(int patientID) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return null; }
        String apptString = "Past appointments: \n\n";
        Appointment[] pastAppts = patient.getPastAppointments();
        for (int i = 0; i < pastAppts.length; i++) {
            if (pastAppts[i] != null) { apptString += pastAppts[i].toString() + "\n"; }
        }
        apptString += "\nUpcoming appointments: \n\n";
        Appointment[] upcomingAppts = patient.getUpcomingAppointments();
        for (int i = 0; i < upcomingAppts.length; i++) {
            if (upcomingAppts[i] != null) { apptString += upcomingAppts[i].toString() + "\n"; }
        }
        return apptString;
    }

    /**
     * Adds an appointment to history
     * @param patientID the ID of the patient
     * @param appt the appointment to be added to history
     * @return boolean if the appointment is successfully added to history
     */
    public boolean addtoHistory(int patientID, Appointment appt) {
        Patient patient = searchPatientByID(patientID);
        if (patient == null) { return false; }
        patient.addToHistory(appt);
        return true;
    }

    /**
     * Logs a medication that was administered
     * @param patientID the ID of the patient
     * @param med the medication administered
     * @return boolean if the medication is successfully logged
     */
    public boolean logMedicationAdministeredForPatient(int patientID, Medication med) {
        Patient patient = searchPatientByID(patientID);
        if (patient != null && patient instanceof InPatient) {
            ((InPatient) patient).logMedicationsAdministered(med);
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
    public boolean recordVitalsForPatient(int patientID, double heartRate, double bloodPressure) {
        Patient patient = searchPatientByID(patientID);
        if (patient != null && patient instanceof InPatient) {
            ((InPatient) patient).recordVitals(heartRate, bloodPressure);
            return true;
        }
        return false;
    }

}