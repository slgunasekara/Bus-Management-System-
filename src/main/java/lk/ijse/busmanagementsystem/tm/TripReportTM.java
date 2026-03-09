package lk.ijse.busmanagementsystem.tm;

import java.time.LocalDate;

public class TripReportTM {
    private int tripId;
    private LocalDate tripDate;
    private String busNumber;
    private String route;
    private String category;

    public TripReportTM() {
    }

    public TripReportTM(int tripId, LocalDate tripDate, String busNumber, String route, String category) {
        this.tripId = tripId;
        this.tripDate = tripDate;
        this.busNumber = busNumber;
        this.route = route;
        this.category = category;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TripReportTM{" +
                "tripId=" + tripId +
                ", tripDate=" + tripDate +
                ", busNumber='" + busNumber + '\'' +
                ", route='" + route + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}