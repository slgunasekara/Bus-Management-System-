package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.MaintenanceDTO;

import java.sql.SQLException;
import java.util.List;

public interface MaintenanceBO extends SuperBO {
    List<MaintenanceDTO> getAllMaintenance() throws SQLException, ClassNotFoundException;
    boolean saveMaintenance(MaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException;
    boolean updateMaintenance(MaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException;
    boolean deleteMaintenance(String maintenanceId) throws SQLException, ClassNotFoundException;
    boolean isBusExists(int busId) throws SQLException, ClassNotFoundException;
    List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException;
    String getBusDetails(int busId) throws SQLException, ClassNotFoundException;
    List<MaintenanceDTO> searchMaintenance(String keyword) throws SQLException, ClassNotFoundException;
}