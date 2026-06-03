package appointment;

import staff.Doctor;

public class RoutineCheckup extends Appointment {
    private int clinicRoomNum;
    private Doctor mainDoctor;

    //constructor
    public RoutineCheckup(int apptID, Patient patient, Staff[] staffList, Date date, 
    double time, double duration, double cost, String status, int clinicRoomNum, Doctor mainDoctor) {
        super(apptID, patient, staffList, date, time, duration, cost, status);
        this.clinicRoomNum = clinicRoomNum;
        this.mainDoctor = mainDoctor;
    }

    //accessors and mutators
    public int getClinicRoomNum() {
        return clinicRoomNum;
    }

    public void setClinicRoomNum(int clinicRoomNum) {
        this.clinicRoomNum = clinicRoomNum;
    }

    public Doctor getMainDoctor() {
        return mainDoctor;
    }

    public void setMainDoctor(Doctor mainDoctor) {
        this.mainDoctor = mainDoctor;
    }

    public void assignClinicRoom(int clinicRoomNum) {
        //???????
    }

    public void markNoShow() {
        status = STATUS_NO_SHOW;
        cost = NO_SHOW_FEE;
    }

    public String toString() {
        return "Routine Checkup Appointment: " + super.toString() + ", 
        Clinic Room Number: " + clinicRoomNum + ", Main Doctor: " + mainDoctor.getName();
    }

    public double calculateCost() {
        cost = 100; //base cost for routine checkup
    }

    public boolean validateBooking(boolean doctorIsFree, boolean roomIsFree) {
        return doctorIsFree && roomIsFree && clinicRoomNum > 0 && mainDoctor != null;
    }

    
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
    

   public void assignStaff(Doctor mainDr) {
        this.mainDoctor = mainDr;
        staffList[0] = mainDr; //assuming the main doctor is always the first staff member in the list
   }

}
