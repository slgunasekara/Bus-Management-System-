module lk.ijse.busmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires javafx.base;
    requires java.desktop;
    requires javafx.graphics;

    // Export packages to JavaFX
    opens lk.ijse.busmanagementsystem to javafx.fxml;
    exports lk.ijse.busmanagementsystem;

    // Export and open controller package to JavaFX
    opens lk.ijse.busmanagementsystem.controller to javafx.fxml;
    exports lk.ijse.busmanagementsystem.controller;

    // CRITICAL: Export and open DTO package for JasperReports
    // JasperReports needs reflective access to read bean properties
    exports lk.ijse.busmanagementsystem.dto;
    opens lk.ijse.busmanagementsystem.dto;

    // Export other packages if needed
    exports lk.ijse.busmanagementsystem.model;
    exports lk.ijse.busmanagementsystem.db;
    exports lk.ijse.busmanagementsystem.util;
    opens lk.ijse.busmanagementsystem.util to javafx.fxml;
}

