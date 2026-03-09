package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bus {

    private int busId;
    private String busBrandName;
    private String busNumber;
    private String busType;
    private int noOfSeats;
    private String busStatus;
    private LocalDate manufactureDate;
    private LocalDate insuranceExpiryDate;
    private LocalDate licenseRenewalDate;
    private int currentMileage;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Bus() {
    }


    public Bus(int busId, String busBrandName, String busNumber, String busType,
                  int noOfSeats, String busStatus, LocalDate manufactureDate,
                  LocalDate insuranceExpiryDate, LocalDate licenseRenewalDate,
                  int currentMileage, int createdBy, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.busId = busId;
        this.busBrandName = busBrandName;
        this.busNumber = busNumber;
        this.busType = busType;
        this.noOfSeats = noOfSeats;
        this.busStatus = busStatus;
        this.manufactureDate = manufactureDate;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.licenseRenewalDate = licenseRenewalDate;
        this.currentMileage = currentMileage;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Bus(int busId, String busBrandName, String busNumber, String busType,
                  int noOfSeats, String busStatus, LocalDate manufactureDate,
                  LocalDate insuranceExpiryDate, LocalDate licenseRenewalDate,
                  int currentMileage, int createdBy) {
        this.busId = busId;
        this.busBrandName = busBrandName;
        this.busNumber = busNumber;
        this.busType = busType;
        this.noOfSeats = noOfSeats;
        this.busStatus = busStatus;
        this.manufactureDate = manufactureDate;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.licenseRenewalDate = licenseRenewalDate;
        this.currentMileage = currentMileage;
        this.createdBy = createdBy;
    }


    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getBusBrandName() {
        return busBrandName;
    }

    public void setBusBrandName(String busBrandName) {
        this.busBrandName = busBrandName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(String busStatus) {
        this.busStatus = busStatus;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public LocalDate getLicenseRenewalDate() {
        return licenseRenewalDate;
    }

    public void setLicenseRenewalDate(LocalDate licenseRenewalDate) {
        this.licenseRenewalDate = licenseRenewalDate;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
