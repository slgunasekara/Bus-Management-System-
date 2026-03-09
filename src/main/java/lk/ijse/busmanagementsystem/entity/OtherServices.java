package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;

public class OtherServices {

    private int serviceId;
    private Integer busId;
    private Integer tripId;
    private String serviceName;
    private double cost;
    private LocalDate date;
    private String description;
    private int createdBy;

    public OtherServices() {
    }

    public OtherServices(int serviceId, Integer busId, Integer tripId,
                            String serviceName, double cost, LocalDate date,
                            String description, int createdBy) {
        this.serviceId = serviceId;
        this.busId = busId;
        this.tripId = tripId;
        this.serviceName = serviceName;
        this.cost = cost;
        this.date = date;
        this.description = description;
        this.createdBy = createdBy;
    }


    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
