package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class TripReportTM {
    private int tripId;
    private LocalDate tripDate;
    private int busId;
    private String route;
    private String status;

    public TripReportTM() {
    }

    public TripReportTM(int tripId, LocalDate tripDate, int busId, String route, String status) {
        this.tripId = tripId;
        this.tripDate = tripDate;
        this.busId = busId;
        this.route = route;
        this.status = status;
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

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TripReportTM{" +
                "tripId=" + tripId +
                ", tripDate=" + tripDate +
                ", busId=" + busId +
                ", route='" + route + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}