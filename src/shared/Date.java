package shared;

public class Date {
    private int year;
    private int month;
    private int day;

    //constructor
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date(String dateStr) {
        String[] parts = dateStr.split(" ");
        switch(parts[0]) {
            case "January":
                this.month = 1;
                break;
            case "February":
                this.month = 2;
                break;
            case "March":
                this.month = 3;
                break;
            case "April":
                this.month = 4;
                break;
            case "May":
                this.month = 5;
                break;
            case "June":
                this.month = 6;
                break;
            case "July":
                this.month = 7;
                break;
            case "August":
                this.month = 8;
                break;
            case "September":
                this.month = 9;
                break;
            case "October":
                this.month = 10;
                break;
            case "November":
                this.month = 11;
                break;
            case "December":
                this.month = 12;
                break;
        }
        this.year = Integer.parseInt(parts[2]);
        this.day = Integer.parseInt(parts[1]);
    }

    //@override
    public String toString() {
        String monthStr = "";
        switch(this.month) {
            case 1:
                monthStr = "January";
                break;
            case 2:
                monthStr = "February";
                break;
            case 3:
                monthStr = "March";
                break;
            case 4:
                monthStr = "April";
                break;
            case 5:
                monthStr = "May";
                break;
            case 6:
                monthStr = "June";
                break;
            case 7:
                monthStr = "July";
                break;
            case 8:
                monthStr = "August";
                break;
            case 9:
                monthStr = "September";
                break;
            case 10:
                monthStr = "October";
                break;
            case 11:
                monthStr = "November";
                break;
            case 12:
                monthStr = "December";
                break;
        }
        return monthStr + " " + this.day + ", " + this.year;

    }

    public int compareTo(Date other) {
        if(this.year != other.year) {
            return this.year - other.year;
        } else if(this.month != other.month) {
            return this.month - other.month;
        } else {
            return this.day - other.day;
        }
    }


}
