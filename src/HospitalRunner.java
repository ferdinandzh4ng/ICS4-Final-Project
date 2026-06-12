import appointment.*;
import patient.*;
import shared.*;
import staff.*;

import java.util.Scanner;

/**
 * File: HospitalRunner.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 2, 2026
 * Description: This class runs the hospital management system,
 *              providing menus to manage patients, appointments, and staff.
 */
public class HospitalRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        StaffManager staffManager = new StaffManager(50);
        PatientManager patientManager = new PatientManager(100);
        ApptManager apptManager = new ApptManager(200, staffManager, patientManager);

        staffManager.loadFromFile("data/staff.txt");
        boolean patientsLoaded = patientManager.loadPatientInfo("data/patients.txt");
        if (!patientsLoaded) {
            System.out.println("Warning: could not load patients.txt — file may be corrupt or missing.");
        }
        apptManager.loadFromFile("data/appointments.txt");
        patientManager.syncAppointmentsFromManager(apptManager);
        staffManager.syncSchedulesFromAppointments(
                apptManager.getAppointments(), apptManager.getNumAppointments());

        int nextApptID = 5000;
        Appointment[] loaded = apptManager.getAppointments();
        for (int i = 0; i < apptManager.getNumAppointments(); i++) {
            if (loaded[i] != null && loaded[i].getApptID() >= nextApptID) {
                nextApptID = loaded[i].getApptID() + 1;
            }
        }

        System.out.println("=== Hospital Management System ===");
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("--- Main Menu ---");
            System.out.println("[1] Staff Management");
            System.out.println("[2] Patient Management");
            System.out.println("[3] Appointment Management");
            System.out.println("[4] Save All Data");
            System.out.println("[5] Exit");

            int choice = -1;
            while (choice == -1) {
                System.out.print("> ");
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Error: please enter a valid integer.");
                }
            }

            if (choice == 1) {
                // ── Staff Management submenu ──────────────────────────────────────────────
                boolean staffBack = false;
                while (!staffBack) {
                    System.out.println();
                    System.out.println("--- Staff Management ---");
                    System.out.println("[1] Search by Specialty and Experience");
                    System.out.println("[2] Search by Name");
                    System.out.println("[3] Sort Staff by Name");
                    System.out.println("[4] Sort Staff by Experience");
                    System.out.println("[5] List All Staff");
                    System.out.println("[6] View Staff Schedule");
                    System.out.println("[7] Run Payroll");
                    System.out.println("[8] Add Staff");
                    System.out.println("[9] Remove Staff");
                    System.out.println("[10] Edit Staff");
                    System.out.println("[11] Back");

                    int staffChoice = -1;
                    while (staffChoice == -1) {
                        System.out.print("> ");
                        try {
                            staffChoice = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Error: please enter a valid integer.");
                        }
                    }

                    if (staffChoice == 1) {
                        System.out.println("--- Search Staff by Specialty and Experience ---");
                        System.out.print("Specialty: ");
                        String specialty = scanner.nextLine().trim();
                        int minExp = -1;
                        while (minExp == -1) {
                            System.out.print("Minimum years of experience: ");
                            try {
                                minExp = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        Staff[] results = staffManager.findStaff(specialty, minExp);
                        System.out.println("Results (" + results.length + " found):");
                        for (int i = 0; i < results.length; i++) {
                            Staff s = results[i];
                            String line = "[" + (i + 1) + "] " + s.getName()
                                    + "  -- " + s.getSpecialization()
                                    + ", " + s.getExperience() + " years";
                            if (s instanceof Doctor) {
                                line += ", License: " + ((Doctor) s).getLicenseNumber();
                            }
                            System.out.println(line);
                        }

                    } else if (staffChoice == 2) {
                        System.out.print("Staff name: ");
                        String name = scanner.nextLine().trim();
                        Staff found = staffManager.findStaff(name);
                        if (found == null) {
                            System.out.println("No staff member found with that name.");
                        } else {
                            System.out.println(staffManager.formatStaffDetails(found));
                        }

                    } else if (staffChoice == 3) {
                        staffManager.sortStaff();
                        System.out.println("--- Staff Sorted by Name ---");
                        System.out.printf("%-20s %-12s %-18s %-12s%n",
                                "Name", "Staff ID", "Specialization", "Type");
                        Staff[] staff = staffManager.getStaffArray();
                        for (int i = 0; i < staff.length; i++) {
                            Staff s = staff[i];
                            if (s == null) {
                                continue;
                            }
                            String typeName = s.getClass().getSimpleName();
                            System.out.printf("%-20s %-12s %-18s %-12s%n",
                                    s.getName(), s.getStaffID(), s.getSpecialization(), typeName);
                        }

                    } else if (staffChoice == 4) {
                        staffManager.sortStaffByExp();
                        System.out.println("--- Staff Sorted by Experience ---");
                        System.out.printf("%-20s %-12s %-18s %-12s %-12s%n",
                                "Name", "Staff ID", "Specialization", "Experience", "Type");
                        Staff[] staff = staffManager.getStaffArray();
                        for (int i = 0; i < staff.length; i++) {
                            Staff s = staff[i];
                            if (s == null) {
                                continue;
                            }
                            String typeName = s.getClass().getSimpleName();
                            System.out.printf("%-20s %-12s %-18s %-12d %-12s%n",
                                    s.getName(), s.getStaffID(), s.getSpecialization(),
                                    s.getExperience(), typeName);
                        }

                    } else if (staffChoice == 5) {
                        Staff[] staff = staffManager.getStaffArray();
                        if (staff.length == 0) {
                            System.out.println("No staff on record.");
                        } else {
                            for (Staff s : staff) {
                                System.out.println(s.getName() + " (" + s.getStaffID() + ") — "
                                        + s.getSpecialization());
                            }
                        }

                    } else if (staffChoice == 6) {
                        System.out.print("Staff name: ");
                        String name = scanner.nextLine().trim();
                        Staff found = staffManager.findStaff(name);
                        if (found == null) {
                            System.out.println("Error: staff member not found.");
                        } else {
                            System.out.println(staffManager.checkShifts(found));
                        }

                    } else if (staffChoice == 7) {
                        System.out.printf("Total payroll: $%.2f%n", staffManager.runPayroll());

                    } else if (staffChoice == 8) {
                        System.out.println("--- Add Staff ---");
                        System.out.println("[1] Doctor  [2] Nurse  [3] Surgeon");
                        int roleChoice = -1;
                        while (roleChoice == -1) {
                            System.out.print("> ");
                            try {
                                roleChoice = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        if (roleChoice < 1 || roleChoice > 3) {
                            System.out.println("Error: invalid role.");
                            continue;
                        }
                        System.out.print("Staff ID: ");
                        String staffID = scanner.nextLine().trim();
                        System.out.print("Name: ");
                        String staffName = scanner.nextLine().trim();
                        int experience = -1;
                        while (experience == -1) {
                            System.out.print("Years of experience: ");
                            try {
                                experience = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        System.out.print("Specialization: ");
                        String staffSpec = scanner.nextLine().trim();
                        String[] offDays = new String[10];
                        Appointment[] schedule = new Appointment[200];

                        if (roleChoice == 1) {
                            System.out.print("License number: ");
                            String license = scanner.nextLine().trim();
                            double fee = -1;
                            while (fee == -1) {
                                System.out.print("Consultation fee: ");
                                try {
                                    fee = Double.parseDouble(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid number.");
                                }
                            }
                            int maxPatients = -1;
                            while (maxPatients == -1) {
                                System.out.print("Max patients: ");
                                try {
                                    maxPatients = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            staffManager.addStaff(new Doctor(staffID, staffName, experience,
                                    staffSpec, offDays, schedule, license, fee, maxPatients));

                        } else if (roleChoice == 2) {
                            System.out.print("Ward: ");
                            String ward = scanner.nextLine().trim();
                            System.out.print("Shift type (Day/Night/Rotating): ");
                            String shiftType = scanner.nextLine().trim();
                            double hourlyRate = -1;
                            while (hourlyRate == -1) {
                                System.out.print("Hourly rate: ");
                                try {
                                    hourlyRate = Double.parseDouble(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid number.");
                                }
                            }
                            int hoursWorked = -1;
                            while (hoursWorked == -1) {
                                System.out.print("Hours worked this week: ");
                                try {
                                    hoursWorked = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            staffManager.addStaff(new Nurse(staffID, staffName, experience,
                                    staffSpec, offDays, schedule, ward, shiftType,
                                    hourlyRate, hoursWorked));

                        } else {
                            int operatingRoom = -1;
                            while (operatingRoom == -1) {
                                System.out.print("Operating room number: ");
                                try {
                                    operatingRoom = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            int surgeriesDone = -1;
                            while (surgeriesDone == -1) {
                                System.out.print("Surgeries completed: ");
                                try {
                                    surgeriesDone = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            System.out.print("Specialty area: ");
                            String specialtyArea = scanner.nextLine().trim();
                            double surgeryFee = -1;
                            while (surgeryFee == -1) {
                                System.out.print("Surgery fee per procedure: ");
                                try {
                                    surgeryFee = Double.parseDouble(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid number.");
                                }
                            }
                            staffManager.addStaff(new Surgeon(staffID, staffName, experience,
                                    staffSpec, offDays, schedule, operatingRoom, surgeriesDone,
                                    specialtyArea, surgeryFee));
                        }
                        System.out.println("Staff member added.");

                    } else if (staffChoice == 9) {
                        System.out.print("Staff ID to remove: ");
                        String removeID = scanner.nextLine().trim();
                        if (staffManager.removeStaff(removeID)) {
                            System.out.println("Staff member removed.");
                        } else {
                            System.out.println("Error: staff member not found.");
                        }

                    } else if (staffChoice == 10) {
                        System.out.print("Staff ID to edit: ");
                        String editID = scanner.nextLine().trim();
                        System.out.print("New name: ");
                        String newName = scanner.nextLine().trim();
                        int newExp = -1;
                        while (newExp == -1) {
                            System.out.print("New years of experience: ");
                            try {
                                newExp = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        System.out.print("New specialization: ");
                        String newSpec = scanner.nextLine().trim();
                        if (staffManager.updateStaff(editID, newName, newExp, newSpec)) {
                            System.out.println("Staff member updated.");
                        } else {
                            System.out.println("Error: staff member not found.");
                        }

                    } else if (staffChoice == 11) {
                        staffBack = true;

                    } else {
                        System.out.println("Error: invalid choice.");
                    }
                }

            } else if (choice == 2) {
                // ── Patient Management submenu ────────────────────────────────────────────
                boolean patientBack = false;
                while (!patientBack) {
                    System.out.println();
                    System.out.println("--- Patient Management ---");
                    System.out.println("[1] Register InPatient");
                    System.out.println("[2] Register OutPatient");
                    System.out.println("[3] Register Emergency Patient");
                    System.out.println("[4] Search by Patient ID");
                    System.out.println("[5] Search by Patient Name");
                    System.out.println("[6] Sort Patients by Ward then Patient ID");
                    System.out.println("[7] Sort Patients by Date Registered");
                    System.out.println("[8] Sort Patients by Patient ID");
                    System.out.println("[9] List All Patients");
                    System.out.println("[10] Check In Patient");
                    System.out.println("[11] Check Out Patient");
                    System.out.println("[12] View Patient Appointments");
                    System.out.println("[13] Delete Patient");
                    System.out.println("[14] Medical Records");
                    System.out.println("[15] Back");

                    int patientChoice = -1;
                    while (patientChoice == -1) {
                        System.out.print("> ");
                        try {
                            patientChoice = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Error: please enter a valid integer.");
                        }
                    }

                    if (patientChoice >= 1 && patientChoice <= 3) {
                        // ── Shared base fields ──
                        String type;
                        if (patientChoice == 1) {
                            type = "InPatient";
                        } else if (patientChoice == 2) {
                            type = "OutPatient";
                        } else {
                            type = "EmergencyPatient";
                        }
                        System.out.println("--- Register " + type + " ---");
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        System.out.print("First name: ");
                        String first = scanner.nextLine().trim();
                        System.out.print("Last name: ");
                        String last = scanner.nextLine().trim();
                        System.out.print("Date of birth (YYYY-MM-DD): ");
                        String dobStr = scanner.nextLine().trim();
                        if (!dobStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date dob = new Date(dobStr);
                        System.out.print("Ward: ");
                        String ward = scanner.nextLine().trim();
                        System.out.print("Address: ");
                        String address = scanner.nextLine().trim();
                        long phone = -1;
                        while (phone == -1) {
                            System.out.print("Phone number: ");
                            try {
                                phone = Long.parseLong(scanner.nextLine().trim());
                                if (phone < 0) {
                                    phone = -1;
                                    System.out.println("Error: phone number must be positive.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid number.");
                            }
                        }
                        int ohip = -1;
                        while (ohip == -1) {
                            System.out.print("OHIP number (10 digits): ");
                            try {
                                ohip = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        System.out.print("Date registered (YYYY-MM-DD): ");
                        String regStr = scanner.nextLine().trim();
                        if (!regStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date registered = new Date(regStr);
                        System.out.print("Gender (M/F): ");
                        String genderStr = scanner.nextLine().trim();
                        char gender;
                        if (genderStr.isEmpty()) {
                            gender = ' ';
                        } else {
                            gender = genderStr.charAt(0);
                        }
                        long emergencyPhone = -1;
                        while (emergencyPhone == -1) {
                            System.out.print("Emergency contact phone: ");
                            try {
                                emergencyPhone = Long.parseLong(scanner.nextLine().trim());
                                if (emergencyPhone < 0) {
                                    emergencyPhone = -1;
                                    System.out.println("Error: phone number must be positive.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid number.");
                            }
                        }

                        // ── Type-specific fields ──
                        boolean ok;
                        if (type.equals("InPatient")) {
                            System.out.print("Day in (YYYY-MM-DD, or 0-0-0 if not yet admitted): ");
                            String dayInStr = scanner.nextLine().trim();
                            Date dayIn;
                            if (dayInStr.equals("0-0-0")) {
                                dayIn = null;
                            } else {
                                dayIn = new Date(dayInStr);
                            }
                            System.out.print("Day out (YYYY-MM-DD, or 0-0-0 if not yet discharged): ");
                            String dayOutStr = scanner.nextLine().trim();
                            Date dayOut;
                            if (dayOutStr.equals("0-0-0")) {
                                dayOut = null;
                            } else {
                                dayOut = new Date(dayOutStr);
                            }
                            System.out.print("Hospital bed assigned (true/false): ");
                            boolean hospitalBed = Boolean.parseBoolean(scanner.nextLine().trim());
                            ok = patientManager.registerInPatient(id, first, last, dob, ward,
                                    address, phone, ohip, registered, gender, emergencyPhone, null,
                                    dayIn, dayOut, hospitalBed);

                        } else if (type.equals("OutPatient")) {
                            int apptTiming = -1;
                            while (apptTiming == -1) {
                                System.out.print("Months until next appointment: ");
                                try {
                                    apptTiming = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            ok = patientManager.registerOutPatient(id, first, last, dob, ward,
                                    address, phone, ohip, registered, gender, emergencyPhone, null,
                                    apptTiming);

                        } else {
                            int arrivalTime = -1;
                            while (arrivalTime == -1) {
                                System.out.print("Arrival time (HHMM, e.g. 1430): ");
                                try {
                                    arrivalTime = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            System.out.print("Day in (YYYY-MM-DD, or 0-0-0 if not yet admitted): ");
                            String dayInStr = scanner.nextLine().trim();
                            Date dayIn;
                            if (dayInStr.equals("0-0-0")) {
                                dayIn = null;
                            } else {
                                dayIn = new Date(dayInStr);
                            }
                            System.out.print("Day out (YYYY-MM-DD, or 0-0-0 if not yet discharged): ");
                            String dayOutStr = scanner.nextLine().trim();
                            Date dayOut;
                            if (dayOutStr.equals("0-0-0")) {
                                dayOut = null;
                            } else {
                                dayOut = new Date(dayOutStr);
                            }
                            System.out.print("Presenting complaint: ");
                            String complaint = scanner.nextLine().trim();
                            System.out.print("Arrival type (Ambulance/Walk-in): ");
                            String arrivalType = scanner.nextLine().trim();
                            System.out.print("Status (Awaiting triage/Stable/Critical/In Treatment/Discharged): ");
                            String status = scanner.nextLine().trim();
                            ok = patientManager.registerEmergencyPatient(id, first, last, dob, ward,
                                    address, phone, ohip, registered, gender, emergencyPhone, null,
                                    arrivalTime, dayIn, dayOut, complaint, arrivalType, status);
                        }

                        if (ok) {
                            System.out.println("Patient registered successfully.");
                        } else {
                            System.out.println("Error: could not register patient (check OHIP or capacity).");
                        }

                    } else if (patientChoice == 4) {
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        Patient p = patientManager.searchPatientByPatientID(id);
                        if (p == null) {
                            System.out.println("No patient found with that ID.");
                        } else {
                            System.out.println(p.toString());
                        }

                    } else if (patientChoice == 5) {
                        System.out.print("First name: ");
                        String firstName = scanner.nextLine().trim();
                        System.out.print("Last name: ");
                        String lastName = scanner.nextLine().trim();
                        Patient p = patientManager.searchPatientByName(firstName, lastName);
                        if (p == null) {
                            System.out.println("No patient found with that name.");
                        } else {
                            System.out.println(p.toString());
                        }

                    } else if (patientChoice == 6) {
                        patientManager.sortByWardThenPatientID();
                        System.out.println("--- Patients Sorted by Ward ---");
                        System.out.printf("%-12s %-20s %-6s %-16s %-12s%n",
                                "Ward", "Name", "ID", "Type", "Admitted");
                        Patient[] patients = patientManager.getPatients();
                        for (int i = 0; i < patientManager.getNumPatients(); i++) {
                            Patient p = patients[i];
                            if (p == null) {
                                continue;
                            }
                            String admitted = p.getAdmittedDateDisplay();
                            System.out.printf("%-12s %-20s %04d   %-16s %-12s%n",
                                    p.getWard(), p.getLastName() + ", " + p.getFirstName(),
                                    p.getPatientID(), p.getClass().getSimpleName(), admitted);
                        }

                    } else if (patientChoice == 7) {
                        patientManager.sortByDateEntered();
                        System.out.println("Patients sorted by date registered.\n");
                        System.out.println(patientManager.listAllPatients());

                    } else if (patientChoice == 8) {
                        patientManager.sortByPatientID();
                        System.out.println("Patients sorted by patient ID.\n");
                        System.out.println(patientManager.listAllPatients());

                    } else if (patientChoice == 9) {
                        System.out.println(patientManager.listAllPatients());

                    } else if (patientChoice == 10) {
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        if (patientManager.checkInPatient(id)) {
                            System.out.println("Patient checked in successfully.");
                        } else {
                            System.out.println("Error: check-in failed.");
                        }

                    } else if (patientChoice == 11) {
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        Patient p = patientManager.searchPatientByPatientID(id);
                        if (p == null) {
                            System.out.println("Error: patient not found.");
                        } else {
                            System.out.println("--- Check Out: " + p.getFirstName() + " "
                                    + p.getLastName() + " (ID: " + id + ") ---");
                            System.out.println("Ward: " + p.getWard());
                            if (p instanceof InPatient) {
                                InPatient ip = (InPatient) p;
                                System.out.println("Days admitted: " + ip.getDaysAdmitted());
                                System.out.printf("Room fee (%d days x $%.2f)     = $%.2f%n",
                                        ip.getDaysAdmitted(), InPatient.DAILY_ROOM_RATE,
                                        ip.getRoomFee());
                                System.out.printf("Appointment fees                = $%.2f%n",
                                        ip.getAppointmentFees());
                                System.out.println("----------------------------------------");
                                System.out.printf("Total Bill                      = $%.2f%n",
                                        ip.calculateBill());
                            }
                            System.out.println("Follow-up type: [1] Routine Checkup  [2] Surgery  [3] None");
                            int followChoice = -1;
                            while (followChoice == -1) {
                                System.out.print("> ");
                                try {
                                    followChoice = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            String followUp;
                            if (followChoice == 1) {
                                followUp = "checkup";
                            } else if (followChoice == 2) {
                                followUp = "surgery";
                            } else {
                                followUp = "none";
                            }
                            if (patientManager.checkOutPatient(id, followUp)) {
                                System.out.println("Checked out successfully.");
                                if (p instanceof InPatient) {
                                    InPatient ip = (InPatient) p;
                                    if (ip.getDayOut() != null) {
                                        System.out.println("Day out        : "
                                                + ip.getDayOut().toISODateString());
                                    }
                                    for (Appointment a : p.getUpcomingAppointments()) {
                                        if (a != null && a.getDate().compareTo(PatientManager.CUR_DATE) > 0) {
                                            System.out.println("Next appointment: "
                                                    + a.getDate().toISODateString());
                                            break;
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Error: check-out failed.");
                            }
                        }
                    } else if (patientChoice == 12) {
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        String listing = patientManager.listAppointmentsForPatient(id);
                        if (listing == null) {
                            System.out.println("Error: patient not found.");
                        } else {
                            System.out.println(listing);
                        }

                    } else if (patientChoice == 13) {
                        int id = -1;
                        while (id == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                id = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        if (patientManager.deletePatient(id)) {
                            System.out.println("Patient deleted.");
                        } else {
                            System.out.println("Error: patient not found.");
                        }

                    } else if (patientChoice == 14) {
                        boolean medBack = false;
                        while (!medBack) {
                            System.out.println();
                            System.out.println("--- Medical Records ---");
                            System.out.println("[1] Add Diagnosis");
                            System.out.println("[2] Delete Diagnosis");
                            System.out.println("[3] Update Diagnosis");
                            System.out.println("[4] Add Medication");
                            System.out.println("[5] Delete Medication");
                            System.out.println("[6] Update Medication");
                            System.out.println("[7] Add Allergy");
                            System.out.println("[8] Delete Allergy");
                            System.out.println("[9] Update Allergy");
                            System.out.println("[10] Add Medical History");
                            System.out.println("[11] Delete Medical History");
                            System.out.println("[12] Add Family History");
                            System.out.println("[13] Delete Family History");
                            System.out.println("[14] Record Vitals (InPatient)");
                            System.out.println("[15] Log Medication Administered (InPatient)");
                            System.out.println("[16] Back");

                            int medChoice = -1;
                            while (medChoice == -1) {
                                System.out.print("> ");
                                try {
                                    medChoice = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }

                            int medPatientID = -1;
                            if (medChoice >= 1 && medChoice <= 15) {
                                while (medPatientID == -1) {
                                    System.out.print("Patient ID: ");
                                    try {
                                        medPatientID = Integer.parseInt(scanner.nextLine().trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Error: please enter a valid integer.");
                                    }
                                }
                            }

                            if (medChoice == 1) {
                                System.out.print("Diagnosis: ");
                                String diagnosis = scanner.nextLine().trim();
                                if (patientManager.addDiagnosis(medPatientID, diagnosis)) {
                                    System.out.println("Diagnosis added.");
                                } else {
                                    System.out.println("Error: patient not found.");
                                }

                            } else if (medChoice == 2) {
                                System.out.print("Diagnosis to delete: ");
                                String diagnosis = scanner.nextLine().trim();
                                if (patientManager.deleteDiagnosis(medPatientID, diagnosis)) {
                                    System.out.println("Diagnosis deleted.");
                                } else {
                                    System.out.println("Error: diagnosis not found or patient not found.");
                                }

                            } else if (medChoice == 3) {
                                System.out.print("Original diagnosis: ");
                                String orgDiagnosis = scanner.nextLine().trim();
                                System.out.print("New diagnosis: ");
                                String newDiagnosis = scanner.nextLine().trim();
                                if (patientManager.updateDiagnosis(medPatientID, orgDiagnosis, newDiagnosis)) {
                                    System.out.println("Diagnosis updated.");
                                } else {
                                    System.out.println("Error: could not update diagnosis.");
                                }

                            } else if (medChoice == 4) {
                                System.out.print("Medication name: ");
                                String medName = scanner.nextLine().trim();
                                System.out.print("Dosage: ");
                                String dosage = scanner.nextLine().trim();
                                if (patientManager.addMedication(medPatientID, medName, dosage)) {
                                    System.out.println("Medication added.");
                                } else {
                                    System.out.println("Error: patient not found or medication allergy.");
                                }

                            } else if (medChoice == 5) {
                                System.out.print("Medication name to delete: ");
                                String medName = scanner.nextLine().trim();
                                if (patientManager.deleteMedication(medPatientID, medName)) {
                                    System.out.println("Medication deleted.");
                                } else {
                                    System.out.println("Error: medication not found or patient not found.");
                                }

                            } else if (medChoice == 6) {
                                System.out.print("Original medication name: ");
                                String orgMed = scanner.nextLine().trim();
                                System.out.print("New medication name: ");
                                String newMed = scanner.nextLine().trim();
                                System.out.print("New dosage: ");
                                String newDosage = scanner.nextLine().trim();
                                if (patientManager.updateMedication(medPatientID, orgMed, newMed, newDosage)) {
                                    System.out.println("Medication updated.");
                                } else {
                                    System.out.println("Error: could not update medication.");
                                }

                            } else if (medChoice == 7) {
                                System.out.print("Allergy: ");
                                String allergy = scanner.nextLine().trim();
                                if (patientManager.addAllergy(medPatientID, allergy)) {
                                    System.out.println("Allergy added.");
                                } else {
                                    System.out.println("Error: patient not found.");
                                }

                            } else if (medChoice == 8) {
                                System.out.print("Allergy to delete: ");
                                String allergy = scanner.nextLine().trim();
                                if (patientManager.deleteAllergy(medPatientID, allergy)) {
                                    System.out.println("Allergy deleted.");
                                } else {
                                    System.out.println("Error: allergy not found or patient not found.");
                                }

                            } else if (medChoice == 9) {
                                System.out.print("Original allergy: ");
                                String orgAllergy = scanner.nextLine().trim();
                                System.out.print("New allergy: ");
                                String newAllergy = scanner.nextLine().trim();
                                if (patientManager.updateAllergy(medPatientID, orgAllergy, newAllergy)) {
                                    System.out.println("Allergy updated.");
                                } else {
                                    System.out.println("Error: could not update allergy.");
                                }

                            } else if (medChoice == 10) {
                                System.out.print("Medical history entry: ");
                                String medHistory = scanner.nextLine().trim();
                                if (patientManager.addMedicalHistory(medPatientID, medHistory)) {
                                    System.out.println("Medical history added.");
                                } else {
                                    System.out.println("Error: patient not found.");
                                }

                            } else if (medChoice == 11) {
                                System.out.print("Medical history entry to delete: ");
                                String medHistory = scanner.nextLine().trim();
                                if (patientManager.deleteMedicalHistory(medPatientID, medHistory)) {
                                    System.out.println("Medical history deleted.");
                                } else {
                                    System.out.println("Error: entry not found or patient not found.");
                                }

                            } else if (medChoice == 12) {
                                System.out.print("Family history entry: ");
                                String famHistory = scanner.nextLine().trim();
                                if (patientManager.addFamilyHistory(medPatientID, famHistory)) {
                                    System.out.println("Family history added.");
                                } else {
                                    System.out.println("Error: patient not found.");
                                }

                            } else if (medChoice == 13) {
                                System.out.print("Family history entry to delete: ");
                                String famHistory = scanner.nextLine().trim();
                                if (patientManager.deleteFamilyHistory(medPatientID, famHistory)) {
                                    System.out.println("Family history deleted.");
                                } else {
                                    System.out.println("Error: entry not found or patient not found.");
                                }

                            } else if (medChoice == 14) {
                                double heartRate = -1;
                                while (heartRate == -1) {
                                    System.out.print("Heart rate: ");
                                    try {
                                        heartRate = Double.parseDouble(scanner.nextLine().trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Error: please enter a valid number.");
                                    }
                                }
                                double bloodPressure = -1;
                                while (bloodPressure == -1) {
                                    System.out.print("Blood pressure: ");
                                    try {
                                        bloodPressure = Double.parseDouble(scanner.nextLine().trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Error: please enter a valid number.");
                                    }
                                }
                                if (patientManager.recordVitalsForPatient(medPatientID, heartRate, bloodPressure)) {
                                    System.out.println("Vitals recorded.");
                                } else {
                                    System.out.println("Error: patient not found or not an InPatient.");
                                }

                            } else if (medChoice == 15) {
                                System.out.print("Medication name: ");
                                String medName = scanner.nextLine().trim();
                                System.out.print("Dosage: ");
                                String dosage = scanner.nextLine().trim();
                                if (patientManager.logMedicationAdministeredForPatient(medPatientID,
                                        new Medication(medName, dosage))) {
                                    System.out.println("Medication administration logged.");
                                } else {
                                    System.out.println("Error: patient not found or not an InPatient.");
                                }

                            } else if (medChoice == 16) {
                                medBack = true;

                            } else {
                                System.out.println("Error: invalid choice.");
                            }
                        }
                    } else if (patientChoice == 15) {
                        patientBack = true;
                    } else {
                        System.out.println("Error: invalid choice.");
                    }
                }

            } else if (choice == 3) {
                // ── Appointment Management submenu ────────────────────────────────────────
                boolean apptBack = false;
                while (!apptBack) {
                    System.out.println();
                    System.out.println("--- Appointment Management ---");
                    System.out.println("[1] Book Appointment");
                    System.out.println("[2] Cancel Appointment");
                    System.out.println("[3] Reschedule Appointment");
                    System.out.println("[4] View Daily Schedule");
                    System.out.println("[5] View Upcoming Appointments");
                    System.out.println("[6] Daily Cost Summary");
                    System.out.println("[7] Sort Appointments by Date");
                    System.out.println("[8] Sort Appointments by Patient and Date");
                    System.out.println("[9] Back");

                    int apptChoice = -1;
                    while (apptChoice == -1) {
                        System.out.print("> ");
                        try {
                            apptChoice = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Error: please enter a valid integer.");
                        }
                    }

                    if (apptChoice == 1) {
                        System.out.println("--- Book Appointment ---");
                        System.out.println("[1] Routine Checkup  [2] Surgery  [3] Emergency Visit");
                        int bookType = -1;
                        while (bookType == -1) {
                            System.out.print("> ");
                            try {
                                bookType = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        int patientID = -1;
                        while (patientID == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                patientID = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        Patient patient = patientManager.searchPatientByPatientID(patientID);
                        if (patient == null) {
                            System.out.println("Error: patient not found.");
                            continue;
                        }
                        System.out.print("Date (YYYY-MM-DD): ");
                        String dateStr = scanner.nextLine().trim();
                        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date date = new Date(dateStr);
                        double time = -1;
                        while (time == -1) {
                            System.out.print("Time (e.g. 10.30): ");
                            try {
                                time = Double.parseDouble(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid number.");
                            }
                        }

                        if (bookType == 1) {
                            Doctor doctor = null;
                            for (Staff s : staffManager.getStaffArray()) {
                                if (s instanceof Doctor && !s.hasTimeConflict(date, time)) {
                                    doctor = (Doctor) s;
                                    break;
                                }
                            }
                            if (doctor == null) {
                                System.out.println("Error: no available doctor at that time.");
                                continue;
                            }
                            RoutineCheckup appt = new RoutineCheckup(
                                    nextApptID, patient, new Staff[1], date, time,
                                    0.0, 0.0, Appointment.STATUS_SCHEDULED, 0, doctor);
                            appt.estimateDuration("Annual Physical");
                            appt.assignStaff(doctor);
                            if (appt.assignClinicRoom(apptManager) <= 0) {
                                System.out.println("Error: no clinic room available.");
                                continue;
                            }
                            if (!apptManager.addAppointment(appt)) {
                                System.out.println("Error: could not book appointment.");
                                continue;
                            }
                            patientManager.addAppointment(patientID, appt);
                            staffManager.addShift(doctor.getName(), appt);
                            int hours = (int) time;
                            int minutes = (int) Math.round((time - hours) * 100);
                            String timeFormatted = String.format("%02d:%02d", hours, minutes);
                            System.out.println("Appointment booked successfully.");
                            System.out.println("Appointment ID   : " + appt.getApptID());
                            System.out.println("Patient          : " + patient.getFirstName()
                                    + " " + patient.getLastName() + " (ID: " + patientID + ")");
                            System.out.println("Staff            : Dr. " + doctor.getName()
                                    + " (Doctor)");
                            System.out.println("Date / Time      : " + date.toISODateString()
                                    + " at " + timeFormatted);
                            System.out.println("Clinic Room      : " + appt.getRoomNum());
                            System.out.println("Est. Duration    : "
                                    + (int) (appt.getDuration() * 60) + " min");
                            System.out.printf("Cost             : $%.2f%n", appt.calculateCost());
                            nextApptID++;

                        } else if (bookType == 2) {
                            System.out.println("Valid surgery types:");
                            for (int i = 0; i < Surgery.SURGERY_TYPES.length; i++) {
                                System.out.println("  - " + Surgery.SURGERY_TYPES[i]);
                            }
                            String surgeryType = null;
                            while (surgeryType == null) {
                                System.out.print("Surgery type: ");
                                String input = scanner.nextLine().trim();
                                for (int i = 0; i < Surgery.SURGERY_TYPES.length; i++) {
                                    if (Surgery.SURGERY_TYPES[i].equalsIgnoreCase(input)) {
                                        surgeryType = Surgery.SURGERY_TYPES[i];
                                        break;
                                    }
                                }
                                if (surgeryType == null) {
                                    System.out.println("Error: invalid surgery type. "
                                            + "Must be one of the listed types.");
                                }
                            }
                            int preferredOR = -1;
                            while (preferredOR == -1) {
                                System.out.print("Preferred OR number: ");
                                try {
                                    preferredOR = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            Surgeon surgeon = null;
                            for (Staff s : staffManager.getStaffArray()) {
                                if (s instanceof Surgeon) {
                                    Surgeon sg = (Surgeon) s;
                                    if (sg.getSpecialtyArea() != null
                                            && sg.getSpecialtyArea().equalsIgnoreCase(surgeryType)
                                            && !sg.hasTimeConflict(date, time)) {
                                        surgeon = sg;
                                        break;
                                    }
                                }
                            }
                            if (surgeon == null) {
                                System.out.println("Error: no available surgeon.");
                                continue;
                            }
                            Nurse[] nurses = staffManager.getAvailableNurses(
                                    patient.getWard(), date, time, 1);
                            if (nurses == null) {
                                nurses = new Nurse[0];
                            }
                            Surgery appt = new Surgery(
                                    nextApptID, patient, new Staff[1 + nurses.length], date, time,
                                    2.0, 0.0, Appointment.STATUS_SCHEDULED,
                                    0, "General", 0.0, surgeryType, null);
                            appt.estimateDuration();
                            if (appt.assignOperatingRoom(apptManager, preferredOR) <= 0) {
                                System.out.println("Error: no operating room available.");
                                continue;
                            }
                            appt.assignStaff(surgeon, nurses);
                            appt.calculateCost();
                            if (!appt.validateBooking()) {
                                System.out.println("Error: surgery booking validation failed.");
                                continue;
                            }
                            if (!apptManager.addAppointment(appt)) {
                                System.out.println("Error: could not book surgery.");
                                continue;
                            }
                            patientManager.addAppointment(patientID, appt);
                            staffManager.addShift(surgeon.getName(), appt);
                            for (Nurse nurse : nurses) {
                                if (nurse != null) {
                                    staffManager.addShift(nurse.getName(), appt);
                                }
                            }
                            System.out.println("Surgery booked successfully.");
                            System.out.println("Appointment ID   : " + appt.getApptID());
                            System.out.printf("Cost             : $%.2f%n", appt.calculateCost());
                            nextApptID++;

                        } else if (bookType == 3) {
                            int urgencyIdx = -1;
                            while (urgencyIdx == -1) {
                                System.out.print("Urgency index (1-5): ");
                                try {
                                    urgencyIdx = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: please enter a valid integer.");
                                }
                            }
                            if (urgencyIdx < 1 || urgencyIdx > 5) {
                                System.out.println("Error: urgency must be between 1 and 5.");
                                continue;
                            }
                            EmergencyVisit appt = new EmergencyVisit(
                                    nextApptID, patient, new Staff[5], date, time,
                                    0.0, 0.0, Appointment.STATUS_SCHEDULED, 0, urgencyIdx);
                            appt.estimateDuration();
                            if (appt.assignEmergencyRoom(apptManager) <= 0) {
                                System.out.println("Error: no emergency room available.");
                                continue;
                            }
                            Doctor doctor = staffManager.getTraumaDoctor(urgencyIdx, date, time);
                            if (doctor == null) {
                                System.out.println("Error: no available ER doctor.");
                                continue;
                            }
                            appt.assignStaff(doctor, urgencyIdx);
                            Nurse triageNurse = staffManager.getTriageNurse(date, time);
                            if (triageNurse != null) {
                                appt.autoAssignNurse(triageNurse);
                            }
                            appt.calculateCost();
                            if (!apptManager.addAppointment(appt)) {
                                System.out.println("Error: could not book emergency visit.");
                                continue;
                            }
                            patientManager.addAppointment(patientID, appt);
                            staffManager.addShift(doctor.getName(), appt);
                            if (triageNurse != null) {
                                staffManager.addShift(triageNurse.getName(), appt);
                            }
                            System.out.println("Emergency visit booked successfully.");
                            System.out.println("Appointment ID   : " + appt.getApptID());
                            System.out.printf("Cost             : $%.2f%n", appt.calculateCost());
                            nextApptID++;

                        } else {
                            System.out.println("Error: invalid appointment type.");
                        }

                    } else if (apptChoice == 2) {
                        int apptID = -1;
                        while (apptID == -1) {
                            System.out.print("Appointment ID to cancel: ");
                            try {
                                apptID = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        if (apptManager.cancelAppointment(apptID)) {
                            System.out.println("Appointment cancelled.");
                        } else {
                            System.out.println("Error: appointment not found.");
                        }

                    } else if (apptChoice == 3) {
                        int apptID = -1;
                        while (apptID == -1) {
                            System.out.print("Appointment ID: ");
                            try {
                                apptID = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        System.out.print("New date (YYYY-MM-DD): ");
                        String newDateStr = scanner.nextLine().trim();
                        if (!newDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date newDate = new Date(newDateStr);
                        double newTime = -1;
                        while (newTime == -1) {
                            System.out.print("New time (e.g. 14.00): ");
                            try {
                                newTime = Double.parseDouble(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid number.");
                            }
                        }
                        if (apptManager.rescheduleAppointment(apptID, newDate, newTime)) {
                            System.out.println("Appointment rescheduled.");
                        } else {
                            System.out.println("Error: could not reschedule.");
                        }

                    } else if (apptChoice == 4) {
                        System.out.print("Date (YYYY-MM-DD): ");
                        String schedDateStr = scanner.nextLine().trim();
                        if (!schedDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date schedDate = new Date(schedDateStr);
                        System.out.println("--- Daily Schedule: " + schedDate.toISODateString()
                                + " ---");
                        Appointment[] appts = apptManager.getAppointments();
                        int count = apptManager.getNumAppointments();
                        Appointment[] dayAppts = new Appointment[count];
                        int dayCount = 0;
                        for (int i = 0; i < count; i++) {
                            if (appts[i] != null
                                    && appts[i].getDate().compareTo(schedDate) == 0
                                    && appts[i].isActive()) {
                                dayAppts[dayCount++] = appts[i];
                            }
                        }
                        ApptManager.sortByDateThenTime(dayAppts, dayCount);
                        if (dayCount == 0) {
                            System.out.println("No appointments scheduled for this date.");
                        } else {
                            for (int i = 0; i < dayCount; i++) {
                                Appointment a = dayAppts[i];
                                Patient p = a.getPatient();
                                String patientLabel = "Unknown";
                                if (p != null) {
                                    patientLabel = p.getFirstName() + " " + p.getLastName()
                                            + " (" + p.getPatientID() + ")";
                                }
                                int h = (int) a.getTime();
                                int m = (int) Math.round((a.getTime() - h) * 100);
                                String timeFmt = String.format("%02d:%02d", h, m);
                                String typeLabel = a.getTypeLabel();
                                String location = a.getLocationLabel();
                                String staffName = "Unassigned";
                                Staff[] team = a.getStaffList();
                                if (team != null) {
                                    for (Staff s : team) {
                                        if (s != null) {
                                            if (s instanceof Doctor) {
                                                staffName = "Dr. " + s.getName();
                                            } else {
                                                staffName = s.getName();
                                            }
                                            break;
                                        }
                                    }
                                }
                                System.out.printf("%-5s  #%d  %-16s %-22s %-6s  %s%n",
                                        timeFmt, a.getApptID(), typeLabel,
                                        patientLabel, location, staffName);
                            }
                        }

                    } else if (apptChoice == 5) {
                        int pid = -1;
                        while (pid == -1) {
                            System.out.print("Patient ID: ");
                            try {
                                pid = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error: please enter a valid integer.");
                            }
                        }
                        apptManager.viewUpcomingAppointments(pid);

                    } else if (apptChoice == 6) {
                        System.out.print("Date (YYYY-MM-DD): ");
                        String costDateStr = scanner.nextLine().trim();
                        if (!costDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
                            continue;
                        }
                        Date costDate = new Date(costDateStr);
                        double total = apptManager.runCostSummary(costDate);
                        System.out.printf("Total revenue on %s: $%.2f%n",
                                costDate.toISODateString(), total);

                    } else if (apptChoice == 7) {
                        apptManager.sortByDate();
                        System.out.println("--- Appointments Sorted by Date ---");
                        System.out.printf("%-12s %-20s %-15s %-18s %-12s%n",
                                "Date", "Patient Name", "Appointment ID", "Type", "Status");
                        Appointment[] appts = apptManager.getAppointments();
                        for (int i = 0; i < apptManager.getNumAppointments(); i++) {
                            Appointment a = appts[i];
                            if (a == null) {
                                continue;
                            }
                            String patientName = a.getPatient().getFirstName() + " "
                                    + a.getPatient().getLastName();
                            String typeName = a.getClass().getSimpleName();
                            System.out.printf("%-12s %-20s %-15d %-18s %-12s%n",
                                    a.getDate(), patientName, a.getApptID(),
                                    typeName, a.getStatus());
                        }

                    } else if (apptChoice == 8) {
                        apptManager.sortByPatientThenDate();
                        System.out.println("--- Appointments Sorted by Patient then Date ---");
                        System.out.printf("%-20s %-12s %-15s %-18s %-12s%n",
                                "Patient Name", "Date", "Appointment ID", "Type", "Status");
                        Appointment[] appts = apptManager.getAppointments();
                        for (int i = 0; i < apptManager.getNumAppointments(); i++) {
                            Appointment a = appts[i];
                            if (a == null) {
                                continue;
                            }
                            String patientName = a.getPatient().getFirstName() + " "
                                    + a.getPatient().getLastName();
                            String typeName = a.getClass().getSimpleName();
                            System.out.printf("%-20s %-12s %-15d %-18s %-12s%n",
                                    patientName, a.getDate(), a.getApptID(),
                                    typeName, a.getStatus());
                        }

                    } else if (apptChoice == 9) {
                        apptBack = true;

                    } else {
                        System.out.println("Error: invalid choice.");
                    }
                }

            } else if (choice == 4) {
                staffManager.saveToFile("data/staff.txt");
                if (patientsLoaded) {
                    patientManager.savePatientInfo("data/patients.txt");
                } else {
                    System.out.println("Warning: skipping patient save — data was not loaded successfully.");
                }
                apptManager.saveToFile("data/appointments.txt");
                System.out.println("All data saved.");

            } else if (choice == 5) {
                staffManager.saveToFile("data/staff.txt");
                if (patientsLoaded) {
                    patientManager.savePatientInfo("data/patients.txt");
                } else {
                    System.out.println("Warning: skipping patient save — data was not loaded successfully.");
                }
                apptManager.saveToFile("data/appointments.txt");
                System.out.println("Goodbye.");
                running = false;

            } else {
                System.out.println("Error: invalid choice. Please enter 1-5.");
            }
        }

        scanner.close();
    }
}