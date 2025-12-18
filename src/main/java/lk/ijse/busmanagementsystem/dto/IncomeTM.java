package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class IncomeTM {
    private int tripId;
    private String busNumber;  // Bus number display කරන්න
    private LocalDate tripDate;
    private double totalIncome;

    public IncomeTM() {
    }

    public IncomeTM(int tripId, String busNumber, LocalDate tripDate, double totalIncome) {
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

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Override
    public String toString() {
        return "IncomeTM{" +
                "tripId=" + tripId +
                ", busNumber='" + busNumber + '\'' +
                ", tripDate=" + tripDate +
                ", totalIncome=" + totalIncome +
                '}';
    }
}