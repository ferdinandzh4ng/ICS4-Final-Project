package appointment;

import java.io.*;

public class ApptManager {
    private Appointment[] appointments; //array to store all appointments
    private int numAppointments; //number of appointments currently stored
    private int maxAppointments; //maximum capacity of the appointments array

    /**
     * Constructor for the appointment manager
     * @param maxAppointments the maximum number of appointments that can be stored in the manager
     */
    public ApptManager(int maxAppointments) {
        this.maxAppointments = maxAppointments;
        appointments = new Appointment[maxAppointments];
        numAppointments = 0;
    }

    //method to add an appointment to the manager
    public void addAppointment(Appointment appt) {
        if (numAppointments < maxAppointments) {
            appointments[numAppointments] = appt;
            numAppointments++;
        } else {
            System.out.println("Appointment manager is full. Cannot add more appointments.");
        }
    }

    /**
     * Helper method to check if a room of a specific type is occupied at a given time
     * @param apptClass the class of the appointment type (e.g., RoutineCheckup.class)
     * @param testRoomNum the room number to check
     * @param targetDate the date to check
     * @param targetTime the time to check
     * @return true if the room is occupied, false otherwise
     */
    /**
 * Scans the schedule to see if a physical room is already booked during a specific time block.
 */
    public boolean isRoomOccupied(Class<?> apptClass, int testRoomNum, Date targetDate, double targetTime, double targetDuration) {
        for (int i = 0; i < numAppt; i++) {
            Appointment a = apptList[i];

            // Skip cancelled, done, or no-show appointments since they don't occupy the room
            if (!a.isActive()) {
                continue;
            }

            // Check if the appointment is on the same date as the target
            if (a.getDate().compareTo(targetDate) == 0) {
                
                // Ensure they are the same type of room, and the room numbers match.
                if (a.getClass().equals(apptClass) && a.getRoomNum() == testRoomNum) {
                    
                    //Check for duration overlap
                    int existingStart = Appointment.toMinutes(a.getTime());
                    int existingEnd = existingStart + (int) Math.round(a.getDuration() * 60);
                    
                    int targetStart = Appointment.toMinutes(targetTime);
                    int targetEnd = targetStart + (int) Math.round(targetDuration * 60);

                    // If the time blocks overlap, the room is currently occupied
                    if (!(targetStart >= existingEnd || existingStart >= targetEnd)) {
                        return true;
                    }
                }
            }
        }
        return false; // No conflicts found, the room is not occupied
    }

}
