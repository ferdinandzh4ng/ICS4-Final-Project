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

import shared.Date;
import patient.Patient;
import staff.Nurse;
import staff.Staff;
import staff.Surgeon;

public class Surgery extends Appointment {
    private int operatingRoomNum; //number of the operating room assigned for the surgery
    private String anaesthesiaType; //type of anaesthesia used for the surgery (e.g., general, local, regional)
    private double anaesthesiaDose; //dose of anaesthesia administered for the surgery (in milligrams)
    private String type; //type of surgery
    private String preOpInstructions; //pre-operative instructions for the patient (e.g., fasting requirements, medication restrictions)

    //constants
    public static final int MAX_OR_ROOMS = 10;
    public static final int SURGERY_COST_BASE = 3000; // fallback base cost when surgery type is unknown

    // Canonical surgery types — must match Surgeon.specialtyArea in data/staff.txt
    public static final String[] SURGERY_TYPES = {
        "General", "Orthopedic", "Cardiac", "Neuro", "Oncology", "Vascular"
    };

    // Parallel arrays indexed by SURGERY_TYPES
    private static final double[] SURGERY_FEES = {
        3500.00, 4200.00, 5500.00, 6000.00, 4800.00, 5200.00
    };
    private static final double[] SURGERY_HOURS = {
        2.0, 2.5, 4.0, 3.5, 3.0, 2.5
    };

    // Anaesthesia surcharges (matches General / Regional / Local in data files)
    public static final double ANAESTH_GENERAL_SURCHARGE = 500.00;
    public static final double ANAESTH_REGIONAL_SURCHARGE = 350.00;
    public static final double ANAESTH_LOCAL_SURCHARGE = 150.00;
    public static final double ANAESTH_DOSE_RATE = 25.00; // dollars per mg of anaesthesia dose

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
    public Surgery(int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status,
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

    @Override
    public String getTypeLabel() {
        return "Surgery";
    }

    @Override
    public String getLocationLabel() {
        return "OR " + operatingRoomNum;
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
     * Returns the pre-operative instructions for the surgery.
     * @return the pre-operative instructions string, or empty string if none
     */
    public String getPreOpInstructions() {
        if (preOpInstructions != null) {
            return preOpInstructions;
        } else {
            return "";
        }
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

        if (!manager.isRoomOccupied(this.getClass(), preferredOR, this.getDate(), this.getTime(), this.getDuration())) {
            this.operatingRoomNum = preferredOR;
            return preferredOR;
        }

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
    @Override
    public String toString() {
        return "Surgery Appointment: " + super.toString() + ", Operating Room Number: " + operatingRoomNum
            + ", Anaesthesia Type: " + anaesthesiaType + ", Anaesthesia Dose: " + anaesthesiaDose
            + ", Surgery Type: " + type + ", Pre-Op Instructions: " + preOpInstructions;
    }

    /**
     * Calculates the cost of the surgery based on its type and the anaesthesia used.
     * @return the calculated cost of the surgery
     */
    @Override
    public double calculateCost() {
        double procedureFee = SURGERY_COST_BASE;
        if (type != null) {
            for (int i = 0; i < SURGERY_TYPES.length; i++) {
                if (SURGERY_TYPES[i].equalsIgnoreCase(type.trim())) {
                    procedureFee = SURGERY_FEES[i];
                    break;
                }
            }
        }

        double anaesthesiaFee = 0.0;
        if (anaesthesiaType != null) {
            switch (anaesthesiaType) {
                case "General":
                    anaesthesiaFee = ANAESTH_GENERAL_SURCHARGE;
                    break;
                case "Regional":
                    anaesthesiaFee = ANAESTH_REGIONAL_SURCHARGE;
                    break;
                case "Local":
                    anaesthesiaFee = ANAESTH_LOCAL_SURCHARGE;
                    break;
                default:
                    anaesthesiaFee = 0.0;
                    break;
            }
        }

        double doseFee = anaesthesiaDose * ANAESTH_DOSE_RATE;
        setCost(procedureFee + anaesthesiaFee + doseFee);
        return getCost();
    }

    @Override
    /**
     * Validates the surgery booking by ensuring that an operating room has been successfully assigned and that the lead surgeon's specialty matches the surgery type.
     * @return true if the booking is valid (room available and surgeon qualified), false otherwise
     */
    public boolean validateBooking() {
        boolean validType = false;
        if (type != null) {
            for (int i = 0; i < SURGERY_TYPES.length; i++) {
                if (SURGERY_TYPES[i].equalsIgnoreCase(type.trim())) {
                    validType = true;
                    break;
                }
            }
        }
        if (!validType) {
            System.out.println("Validation Error: Invalid surgery type.");
            return false;
        }

        if (this.operatingRoomNum <= 0) {
            return false;
        }

        Staff[] team = this.getStaffList();
        if (team == null || team.length == 0 || team[0] == null) {
            return false; // No surgeon assigned at all
        }
        Surgeon leadSurgeon = (Surgeon) team[0];
        String specialtyArea = leadSurgeon.getSpecialtyArea();

        if (specialtyArea != null && specialtyArea.equalsIgnoreCase(type.trim())) {
            return true;
        }
        System.out.println("Validation Error: Surgeon specialty does not match Surgery type.");
        return false;
    }

    /**
     * Overloaded method that assigns staff to the surgery.
     * @param surgeon the lead surgeon for the surgery
     * @param nurses the nurses to assign
     */
    public void assignStaff(Surgeon surgeon, Nurse[] nurses) {
        int nurseCount;
        if (nurses == null) {
            nurseCount = 0;
        } else {
            nurseCount = nurses.length;
        }
        int requiredSize = 1 + nurseCount;
        Staff[] list = getStaffList();
        if (list == null || list.length < requiredSize) {
            list = new Staff[requiredSize];
        }
        list[0] = surgeon;
        if (nurses != null) {
            for (int j = 0; j < nurses.length; j++) {
                list[j + 1] = nurses[j];
            }
        }
        super.assignStaff(list);
    }

    /**
     * Overloaded method that estimates the duration of the surgery based on its type.
     * @return the estimated duration
     */
    public double estimateDuration() {
        double hours = SURGERY_HOURS[0];
        if (type != null) {
            for (int i = 0; i < SURGERY_TYPES.length; i++) {
                if (SURGERY_TYPES[i].equalsIgnoreCase(type.trim())) {
                    hours = SURGERY_HOURS[i];
                    break;
                }
            }
        }

        if (anaesthesiaType != null) {
            switch (anaesthesiaType) {
                case "General":
                    hours += 0.5;
                    break;
                case "Regional":
                    hours += 0.25;
                    break;
                case "Local":
                    hours -= 0.25;
                    break;
                default:
                    break;
            }
        }

        if (hours < 1.0) {
            hours = 1.0;
        }

        setDuration(hours);
        return hours;
    }
}
