package appointment;

import shared.Date;
import patient.Patient;
import staff.Doctor;
import staff.Nurse;
import staff.Staff;
import staff.Surgeon;

public class Surgery extends Appointment{
    private double anaesthesiaDose;
    private String anaesthesiaType;
    private String type;
    private int operatingRoom;
    private String preOpInstructions;

    public Surgery (int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status, double anaesthesiaDose, String anaesthesiaType, String type, int operatingRoom, String preOpInstructions) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.anaesthesiaDose = anaesthesiaDose;
        this.anaesthesiaType = anaesthesiaType;
        this.type = type;
        this.operatingRoom = operatingRoom;
        this.preOpInstructions = preOpInstructions;
    }
}
