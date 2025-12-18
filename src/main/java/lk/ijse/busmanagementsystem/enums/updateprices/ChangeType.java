package lk.ijse.busmanagementsystem.enums.updateprices;

public enum ChangeType {
    INCREMENT("INCREMENT"),
    DECREMENT("DECREMENT");

    private final String value;

    ChangeType(String value) {
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