package appointment;

import patient.Patient;
import shared.Date;
import staff.Doctor;
import staff.Staff;

public class RoutineCheckup extends Appointment {
    private int clinicRoomNum;
    private Doctor mainDoctor;

    public RoutineCheckup (int apptID, Patient patient, Staff[] staffList, Date date, double time, double duration, double cost, String status, int clinicRoomNum, Doctor mainDoctor) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.clinicRoomNum = clinicRoomNum;
        this.mainDoctor = mainDoctor;
    }
}
