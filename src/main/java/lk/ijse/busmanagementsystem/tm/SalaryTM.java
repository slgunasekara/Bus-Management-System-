package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class SalaryTM {
    private int empId;
    private String employeeName;
    private LocalDate salaryDate;
    private String amount;

    public SalaryTM() {
    }

    public SalaryTM(int empId, String employeeName, LocalDate salaryDate, String amount) {
        this.empId = empId;
        this.employeeName = employeeName;
        this.salaryDate = salaryDate;
        this.amount = amount;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SalaryTM{" +
                "empId=" + empId +
                ", employeeName='" + employeeName + '\'' +
                ", salaryDate=" + salaryDate +
                ", amount='" + amount + '\'' +
                '}';
    }
}