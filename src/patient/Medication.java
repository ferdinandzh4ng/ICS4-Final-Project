package patient;

/** 
 * File: Medication.java
 * Name: Caroline Chan
 * Date: June 1, 2026
 * Class: ICS4U1
 * Description: Represents a Medication prescribed to a patient, with its name and dosage.
*/
public class Medication {
    private String medName; // name of the medication
    private String dosage; // dosage of the medication

    /**
     * Constructor for the Medication class
     * @param medName the name of the medication
     * @param dosage the dosage of the medication
     */
    public Medication (String medName, String dosage) {
        this.medName = medName;
        this.dosage = dosage;
    }

    /**
     * Accessor method for medName
     * @return String the medication name
     */
    public String getMedName() {
        return medName;
    }

    /**
     * Accessor method for dosage
     * @return String the medication dosage
     */
    public String getDosage() {
        return dosage;
    }
}
