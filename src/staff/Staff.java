/**
 * Staff.java
 * ICS4U Hospital Management System — Staff Module
 * Ferdinand Zhang
 * June 1, 2026
 *
 * Abstract base class for all hospital staff (Doctor, Nurse, Surgeon).
 * Defines shared fields, schedule conflict checks, and off-day management.
 *
 */

package staff;

import appointment.*;
import patient.*;
import shared.Date;

public abstract class Staff {

    private String name;
    private String staffID;
    private int experience;
    private String specialization;
    private String[] offDays;
    private Appointment[] schedule;

    /**
     * Constructs a staff member with the given shared attributes.
     *
     * @param staffID        unique staff identifier
     * @param name           full name
     * @param experience     years of experience
     * @param specialization medical specialization area
     * @param offDays        array for scheduled off-day dates
     * @param schedule       array for personal appointment/shift schedule
     */
    public Staff(String staffID, String name, int experience, String specialization,
            String[] offDays, Appointment[] schedule) {
        this.staffID = staffID;
        this.name = name;
        this.experience = experience;
        this.specialization = specialization;
        this.offDays = offDays;
        this.schedule = schedule;
    }

    /**
     * Assigns patients to this staff member (role-specific logic in subclasses).
     *
     * @param patients array of patients to assign
     */
    public abstract void assignPatients(Patient[] patients);

    /**
     * Adds an appointment to this staff member's schedule (subclass-specific).
     *
     * @param appt the appointment to add
     */
    public abstract void addAppointment(Appointment appt);

    /**
     * Computes pay for the current period (subclass-specific formula).
     *
     * @return pay amount for the current period
     */
    public abstract double calculatePay();

    /**
     * Returns a formatted string representation of this staff member's schedule.
     *
     * @return formatted schedule string
     */
    public abstract String getSchedule();

    /**
     * Returns formatted staff information for display and file persistence.
     *
     * @return formatted staff info string
     */
    @Override
    public abstract String toString();

    /**
     * Validates a date string, checks for duplicates, and records an off day.
     * Format validation + linear search through offDays.
     *
     * @param date date in YYYY-MM-DD format
     */
    public void takeOffDay(String date) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
            return;
        }

        // Linear search — check for duplicate off day
        for (int i = 0; i < offDays.length; i++) {
            if (offDays[i] != null && offDays[i].equals(date)) {
                System.out.println("Error: off day already scheduled for " + date + ".");
                return;
            }
        }

        // Add to next available slot
        for (int i = 0; i < offDays.length; i++) {
            if (offDays[i] == null) {
                offDays[i] = date;
                return;
            }
        }
        System.out.println("Error: no available off-day slots.");
    }

    /**
     * Compares this staff member to another object by staff ID.
     *
     * @param other the object to compare
     * @return true if both objects represent the same staff ID
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Staff)) {
            return false;
        }
        return equals((Staff) other);
    }

    /**
     * Returns a hash code based on the staff ID.
     *
     * @return hash code for this staff member
     */
    @Override
    public int hashCode() {
        return staffID.hashCode();
    }

    /**
     * Compares this staff member to another by staff ID.
     *
     * @param other staff member to compare
     * @return true if both have the same staffID; false if other is null or IDs differ
     */
    public boolean equals(Staff other) {
        if (other == null) {
            return false;
        }
        return staffID.equals(other.staffID);
    }

    /**
     * Checks whether an appointment already exists at the given date and time.
     * Linear search through schedule.
     *
     * @param date appointment date
     * @param time appointment time (24-hour, stored as double)
     * @return true if a conflicting appointment exists; false otherwise
     */
    public boolean hasTimeConflict(Date date, double time) {
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] != null
                    && schedule[i].getDate().compareTo(date) == 0
                    && schedule[i].getTime() == time) {
                return true;
            }
        }
        return false;
    }

    /** @return full name */
    public String getName() {
        return name;
    }

    /**
     * @param name full name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return unique staff identifier */
    public String getStaffID() {
        return staffID;
    }

    /**
     * @param staffID unique staff identifier to set
     */
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    /** @return years of experience */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience years of experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /** @return medical specialization area */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @param specialization medical specialization area to set
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Returns a defensive copy of the off-days array.
     *
     * @return copy of scheduled off-day dates
     */
    public String[] getOffDays() {
        String[] copy = new String[offDays.length];
        for (int i = 0; i < offDays.length; i++) {
            copy[i] = offDays[i];
        }
        return copy;
    }

    /**
     * @param offDays off-days array to set
     */
    public void setOffDays(String[] offDays) {
        this.offDays = offDays;
    }

    /**
     * Returns a defensive copy of the appointment schedule array.
     *
     * @return copy of personal appointment/shift schedule
     */
    public Appointment[] getScheduleArray() {
        Appointment[] copy = new Appointment[schedule.length];
        for (int i = 0; i < schedule.length; i++) {
            copy[i] = schedule[i];
        }
        return copy;
    }

    /**
     * @param schedule appointment schedule array to set
     */
    public void setScheduleArray(Appointment[] schedule) {
        this.schedule = schedule;
    }

    /**
     * Returns the internal schedule array for subclass use when adding or
     * searching appointments. Do not expose this reference outside the hierarchy.
     *
     * @return internal appointment schedule array
     */
    protected Appointment[] getScheduleSlots() {
        return schedule;
    }

    /**
     * Returns non-null schedule appointments sorted by date, then time.
     * Selection sort — curriculum-aligned pattern used elsewhere in the project.
     *
     * @return compact array of scheduled appointments in chronological order
     */
    protected Appointment[] getSortedScheduleAppointments() {
        Appointment[] slots = getScheduleSlots();
        Appointment[] sorted = new Appointment[slots.length];
        int count = 0;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                sorted[count] = slots[i];
                count++;
            }
        }

        ApptManager.sortByDateThenTime(sorted, count);
        return sorted;
    }

    /**
     * Returns the internal off-days array for subclass use.
     * Do not expose this reference outside the hierarchy.
     *
     * @return internal off-days array
     */
    protected String[] getOffDaySlots() {
        return offDays;
    }

    /**
     * Formats a stored appointment time (hh.mm as double) for display as HH:MM.
     *
     * @param time appointment time in 24-hour double form
     * @return formatted time string
     */
    protected String formatTime(double time) {
        int hours = (int) time;
        int minutes = (int) Math.round((time - hours) * 100);
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * Formats off-days for staff.txt output (comma-separated YYYY-MM-DD or NONE).
     *
     * @return off-days line for file persistence
     */
    protected String formatOffDaysForFile() {
        String[] slots = getOffDaySlots();
        StringBuilder offDayLine = new StringBuilder();
        boolean any = false;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                if (any) {
                    offDayLine.append(",");
                }
                offDayLine.append(slots[i]);
                any = true;
            }
        }
        if (!any) {
            return "NONE";
        }
        return offDayLine.toString();
    }
}
