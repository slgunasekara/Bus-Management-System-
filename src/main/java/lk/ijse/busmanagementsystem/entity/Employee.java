package lk.ijse.busmanagementsystem.entity;

import lk.ijse.busmanagementsystem.enums.EmployeeCategory;
import lk.ijse.busmanagementsystem.enums.EmployeeStatus;

import java.time.LocalDate;

public class Employee {

    private int empId;
    private EmployeeCategory empCategory;
    private String empName;
    private String address;
    private String contactNo;
    private String nicNo;
    private String ntcNo;
    private String drivingLicenceNo;
    private LocalDate joinDate;
    private LocalDate exitDate;
    private EmployeeStatus empStatus;
    private int createdBy;
    private String createdByUsername;


    public Employee() {
    }


    public Employee(int empId, EmployeeCategory empCategory, String empName,
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


    public Employee(int empId, EmployeeCategory empCategory, String empName,
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
}
