package lk.ijse.busmanagementsystem.entity;

public class TripEmployee {


    private int tripEmpId;
    private int tripId;
    private int empId;
    private String roleInTrip;
    private int createdBy;


    public TripEmployee() {
    }


    public TripEmployee(int tripId, int empId, String roleInTrip, int createdBy) {
        this.tripId = tripId;
        this.empId = empId;
        this.roleInTrip = roleInTrip;
        this.createdBy = createdBy;
    }


    public TripEmployee(int tripEmpId, int tripId, int empId, String roleInTrip, int createdBy) {
        this.tripEmpId = tripEmpId;
        this.tripId = tripId;
        this.empId = empId;
        this.roleInTrip = roleInTrip;
        this.createdBy = createdBy;
    }


    public int getTripEmpId() {
        return tripEmpId;
    }

    public void setTripEmpId(int tripEmpId) {
        this.tripEmpId = tripEmpId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getRoleInTrip() {
        return roleInTrip;
    }

    public void setRoleInTrip(String roleInTrip) {
        this.roleInTrip = roleInTrip;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
