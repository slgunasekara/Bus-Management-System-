package lk.ijse.busmanagementsystem.enums;

public enum EmployeeCategory {
    DRIVER("Driver"),
    CONDUCTOR("Conductor"),
    MANAGER("Manager"),
    HELPER("Helper"),
    CLEANER("Cleaner");

    private final String displayName;

    EmployeeCategory(String displayName) {
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






