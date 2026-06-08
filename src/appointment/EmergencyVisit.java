/** 
 * File: EmergencyVisit.java
 * Name: Ida Luo
 * Date: June 6, 2026
 * Class: ICS4U1
 * Description: This class represents an emergency visit appointment in the hospital management system. It extends the abstract Appointment class and includes specific fields and methods related to emergency visits, such as emergency room number, urgency index, and triage functionality. The class also includes functionality to assign an emergency room based on availability and urgency.
*/

package appointment;

import patient.Patient;
import shared.Date;
import staff.Doctor;
import staff.Nurse;
import staff.Staff;

public class EmergencyVisit extends Appointment {
    private int emergencyRoomNum; //number of the emergency room assigned for the visit
    private int urgencyIdx; //urgency index from 1 to 5, 5 being most urgent

    //constants
    public static final int MAX_ER_BAYS = 10;

    /**
     * Constructor for the EmergencyVisit class
     * @param apptID unique identifier for the appointment
     * @param patient the patient associated with the appointment
     * @param staffList the staff members associated with the appointment
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @param duration the duration of the appointment
     * @param cost the cost of the appointment
     * @param status the status of the appointment
     * @param emergencyRoomNum the emergency room number assigned for the visit
     * @param urgenceyIdx the urgency index from 1 to 5, 5 being most urgent
     */
    public EmergencyVisit(int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status,
     int emergencyRoomNum, int urgencyIdx) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.emergencyRoomNum = emergencyRoomNum;
        this.urgencyIdx = urgencyIdx;
    }

    @Override
    /**
     * Returns the emergency room number assigned for the visit.
     * @return the emergency room number
     */
    public int getRoomNum() {
        return emergencyRoomNum;
    }

    /**
    * Sets the emergency room number for the visit.
    * @param emergencyRoomNum the emergency room number to set
    */
    public void setEmergencyRoomNum(int emergencyRoomNum) {
        this.emergencyRoomNum = emergencyRoomNum;
    }

    /**
     * Returns the urgency index for the visit.
     * @return the urgency index
     */
    public int getUrgencyIdx() {
        return urgencyIdx;
    }

    /**
     * Sets the urgency index for the visit.
     * @param urgencyIdx the urgency index to set
     */
    public void setUrgencyIdx(int urgencyIdx) {
        this.urgencyIdx = urgencyIdx;
    }

    /**
     * Automatically assigns a nurse to the emergency visit.
     * @param n the nurse to assign
     */
    public void autoAssignNurse(Nurse n) {
        // Staff assignment is handled elsewhere. This method is intentionally left blank.
    }

    /**
     * Assigns a senior doctor and trauma nurses to the emergency visit.
     * @param d the senior doctor to assign
     * @param nurses the array of trauma nurses to assign
     */
    public void urgentAssignStaff(Doctor d, Nurse[] nurses) {
        // Staff assignment is handled elsewhere. This method is intentionally left blank.
    }

    /**
     * Calculates the number of nurses needed for the emergency visit based on the urgency index. The method uses a simple threshold system where higher urgency levels require more nurses. For example, an urgency index of 4 or 5 may require 3 nurses, while an index of 3 may require 2 nurses, and lower indices may only require 1 nurse.
     * @return the calculated number of nurses needed for the emergency visit
     */
    public int calculateNursesNeeded() {
        if (this.urgencyIdx >= 4) {
            return 3;
        } else if (this.urgencyIdx == 3) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Assigns an emergency room for the visit by checking the appointment manager for availability. The method loops through all available emergency rooms to find an open one. If no rooms are available, it will return -1 to indicate that no room was assigned.
     * @param manager the appointment manager to check for room availability
     * @return the assigned emergency room number, or -1 if no rooms are available
     */
    public int assignEmergencyRoom(ApptManager manager) {
        int maxERBays = MAX_ER_BAYS; 

        for (int room = 1; room <= maxERBays; room++) {
            // Checking if the ER bay is currently occupied
            if (!manager.isRoomOccupied(EmergencyVisit.class, room, this.getDate(), this.getTime(), this.getDuration())) {
                this.emergencyRoomNum = room;
                return room;
            }
        }
        System.out.println("CRITICAL ERROR: ER is at maximum capacity! Divert to another hospital.");
        return -1; // Return -1 to indicate no room was assigned
    }

    @Override
    /**
     * Returns a string representation of the emergency visit.
     * @return the string representation
     */
    public String toString() {
        // Call super.toString() and add ER specific variables
        return super.toString() + 
               "\nEmergency Room: " + this.emergencyRoomNum + 
               "\nUrgency Index: " + this.urgencyIdx;
    }

    @Override
    /**
     * Calculates the total cost of the emergency visit based on the base fee, medications, and duration.
     * @return the calculated total cost
     */
    public double calculateCost() {
        double baseFee = 150.00; // Standard ER entry fee
        double hourlyRate = 100.00; 
        
        // Base fee + duration * hourly rate
        double totalCost = baseFee + (this.getDuration() * hourlyRate);
        
        return totalCost;
    }

    @Override
    /**
     * Validates the emergency visit booking by ensuring that an emergency room has been successfully assigned. Since emergency visits are urgent by nature, the primary validation is to confirm that the appointment has a valid emergency room number. If the emergency room number is greater than 0, it indicates that a room has been assigned and the booking is valid. If no room was assigned (emergencyRoomNum <= 0), the booking is considered invalid.
     * @return true if the booking is valid (emergency room assigned), false otherwise
     */
    public boolean validateBooking() {
        // 1. Check if an emergency room number was successfully assigned
        if (this.emergencyRoomNum > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Overloaded method that estimates the duration of the emergency visit based on the urgency index.
     * @return the estimated duration
     */
    public double estimateDuration() {
        double duration = 0.5; //Baseline value of 0.5 hours
        
        duration = duration * this.urgencyIdx; // Multiply by urgencyIdx
        
        if (this.urgencyIdx >= 2) {
            duration += 1.5; // Add an additional 1.5 hours
        }
        if (this.urgencyIdx >= 4) {
            duration += 2.0; // Add an additional 2 hours for critical care
        }
        
        this.setDuration(duration); // Update the internal duration variable
        return duration;
    }

    /**
     * Overloaded method that assigns a doctor to the emergency visit.
     * @param d the doctor to assign
     */
    public void assignStaff(Doctor d) {
        // Staff assignment is handled elsewhere. This method is intentionally left blank.
    }

}
