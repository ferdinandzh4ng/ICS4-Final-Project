
import java.util.*;

public class HospitalRunner {
    public static void main(String[] args) {
        String mainSelectionStr;
        int mainSelection = -1;
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        
        while (!exit) {
            // main menu
            System.out.println("Main Menu: ");
            System.out.println("1. Manage Appointments");
            System.out.println("2. Manage Patients");
            System.out.println("3. Manage Staff");
            System.out.println("4: Exit");
            System.out.println("Enter your selection (1, 2, 3): ");
            
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

            if (mainSelection == 1) {
                System.out.println("Appointment Management: ");
                System.out.println();
            }
        }
    }
}
