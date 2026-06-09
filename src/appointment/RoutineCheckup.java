/** 
 * File: RoutineCheckup.java
 * Name: Ida Luo
 * Date: June 7, 2026
 * Class: ICS4U1
 * Description: This class represents a routine checkup appointment in the hospital management system. 
 * It extends the abstract Appointment class and includes specific fields and methods related to routine checkups. 
 * The class also includes functionality to assign a clinic room based on availability and to mark the appointment as a no-show if necessary.
*/

package appointment;

import staff.Doctor;

public class RoutineCheckup extends Appointment {
    //fields
    private int clinicRoomNum; //number of the clinic room assigned for the checkup

    //constants
    public static final int MAX_CLINIC_ROOMS = 20; //Maximum number of clinic rooms available for routine checkups

    /**
     * Constructor for routine checkup appointment
     * @param apptID unique identifier for the appointment
     * @param patient the patient associated with the appointment
     * @param staffList the staff members associated with the appointment
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @param status the status of the appointment
     * @param clinicRoomNum the room number assigned for the checkup
     */
    public RoutineCheckup(int apptID, Patient patient, Staff[] staffList, Date date, 
    double time, double duration, double cost, String status, int clinicRoomNum) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.clinicRoomNum = clinicRoomNum;
    }

    //accessors and mutators
    @Override
    /**
     * For routine checkups, the "room number" refers to the clinic room number.
     */
    public int getRoomNum() {
        return clinicRoomNum;
    }

    /**
     * Sets the clinic room number for the routine checkup.
     * @param clinicRoomNum the room number to set
     */
    public void setClinicRoomNum(int clinicRoomNum) {
        this.clinicRoomNum = clinicRoomNum;
    }

    /**
    * Assigns a clinic room for the routine checkup by checking the appointment manager for availability.
    * It will assign the first available clinic room it finds. If no rooms are available, it will print an error message.
    * @param manager the appointment manager to check for room availability
    * @return the assigned clinic room number, or -1 if no rooms are available
    */
    public int assignClinicRoom(ApptManager manager) {
    int maxClinicRooms = MAX_CLINIC_ROOMS;

        for (int room = 1; room <= maxClinicRooms; room++) {
            // Test each room number
            if (!manager.isRoomOccupied(this.getClass(), room, this.getDate(), this.getTime(), this.getDuration())) {
                this.clinicRoomNum = room;
                return room; // Return the assigned room number
            }
        }
        return -1; // Return -1 to indicate no room was assigned
    }

    /**
     * Marks the routine checkup as a no-show
     */
    public void markNoShow() {
        status = STATUS_NO_SHOW;
        cost = NO_SHOW_FEE;
    }

    @Override
    /**
     * Returns a string representation of the routine checkup appointment
     * @return a string describing the appointment details
     */
    public String toString() {
        return "Routine Checkup Appointment: " + super.toString() + ", 
        Clinic Room Number: " + clinicRoomNum + ", Main Doctor: " + mainDoctor.getName();
    }

    @Override
    /**
     * Calculates the cost of the routine checkup based on the base fee.
     */
     public void calculateCost() {
        cost = 100; //base cost for routine checkup
    }

    @Override
    /**
     * Validates the routine checkup booking by ensuring that a clinic room and a main doctor have been assigned.
     * @return true if the booking is valid, false otherwise
     */
    public boolean validateBooking() {
        // Check if a valid clinic room has been assigned
        if (this.clinicRoomNum <= 0) {
            return false;
        }
        // Check if a main doctor has been assigned        
        if (this.mainDoctor == null) {
            return false;
        }
        // Both valid, return true
        return true; 
    }

    /**
     * Overloaded method that estimates the duration of the routine checkup based on the reason for visit
     * @param reasonForVisit the reason for the visit
     * @return the estimated duration
     */ 
    public double estimateDuration(String reasonForVisit) {
        duration = 15; //default duration
        switch(reasonForVisit) {
            case "Annual Physical":
                duration += 15;
            case "Flu Symptoms":
                duration += 10;
            case "Vaccination":
                duration += 15;
        }
        return duration;
    }
    
    /**
     * Overloaded method that assigns staff to the routine checkup
     * @param mainDr the main doctor for the checkup
     */
   public void assignStaff(Doctor mainDr) {
        staffList[0] = mainDr; //assuming the main doctor is always the first staff member in the list
   }

}
