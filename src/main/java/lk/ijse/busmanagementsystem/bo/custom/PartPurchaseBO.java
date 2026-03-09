package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.PartPurchaseDTO;
import lk.ijse.busmanagementsystem.tm.PartPurchaseTM;

import java.sql.SQLException;
import java.util.List;

public interface PartPurchaseBO extends SuperBO {
    List<Integer> getAllActiveBusIds() throws SQLException;
    List<Integer> getAllMaintenanceIds() throws SQLException;
    String getBusDetails(int busId) throws SQLException;
    String getMaintenanceDetails(int maintId) throws SQLException;
    boolean isBusExists(int busId) throws SQLException;
    boolean isMaintenanceExists(int maintId) throws SQLException;
    boolean savePartPurchase(PartPurchaseDTO dto) throws SQLException, ClassNotFoundException;
    boolean updatePartPurchase(PartPurchaseDTO dto) throws SQLException, ClassNotFoundException;
    boolean deletePartPurchase(String purchaseId) throws SQLException, ClassNotFoundException;
    List<PartPurchaseTM> getAllPartPurchases() throws SQLException;
    PartPurchaseDTO searchPartPurchase(String purchaseId) throws SQLException, ClassNotFoundException;
    List<PartPurchaseTM> searchPartPurchases(String keyword) throws SQLException;

    double calculateTotalCost(int quantity, double unitPrice);
}