package appointment;

import staff.Doctor;
import staff.Nurse;

public class EmergencyVisit {
    private int emergencyRoomNum;
    private int urgenceyIdx; //from 1 to 5, 5 being most urgent

    //constructor
    public EmergencyVisit(int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status,
     int emergencyRoomNum, int urgenceyIdx) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.emergencyRoomNum = emergencyRoomNum;
        this.urgenceyIdx = urgenceyIdx;
    }

    //accessors and mutators
    public int getEmergencyRoomNum() {
        return emergencyRoomNum;
    }

    public void setEmergencyRoomNum(int emergencyRoomNum) {
        this.emergencyRoomNum = emergencyRoomNum;
    }

    public int getUrgenceyIdx() {
        return urgenceyIdx;
    }

    public void setUrgenceyIdx(int urgenceyIdx) {
        this.urgenceyIdx = urgenceyIdx;
    }
}
