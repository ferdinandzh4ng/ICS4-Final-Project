package appointment;

import staff.Nurse;
import staff.Surgeon;

public class Surgery {
    private int operatingRoomNum;
    private String anaesthesiaType;
    private double anaesthesiaDose;
    private String type;
    private String preOpInstructions;

    //constructor
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
    public int getOperatingRoomNum() {
        return operatingRoomNum;
    }

    public void setOperatingRoomNum(int operatingRoomNum) {
        this.operatingRoomNum = operatingRoomNum;
    }

    public String getAnaesthesiaType() {
        return anaesthesiaType;
    }

    public void setAnaesthesiaType(String anaesthesiaType) {
        this.anaesthesiaType = anaesthesiaType;
    }

    public double getAnaesthesiaDose() {
        return anaesthesiaDose;
    }

    public void setAnaesthesiaDose(double anaesthesiaDose) {
        this.anaesthesiaDose = anaesthesiaDose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int assignOperatingRoom(int operatingRoomNum) {
        //???????
    }

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

    public String toString() {
        return "Surgery Appointment: " + super.toString() + ", Operating Room Number: " + operatingRoomNum + 
        ", Anaesthesia Type: " + anaesthesiaType + ", Anaesthesia Dose: " + anaesthesiaDose + 
        ", Surgery Type: " + type + ", Pre-Op Instructions: " + preOpInstructions;
    }

    public double calculateCost() {
        cost = 5000; //base cost for surgery
        //COME UP WITH GENERAL TYPES OF SURGERIES AND ADD COSTS
    }

    public boolean validateBooking() {
        //later
    }

    public void assignStaff(Surgeon surgeon, Nurse[] nurses) {
        staffList[0] = surgeon;
        for(int j = 0; j < nurses.length; j++) {
            staffList[j + 1] = nurses[j];
        }
    }
}
