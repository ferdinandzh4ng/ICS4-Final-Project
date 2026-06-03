package patient;

import appointment.Appointment;
import shared.Date;
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
     * Returns a string representation of the OutPatient object, including the patient's information and appointment details
     * @return a string representation of the OutPatient object
     */
    @Override
    public String toString () {
        return super.toString() + "\nAppointment Timing (months): " + appointmentTimingMonths;
    }


}
