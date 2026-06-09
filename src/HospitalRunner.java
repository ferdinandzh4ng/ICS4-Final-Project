
import java.util.*;

public class HospitalRunner {

    public static void main(String[] args) {
        String mainSelectionStr;
        int mainSelection = -1;
        String patientSelectionStr;
        int patientSelection = -1;
        boolean exit = false;
        boolean valid = false;
        Scanner sc = new Scanner(System.in);

        while (!exit) {
            // main menu
            System.out.println("Main Menu: ");
            System.out.println("1. Manage Appointments");
            System.out.println("2. Manage Patients");
            System.out.println("3. Manage Staff");
            System.out.println("4: Exit");
            System.out.print("Enter your selection (1, 2, 3): ");

            mainSelectionStr = sc.nextLine();

            switch (mainSelectionStr) {
                case "1":
                    mainSelection = 1;
                    break;
                case "2":
                    mainSelection = 2;
                    break;
                case "3":
                    mainSelection = 3;
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
            System.out.println();

            if (mainSelection == 2) {
                System.out.println("Patient Management: ");
                System.out.println("1. Register, delete, or update a patient");
                System.out.println("2. Search for a patient");
                System.out.println("3. Sort patients");
                System.out.println("4. Manage patient appointments, diagnoses, medication, allergies, or medical/family history");
                System.out.println("5. Manage in-patient specific information (medications administered and vitals log)");
                System.out.println("6. Manage emergency-patient specific information (patient status)");
                System.out.println("7. Update assigned staff for patient");
                System.out.println("8. Check in a patient");
                System.out.println("9. Check out a patient");
                System.out.println("10. Calculate total cost for a patient");
                System.out.println("11. List all patients");
                System.out.println("12. List all patient appointments");
                System.out.println("13. Back to main menu");
                System.out.print("Enter your selection (1, 2, ... 13): ");

                patientSelectionStr = sc.nextLine();
                valid = true;

                switch (patientSelectionStr) {
                    case "1":
                        patientSelection = 1;
                        break;
                    case "2":
                        patientSelection = 2;
                        break;
                    case "3":
                        patientSelection = 3;
                        break;
                    case "4":
                        patientSelection = 4;
                        break;
                    case "5":
                        patientSelection = 5;
                        break;
                    case "6":
                        patientSelection = 6;
                        break;
                    case "7":
                        patientSelection = 7;
                        break;
                    case "8":
                        patientSelection = 8;
                        break;
                    case "9":
                        patientSelection = 9;
                        break;
                    case "10":
                        patientSelection = 10;
                        break;
                    case "11":
                        patientSelection = 11;
                        break;
                    case "12":
                        patientSelection = 12;
                        break;
                    case "13":
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }

                if (patientSelection == 1) {
                    System.out.println("Register, delete, or update a patient:");
                    
                }

                mainSelection = -1;
                System.out.println();
            }
        }
    }
}
