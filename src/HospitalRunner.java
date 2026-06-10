import java.util.*;
import patient.*;
import shared.Date;
import staff.Staff;

public class HospitalRunner {

    static final String PATIENT_FILE = "patients.txt";
    static final String APPT_FILE = "appointments.txt";
    static final int MAX_PATIENTS = 100;

    /**
     * Reads a non-blank line from the scanner
     * @param sc the Scanner to read from
     * @param label the prompt label to display
     * @return the trimmed non-blank input string
     */
    private static String prompt(Scanner sc, String label) {
        String val = "";
        while (val.isEmpty()) {
            System.out.print(label);
            val = sc.nextLine().trim();
            if (val.isEmpty()) System.out.println("  (cannot be blank, try again)");
        }
        return val;
    }

    /**
     * Reads an integer from the scanner, re-prompting on invalid input
     * @param sc the Scanner to read from
     * @param label the prompt label to display
     * @return the parsed integer value
     */
    private static int promptInt(Scanner sc, String label) {
        while (true) {
            try {
                return Integer.parseInt(prompt(sc, label));
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a double from the scanner, re-prompting on invalid input
     * @param sc the Scanner to read from
     * @param label the prompt label to display
     * @return the parsed double value
     */
    private static double promptDouble(Scanner sc, String label) {
        while (true) {
            try {
                return Double.parseDouble(prompt(sc, label));
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a number.");
            }
        }
    }

    /**
     * Reads a date in YYYY-MM-DD format from the scanner, re-prompting on invalid input
     * @param sc the Scanner to read from
     * @param label the prompt label to display
     * @return a Date object representing the parsed date
     */
    private static Date promptDate(Scanner sc, String label) {
        while (true) {
            String raw = prompt(sc, label + " (YYYY-MM-DD): ");
            String[] p = raw.split("-");
            if (p.length == 3) {
                try {
                    int y = Integer.parseInt(p[0]);
                    int m = Integer.parseInt(p[1]);
                    int d = Integer.parseInt(p[2]);
                    return new Date(y, m, d);
                } catch (NumberFormatException ignored) {}
            }
            System.out.println("  Invalid date format, try again.");
        }
    }

    /**
     * Reads a single character from the scanner, re-prompting if input is empty
     * @param sc the Scanner to read from
     * @param label the prompt label to display
     * @return the first character of the input string
     */
    private static char promptChar(Scanner sc, String label) {
        while (true) {
            String raw = prompt(sc, label);
            if (!raw.isEmpty()) return raw.charAt(0);
        }
    }

    /**
     * Searches for a Staff object by staff ID in the given array
     * @param allStaff the array of staff to search through
     * @param staffID the staff ID to search for
     * @return the Staff if found, null otherwise
     */
    private static Staff findStaff(Staff[] allStaff, String staffID) {
        for (Staff s : allStaff) {
            if (s != null && s.getStaffID().equals(staffID)) return s;
        }
        return null;
    }

    /**
     * Prompts for the 12 fields shared by all patient types and returns them as an Object array
     * Index order: [0] patientID, [1] firstName, [2] lastName, [3] dateOfBirth,
     *              [4] ward, [5] address, [6] phoneNum, [7] numOHIP,
     *              [8] dateRegistered, [9] gender, [10] emergencyContactPhoneNumber, [11] assignedStaff
     * @param sc the Scanner to read from
     * @param allStaff the array of staff to look up the assigned staff member from
     * @return an Object array containing the 12 base patient fields
     */
    private static Object[] promptBasePatientFields(Scanner sc, Staff[] allStaff) {
        int patientID = promptInt(sc, "  Patient ID: ");
        String firstName = prompt(sc, "  First name: ");
        String lastName = prompt(sc, "  Last name: ");
        Date dob = promptDate(sc, "  Date of birth");
        String ward = prompt(sc, "  Ward: ");
        String address = prompt(sc, "  Address: ");
        int phone = promptInt(sc, "  Phone number: ");
        int ohip = promptInt(sc, "  OHIP number: ");
        Date dateReg = promptDate(sc, "  Date registered");
        char gender = promptChar(sc, "  Gender (M/F/O): ");
        int emergPhone = promptInt(sc, "  Emergency contact #: ");

        Staff assigned = null;
        while (true) {
            String sid = prompt(sc, "  Assigned staff ID: ");
            assigned = findStaff(allStaff, sid);
            if (assigned != null) break;
            System.out.println("  Staff ID \"" + sid + "\" not found. Try again.");
        }

        return new Object[]{patientID, firstName, lastName, dob, ward, address, phone, ohip, dateReg, gender, emergPhone, assigned};
    }

    /**
     * Displays the register, delete, and update sub-menu and handles user selections
     * @param sc the Scanner to read from
     * @param pm the PatientManager to operate on
     * @param allStaff the array of staff for staff lookups
     */
    private static void menuRegisterDeleteUpdate(Scanner sc, PatientManager pm, Staff[] allStaff) {
        while (true) {
            System.out.println();
            System.out.println("  Register / Delete / Update");
            System.out.println("  1. Register an in-patient");
            System.out.println("  2. Register an out-patient");
            System.out.println("  3. Register an emergency patient");
            System.out.println("  4. Delete a patient");
            System.out.println("  5. Update a patient (re-enter all fields)");
            System.out.println("  6. Back");
            System.out.print("  Selection: ");
            String sel = sc.nextLine().trim();
            System.out.println();

            switch (sel) {
                case "1": {
                    System.out.println("  --- Register In-Patient ---");
                    Object[] b = promptBasePatientFields(sc, allStaff);
                    Date dayIn = promptDate(sc, "  Day in");
                    Date dayOut = promptDate(sc, "  Day out");
                    boolean bed = Boolean.parseBoolean(prompt(sc, "  Hospital bed assigned? (true/false): ").trim());
                    boolean ok = pm.registerInPatient(
                        (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                        (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                        (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11]
                    );
                    if (ok) {
                        Patient p = pm.searchPatientByID((int)b[0]);
                        if (p instanceof InPatient) {
                            ((InPatient) p).setDayIn(dayIn);
                            ((InPatient) p).setDayOut(dayOut);
                            ((InPatient) p).setHospitalBed(bed);
                        }
                        System.out.println("  In-patient registered successfully.");
                    } else {
                        System.out.println("  Registration failed (capacity full or invalid OHIP).");
                    }
                    break;
                }

                case "2": {
                    System.out.println("  --- Register Out-Patient ---");
                    Object[] b = promptBasePatientFields(sc, allStaff);
                    int apptMonths = promptInt(sc, "  Appointment timing (months): ");
                    boolean ok = pm.registerOutPatient(
                        (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                        (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                        (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11]
                    );
                    if (ok) {
                        Patient p = pm.searchPatientByID((int)b[0]);
                        if (p instanceof OutPatient) {
                            ((OutPatient) p).setAppointmentTimingMonths(apptMonths);
                        }
                        System.out.println("  Out-patient registered successfully.");
                    } else {
                        System.out.println("  Registration failed (capacity full or invalid OHIP).");
                    }
                    break;
                }

                case "3": {
                    System.out.println("  --- Register Emergency Patient ---");
                    Object[] b = promptBasePatientFields(sc, allStaff);
                    int arrivalTime = promptInt(sc, "  Arrival time (HHMM int): ");
                    Date dayIn = promptDate(sc, "  Day in");
                    Date dayOut = promptDate(sc, "  Day out");
                    String complaint = prompt(sc, "  Presenting complaint: ");
                    String arrivalType = prompt(sc, "  Arrival type: ");
                    String status = prompt(sc, "  Status: ");
                    boolean ok = pm.registerEmergencyPatient(
                        (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                        (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                        (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11]
                    );
                    if (ok) {
                        Patient p = pm.searchPatientByID((int)b[0]);
                        if (p instanceof EmergencyPatient) {
                            EmergencyPatient ep = (EmergencyPatient) p;
                            ep.setArrivalTime(arrivalTime);
                            ep.setDayIn(dayIn);
                            ep.setDayOut(dayOut);
                            ep.setPresentingComplaint(complaint);
                            ep.setArrivalType(arrivalType);
                            ep.setStatus(status);
                        }
                        System.out.println("  Emergency patient registered successfully.");
                    } else {
                        System.out.println("  Registration failed (capacity full or invalid OHIP).");
                    }
                    break;
                }

                case "4": {
                    int pid = promptInt(sc, "  Patient ID to delete: ");
                    if (pm.deletePatient(pid)) {
                        System.out.println("  Patient " + pid + " deleted.");
                    } else {
                        System.out.println("  Patient not found.");
                    }
                    break;
                }

                case "5": {
                    int pid = promptInt(sc, "  Patient ID to update: ");
                    Patient existing = pm.searchPatientByID(pid);
                    if (existing == null) {
                        System.out.println("  Patient not found.");
                        break;
                    }
                    System.out.println("  Patient found (" + existing.getClass().getSimpleName()
                                       + "). Re-enter all fields:");
                    Object[] b = promptBasePatientFields(sc, allStaff);
                    Patient updated;
                    if (existing instanceof InPatient) {
                        Date dayIn = promptDate(sc, "  Day in");
                        Date dayOut = promptDate(sc, "  Day out");
                        boolean bed = Boolean.parseBoolean(prompt(sc, "  Hospital bed? (true/false): ").trim());
                        updated = new InPatient(
                            (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                            (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                            (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11],
                            dayIn, dayOut, bed);
                    } else if (existing instanceof OutPatient) {
                        int months = promptInt(sc, "  Appointment timing (months): ");
                        updated = new OutPatient(
                            (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                            (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                            (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11],
                            months);
                    } else {
                        int arrivalTime = promptInt(sc, "  Arrival time (HHMM): ");
                        Date dayIn = promptDate(sc, "  Day in");
                        Date dayOut = promptDate(sc, "  Day out");
                        String complaint = prompt(sc, "  Presenting complaint: ");
                        String arrType = prompt(sc, "  Arrival type: ");
                        String status = prompt(sc, "  Status: ");
                        updated = new EmergencyPatient(
                            (int)b[0], (String)b[1], (String)b[2], (Date)b[3],
                            (String)b[4], (String)b[5], (int)b[6], (int)b[7],
                            (Date)b[8], (char)b[9], (int)b[10], (Staff)b[11],
                            arrivalTime, dayIn, dayOut, complaint, arrType, status);
                    }
                    if (pm.updatePatient(pid, updated)) {
                        System.out.println("  Patient updated successfully.");
                    } else {
                        System.out.println("  Update failed.");
                    }
                    break;
                }

                case "6":
                    return;

                default:
                    System.out.println("  Invalid selection.");
            }
        }
    }

    /**
     * Displays the search sub-menu and handles searching by ID or name
     * @param sc the Scanner to read from
     * @param pm the PatientManager to search in
     */
    private static void menuSearch(Scanner sc, PatientManager pm) {
        System.out.println();
        System.out.println("  Search by:");
        System.out.println("  1. Patient ID");
        System.out.println("  2. First and last name");
        System.out.print("  Selection: ");
        String sel = sc.nextLine().trim();

        if (sel.equals("1")) {
            int pid = promptInt(sc, "  Patient ID: ");
            Patient p = pm.searchPatientByID(pid);
            if (p == null) {
                System.out.println("  Not found.");
            } else {
                System.out.println(p);
            }
        } else if (sel.equals("2")) {
            String first = prompt(sc, "  First name: ");
            String last = prompt(sc, "  Last name : ");
            int pid = pm.searchPatientIDByName(first, last);
            if (pid == -1) {
                System.out.println("  Not found.");
            } else {
                System.out.println("  Found patient ID: " + pid);
                System.out.println(pm.searchPatientByID(pid));
            }
        } else {
            System.out.println("  Invalid selection.");
        }
    }

    /**
     * Displays the sort sub-menu and handles sorting by ID, date registered, or ward
     * @param sc the Scanner to read from
     * @param pm the PatientManager to sort
     */
    private static void menuSort(Scanner sc, PatientManager pm) {
        System.out.println();
        System.out.println("  Sort by:");
        System.out.println("  1. Patient ID");
        System.out.println("  2. Date registered");
        System.out.println("  3. Ward then patient ID");
        System.out.print("  Selection: ");
        String sel = sc.nextLine().trim();

        switch (sel) {
            case "1":
                pm.sortByPatientID();
                System.out.println("  Sorted by patient ID.");
                break;
            case "2":
                pm.sortByDateRegistered();
                System.out.println("  Sorted by date registered.");
                break;
            case "3":
                pm.sortByWardThenPatientID();
                System.out.println("  Sorted by ward then patient ID.");
                break;
            default:
                System.out.println("  Invalid selection.");
        }
    }

    /**
     * Displays the manage records sub-menu and handles diagnoses, medications,
     * allergies, and medical/family history operations
     * @param sc the Scanner to read from
     * @param pm the PatientManager to operate on
     */
    private static void menuManageRecords(Scanner sc, PatientManager pm) {
        System.out.println();
        System.out.println("  Manage Records:");
        System.out.println("  1.  Add diagnosis");
        System.out.println("  2.  Delete diagnosis");
        System.out.println("  3.  Update diagnosis");
        System.out.println("  4.  Add medication");
        System.out.println("  5.  Delete medication");
        System.out.println("  6.  Update medication");
        System.out.println("  7.  Add allergy");
        System.out.println("  8.  Delete allergy");
        System.out.println("  9.  Update allergy");
        System.out.println("  10. Add medical history");
        System.out.println("  11. Delete medical history");
        System.out.println("  12. Add family history");
        System.out.println("  13. Delete family history");
        System.out.print("  Selection: ");
        String sel = sc.nextLine().trim();
        System.out.println();

        int pid;
        boolean ok;

        switch (sel) {
            case "1":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.addDiagnosis(pid, prompt(sc, "  Diagnosis: "));
                if (ok) {
                    System.out.println("  Added.");
                } else {
                    System.out.println("  Failed - patient not found.");
                }
                break;
            case "2":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteDiagnosis(pid, prompt(sc, "  Diagnosis: "));
                if (ok) {
                    System.out.println("  Deleted.");
                } else {
                    System.out.println("  Failed - patient or diagnosis not found.");
                }
                break;
            case "3":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteDiagnosis(pid,
                          prompt(sc, "  Original diagnosis: "),
                          prompt(sc, "  New diagnosis: "));
                if (ok) {
                    System.out.println("  Updated.");
                } else {
                    System.out.println("  Failed - patient or original diagnosis not found.");
                }
                break;
            case "4":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.addMedication(pid,
                          prompt(sc, "  Med name: "),
                          prompt(sc, "  Dosage: "));
                if (ok) {
                    System.out.println("  Added.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "5":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteMedication(pid, prompt(sc, "  Med name: "));
                if (ok) {
                    System.out.println("  Deleted.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "6":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.updateMedication(pid,
                          prompt(sc, "  Original med name: "),
                          prompt(sc, "  New med name: "),
                          prompt(sc, "  New dosage: "));
                if (ok) {
                    System.out.println("  Updated.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "7":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.addAllergy(pid, prompt(sc, "  Allergy: "));
                if (ok) {
                    System.out.println("  Added.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "8":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteAllergy(pid, prompt(sc, "  Allergy: "));
                if (ok) {
                    System.out.println("  Deleted.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "9":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.updateAllergy(pid,
                          prompt(sc, "  Original allergy: "),
                          prompt(sc, "  New allergy: "));
                if (ok) {
                    System.out.println("  Updated.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "10":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.addMedicalHistory(pid, prompt(sc, "  Medical history: "));
                if (ok) {
                    System.out.println("  Added.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "11":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteMedicalHistory(pid, prompt(sc, "  Medical history: "));
                if (ok) {
                    System.out.println("  Deleted.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "12":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.addFamilyHistory(pid, prompt(sc, "  Family history: "));
                if (ok) {
                    System.out.println("  Added.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            case "13":
                pid = promptInt(sc, "  Patient ID: ");
                ok = pm.deleteFamilyHistory(pid, prompt(sc, "  Family history: "));
                if (ok) {
                    System.out.println("  Deleted.");
                } else {
                    System.out.println("  Failed.");
                }
                break;
            default:
                System.out.println("  Invalid selection.");
        }
    }

    /**
     * Displays the in-patient sub-menu and handles logging medications administered
     * and recording vitals
     * @param sc the Scanner to read from
     * @param pm the PatientManager to operate on
     */
    private static void menuInPatient(Scanner sc, PatientManager pm) {
        System.out.println();
        System.out.println("  In-Patient Management:");
        System.out.println("  1. Log medication administered");
        System.out.println("  2. Record vitals");
        System.out.print("  Selection: ");
        String sel = sc.nextLine().trim();
        System.out.println();

        int pid = promptInt(sc, "  Patient ID: ");

        if (sel.equals("1")) {
            String medName = prompt(sc, "  Medication name: ");
            String dosage = prompt(sc, "  Dosage: ");
            Medication med = new Medication(medName, dosage);
            boolean ok = pm.logMedicationAdministeredForPatient(pid, med);
            if (ok) {
                System.out.println("  Logged.");
            } else {
                System.out.println("  Failed - patient not found or not an in-patient.");
            }
        } else if (sel.equals("2")) {
            double hr = promptDouble(sc, "  Heart rate: ");
            double bp = promptDouble(sc, "  Blood pressure: ");
            boolean ok = pm.recordVitalsForPatient(pid, hr, bp);
            if (ok) {
                System.out.println("  Vitals recorded.");
            } else {
                System.out.println("  Failed - patient not found or not an in-patient.");
            }
        } else {
            System.out.println("  Invalid selection.");
        }
    }

    /**
     * Prompts for a new status and updates it for the specified emergency patient
     * @param sc the Scanner to read from
     * @param pm the PatientManager to operate on
     */
    private static void menuEmergencyPatient(Scanner sc, PatientManager pm) {
        System.out.println();
        int pid = promptInt(sc, "  Patient ID: ");
        String status = prompt(sc, "  New status: ");
        boolean ok = pm.setEmergencyPatientStatus(pid, status);
        if (ok) {
            System.out.println("  Status updated.");
        } else {
            System.out.println("  Failed - patient not found or not an emergency patient.");
        }
    }

    /**
     * Entry point for the hospital management system
     * Loads patient and appointment data from files if available,
     * then presents the main menu until the user exits
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        PatientManager pm = new PatientManager(MAX_PATIENTS);
        boolean patientsLoaded = pm.loadPatientInfo(PATIENT_FILE);
        if (patientsLoaded) {
            System.out.println("Patients loaded from " + PATIENT_FILE + ".");
            boolean apptsLoaded = pm.loadPatientAppts(APPT_FILE);
            if (apptsLoaded) {
                System.out.println("Appointments loaded from " + APPT_FILE + ".");
            } else {
                System.out.println("No appointment file found.");
            }
        } else {
            System.out.println("No patient file found.");
        }

        // change later
        Staff[] allStaff = new Staff[0];

        boolean exit = false;

        while (!exit) {
            System.out.println();
            System.out.println("========== Main Menu ==========");
            System.out.println("1. Manage Appointments");
            System.out.println("2. Manage Patients");
            System.out.println("3. Manage Staff");
            System.out.println("4. Save and exit");
            System.out.print("Selection: ");
            String mainSel = sc.nextLine().trim();
            System.out.println();

            switch (mainSel) {
                case "1":
                case "3":
                    break;

                case "4":
                    exit = true;
                    break;

                case "2":
                    boolean backToMain = false;
                    while (!backToMain) {
                        System.out.println();
                        System.out.println("  ===== Patient Management =====");
                        System.out.println("  1.  Register, delete, or update a patient");
                        System.out.println("  2.  Search for a patient");
                        System.out.println("  3.  Sort patients");
                        System.out.println("  4.  Manage diagnoses, medications, allergies, history");
                        System.out.println("  5.  In-patient: log medications administered / vitals");
                        System.out.println("  6.  Emergency patient: update status");
                        System.out.println("  7.  Update assigned staff for a patient");
                        System.out.println("  8.  Check in a patient");
                        System.out.println("  9.  Check out a patient");
                        System.out.println("  10. Calculate total cost for a patient");
                        System.out.println("  11. List all patients");
                        System.out.println("  12. List all appointments for a patient");
                        System.out.println("  13. Back to main menu");
                        System.out.print("  Selection: ");
                        String patSel = sc.nextLine().trim();
                        System.out.println();

                        switch (patSel) {
                            case "1":
                                menuRegisterDeleteUpdate(sc, pm, allStaff);
                                break;

                            case "2":
                                menuSearch(sc, pm);
                                break;

                            case "3":
                                menuSort(sc, pm);
                                break;

                            case "4":
                                menuManageRecords(sc, pm);
                                break;

                            case "5":
                                menuInPatient(sc, pm);
                                break;

                            case "6":
                                menuEmergencyPatient(sc, pm);
                                break;

                            case "7": {
                                int pid = promptInt(sc, "  Patient ID: ");
                                String sid = prompt(sc, "  Staff ID: ");
                                Staff s = findStaff(allStaff, sid);
                                if (s == null) {
                                    System.out.println("  Staff ID \"" + sid + "\" not found.");
                                } else {
                                    boolean ok = pm.updateAssignedStaffForPatient(pid, s);
                                    if (ok) {
                                        System.out.println("  Staff updated.");
                                    } else {
                                        System.out.println("  Patient not found.");
                                    }
                                }
                                break;
                            }

                            case "8": {
                                int pid = promptInt(sc, "  Patient ID: ");
                                boolean ok = pm.checkInPatient(pid);
                                if (ok) {
                                    System.out.println("  Checked in.");
                                } else {
                                    System.out.println("  Patient not found.");
                                }
                                break;
                            }

                            case "9": {
                                int pid = promptInt(sc, "  Patient ID: ");
                                String follow = prompt(sc, "  Follow-up type: ");
                                boolean ok = pm.checkOutPatient(pid, follow);
                                if (ok) {
                                    System.out.println("  Checked out.");
                                } else {
                                    System.out.println("  Patient not found.");
                                }
                                break;
                            }

                            case "10": {
                                int pid = promptInt(sc, "  Patient ID: ");
                                double cost = pm.calculateTotalCostForPatient(pid);
                                if (cost < 0) {
                                    System.out.println("  Patient not found.");
                                } else {
                                    System.out.printf("  Total cost: $%.2f%n", cost);
                                }
                                break;
                            }

                            case "11":
                                System.out.println(pm.listAllPatients());
                                break;

                            case "12": {
                                int pid = promptInt(sc, "  Patient ID: ");
                                String appts = pm.listAppointmentsForPatient(pid);
                                if (appts == null) {
                                    System.out.println("  Patient not found.");
                                } else {
                                    System.out.println(appts);
                                }
                                break;
                            }

                            case "13":
                                backToMain = true;
                                break;

                            default:
                                System.out.println("  Invalid selection.");
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid selection.");
            }
        }

        boolean savedPatients = pm.savePatientInfo(PATIENT_FILE);
        boolean savedAppts = pm.savePatientAppts(APPT_FILE);
        if (savedPatients) {
            System.out.println("Patients saved to " + PATIENT_FILE + ".");
        } else {
            System.out.println("Error saving patients.");
        }
        if (savedAppts) {
            System.out.println("Appointments saved to " + APPT_FILE + ".");
        } else {
            System.out.println("Error saving appointments.");
        }
        sc.close();
    }
}