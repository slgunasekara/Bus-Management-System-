package lk.ijse.busmanagementsystem.dto;

/**
 * TM (Table Model) Class - Trip එකට assign කරපු employees display කරන්න
 * UI table එකේ පෙන්වන සියලු fields තියෙනවා
 */
public class TripEmployeeTM {

    private int tripEmpId;
    private int empId;
    private String empName;
    private String empCategory;
    private String roleInTrip;
    private String contactNo;
    private String assignedDate;

    public TripEmployeeTM() {
    }

    // Full Constructor (Database එකෙන් load කරද්දී)
    public TripEmployeeTM(int tripEmpId, int empId, String empName, String empCategory,
                          String roleInTrip, String contactNo, String assignedDate) {
        this.tripEmpId = tripEmpId;
        this.empId = empId;
        this.empName = empName;
        this.empCategory = empCategory;
        this.roleInTrip = roleInTrip;
        this.contactNo = contactNo;
        this.assignedDate = assignedDate;
    }

    // Constructor without tripEmpId and date (UI වලින් add කරද්දී)
    public TripEmployeeTM(int empId, String empName, String empCategory,
                          String roleInTrip, String contactNo) {
        this.empId = empId;
        this.empName = empName;
        this.empCategory = empCategory;
        this.roleInTrip = roleInTrip;
        this.contactNo = contactNo;
    }

    // Getters and Setters
    public int getTripEmpId() {
        return tripEmpId;
    }

    public void setTripEmpId(int tripEmpId) {
        this.tripEmpId = tripEmpId;
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

    public String getEmpCategory() {
        return empCategory;
    }

    public void setEmpCategory(String empCategory) {
        this.empCategory = empCategory;
    }

    public String getRoleInTrip() {
        return roleInTrip;
    }

    public void setRoleInTrip(String roleInTrip) {
        this.roleInTrip = roleInTrip;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    @Override
    public String toString() {
        return "TripEmployeeTM{" +
                "tripEmpId=" + tripEmpId +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", empCategory='" + empCategory + '\'' +
                ", roleInTrip='" + roleInTrip + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", assignedDate='" + assignedDate + '\'' +
                '}';
    }
}