package appointment;

import java.util.Arrays;
import patient.Patient;
import staff.Staff;
import shared.Date;

public abstract class Appointment {
    private int apptID; //unique identifier for each appointment
    private Patient patient; //the patient associated with the appointment
    private Staff[] staffList; //the staff members associated with the appointment (can be multiple)
    private Date date; //date of the appointment
    private double time; //time of the appointment (in 24-hour format, e.g. 14.30 for 2:30 PM)
    private double duration; //duration of the appointment
    private double cost; //cost of the appointment
    private String status; //status of the appointment (e.g. "Scheduled", "Done", "Cancelled")

    //constants
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_DONE = "Done";
    public static final String STATUS_NO_SHOW = "No Show";
    public final int noShowFee = 50;

    //constructor
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

    //accessors and mutators
    public int getApptID() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff[] getStaffList() {
        return staffList == null ? null : Arrays.copyOf(staffList, staffList.length);
    }

    public void setStaffList(Staff[] staffList) {
        assignStaff(staffList);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

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
        Date curdate = this.date;
        double curtime = this.time;

        date = newDate;
        time = newTime;

        if (validateBooking()) {
            return true;
        } else {
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
        if (!(obj instanceof Appointment)) {
            return false;
        }

        Appointment other = (Appointment) obj;
        if (this == other || this.apptID == other.apptID) {
            return false;
        }

        if (!this.date.equals(other.date)) {
            return false;
        }

        if (this.staffList == null || other.staffList == null) {
            return false;
        }

        int thisStart = toMinutes(this.time);
        int otherStart = toMinutes(other.time);
        int thisEnd = thisStart + (int) Math.round(this.duration * 60);
        int otherEnd = otherStart + (int) Math.round(other.duration * 60);
        if (thisStart >= otherEnd || otherStart >= thisEnd) {
            return false;
        }

        for (int j = 0; j < this.staffList.length; j++) {
            for (int k = 0; k < other.staffList.length; k++) {
                if (this.staffList[j].equals(other.staffList[k])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if this appointment is equal to another object
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) obj;
        return this.apptID == other.apptID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(apptID);
    }

    /**
     * Converts a time in hh.mm format to minutes
     * @param hhmm the time in hh.mm format
     * @return the time in minutes
     */
    private static int toMinutes(double hhmm) {
        int hours = (int) hhmm;
        int minutes = (int) Math.round((hhmm - hours) * 100);
        return hours * 60 + minutes;
    }

    /**
     * Marks the appointment as done and adds it to the patient's history
     */
    public void markDone() {
        status = STATUS_DONE;
        patient.addToHistory(this);
    }

    /**
     * Returns a string representation of the appointment
     * @return a string representation of the appointment
     */
    @Override
    public String toString() {
        String staffStr = "";
        if (staffList != null) {
            for (int i = 0; i < staffList.length; i++) {
                staffStr += staffList[i].getID() + " ";
            }
        } else {
            staffStr = "None";
        }

        return "Appointment ID: " + apptID + "\nPatient: " + patient.getName()
            + "\nStaff: " + staffStr + "\nDate: " + date.toString() + "\nTime: " + time + "\nDuration: " + duration + "\nCost: " + cost + "\nStatus: " + status;
    }

    //abstract methods
    abstract public double calculateCost();

    abstract public boolean validateBooking();

    //method to assign staff members to the appointment (will be overridden in subclasses if needed)
    public void assignStaff(Staff[] staffTeam) {
        this.staffList = staffTeam == null ? null : Arrays.copyOf(staffTeam, staffTeam.length);
    }
}
