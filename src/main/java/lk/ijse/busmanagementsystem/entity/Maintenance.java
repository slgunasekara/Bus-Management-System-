package lk.ijse.busmanagementsystem.entity;

import lk.ijse.busmanagementsystem.enums.MaintenanceType;

import java.time.LocalDate;

public class Maintenance {

    private int maintId;
    private int busId;
    private MaintenanceType maintenanceType;
    private LocalDate serviceDate;
    private double mileage;
    private double cost;
    private String technician;
    private String description;
    private int createdBy;

    public Maintenance() {
    }

    public Maintenance(int maintenanceId, int busId, MaintenanceType maintenanceType,
                          LocalDate serviceDate, double mileage, double cost,
                          String technician, String description, int createdBy) {
        this.maintId = maintenanceId;
        this.busId = busId;
        this.maintenanceType = maintenanceType;
        this.serviceDate = serviceDate;
        this.mileage = mileage;
        this.cost = cost;
        this.technician = technician;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getMaintId() {
        return maintId;
    }

    public void setMaintId(int maintId) {
        this.maintId = maintId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
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
}
