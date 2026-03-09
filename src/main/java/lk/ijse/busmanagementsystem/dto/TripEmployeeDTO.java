package lk.ijse.busmanagementsystem.dto;


public class TripEmployeeDTO {

    private int tripEmpId;
    private int tripId;
    private int empId;
    private String roleInTrip;
    private int createdBy;


    public TripEmployeeDTO() {
    }


    public TripEmployeeDTO(int tripId, int empId, String roleInTrip, int createdBy) {
        this.tripId = tripId;
        this.empId = empId;
        this.roleInTrip = roleInTrip;
        this.createdBy = createdBy;
    }


    public TripEmployeeDTO(int tripEmpId, int tripId, int empId, String roleInTrip, int createdBy) {
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

    @Override
    public String toString() {
        return "TripEmployeeDTO{" +
                "tripEmpId=" + tripEmpId +
                ", tripId=" + tripId +
                ", empId=" + empId +
                ", roleInTrip='" + roleInTrip + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}