/**
 * File: Appointment.java
 * Name: Ida Luo
 * Date: June 7, 2026
 * Class: ICS4U1
 * Description: This class represents an appointment in the hospital management system. It is an abstract class that provides a base structure for different types of appointments, including fields and methods that are common to all appointment types.
*/

package appointment;

import java.util.Arrays;
import patient.Patient;
import staff.Staff;
import shared.Date;

public abstract class Appointment {
    // Fields
    private int apptID; //unique identifier for each appointment
    private Patient patient; //the patient associated with the appointment
    private Staff[] staffList; //the staff members associated with the appointment (can be multiple)
    private Date date; //date of the appointment
    private double time; //time of the appointment (in 24-hour format, e.g. 14.30 for 2:30 PM)
    private double duration; //duration of the appointment
    private double cost; //cost of the appointment
    private String status; //status of the appointment (e.g. "Scheduled", "Done", "Cancelled")

    //Constants
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_DONE = "Done";
    public static final String STATUS_NO_SHOW = "No Show";    
    public static final int NO_SHOW_FEE = 50;

    /**
     * Constructor for the Appointment class
     * @param apptID unique identifier for the appointment
     * @param patient the patient associated with the appointment
     * @param staffList the staff members associated with the appointment
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @param duration the duration of the appointment
     * @param cost the cost of the appointment
     * @param status the status of the appointment
     */
    public Appointment(int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status) {
        this.apptID = apptID;
        this.patient = patient;
        this.staffList = staffList == null ? null : Arrays.copyOf(staffList, staffList.length);
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.cost = cost;
        this.status = status;
    }

    // Accessors and Mutators
    /**
     * Returns the unique identifier for the appointment.
     * @return the appointment ID
     */
    public int getApptID() {
        return apptID;
    }

    /**
     * Sets the unique identifier for the appointment.
     * @param apptID the appointment ID to set
     */
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    /**
     * Returns the patient associated with the appointment.
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Sets the patient associated with the appointment.
     * @param patient the patient to set
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Returns a copy of the staff list associated with the appointment.
     * @return a copy of the staff list
     */
    public Staff[] getStaffList() {
        return staffList == null ? null : Arrays.copyOf(staffList, staffList.length);
    }

    /**
     * Sets the staff list associated with the appointment.
     * @param staffList the staff list to set
     */
    public void setStaffList(Staff[] staffList) {
        assignStaff(staffList);
    }

    /**
     * Returns the date of the appointment.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the appointment.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the time of the appointment.
     * @return the time
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the time of the appointment.
     * @param time the time to set
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Returns the duration of the appointment.
     * @return the duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the appointment.
     * @param duration the duration to set
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Returns the cost of the appointment.
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the appointment.
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns the status of the appointment.
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the appointment.
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Cancels the appointment
     */
    public void cancel() {
        status = STATUS_CANCELLED;
    }

    /**
     * Reschedules the appointment
     * @param newDate the new date for the appointment
     * @param newTime the new time for the appointment
     * @return true if rescheduling is successful, false otherwise
     */
    public boolean reschedule(Date newDate, double newTime) {
        // Store current date and time in temp variables
        Date curdate = this.date;
        double curtime = this.time;

        // Change date and time
        date = newDate;
        time = newTime;

        if (validateBooking()) {
            return true;
        } else {
            // Revert to old date and time if unsuccessful validation
            date = curdate;
            time = curtime;
            return false;
        }
    }

