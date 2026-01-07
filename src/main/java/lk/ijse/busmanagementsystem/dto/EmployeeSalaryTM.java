package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class EmployeeSalaryTM {
    private int salaryId;
    private int empId;
    private String empName;
    private Integer tripId;
    private double amount;
    private String description;
    private LocalDate date;
    private String createdByUsername;


    public EmployeeSalaryTM() {
    }


    public EmployeeSalaryTM(int salaryId, int empId, String empName,
                            Integer tripId, double amount, String description,
                            LocalDate date, String createdByUsername) {
        this.salaryId = salaryId;
        this.empId = empId;
        this.empName = empName;
        this.tripId = tripId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.createdByUsername = createdByUsername;
    }


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

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    @Override
    public String toString() {
        return "EmployeeSalaryTM{" +
                "salaryId=" + salaryId +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", tripId=" + tripId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}