package lk.ijse.busmanagementsystem.enums;


public enum TripCategory {
    ROUTE("Route Service"),
    SCHOOL_SERVICE("School Service"),
    OFFICE_SERVICE("Office Service"),
    PRIVATE_TRIP("Private Trip");

    private final String displayName;

    TripCategory(String displayName) {
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