package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.UpdatePricesDTO;

import java.sql.SQLException;
import java.util.List;

public interface UpdatePricesBO extends SuperBO {
    List<UpdatePricesDTO> getAllUpdatePrices() throws SQLException, ClassNotFoundException;
    boolean saveUpdatePrice(UpdatePricesDTO priceDTO) throws SQLException, ClassNotFoundException;
    boolean deleteUpdatePrice(String priceId) throws SQLException, ClassNotFoundException;
    UpdatePricesDTO searchUpdatePrice(String id) throws SQLException, ClassNotFoundException;
    List<UpdatePricesDTO> searchUpdatePrices(String keyword) throws SQLException, ClassNotFoundException;
    double calculateChangeAmount(double previousValue, double newValue);
    double calculatePercentageChange(double previousValue, double newValue);
    Double getLatestPrice(String updateType) throws SQLException, ClassNotFoundException;
}