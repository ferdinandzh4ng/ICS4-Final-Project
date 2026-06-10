/**
 * StaffManager.java
 * ICS4U Hospital Management System — Staff Module
 * Ferdinand Zhang
 * June 8, 2026
 *
 * Central controller for staff records: CRUD, search, sort, payroll,
 * shift management, availability queries, and staff.txt file I/O.
 */

package staff;

import appointment.Appointment;
import shared.Date;

import java.io.*;

public class StaffManager {

    private static final int OFF_DAYS_CAPACITY = 10;
    private static final int SCHEDULE_CAPACITY = 20;

    private Staff[] staffArray;
    private int staffCount;

    /**
     * Constructs a staff manager with the given array capacity.
     *
     * @param maxStaff maximum number of staff records that can be stored
     */
    public StaffManager(int maxStaff) {
        staffArray = new Staff[maxStaff];
        staffCount = 0;
    }

    /**
     * Adds a staff member to the end of the staff array.
     * Bounds check + insert at end of staffArray.
     *
     * @param s staff member to add
     */
    public void addStaff(Staff s) {
        if (s == null) {
            return;
        }
        if (staffCount >= staffArray.length) {
            System.out.println("Error: staff array at capacity.");
            return;
        }
        staffArray[staffCount] = s;
        staffCount++;
    }

