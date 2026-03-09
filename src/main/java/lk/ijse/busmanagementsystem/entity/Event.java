package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private int eventId;
    private int busId;
    private String busNumber;
    private String startLocation;
    private String endLocation;
    private double eventValue;
    private LocalDate eventDate;
    private String customerName;
    private String customerContact;
    private String customerNic;
    private String customerAddress;
    private String description;
    private boolean eventCompleted;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Event() {
    }


    public Event(int eventId, int busId, String busNumber, String startLocation,
                    String endLocation, double eventValue, LocalDate eventDate,
                    String customerName, String customerContact, String customerNic,
                    String customerAddress, String description, boolean eventCompleted,
                    int createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.eventId = eventId;
        this.busId = busId;
        this.busNumber = busNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.eventValue = eventValue;
        this.eventDate = eventDate;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.customerNic = customerNic;
        this.customerAddress = customerAddress;
        this.description = description;
        this.eventCompleted = eventCompleted;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Event(int eventId, int busId, String startLocation, String endLocation,
                    double eventValue, LocalDate eventDate, String customerName,
                    String customerContact, String customerNic, String customerAddress,
                    String description, boolean eventCompleted, int createdBy) {
        this.eventId = eventId;
        this.busId = busId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.eventValue = eventValue;
        this.eventDate = eventDate;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.customerNic = customerNic;
        this.customerAddress = customerAddress;
        this.description = description;
        this.eventCompleted = eventCompleted;
        this.createdBy = createdBy;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public double getEventValue() {
        return eventValue;
    }

    public void setEventValue(double eventValue) {
        this.eventValue = eventValue;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEventCompleted() {
        return eventCompleted;
    }

    public void setEventCompleted(boolean eventCompleted) {
        this.eventCompleted = eventCompleted;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