    /**
     * Checks if this appointment overlaps with another object
     * (overlap is defined as having at least one staff member in common and overlapping time ranges on the same date)
     * @param obj the object to compare with
     * @return true if the objects overlap, false otherwise
     */
    public boolean overlap(Object obj) {
        // Check instance and cast
        if (!(obj instanceof Appointment)) {
            return false;
        }

        Appointment other = (Appointment) obj;
        // Ignore if the 2 appointments have the same id
        if (this == other || this.equals(other)) {
            return false;
        }
        // Check for overlapping date
        if (!this.date.equals(other.date)) {
            return false;
        }
        // Check for null staff lists
        if (this.staffList == null || other.staffList == null) {
            return false;
        }

        // Check for overlaping time
        int thisStart = toMinutes(this.time);
        int otherStart = toMinutes(other.time);
        int thisEnd = thisStart + (int) Math.round(this.duration * 60);
        int otherEnd = otherStart + (int) Math.round(other.duration * 60);
        if (thisStart >= otherEnd || otherStart >= thisEnd) {
            return false;
        }

        // Check for common staff members
        for (int j = 0; j < other.staffList.length; j++) {
            if (this.hasStaffMember(other.staffList[j])) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Checks if this appointment is equal to another object
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj) {
        // Check and create instance of Appointment
        if (!(obj instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) obj;
        
        //Compare apptIDs
        return this.apptID == other.apptID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(apptID);
    }

    /**
     * Marks the appointment as done and adds it to the patient's history
     */
    public void markDone() {
        status = STATUS_DONE;
        patient.addToHistory(this);
    }

    /**
     * Assign staff to an appointment (will be overridden in child classes)
     * @param staffTeam staff array to assign
     */
    public void assignStaff(Staff[] staffTeam) {
        this.staffList = staffTeam == null ? null : Arrays.copyOf(staffTeam, staffTeam.length);
    }

    @Override
    /**
     * Returns a string representation of the appointment
     * @return a string representation of the appointment
     */
    public String toString() {
        // Format for empty staffList array
        String staffStr = "";
        if (staffList != null) {
            for (int i = 0; i < staffList.length; i++) {
                staffStr += staffList[i].getStaffID() + " ";
            }
        } else {
            staffStr = "None";
        }

        // Format time
        int hours = (int) time;
        int minutes = toMinutes(time) - hours * 60;
        String timeStr = String.format("%02d:%02d", hours, minutes);

        String patientName = "Unknown";
        if (patient != null) {
            patientName = patient.getFirstName() + " " + patient.getLastName();
        }

        //Return String of information
        return "Appointment ID: " + apptID + "\nPatient: " + patientName
            + "\nStaff: " + staffStr + "\nDate: " + date.toString() + "\nTime: " + timeStr + "\nDuration: " + duration + "\nCost: " + cost + "\nStatus: " + status;
    }

    //abstract methods
    abstract public double calculateCost();

    abstract public boolean validateBooking();

    abstract public int getRoomNum();

    /**
     * Returns a display label for this appointment type (e.g. "Routine Checkup").
     * @return human-readable appointment type label
     */
    public abstract String getTypeLabel();

    /**
     * Returns a display label for this appointment's location (e.g. "Rm 3", "OR 2").
     * @return human-readable location label
     */
    public abstract String getLocationLabel();

    // Helper methods
    /**
     * Helper method that converts a time in hh.mm format to minutes
     * @param hhmm the time in hh.mm format
     * @return the time in minutes
     */
    public static int toMinutes(double hhmm) {
        int hours = (int) hhmm;
        int minutes = (int) Math.round((hhmm - hours) * 100);
        return hours * 60 + minutes;
    }

    /**
     * Helper method that checks if the appointment is active (not cancelled, done, or no show)
     * @return true if the appointment is active, false otherwise
     */
    public boolean isActive() {
        return !this.status.equals(STATUS_CANCELLED) && !this.status.equals(STATUS_DONE) && !this.status.equals(STATUS_NO_SHOW);
    }

    /**
     * Helper to verify if a specific staff member is working on this appointment
     * @param toCheck Staff member to check
     * @return true if staff member is part of that staff list, otherwise false
     */
    public boolean hasStaffMember(Staff toCheck) {
        if (staffList == null || toCheck == null) return false;
        for (int i = 0; i < staffList.length; i++) {
            if (staffList[i] != null && staffList[i].equals(toCheck)) {
                return true;
            }
        }
        return false;
    }   
}
