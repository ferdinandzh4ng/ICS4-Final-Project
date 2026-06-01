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
                this.month = 01;
                break;
            case "February":
                this.month = 02;
                break;
            case "March":
                this.month = 03;
                break;
            case "April":
                this.month = 04;
                break;
            case "May":
                this.month = 05;
                break;
            case "June":
                this.month = 06;
                break;
            case "July":
                this.month = 07;
                break;
            case "August":
                this.month = 08;
                break;
            case "September":
                this.month = 09;
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

@override
    public String toString() {
        String monthStr = "";
        switch(this.month) {
            case 01:
                monthStr = "January";
                break;
            case 02:
                monthStr = "February";
                break;
            case 03:
                monthStr = "March";
                break;
            case 04:
                monthStr = "April";
                break;
            case 05:
                monthStr = "May";
                break;
            case 06:
                monthStr = "June";
                break;
            case 07:
                monthStr = "July";
                break;
            case 08:
                monthStr = "August";
                break;
            case 09:
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
