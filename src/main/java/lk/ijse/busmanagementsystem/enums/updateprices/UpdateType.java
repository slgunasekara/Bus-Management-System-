package lk.ijse.busmanagementsystem.enums.updateprices;

public enum UpdateType {
    FUEL("FUEL"),
    TICKET("TICKET");

    private final String value;

    UpdateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}