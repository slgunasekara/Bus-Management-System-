package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;

public class UpdatePrices {

    private int updatePricesId;
    private String updateType;  // FUEL or TICKET
    private String changeType;  // INCREMENT or DECREMENT
    private double previousValue;
    private double newValue;
    private double changeAmount;
    private double percentageChange;
    private LocalDate changeDate;
    private String description;
    private int createdBy;


    public UpdatePrices() {
    }


    public UpdatePrices(int updatePricesId, String updateType, String changeType,
                           double previousValue, double newValue, double changeAmount,
                           double percentageChange, LocalDate changeDate,
                           String description, int createdBy) {
        this.updatePricesId = updatePricesId;
        this.updateType = updateType;
        this.changeType = changeType;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.changeAmount = changeAmount;
        this.percentageChange = percentageChange;
        this.changeDate = changeDate;
        this.description = description;
        this.createdBy = createdBy;
    }


    public int getUpdatePricesId() {
        return updatePricesId;
    }

    public void setUpdatePricesId(int updatePricesId) {
        this.updatePricesId = updatePricesId;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    public double getNewValue() {
        return newValue;
    }

    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public LocalDate getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDate changeDate) {
        this.changeDate = changeDate;
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
