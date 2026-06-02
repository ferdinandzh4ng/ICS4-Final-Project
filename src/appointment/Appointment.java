package appointment;

public abstract class Appointment {
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

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
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


    public void cancel() {
        status = "Cancelled";
    }

    public boolean reschedule(Date newDate, double newTime) {
        //store current date and time in case rescheduling fails
        Date curdate = this.date;
        double curtime = this.time;

        date = newDate;
        time = newTime;

        if (validateBooking()) {
            return true;
        } else {
            //revert to original date and time if rescheduling fails
            date = curdate;
            time = curtime;
            return false;

        }
    }

    public boolean equals(Appointment other) {
        if (this.date.equals(other.date) && this.time == other.time && this.roomNum == other.getRoomNum()) {
            for (int j = 0; j < this.staff.length; j++) {
                    if (this.staff[j].equals(other.staff[k])) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void markDone() {
        status = "Done";
        patient.addToHistory(this);
    }

    public String toString() {
        String staffStr = "";
        for (int i = 0; i < staff.length; i++) {
            staffStr += staff[i].getID() + " ";
        }
        return "Appointment ID: " + apptID + "\nPatient: " + patient.getName() 
        + "\nStaff: " + staffStr + "\nDate: " + date.toString() + "\nTime: " + time + "\nDuration: " + duration + "\nCost: " + cost + "\nStatus: " + status;
    }

    abstract public double calculateCost();

    abstract public boolean validateBooking();

    public void assignStaff(Staff[] staffTeam) {
        for (int i = 0; i < staffTeam.length; i++) {
            this.staff[i] = staffTeam[i];
        }
    }





    

}
