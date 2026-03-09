package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.PartPurchase;
import lk.ijse.busmanagementsystem.tm.PartPurchaseTM;

import java.sql.SQLException;
import java.util.List;

public interface PartPurchaseDAO extends CrudDAO<PartPurchase> {

    List<PartPurchaseTM> getAllTM() throws SQLException;

    List<PartPurchaseTM> searchPartPurchases(String keyword) throws SQLException;

    boolean isBusExists(int busId) throws SQLException;
    boolean isMaintenanceExists(int maintId) throws SQLException;

    List<Integer> getAllActiveBusIds() throws SQLException;
    List<Integer> getAllMaintenanceIds() throws SQLException;

    String getBusDetails(int busId) throws SQLException;
    String getMaintenanceDetails(int maintId) throws SQLException;
}
