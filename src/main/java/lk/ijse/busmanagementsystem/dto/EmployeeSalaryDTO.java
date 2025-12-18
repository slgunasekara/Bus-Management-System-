package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class EmployeeSalaryDTO {
    private int salaryId;
    private int empId;
    private Integer tripId;  // Nullable - can be null if not trip-related
    private double amount;
    private String description;
    private LocalDate date;
    private int createdBy;
    private String createdByUsername;

    // Default Constructor
    public EmployeeSalaryDTO() {
    }

    // Constructor without createdByUsername
    public EmployeeSalaryDTO(int salaryId, int empId, Integer tripId,
                             double amount, String description,
                             LocalDate date, int createdBy) {
        this.salaryId = salaryId;
        this.empId = empId;
        this.tripId = tripId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.createdBy = createdBy;
    }

    // Constructor with createdByUsername
    public EmployeeSalaryDTO(int salaryId, int empId, Integer tripId,
                             double amount, String description,
                             LocalDate date, int createdBy,
                             String createdByUsername) {
        this.salaryId = salaryId;
        this.empId = empId;
        this.tripId = tripId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.createdBy = createdBy;
        this.createdByUsername = createdByUsername;
    }

    // Getters and Setters
    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return "EmployeeSalaryDTO{" +
                "salaryId=" + salaryId +
                ", empId=" + empId +
                ", tripId=" + tripId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", createdBy=" + createdBy +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}