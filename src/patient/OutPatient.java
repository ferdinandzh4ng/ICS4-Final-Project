package patient;

import appointment.Appointment;
import appointment.RoutineCheckup;
import appointment.Surgery;
import shared.Date;
import staff.Doctor;
import staff.Staff;

public class OutPatient extends Patient {
    private int appointmentTimingMonths; // The number of months until the patient's next appointment

    /**
     * Constructor for the OutPatient class
     * @param patientID to be assigned to the patient
     * @param firstName to be assigned to the patient
     * @param lastName to be assigned to the patient
     * @param dateOfBirth to be assigned to the patient
     * @param ward to be assigned to the patient
     * @param address to be assigned to the patient
     * @param phoneNum to be assigned to the patient
     * @param numOHIP to be assigned to the patient
     * @param dateRegistered to be assigned to the patient
     * @param gender to be assigned to the patient
     * @param emergencyContactPhoneNumber to be assigned to the patient
     * @param assignedStaff to be assigned to the patient
     * @param appointmentTimingMonths the number of months until the patient's next appointment
     */
    public OutPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, int phoneNum, int numOHIP, Date dateRegistered, char gender, int emergencyContactPhoneNumber, Staff assignedStaff) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        appointmentTimingMonths = 6;
    }

    /**
     * Sets the number of months until the patient's next appointment
     * @param appointmentTimingMonths the number of months until the patient's next appointment
     */
    public void setAppointmentTimingMonths(int appointmentTimingMonths) {
        this.appointmentTimingMonths = appointmentTimingMonths;
    }

    /**
     * Returns the number of months until the patient's next appointment
     * @return the number of months until the next appointment
     */
    public int getAppointmentTimingMonths() {
        return appointmentTimingMonths;
    }

    /**
     * Returns a string representation of the OutPatient object, including the patient's information and appointment details
     * @return a string representation of the OutPatient object
     */
    @Override
    public String toString () {
        return super.toString() + "\nAppointment Timing (months): " + appointmentTimingMonths;
    }

    /**
     * Checks in the patient
     * @return true if the patient is checked in successfully, false otherwise
     */
    @Override
    public boolean checkIn() {
        Appointment set = getApptByDateUpcoming(PatientManager.CUR_DATE);
        if (set != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks out the patient from the hospital
     * @param followUp the type of follow-up care the patient will receive
     * @return true if the patient is successfully checked out, false otherwise
     */
    @Override
    public boolean checkOut(String followUp) {
        if (followUp.equals("checkup")) {
            scheduleNextRoutineCheckup();
            return true;
        } else if (followUp.equals("surgery")) {
            scheduleNextSurgery();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Schedules a routine checkup
     */
    @Override
    public void scheduleNextRoutineCheckup() {
        Appointment completed = getApptByDatePast(PatientManager.CUR_DATE);
        Doctor mainDoctorPlaceholder = new Doctor();
        Appointment newAppt = new RoutineCheckup(
            completed.getApptID() + 1,
            completed.getPatient(),
            completed.getStaffList(),
            PatientManager.CUR_DATE.addDays(appointmentTimingMonths * 30),
            completed.getTime(),
            completed.getDuration(),
            completed.getCost(),
            "future",
            1,
            mainDoctorPlaceholder);
        boolean validated = false;
        int dayCounter = appointmentTimingMonths * 30 + 1;

        while (!validated) {
            if (newAppt.validateBooking()) {
                validated = true;
            } else {
                newAppt.setDate(PatientManager.CUR_DATE.addDays(dayCounter));
                dayCounter++;
            }
        }

        addUpcomingAppointment(newAppt);
    }

    /**
     * Shedules a surgery appointment
     */
    @Override
    public void scheduleNextSurgery () {
        Appointment completed = getApptByDatePast(PatientManager.CUR_DATE);
        Appointment newAppt = new Surgery(
            completed.getApptID() + 1,
            completed.getPatient(),
            completed.getStaffList(),
            PatientManager.CUR_DATE.addDays(appointmentTimingMonths * 30),
            completed.getTime(),
            completed.getDuration(),
            completed.getCost(),
            "future",
            1,
            "none",
            0.0,
            "general",
            null
        );
        boolean validated = false;
        int dayCounter = appointmentTimingMonths * 30 + 1;

        while (!validated) {
            if (newAppt.validateBooking()) {
                validated = true;
            } else {
                newAppt.setDate(PatientManager.CUR_DATE.addDays(dayCounter));
                dayCounter++;
            }
        }

        addUpcomingAppointment(newAppt);
    }
}
