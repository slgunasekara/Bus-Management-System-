module lk.ijse.busmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires javafx.base;
    requires java.desktop;
    requires javafx.graphics;
    requires java.mail;

    opens lk.ijse.busmanagementsystem to javafx.fxml;
    exports lk.ijse.busmanagementsystem;

    opens lk.ijse.busmanagementsystem.controller to javafx.fxml;
    exports lk.ijse.busmanagementsystem.controller;


    exports lk.ijse.busmanagementsystem.dto;
    opens lk.ijse.busmanagementsystem.dto;

    exports lk.ijse.busmanagementsystem.db;
    exports lk.ijse.busmanagementsystem.util;
    opens lk.ijse.busmanagementsystem.util to javafx.fxml;
    exports lk.ijse.busmanagementsystem.dao.custom.impl;
    exports lk.ijse.busmanagementsystem.tm;
    opens lk.ijse.busmanagementsystem.tm;
}

