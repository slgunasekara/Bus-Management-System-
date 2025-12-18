package lk.ijse.busmanagementsystem.enums;

public enum EmployeeStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_LEAVE("On Leave"), //niwaadu
    TEMPORARY("Temporary"),
    RETIRED("Retired"),
    SUSPENDED("Suspended");

    private final String displayName;

    EmployeeStatus(String displayName) {
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


