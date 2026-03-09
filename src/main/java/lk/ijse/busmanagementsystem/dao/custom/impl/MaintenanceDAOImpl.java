package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.MaintenanceDTO;
import lk.ijse.busmanagementsystem.enums.MaintenanceType;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAOImpl {

    public List<MaintenanceDTO> getAllMaintenance() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Maintenance");
        List<MaintenanceDTO> maintenanceList = new ArrayList<>();

        while (rst.next()) {
            try {
                String typeString = rst.getString("maintenance_type");
                MaintenanceType type;

                // Safe parsing with error handling
                try {
                    type = MaintenanceType.valueOf(typeString);
                } catch (IllegalArgumentException e) {
                    // Log and default to FULL_SERVICE for unknown types
                    System.err.println("⚠️ Unknown maintenance type: '" + typeString +
                            "' for Maintenance ID: " + rst.getInt("maint_id") +
                            ". Defaulting to FULL_SERVICE.");
                    type = MaintenanceType.FULL_SERVICE;
                }

                MaintenanceDTO maintenanceDTO = new MaintenanceDTO(
                        rst.getInt("maint_id"),
                        rst.getInt("bus_id"),
                        type,
                        rst.getDate("date").toLocalDate(),
                        rst.getDouble("Mileage"),
                        rst.getDouble("cost"),
                        rst.getString("Maintained_by"),
                        rst.getString("description"),
                        rst.getInt("created_by")
                );
                maintenanceList.add(maintenanceDTO);
            } catch (Exception e) {
                // Log the error but continue loading other records
                System.err.println("❌ Error loading maintenance record ID: " +
                        rst.getInt("maint_id") + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return maintenanceList;
    }

    public boolean saveMaintenance(MaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Maintenance(bus_id, maintenance_type, date, " +
                "Mileage, cost, Maintained_by, description, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                maintenanceDTO.getBusId(),
                maintenanceDTO.getMaintenanceType().name(),
                java.sql.Date.valueOf(maintenanceDTO.getServiceDate()),
                maintenanceDTO.getMileage(),
                maintenanceDTO.getCost(),
                maintenanceDTO.getTechnician(),
                maintenanceDTO.getDescription(),
                maintenanceDTO.getCreatedBy()
        );
    }

    public boolean updateMaintenance(MaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Maintenance SET bus_id=?, maintenance_type=?, date=?, " +
                "Mileage=?, cost=?, Maintained_by=?, description=? WHERE maint_id=?";
        return CrudUtil.execute(sql,
                maintenanceDTO.getBusId(),
                maintenanceDTO.getMaintenanceType().name(),
                java.sql.Date.valueOf(maintenanceDTO.getServiceDate()),
                maintenanceDTO.getMileage(),
                maintenanceDTO.getCost(),
                maintenanceDTO.getTechnician(),
                maintenanceDTO.getDescription(),
                maintenanceDTO.getMaintId()
        );
    }

    public boolean deleteMaintenance(String maintenanceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Maintenance WHERE maint_id=?", maintenanceId);
    }

    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    //Get all available bus IDs from the Bus table
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus ORDER BY bus_id DESC");
        List<Integer> busIds = new ArrayList<>();

        while (rst.next()) {
            busIds.add(rst.getInt("bus_id"));
        }
        return busIds;
    }

    //Get bus details (Bus Number) by bus ID
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT bus_number FROM Bus WHERE bus_id=?";
        ResultSet rst = CrudUtil.execute(sql, busId);

        if (rst.next()) {
            return "Bus Number: " + rst.getString("bus_number");
        }
        return "Bus not found";
    }

    public List<MaintenanceDTO> searchMaintenance(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Maintenance WHERE " +
                "maint_id LIKE ? OR " +
                "bus_id LIKE ? OR " +
                "maintenance_type LIKE ? OR " +
                "Maintained_by LIKE ? OR " +
                "description LIKE ?";

        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern);

        List<MaintenanceDTO> maintenanceList = new ArrayList<>();

        while (rst.next()) {
            try {
                String typeString = rst.getString("maintenance_type");
                MaintenanceType type;

                // Safe parsing with error handling
                try {
                    type = MaintenanceType.valueOf(typeString);
                } catch (IllegalArgumentException e) {
                    // Log and default to FULL_SERVICE for unknown types
                    System.err.println("⚠️ Unknown maintenance type: '" + typeString +
                            "' for Maintenance ID: " + rst.getInt("maint_id") +
                            ". Defaulting to FULL_SERVICE.");
                    type = MaintenanceType.FULL_SERVICE;
                }

                MaintenanceDTO maintenanceDTO = new MaintenanceDTO(
                        rst.getInt("maint_id"),
                        rst.getInt("bus_id"),
                        type,
                        rst.getDate("date").toLocalDate(),
                        rst.getDouble("Mileage"),
                        rst.getDouble("cost"),
                        rst.getString("Maintained_by"),
                        rst.getString("description"),
                        rst.getInt("created_by")
                );
                maintenanceList.add(maintenanceDTO);
            } catch (Exception e) {
                // Log the error but continue loading other records
                System.err.println("❌ Error loading maintenance record during search: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return maintenanceList;
    }
}