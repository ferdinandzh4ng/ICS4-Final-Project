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
        if (dateStr == null || dateStr.isEmpty()) {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            return;
        }

        // YYYY-MM-DD (API file I/O format)
        if (dateStr.contains("-") && dateStr.length() >= 10) {
            this.year = Integer.parseInt(dateStr.substring(0, 4));
            this.month = Integer.parseInt(dateStr.substring(5, 7));
            this.day = Integer.parseInt(dateStr.substring(8, 10));
            return;
        }

        // YYYYMMDD (appointments.txt format)
        if (dateStr.length() == 8 && dateStr.matches("\\d{8}")) {
            this.year = Integer.parseInt(dateStr.substring(0, 4));
            this.month = Integer.parseInt(dateStr.substring(4, 6));
            this.day = Integer.parseInt(dateStr.substring(6, 8));
            return;
        }

        // "Month day, year" display format
        String[] parts = dateStr.split(" ");
        switch (parts[0]) {
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
            default:
                this.month = 0;
                break;
        }
        this.year = Integer.parseInt(parts[2]);
        this.day = Integer.parseInt(parts[1].replace(",", ""));
    }

    /**
     * Returns the day of the date
     * @return the day of the date
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the month of the date
     * @return the month of the date
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year of the date
     * @return the year of the date
     */
    public int getYear() {
        return year;
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

    /**
     * Compares this date with another date
     * @param other the other date
     * @return a negative integer, zero, or a positive integer as this date is before, at the same time, or after the specified date
     */
    public int compareTo(Date other) {
        if(this.year != other.year) {
            return this.year - other.year;
        } else if(this.month != other.month) {
            return this.month - other.month;
        } else {
            return this.day - other.day;
        }
    }
    
    /**
     * Calculates the difference in years between this date and another date
     * @param other the other date
     * @return the difference in years
     */
    public int yearDiff (Date other) {
        int thisDays = this.day + this.month * 30 + this.year * 365;
        int otherDays = other.day + other.month * 30 + other.year * 365;
        return Math.abs((thisDays - otherDays) / 365);
    }

    @Override
    public boolean equals (Object other) {
        return other instanceof Date && this.year == ((Date)other).getYear() && this.month == ((Date)other).getMonth() && this.day == ((Date)other).getDay();
    }

    /**
     * Checks if the date is valid
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        if (year < 0 || month < 1 || month > 12 || day < 1) {
            return false;
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day <= daysInMonth[month - 1];
    }

    public Date addDays (int daysToAdd) {
        int y = this.year;
        int m = this.month;
        int d = this.day;

        if (daysToAdd >= 0) {
            while (daysToAdd > 0) {
                int daysInMonth = getDaysInMonth(y, m);
                d++;
                if (d > daysInMonth) {
                    d = 1;
                    m++;
                    if (m > 12) {
                        m = 1;
                        y++;
                    }
                }
                daysToAdd--;
            }
        } else {
            while (daysToAdd < 0) {
                d--;
                if (d < 1) {
                    m--;
                    if (m < 1) {
                        m = 12;
                        y--;
                    }
                    d = getDaysInMonth(y, m);
                }
                daysToAdd++;
            }
        }

        return new Date(y, m, d);
    }

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return 28;
            default:
                return 31;
        }
    }

    /**
     * Returns the date in YYYY-MM-DD format for file I/O and OR booking records.
     *
     * @return ISO-style date string
     */
    public String toISODateString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

}
