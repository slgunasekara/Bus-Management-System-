package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;

public class PartPurchase {

    private int purchaseId;
    private Integer busId;
    private Integer maintId;
    private String partName;
    private int quantity;
    private double unitPrice;
    private double totalCost;
    private String supplierName;
    private String partDescription;
    private LocalDate date;
    private int createdBy;


    public PartPurchase() {
    }


    public PartPurchase(int purchaseId, Integer busId, Integer maintId, String partName,
                           int quantity, double unitPrice, double totalCost, String supplierName,
                           String partDescription, LocalDate date, int createdBy) {
        this.purchaseId = purchaseId;
        this.busId = busId;
        this.maintId = maintId;
        this.partName = partName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.supplierName = supplierName;
        this.partDescription = partDescription;
        this.date = date;
        this.createdBy = createdBy;
    }


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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
