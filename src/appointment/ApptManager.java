/** 
 * File: ApptManager.java
 * Name: Ida Luo
 * Date: June 7, 2026
 * Class: ICS4U1
 * Description: This class manages Appointment and its child classes. 
 * It stores an array of appointments.
 * The class also includes functionalities related to searching, sorting and file input/output.
*/

package appointment;

import java.io.*;
import patient.Patient;
import patient.PatientManager;
import shared.Date;
import staff.Doctor;
import staff.Nurse;
import staff.Staff;
import staff.StaffManager;
import staff.Surgeon;

public class ApptManager {
    private Appointment[] appointments; //array to store all appointments
    private int numAppointments; //number of appointments currently stored
    private int maxAppointments; //maximum capacity of the appointments array
    private StaffManager staffManager;
    private PatientManager patientManager;

    /**
     * Constructor for an empty appointment manager
     * @param maxAppointments the maximum number of appointments that can be stored in the manager
     * @param staffManager staffManager instance to connect managers
     * @param patientManager patientManager instance to connect managers
     */
    public ApptManager(int maxAppointments, StaffManager staffManager, PatientManager patientManager) {
        this.maxAppointments = maxAppointments;
        appointments = new Appointment[maxAppointments];
        numAppointments = 0;
        this.staffManager = staffManager;
        this.patientManager = patientManager;
    }

    /**
     * Constructor that loads appointments from a file
     * @param maxAppointments the maximum number of appointments that can be stored in the manager
     * @param staffManager staffManager instance to connect managers
     * @param patientManager patientManager instance to connect managers
     * @param filename name of file to load appointments from
     */
    public ApptManager(int maxAppointments, StaffManager staffManager, PatientManager patientManager, String filename) {
        this.maxAppointments = maxAppointments;
        appointments = new Appointment[maxAppointments];
        numAppointments = 0;
        this.staffManager = staffManager;
        this.patientManager = patientManager;
        this.loadFromFile(filename);
    }