    /**
     * Removes a staff member by staff ID.
     * Linear search + left shift delete.
     *
     * @param id staff ID of the member to remove
     * @return true if the staff member was found and removed; false otherwise
     */
    public boolean removeStaff(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < staffCount; i++) {
            if (staffArray[i].getStaffID().equals(id)) {
                for (int j = i; j < staffCount - 1; j++) {
                    staffArray[j] = staffArray[j + 1];
                }
                staffArray[staffCount - 1] = null;
                staffCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Updates shared fields on an existing staff member identified by ID.
     * Recursive search + update via setters.
     *
     * @param id             staff ID of the member to update
     * @param name           new full name
     * @param experience     new years of experience
     * @param specialization new medical specialization area
     * @return true if the staff member was found and updated; false otherwise
     */
    public boolean updateStaff(String id, String name, int experience, String specialization) {
        Staff found = findStaffByID(id, 0);
        if (found == null) {
            return false;
        }
        found.setName(name);
        found.setExperience(experience);
        found.setSpecialization(specialization);
        return true;
    }

    /**
     * Finds a staff member by exact name match.
     * Linear search by name.
     *
     * @param name full name to search for
     * @return matching staff member, or null if not found
     */
    public Staff findStaff(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < staffCount; i++) {
            if (staffArray[i].getName().equals(name)) {
                return staffArray[i];
            }
        }
        return null;
    }

    /**
     * Finds all staff whose specialization matches and experience meets a minimum.
     * Linear scan — two-criteria search by specialization and minimum experience.
     *
     * @param specialty specialization area to match
     * @param exp       minimum years of experience required
     * @return array of matching staff members (may be empty)
     */
    public Staff[] findStaff(String specialty, int exp) {
        if (specialty == null) {
            return new Staff[0];
        }

        Staff[] results = new Staff[staffCount];
        int found = 0;
        for (int i = 0; i < staffCount; i++) {
            if (staffArray[i].getSpecialization() != null
                    && staffArray[i].getSpecialization().equals(specialty)
                    && staffArray[i].getExperience() >= exp) {
                results[found] = staffArray[i];
                found++;
            }
        }

        Staff[] trimmed = new Staff[found];
        for (int i = 0; i < found; i++) {
            trimmed[i] = results[i];
        }
        return trimmed;
    }

    /**
     * Recursively searches for a staff member by ID starting at the given index.
     * Recursive linear search by staff ID.
     *
     * @param id    staff ID to search for
     * @param index starting index in the staff array
     * @return matching staff member, or null if not found
     */
    public Staff findStaffByID(String id, int index) {
        if (id == null || index >= staffCount) {
            return null;
        }
        if (staffArray[index].getStaffID().equals(id)) {
            return staffArray[index];
        }
        return findStaffByID(id, index + 1);
    }

    /**
     * Sorts all staff alphabetically by name (A–Z).
     * Selection sort by name.
     */
    public void sortStaff() {
        for (int i = 0; i < staffCount - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < staffCount; j++) {
                if (staffArray[j].getName().compareTo(staffArray[minIndex].getName()) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Staff temp = staffArray[i];
                staffArray[i] = staffArray[minIndex];
                staffArray[minIndex] = temp;
            }
        }
    }

    /**
     * Sorts all staff by experience descending; ties broken by name A–Z.
     * Bubble sort — experience descending, then name A–Z on tie.
     */
    public void sortStaffByExp() {
        for (int pass = 0; pass < staffCount - 1; pass++) {
            for (int j = 0; j < staffCount - pass - 1; j++) {
                boolean shouldSwap = staffArray[j].getExperience() < staffArray[j + 1].getExperience();
                if (!shouldSwap
                        && staffArray[j].getExperience() == staffArray[j + 1].getExperience()
                        && staffArray[j].getName().compareTo(staffArray[j + 1].getName()) > 0) {
                    shouldSwap = true;
                }
                if (shouldSwap) {
                    Staff temp = staffArray[j];
                    staffArray[j] = staffArray[j + 1];
                    staffArray[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Returns the formatted schedule for a given staff member.
     * Delegation to Staff.getSchedule().
     *
     * @param s staff member whose schedule to retrieve
     * @return formatted schedule string, or empty string if s is null
     */
    public String checkShifts(Staff s) {
        if (s == null) {
            return "";
        }
        return s.getSchedule();
    }

    /**
     * Adds an appointment shift to a staff member's schedule by name.
     * Linear search by name + delegation to Staff.addAppointment().
     *
     * @param name full name of the staff member
     * @param appt appointment to add to the staff schedule
     */
    public void addShift(String name, Appointment appt) {
        Staff found = findStaff(name);
        if (found == null) {
            System.out.println("Error: staff member not found.");
            return;
        }
        found.addAppointment(appt);
    }

    /**
     * Removes an appointment from a staff member's schedule by name.
     * Linear search by name + left-shift removal from schedule array.
     *
     * @param name full name of the staff member
     * @param appt appointment to remove (matched by appointment ID)
     * @return true if the staff member and appointment were found and removed; false otherwise
     */
    public boolean removeShift(String name, Appointment appt) {
        if (name == null || appt == null) {
            return false;
        }

        Staff found = findStaff(name);
        if (found == null) {
            return false;
        }

        Appointment[] schedule = found.getScheduleArray();
        int index = -1;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] != null && schedule[i].getApptID() == appt.getApptID()) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }

        for (int j = index; j < schedule.length - 1; j++) {
            schedule[j] = schedule[j + 1];
        }
        schedule[schedule.length - 1] = null;
        found.setScheduleArray(schedule);
        return true;
    }

    /**
     * Computes total payroll by summing each staff member's pay.
     * Loop — polymorphic total via Staff.calculatePay().
     *
     * @return combined pay for all staff in the current period
     */
    public double runPayroll() {
        double total = 0.0;
        for (int i = 0; i < staffCount; i++) {
            total += staffArray[i].calculatePay();
        }
        return total;
    }

    /**
     * Loads staff records from a colon-separated text file.
     * File I/O — parses role-tagged records and constructs Doctor, Nurse, or Surgeon objects.
     *
     * @param filename path to the staff data file (e.g. data/staff.txt)
     */
    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int count = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < count; i++) {
                String role = reader.readLine().trim();
                String staffID = reader.readLine().trim();
                String name = reader.readLine().trim();
                int experience = Integer.parseInt(reader.readLine().trim());
                String specialization = reader.readLine().trim();
                String[] offDays = parseOffDays(reader.readLine().trim());
                Appointment[] schedule = new Appointment[SCHEDULE_CAPACITY];

                if (role.equals("Doctor")) {
                    String licenseNumber = reader.readLine().trim();
                    double consultationFee = Double.parseDouble(reader.readLine().trim());
                    int maxPatients = Integer.parseInt(reader.readLine().trim());
                    reader.readLine(); // consume trailing ":"
                    addStaff(new Doctor(staffID, name, experience, specialization,
                            offDays, schedule, licenseNumber, consultationFee, maxPatients));
                } else if (role.equals("Nurse")) {
                    String ward = reader.readLine().trim();
                    String shiftType = reader.readLine().trim();
                    double hourlyRate = Double.parseDouble(reader.readLine().trim());
                    int hoursWorkedThisWeek = Integer.parseInt(reader.readLine().trim());
                    reader.readLine(); // consume trailing ":"
                    addStaff(new Nurse(staffID, name, experience, specialization,
                            offDays, schedule, ward, shiftType, hourlyRate, hoursWorkedThisWeek));
                } else if (role.equals("Surgeon")) {
                    int operatingRoom = Integer.parseInt(reader.readLine().trim());
                    int surgeriesCompleted = Integer.parseInt(reader.readLine().trim());
                    String specialtyArea = reader.readLine().trim();
                    double surgeryFeePerProcedure = Double.parseDouble(reader.readLine().trim());
                    reader.readLine(); // consume trailing ":"
                    addStaff(new Surgeon(staffID, name, experience, specialization,
                            offDays, schedule, operatingRoom, surgeriesCompleted,
                            specialtyArea, surgeryFeePerProcedure));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: could not read " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid file format in " + filename);
        }
    }

    /**
     * Saves all staff records to a colon-separated text file.
     * File I/O — writes staffCount followed by each staff member's toString() output.
     *
     * @param filename path to the staff data file to write
     */
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(staffCount);
            for (int i = 0; i < staffCount; i++) {
                writer.print(staffArray[i].toString());
                writer.println();
            }
        } catch (IOException e) {
            System.out.println("Error: could not write " + filename);
        }
    }

    /**
     * Finds available nurses in a ward who have no conflict at the given date and time.
     * Linear search — skips non-nurses, wrong ward, and busy nurses.
     *
     * @param ward  hospital ward to match (e.g. "ER")
     * @param date  date to check for conflicts
     * @param time  time to check for conflicts (24-hour, stored as double)
     * @param count number of nurses required
     * @return array of available nurses, or null if fewer than count are available
     */
    public Nurse[] getAvailableNurses(String ward, Date date, double time, int count) {
        if (ward == null || date == null || count <= 0) {
            return null;
        }

        Nurse[] available = new Nurse[count];
        int collected = 0;

        for (int i = 0; i < staffCount && collected < count; i++) {
            if (!(staffArray[i] instanceof Nurse)) {
                continue;
            }
            Nurse nurse = (Nurse) staffArray[i];
            if (nurse.getWard() == null || !nurse.getWard().equals(ward)) {
                continue;
            }
            if (nurse.hasTimeConflict(date, time)) {
                continue;
            }
            available[collected] = nurse;
            collected++;
        }

        if (collected < count) {
            return null;
        }
        return available;
    }

    /**
     * Returns the first available ER triage nurse at the given date and time.
     * Delegation to getAvailableNurses with ward "ER".
     *
     * @param date date to check for conflicts
     * @param time time to check for conflicts (24-hour, stored as double)
     * @return available triage nurse, or null if none found
     */
    public Nurse getTriageNurse(Date date, double time) {
        Nurse[] nurses = getAvailableNurses("ER", date, time, 1);
        if (nurses == null || nurses.length == 0) {
            return null;
        }
        return nurses[0];
    }

    /**
     * Finds an available ER doctor for trauma care based on urgency level.
     * Linear search — prefers senior doctors (experience >= 10) when urgencyIdx >= 4.
     *
     * @param urgencyIdx triage severity index (1–5; 4+ requires a senior doctor)
     * @param date       date to check for conflicts
     * @param time       time to check for conflicts (24-hour, stored as double)
     * @return available ER doctor, or null if none found
     */
    public Doctor getTraumaDoctor(int urgencyIdx, Date date, double time) {
        if (date == null) {
            return null;
        }

        boolean needsSenior = urgencyIdx >= 4;

        if (needsSenior) {
            for (int i = 0; i < staffCount; i++) {
                if (staffArray[i] instanceof Doctor) {
                    Doctor doctor = (Doctor) staffArray[i];
                    if (isErDoctor(doctor)
                            && !doctor.hasTimeConflict(date, time)
                            && doctor.getExperience() >= 10) {
                        return doctor;
                    }
                }
            }
        }

        for (int i = 0; i < staffCount; i++) {
            if (staffArray[i] instanceof Doctor) {
                Doctor doctor = (Doctor) staffArray[i];
                if (isErDoctor(doctor) && !doctor.hasTimeConflict(date, time)) {
                    return doctor;
                }
            }
        }
        return null;
    }

    /**
     * Returns the current number of staff records stored.
     *
     * @return number of staff members in the array
     */
    public int getStaffCount() {
        return staffCount;
    }

    /**
     * Returns a defensive copy of the staff array (only populated slots).
     *
     * @return copy of stored staff members
     */
    public Staff[] getStaffArray() {
        Staff[] copy = new Staff[staffCount];
        for (int i = 0; i < staffCount; i++) {
            copy[i] = staffArray[i];
        }
        return copy;
    }

    /**
     * Parses a comma-separated off-days line from staff.txt into an array.
     *
     * @param line off-days line (NONE or comma-separated YYYY-MM-DD dates)
     * @return off-days array with parsed dates in available slots
     */
    private String[] parseOffDays(String line) {
        String[] offDays = new String[OFF_DAYS_CAPACITY];
        if (line == null || line.equals("NONE")) {
            return offDays;
        }

        String[] parts = line.split(",");
        for (int i = 0; i < parts.length && i < OFF_DAYS_CAPACITY; i++) {
            offDays[i] = parts[i].trim();
        }
        return offDays;
    }

    /**
     * Checks whether a doctor's specialization is ER.
     *
     * @param doctor doctor to check
     * @return true if the doctor specializes in ER; false otherwise
     */
    private boolean isErDoctor(Doctor doctor) {
        String specialization = doctor.getSpecialization();
        return specialization != null && specialization.equalsIgnoreCase("ER");
    }

}
