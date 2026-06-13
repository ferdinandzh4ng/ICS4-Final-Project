package patient;

import appointment.*;
import shared.Date;
import staff.*;

/**
 * File: OutPatient.java
 * Name: Caroline Chan
 * Class: ICS4U1
 * Date: June 4, 2026
 * Description: This class represents an out patient in a hospital.
 */

public class OutPatient extends Patient {
    private int appointmentTimingMonths; // The number of months until the patient's next appointment

    /**
     * Constructor for the OutPatient class with all fields
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
    public OutPatient (int patientID, String firstName, String lastName, Date dateOfBirth, String ward, String address, long phoneNum, String numOHIP, Date dateRegistered, char gender, long emergencyContactPhoneNumber, Staff assignedStaff, int appointmentTimingMonths) {
        super(patientID, firstName, lastName, dateOfBirth, ward, address, phoneNum, numOHIP, dateRegistered, gender, emergencyContactPhoneNumber, assignedStaff);
        this.appointmentTimingMonths = appointmentTimingMonths;
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
     * Calculates the total bill for this out-patient (appointment fees only).
     *
     * @return total bill amount
     */
    @Override
    public double calculateBill() {
        return calculateTotalCost();
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
        Appointment todayAppt = getApptByDateUpcoming(PatientManager.CUR_DATE);
        if (todayAppt != null) {
            addToHistory(todayAppt);
        }
        
        calculateBill();
        if (followUp.equals("checkup")) {
            scheduleNextRoutineCheckup();
            return true;
        } else if (followUp.equals("surgery")) {
            scheduleNextSurgery();
            return true;
        } else if (followUp.equals("none")) {
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
        Appointment todayAppt = getApptByDatePast(PatientManager.CUR_DATE);
        Doctor doctor = null;
        if (todayAppt != null) {
            doctor = getFollowUpDoctor(todayAppt);
        }
        RoutineCheckup newAppt = new RoutineCheckup(
            (int)(Math.random() * 1000) + 9000,
            this,
            doctor != null ? new Staff[]{doctor} : null,
            PatientManager.CUR_DATE.addDays(appointmentTimingMonths * 30),
            9.0,
            0.5,
            0.0,
            Appointment.STATUS_SCHEDULED,
            1,
            doctor
        );
        addUpcomingAppointment(newAppt);
    }

    /**
     * Schedules a surgery appointment
     */
    @Override
    public void scheduleNextSurgery() {
        staff.Surgeon placeholderSurgeon = new staff.Surgeon(
            "TBD", "TBD", 0, "General",
            new String[0], new Appointment[0],
            1, 0, "General", 0.0
        );
        Staff[] surgeryStaff = new Staff[]{placeholderSurgeon};
        Surgery newAppt = new Surgery(
            (int)(Math.random() * 1000) + 9000,
            this,
            surgeryStaff,
            PatientManager.CUR_DATE.addDays(appointmentTimingMonths * 30),
            9.0,
            2.0,
            0.0,
            Appointment.STATUS_SCHEDULED,
            1,
            "none",
            0.0,
            "General",
            null
        );
        addUpcomingAppointment(newAppt);
    }
}
