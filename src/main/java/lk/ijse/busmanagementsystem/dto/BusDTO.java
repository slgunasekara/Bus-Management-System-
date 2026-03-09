package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BusDTO {
    private Integer busId;
    private String busBrandName;
    private String busNumber;
    private String busType;
    private Integer noOfSeats;
    private String busStatus;
    private LocalDate manufactureDate;
    private LocalDate insuranceExpiryDate;
    private LocalDate licenseRenewalDate;
    private Integer currentMileage;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BusDTO() {}

    public BusDTO(Integer busId, String busBrandName, String busNumber, String busType,
                  Integer noOfSeats, String busStatus, LocalDate manufactureDate,
                  LocalDate insuranceExpiryDate, LocalDate licenseRenewalDate,
                  Integer currentMileage, Integer createdBy,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    // Constructor for saving new bus (without id, createdAt, updatedAt)
    public BusDTO(String busBrandName, String busNumber, String busType,
                  Integer noOfSeats, String busStatus, LocalDate manufactureDate,
                  LocalDate insuranceExpiryDate, LocalDate licenseRenewalDate,
                  Integer currentMileage, Integer createdBy) {
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

    public Integer getBusId() { return busId; }
    public void setBusId(Integer busId) { this.busId = busId; }

    public String getBusBrandName() { return busBrandName; }
    public void setBusBrandName(String busBrandName) { this.busBrandName = busBrandName; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }

    public Integer getNoOfSeats() { return noOfSeats; }
    public void setNoOfSeats(Integer noOfSeats) { this.noOfSeats = noOfSeats; }

    public String getBusStatus() { return busStatus; }
    public void setBusStatus(String busStatus) { this.busStatus = busStatus; }

    public LocalDate getManufactureDate() { return manufactureDate; }
    public void setManufactureDate(LocalDate manufactureDate) { this.manufactureDate = manufactureDate; }

    public LocalDate getInsuranceExpiryDate() { return insuranceExpiryDate; }
    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) { this.insuranceExpiryDate = insuranceExpiryDate; }

    public LocalDate getLicenseRenewalDate() { return licenseRenewalDate; }
    public void setLicenseRenewalDate(LocalDate licenseRenewalDate) { this.licenseRenewalDate = licenseRenewalDate; }

    public Integer getCurrentMileage() { return currentMileage; }
    public void setCurrentMileage(Integer currentMileage) { this.currentMileage = currentMileage; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "BusDTO{" +
                "busId=" + busId +
                ", busBrandName='" + busBrandName + '\'' +
                ", busNumber='" + busNumber + '\'' +
                ", busType='" + busType + '\'' +
                ", noOfSeats=" + noOfSeats +
                ", busStatus='" + busStatus + '\'' +
                ", manufactureDate=" + manufactureDate +
                ", insuranceExpiryDate=" + insuranceExpiryDate +
                ", licenseRenewalDate=" + licenseRenewalDate +
                ", currentMileage=" + currentMileage +
                '}';
    }
}