package appointment;

public class Appointment {
    private int apptID;
    private Patient patient;
    private Staff staff;
    private Date date;
    private double time;
    private double duration;
    private double cost;
    private String status;

    //constructor
    public Appointment(int apptID, Patient patient, Staff staffList, Date date, double time, double duration, double cost, String status) {
        this.apptID = apptID;
        this.patient = patient;
        this.staffList = staffList;
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

    public Staff getStaffList() {
        return staffList;
    }

    public void setStaffList(Staff staffList) {
        this.staffList = staffList;
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



    

}
