package lk.ijse.busmanagementsystem.dto;

import lk.ijse.busmanagementsystem.enums.TripCategory;
import java.time.LocalDate;

public class TripDTO {
    private int tripId;
    private int busId;
    private TripCategory tripCategory;  // enum type එක add කරන්න
    private String startLocation;
    private String endLocation;
    private double distance;
    private double totalIncome;
    private LocalDate tripDate;
    private String description;
    private int createdBy;
    private String createdByUsername;

    public TripDTO() {
    }

    public TripDTO(int tripId, int busId, TripCategory tripCategory, String startLocation,
                   String endLocation, double distance, double totalIncome,
                   LocalDate tripDate, String description, int createdBy) {
        this.tripId = tripId;
        this.busId = busId;
        this.tripCategory = tripCategory;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.totalIncome = totalIncome;
        this.tripDate = tripDate;
        this.description = description;
        this.createdBy = createdBy;
    }

    public TripDTO(int tripId, int busId, TripCategory tripCategory, String startLocation,
                   String endLocation, double distance, double totalIncome,
                   LocalDate tripDate, String description, int createdBy,
                   String createdByUsername) {
        this.tripId = tripId;
        this.busId = busId;
        this.tripCategory = tripCategory;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.totalIncome = totalIncome;
        this.tripDate = tripDate;
        this.description = description;
        this.createdBy = createdBy;
        this.createdByUsername = createdByUsername;
    }

    // Getters and Setters
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public TripCategory getTripCategory() {
        return tripCategory;
    }

    public void setTripCategory(TripCategory tripCategory) {
        this.tripCategory = tripCategory;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    @Override
    public String toString() {
        return "TripDTO{" +
                "tripId=" + tripId +
                ", busId=" + busId +
                ", tripCategory=" + tripCategory +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", distance=" + distance +
                ", totalIncome=" + totalIncome +
                ", tripDate=" + tripDate +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}