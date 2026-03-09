package lk.ijse.busmanagementsystem.tm;

import java.time.LocalDate;

public class IncomeTM {
    private int tripId;
    private String busNumber;
    private LocalDate tripDate;
    private String totalIncome;

    public IncomeTM() {
    }

    public IncomeTM(int tripId, String busNumber, LocalDate tripDate, String totalIncome) {
        this.tripId = tripId;
        this.busNumber = busNumber;
        this.tripDate = tripDate;
        this.totalIncome = totalIncome;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Override
    public String toString() {
        return "IncomeTM{" +
                "tripId=" + tripId +
                ", busNumber='" + busNumber + '\'' +
                ", tripDate=" + tripDate +
                ", totalIncome='" + totalIncome + '\'' +
                '}';
    }
}