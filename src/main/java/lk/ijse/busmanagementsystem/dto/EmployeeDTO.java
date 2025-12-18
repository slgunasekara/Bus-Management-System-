package lk.ijse.busmanagementsystem.dto;

import lk.ijse.busmanagementsystem.enums.EmployeeCategory;
import lk.ijse.busmanagementsystem.enums.EmployeeStatus;
import java.time.LocalDate;

public class EmployeeDTO {
    private int empId;
    private EmployeeCategory empCategory;
    private String empName;
    private String address;
    private String contactNo;
    private String nicNo;
    private String ntcNo;
    private String drivingLicenceNo;
    private LocalDate joinDate;
    private LocalDate exitDate;  // Database එකේ exit_date
    private EmployeeStatus empStatus;
    private int createdBy;
    private String createdByUsername;

    // Default Constructor
    public EmployeeDTO() {
    }

    // Constructor without createdByUsername
    public EmployeeDTO(int empId, EmployeeCategory empCategory, String empName,
                       String address, String contactNo, String nicNo,
                       String ntcNo, String drivingLicenceNo, LocalDate joinDate,
                       LocalDate exitDate, EmployeeStatus empStatus, int createdBy) {
        this.empId = empId;
        this.empCategory = empCategory;
        this.empName = empName;
        this.address = address;
        this.contactNo = contactNo;
        this.nicNo = nicNo;
        this.ntcNo = ntcNo;
        this.drivingLicenceNo = drivingLicenceNo;
        this.joinDate = joinDate;
        this.exitDate = exitDate;
        this.empStatus = empStatus;
        this.createdBy = createdBy;
    }

    // Constructor with createdByUsername
    public EmployeeDTO(int empId, EmployeeCategory empCategory, String empName,
                       String address, String contactNo, String nicNo,
                       String ntcNo, String drivingLicenceNo, LocalDate joinDate,
                       LocalDate exitDate, EmployeeStatus empStatus,
                       int createdBy, String createdByUsername) {
        this.empId = empId;
        this.empCategory = empCategory;
        this.empName = empName;
        this.address = address;
        this.contactNo = contactNo;
        this.nicNo = nicNo;
        this.ntcNo = ntcNo;
        this.drivingLicenceNo = drivingLicenceNo;
        this.joinDate = joinDate;
        this.exitDate = exitDate;
        this.empStatus = empStatus;
        this.createdBy = createdBy;
        this.createdByUsername = createdByUsername;
    }

    // Getters and Setters
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public EmployeeCategory getEmpCategory() {
        return empCategory;
    }

    public void setEmpCategory(EmployeeCategory empCategory) {
        this.empCategory = empCategory;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getNtcNo() {
        return ntcNo;
    }

    public void setNtcNo(String ntcNo) {
        this.ntcNo = ntcNo;
    }

    public String getDrivingLicenceNo() {
        return drivingLicenceNo;
    }

    public void setDrivingLicenceNo(String drivingLicenceNo) {
        this.drivingLicenceNo = drivingLicenceNo;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public EmployeeStatus getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(EmployeeStatus empStatus) {
        this.empStatus = empStatus;
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
        return "EmployeeDTO{" +
                "empId=" + empId +
                ", empCategory=" + empCategory +
                ", empName='" + empName + '\'' +
                ", address='" + address + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", nicNo='" + nicNo + '\'' +
                ", ntcNo='" + ntcNo + '\'' +
                ", drivingLicenceNo='" + drivingLicenceNo + '\'' +
                ", joinDate=" + joinDate +
                ", exitDate=" + exitDate +
                ", empStatus=" + empStatus +
                ", createdBy=" + createdBy +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}