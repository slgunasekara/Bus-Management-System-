package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class PartPurchaseTM {
    private int purchaseId;
    private Integer busId;
    private String busNumber;        // Bus number for display in table
    private Integer maintId;
    private String partName;
    private int quantity;
    private double unitPrice;
    private double totalCost;
    private String supplierName;
    private String partDescription;
    private LocalDate date;
    private String createdByUsername; // Username for display

    // Default Constructor
    public PartPurchaseTM() {
    }

    // Parameterized Constructor
    public PartPurchaseTM(int purchaseId, Integer busId, String busNumber, Integer maintId,
                          String partName, int quantity, double unitPrice, double totalCost,
                          String supplierName, String partDescription, LocalDate date,
                          String createdByUsername) {
        this.purchaseId = purchaseId;
        this.busId = busId;
        this.busNumber = busNumber;
        this.maintId = maintId;
        this.partName = partName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.supplierName = supplierName;
        this.partDescription = partDescription;
        this.date = date;
        this.createdByUsername = createdByUsername;
    }

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public Integer getMaintId() {
        return maintId;
    }

    public void setMaintId(Integer maintId) {
        this.maintId = maintId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
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
        return "PartPurchaseTM{" +
                "purchaseId=" + purchaseId +
                ", busId=" + busId +
                ", busNumber='" + busNumber + '\'' +
                ", maintId=" + maintId +
                ", partName='" + partName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalCost=" + totalCost +
                ", supplierName='" + supplierName + '\'' +
                ", partDescription='" + partDescription + '\'' +
                ", date=" + date +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}