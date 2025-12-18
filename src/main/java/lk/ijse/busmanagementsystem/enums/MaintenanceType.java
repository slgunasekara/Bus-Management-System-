package lk.ijse.busmanagementsystem.enums;

public enum MaintenanceType {
    MECHANICAL_REPAIR("Mechanical Repair"),
    BODY_REPAIR("Body Repair"),
    TIRE_SERVICE("Tire Service"),
    BRAKE_SERVICE("Brake Service"),
    ELECTRICAL_REPAIR("Electrical Repair"),
    OIL_CHANGE("Oil Change"),  // Add this
    FULL_SERVICE("Full Service");

    private final String displayName;

    MaintenanceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

