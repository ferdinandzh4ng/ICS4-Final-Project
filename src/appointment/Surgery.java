/** 
 * File: Surgery.java
 * Name: Ida Luo
 * Date: June 7, 2026
 * Class: ICS4U1
 * Description: This class represents a surgery appointment in the hospital management system. 
 * It extends the abstract Appointment class and includes specific fields and methods related to surgeries. 
 * The class also includes functionality to assign an operating room based on availability and to give pre-operative instructions.
*/

package appointment;

import staff.Nurse;
import staff.Surgeon;

public class Surgery extends Appointment {
    private int operatingRoomNum; //number of the operating room assigned for the surgery
    private String anaesthesiaType; //type of anaesthesia used for the surgery (e.g., general, local, regional)
    private double anaesthesiaDose; //dose of anaesthesia administered for the surgery (in milligrams)
    private String type; //type of surgery
    private String preOpInstructions; //pre-operative instructions for the patient (e.g., fasting requirements, medication restrictions)

    //constants
    public static final int MAX_OR_ROOMS = 10;
    public static final int SURGERY_COST_BASE = 3000; //base cost for surgery, can be modified based on type and anaesthesia

    /**
     * Constructor for surgery appointment
     * @param apptID unique identifier for the appointment
     * @param patient the patient associated with the appointment
     * @param staffList the staff members associated with the appointment
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @param duration the duration of the appointment
     * @param cost the cost of the appointment
     * @param status the status of the appointment
     * @param operatingRoomNum the room number assigned for the surgery
     * @param anaesthesiaType the type of anaesthesia used for the surgery
     * @param anaesthesiaDose the dose of anaesthesia administered for the surgery
     * @param type the type of surgery
     * @param preOpInstructions the pre-operative instructions for the patient
     */
    public Surgery(int apptID, Patient patient, Staff[] staffList, Date date, double time, String status,
     int operatingRoomNum, String anaesthesiaType, double anaesthesiaDose, String type, String preOpInstructions) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.operatingRoomNum = operatingRoomNum;
        this.anaesthesiaType = anaesthesiaType;
        this.anaesthesiaDose = anaesthesiaDose;
        this.type = type;
        this.preOpInstructions = preOpInstructions;
    }

    //accessors and mutators
    @Override
    /**
     * Returns the operating room number for the surgery. 
     * In the context of a surgery appointment, the "room number" refers to the operating room assigned for the procedure.
     * @return the operating room number
     */
    public int getRoomNum() {
        return operatingRoomNum;
    }

    /**
    * Sets the operating room number for the surgery.
    * @param operatingRoomNum the room number to set
    */
    public void setOperatingRoomNum(int operatingRoomNum) {
        this.operatingRoomNum = operatingRoomNum;
    }

    /**
     * Returns the type of anaesthesia used for the surgery.
     * @return the anaesthesia type
     */
    public String getAnaesthesiaType() {
        return anaesthesiaType;
    }

    /**
     * Sets the type of anaesthesia used for the surgery.
     * @param anaesthesiaType the anaesthesia type to set
     */
    public void setAnaesthesiaType(String anaesthesiaType) {
        this.anaesthesiaType = anaesthesiaType;
    }

    /**
     * Returns the dose of anaesthesia administered for the surgery.
     * @return the anaesthesia dose
     */
    public double getAnaesthesiaDose() {
        return anaesthesiaDose;
    }

    /**
     * Sets the dose of anaesthesia administered for the surgery.
     * @param anaesthesiaDose the anaesthesia dose to set
     */
    public void setAnaesthesiaDose(double anaesthesiaDose) {
        this.anaesthesiaDose = anaesthesiaDose;
    }

    /**
     * Returns the type of surgery.
     * @return the surgery type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of surgery.
     * @param type the surgery type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Overloaded method that assigns an operating room for the surgery by checking the appointment manager for availability. The method first tries to assign the preferred operating room specified by the caller. If the preferred room is occupied, it will loop through all available operating rooms to find an open one. If no rooms are available, it will return -1 to indicate that no room was assigned.
     * @param manager the appointment manager to check for room availability
     * @param preferredOR the preferred operating room number to assign
     * @return the assigned operating room number, or -1 if no rooms are available
     */
    public int assignOperatingRoom(ApptManager manager, int preferredOR) {
        int maxORs = MAX_OR_ROOMS;

        //Try to assign the preferred OR first
        if (!manager.isRoomOccupied(this.getClass(), preferredOR, this.getDate(), this.getTime(), this.getDuration())) {
            this.operatingRoomNum = preferredOR;
            return preferredOR;
        }

        //If the preferred OR is busy, loop through the rest to find any backup
        for (int room = 1; room <= maxORs; room++) {
            if (!manager.isRoomOccupied(this.getClass(), room, this.getDate(), this.getTime(), this.getDuration())) {
                this.operatingRoomNum = room;
                return room;
            }
        }
        return -1; // Return -1 to indicate no room was assigned
    }

    /**
     * Gives pre-operative instructions to the patient based on the type of anaesthesia used for the surgery. The method sets the preOpInstructions field with specific instructions depending on whether general, local, or regional anaesthesia is used. 
     * Additional instructions can be added based on the specific type of surgery being performed.
     */
    public void givePreOpInstructions() {
        switch(anaesthesiaType) {
            case "General":
                preOpInstructions = "Do not eat or drink anything after midnight before the surgery.";
                break;
            case "Local":
                preOpInstructions = "You can eat and drink normally before the surgery.";
                break;
            case "Regional":
                preOpInstructions = "Do not eat or drink anything for at least 6 hours before the surgery.";
                break;
        }

        //additional instructions based on surgery type
    }

    /**
     * Returns a string representation of the surgery appointment.
     * @return a string representing the surgery appointment
     */
    public String toString() {
        return "Surgery Appointment: " + super.toString() + ", Operating Room Number: " + operatingRoomNum + 
        ", Anaesthesia Type: " + anaesthesiaType + ", Anaesthesia Dose: " + anaesthesiaDose + 
        ", Surgery Type: " + type + ", Pre-Op Instructions: " + preOpInstructions;
    }

    /**
     * Calculates the cost of the surgery based on its type and the anaesthesia used.
     * @return the calculated cost of the surgery
     */
    public double calculateCost() {
        cost = SURGERY_COST_BASE; 
        //COME UP WITH GENERAL TYPES OF SURGERIES AND ADD COSTS
        return cost;
    }

    @Override
    /**
     * Validates the surgery booking by ensuring that an operating room has been successfully assigned and that the lead surgeon's specialty matches the surgery type.
     * @return true if the booking is valid (room available and surgeon qualified), false otherwise
     */
    public boolean validateBooking() {
        //validate operating room
        if (this.operatingRoomNum <= 0) {
            return false;
        }

        //validate surgeon specialty matches surgery type
        Staff[] team = this.getStaffList();
        if (team == null || team.length == 0 || team[0] == null) {
            return false; // No surgeon assigned at all
        }
        Surgeon leadSurgeon = (Surgeon) team[0];

        if (leadSurgeon.getSpecialty().equals(this.type)) {
            return true; // The surgeon is qualified for this surgery type
        } else {
            System.out.println("Validation Error: Surgeon specialty does not match Surgery type.");
            return false;
        }
    }

    /**
     * Overloaded method that assigns staff to the surgery.
     * @param surgeon the lead surgeon for the surgery
     * @param nurses the nurses to assign
     */
    public void assignStaff(Surgeon surgeon, Nurse[] nurses) {
        staffList[0] = surgeon;
        for(int j = 0; j < nurses.length; j++) {
            staffList[j + 1] = nurses[j];
        }
    }

    /**
     * Overloaded method that estimates the duration of the surgery based on its type.
     * @return the estimated duration
     */
    public double estimateDuration() {
        //COME UP WITH GENERAL TYPES OF SURGERIES AND ADD DURATIONS
        return duration;
    }
}