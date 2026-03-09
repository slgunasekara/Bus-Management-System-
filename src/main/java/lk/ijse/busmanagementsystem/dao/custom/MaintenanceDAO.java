package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.Maintenance;

import java.sql.SQLException;
import java.util.List;

public interface MaintenanceDAO extends CrudDAO<Maintenance> {

    boolean isBusExists(int busId)
            throws SQLException, ClassNotFoundException;

    List<Integer> getAllBusIds()
            throws SQLException, ClassNotFoundException;

    String getBusDetails(int busId)
            throws SQLException, ClassNotFoundException;

    List<Maintenance> searchMaintenance(String keyword)
            throws SQLException, ClassNotFoundException;
}