    // Accessors and Mutators
    public Appointment[] getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointment[] appointments) {
        this.appointments = appointments;
    }

    public int getNumAppointments() {
        return numAppointments;
    }

    public void setNumAppointments(int numAppointments) {
        this.numAppointments = numAppointments;
    }

    public int getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(int maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    /**
     * Add an appointment to the database
     * @param appt Appointment to add
     * @return true if appointment was successfully added, otherwise false
     */
    public boolean addAppointment(Appointment appt) {
        if (numAppointments >= maxAppointments) {
            System.out.println("Error: System is at maximum capacity.");
            return false;
        }
        if (!appt.validateBooking()) {
            return false; // Internal validation failed
        }
        if (isSlotConflict(appt)) {
            return false; // double booking detected
        }
        // Save the appointment
        appointments[numAppointments] = appt;
        numAppointments++;
        return true;
    }

    /**
     * Cancel an appointment and shifts the array accordingly
     * @param apptID ID of the appointment to cancel
     * @return true if the appointment is succesfully removed, otherwise false
     */
    public boolean cancelAppointment(int apptID) {
        // Find appointment
        Appointment target = searchByID(apptID, 0);
        if (target == null) {
            return false;
        }
        target.cancel(); // Sets status and clears staff

        // Find the exact index to remove it from the array
        int targetIdx = -1;
        for (int i = 0; i < numAppointments && targetIdx == -1; i++) {
            if (appointments[i].getApptID() == apptID) {
                targetIdx = i;
            }
        }

        if (targetIdx == -1) {
            return false;
        }

        // Shift remaining elements left to fill the gap
        for (int i = targetIdx; i < numAppointments - 1; i++) {
            appointments[i] = appointments[i + 1];
        }
        
        appointments[numAppointments - 1] = null; // Clear the last duplicated slot
        numAppointments--;
        return true;
    }

    /**
     * Reschedule an appointment by changing its date and time
     * @param apptID ID of appointment to reschedule
     * @param newDate date to change to
     * @param newTime time to change to
     * @return true if successfully rescheduled, otherwise false
     */
    public boolean rescheduleAppointment(int apptID, Date newDate, double newTime) {
        Appointment target = searchByID(apptID, 0);
        if (target == null) return false;

        Date oldDate = target.getDate();
        double oldTime = target.getTime();

        // Target reschedules itself internally
        if (!target.reschedule(newDate, newTime)) return false;

        // Verify the new time does not overlap with anything else
        if (isSlotConflict(target)) {
            target.reschedule(oldDate, oldTime); // Revert the change
            return false;
        }
        return true;
    }

    /**
     * Check if there is a conflict for any 2 appointments (overlapping staff or room)
     * @param checkAppt appointment to check conflicts for
     * @return true if there is any conflict, otherwise false
     */
    public boolean isSlotConflict(Appointment checkAppt) {
        for (int i = 0; i < numAppointments; i++) {
            Appointment existing = appointments[i];
            
            // Skip cancelled/inactive ones, and skip comparing to itself
            if (!existing.isActive()) {
                continue;
            }
            if (existing.getApptID() == checkAppt.getApptID()) {
                continue;
            }

            // Check for overlap
            if (existing.overlap(checkAppt)) {
                return true; 
            }
        }
        return false;
    }

    /**
     * Load appointment records from a file
     * @param filename name of the file to load from
     * @return true if successfully loaded from file, otherwise false
     */
    public boolean loadFromFile(String filename) {
        int num;
        String line;
        String type;
        int apptID;
        int patientID;
        Date date;
        double time;
        double duration;
        double cost;
        String status;
        int roomNum;
        
        String doctorID;
        String surgeryType;
        String anaesthesiaType;
        double anaesthesiaDose;
        String preOpInstructions;
        String surgeonID;
        int numNurses;

        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            
            // Read the first line for number of records
            String firstLine = in.readLine();
            if (firstLine == null) {
                in.close();
                return false;
            }
            num = Integer.parseInt(firstLine.trim());
            
            // Loop through each appointment record
            for (int i = 0; i < num; i++) {
                line = in.readLine();
                
                // Split the the line by commas
                String[] parts = line.split(",");
                
                // Extract common fields 
                type = parts[0].trim();
                apptID = Integer.parseInt(parts[1].trim());
                patientID = Integer.parseInt(parts[2].trim());
                
                // Find the patient object from database using their ID
                Patient patient = patientManager.searchPatientByPatientID(patientID);
                
                date = new Date(parts[3].trim()); 
                time = Double.parseDouble(parts[4].trim());
                status = parts[5].trim();
                roomNum = Integer.parseInt(parts[6].trim());
                duration = Double.parseDouble(parts[7].trim());
                cost = Double.parseDouble(parts[8].trim());
                
                // Process fields specific to each subclass type
                switch(type) {
                    case "Checkup":
                    case "RoutineCheckup":
                        doctorID = parts[9].trim();
                        
                        // Fetch the Doctor object from database
                        Doctor doctor = (Doctor) staffManager.findStaffByID(doctorID, 0);
                        
                        // Allocate space for the staff array (size 1 for single doctor)
                        Staff[] checkupStaff = new Staff[1];
                        checkupStaff[0] = doctor;
                        
                        // Instantiate and insert into appointments array
                        RoutineCheckup checkupAppt = new RoutineCheckup(
                            apptID, patient, checkupStaff, date, time, duration, cost, status, roomNum, doctor);
                        this.appointments[this.numAppointments] = checkupAppt;
                        this.numAppointments++;
                        break;
                        
                    case "Surgery":
                        surgeryType = parts[9].trim();
                        anaesthesiaType = parts[10].trim();
                        anaesthesiaDose = Double.parseDouble(parts[11].trim());
                        preOpInstructions = parts[12].trim();
                        surgeonID = parts[13].trim();
                        
                        Surgeon surgeon = (Surgeon) staffManager.findStaffByID(surgeonID, 0);
                        
                        // Determine how many nurses are attached to this surgery record 
                        // Calculated by taking the total - existing parts
                        numNurses = parts.length - 14; 
                        
                        // Allocate staff array: index 0 for surgeon + space for all nurses
                        Staff[] surgeryStaff = new Staff[1 + numNurses];
                        surgeryStaff[0] = surgeon;
                        
                        // Loop to find and attach each nurse
                        for (int j = 0; j < numNurses; j++) {
                            String nurseID = parts[14 + j].trim();
                            Nurse nurse = (Nurse) staffManager.findStaffByID(nurseID, 0);
                            surgeryStaff[1 + j] = nurse;
                        }   
                        
                        Surgery surgeryAppt = new Surgery(
                            apptID, patient, surgeryStaff, date, time, duration, cost, 
                            status, roomNum, anaesthesiaType, anaesthesiaDose, surgeryType, preOpInstructions
                        );
                        
                        this.appointments[this.numAppointments] = surgeryAppt;
                        this.numAppointments++;
                        break;
                        
                    case "EmergencyVisit":
                        int urgencyIdx = Integer.parseInt(parts[9].trim());
                        
                        Staff[] emergencyStaff = new Staff[5]; // Max amount of emergency staff
                        
                        EmergencyVisit emergencyAppt = new EmergencyVisit(
                            apptID, patient, emergencyStaff, date, time, duration, cost,
                            status, roomNum, urgencyIdx
                        );
                        
                        this.appointments[this.numAppointments] = emergencyAppt;
                        this.numAppointments++;
                        break;
                        
                    default:
                        System.out.println("Warning: Unknown appointment type format encountered: " + type);
                        break;
                }
            }
            
            in.close(); 
            return true;
            
        } catch (IOException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error reading or parsing the appointment file.");
            return false;
        }
    }

    /**
     * Save information to a file
     * @param filename name of file to save the information to
     * @return true if successfully saved to file, otherwise false
     */
    public boolean saveToFile(String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(String.valueOf(numAppointments));
            out.newLine();

            for (int i = 0; i < numAppointments; i++) {
                Appointment a = appointments[i];
                String line;

                // Common fields: type,apptID,patientID,date,time,status,roomNum,duration,cost
                if (a instanceof RoutineCheckup) {
                    line = "RoutineCheckup";
                } else if (a instanceof Surgery) {
                    line = "Surgery";
                } else if (a instanceof EmergencyVisit) {
                    line = "EmergencyVisit";
                } else {
                    line = "Unknown";
                }
                line = line + "," + a.getApptID() + "," + a.getPatient().getPatientID() + ","
                        + a.getDate().toISODateString().replace("-", "") + ","
                        + a.getTime() + "," + a.getStatus() + "," + a.getRoomNum() + ","
                        + a.getDuration() + "," + a.getCost();

                // Subclass-specific fields
                if (a instanceof RoutineCheckup) {
                    RoutineCheckup rc = (RoutineCheckup) a;
                    String doctorID = rc.getMainDoctor() != null ? rc.getMainDoctor().getStaffID() : "";
                    line = line + "," + doctorID;
                } else if (a instanceof Surgery) {
                    Surgery s = (Surgery) a;
                    Staff[] team = s.getStaffList();
                    String surgeonID = (team != null && team.length > 0 && team[0] != null)
                            ? team[0].getStaffID() : "";
                    line = line + "," + s.getType() + "," + s.getAnaesthesiaType() + ","
                            + s.getAnaesthesiaDose() + "," + s.getPreOpInstructions() + "," + surgeonID;
                    if (team != null) {
                        for (int j = 1; j < team.length; j++) {
                            if (team[j] != null) {
                                line = line + "," + team[j].getStaffID();
                            }
                        }
                    }
                } else if (a instanceof EmergencyVisit) {
                    EmergencyVisit ev = (EmergencyVisit) a;
                    line = line + "," + ev.getUrgencyIdx();
                }

                out.write(line);
                out.newLine();
            }
            out.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving to file.");
            return false;
        }
    }

    /**
     * Recursive linear search to find an appointment matching a patient ID and date.
     * @param patientID target patient ID
     * @param date target date
     * @param idx current search index (start at 0)
     * @return Appointment matching patient and date, null if not found
     */
    public Appointment searchByPatientAndDate(int patientID, Date date, int idx) {
        if (idx >= numAppointments) {
            return null;
        }
        if (appointments[idx].getDate().compareTo(date) == 0
                && appointments[idx].getPatient().getPatientID() == patientID) {
            return appointments[idx];
        }
        return searchByPatientAndDate(patientID, date, idx + 1);
    }
    

    /**
     * Recursive linear search to find an appointment by its ID.
     * @param apptID ID of target appointment
     * @param idx current search index (start at 0)
     * @return Appointment that matches the target ID, null if not found
     */
    public Appointment searchByID(int apptID, int idx) {
        if (idx >= numAppointments) {
            return null;
        }
        if (appointments[idx].getApptID() == apptID) {
            return appointments[idx];
        }
        return searchByID(apptID, idx + 1);
    }

    /**
     * Selection sort appointments by date
     */
    public void sortByDate() {
        // Selection Sort
        for (int i = 0; i < numAppointments - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < numAppointments; j++) {
                if (appointments[j].getDate().compareTo(appointments[minIdx].getDate()) < 0) {
                    minIdx = j; // Found an earlier date
                }
            }
            // Swap
            Appointment temp = appointments[minIdx];
            appointments[minIdx] = appointments[i];
            appointments[i] = temp;
        }
    }

    /**
     * Bubble sort appointments by patient then date
     */
    public void sortByPatientThenDate() {
        // Bubble Sort
        for (int i = 0; i < numAppointments - 1; i++) {
            for (int j = 0; j < numAppointments - i - 1; j++) {
                boolean swapNeeded = false;
                
                int p1 = appointments[j].getPatient().getPatientID();
                int p2 = appointments[j + 1].getPatient().getPatientID();

                if (p1 > p2) {
                    swapNeeded = true;
                } else if (p1 == p2) {
                    // Secondary sort by date if patient ID is the same
                    if (appointments[j].getDate().compareTo(appointments[j + 1].getDate()) > 0) {
                        swapNeeded = true;
                    }
                }

                if (swapNeeded) {
                    Appointment temp = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Output daily schedule for a certain date
     * @param date target date
     */
    public void viewDailySchedule(Date date) {
        System.out.println("*** Schedule for " + date.toString() + " ***");
        for (int i = 0; i < numAppointments; i++) {
            if (appointments[i].getDate().compareTo(date) == 0) {
                System.out.println(appointments[i].toString() + "\n");
            }
        }
    }

    /**
     * Output future appointments (marked as "SCHEDULED") for a given patient
     * @param patientID id of target patient
     */
    public void viewUpcomingAppointments(int patientID) {
        System.out.println("*** Upcoming Appointments for Patient " + patientID + " ***");
        for (int i = 0; i < numAppointments; i++) {
            if (appointments[i].getPatient().getPatientID() == patientID && appointments[i].getStatus().equals(Appointment.STATUS_SCHEDULED)) {
                System.out.println(appointments[i].toString() + "\n");
            }
        }
    }

    /**
     * Calculates total costs of all appointments on a certain date
     * @param date target date
     * @return double representing the costs of all appointments on that day
     */
    public double runCostSummary(Date date) {
        double totalRevenue = 0.0;
        for (int i = 0; i < numAppointments; i++) {
            if (appointments[i].getDate().compareTo(date) == 0) {
                totalRevenue += appointments[i].calculateCost();
            }
        }
        return totalRevenue;
    }

    /**
     * Helper method to check if a room of a specific type is occupied at a given time
     * @param apptClass the class of the appointment type (e.g., RoutineCheckup.class)
     * @param testRoomNum the room number to check
     * @param targetDate the date to check
     * @param targetTime the time to check
     * @param targetDuration the duration to check
     * @return true if the room is occupied, false otherwise
     */
    public boolean isRoomOccupied(Class<?> apptClass, int testRoomNum, Date targetDate, double targetTime, double targetDuration) {
        for (int i = 0; i < numAppointments; i++) {
            Appointment a = appointments[i];

            // Skip inactive appointments
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
