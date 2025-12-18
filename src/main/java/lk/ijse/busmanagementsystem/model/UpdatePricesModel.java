package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.UpdatePricesDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdatePricesModel {

    public List<UpdatePricesDTO> getAllUpdatePrices() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Update_Prices ORDER BY change_date DESC");
        List<UpdatePricesDTO> pricesList = new ArrayList<>();

        while (rst.next()) {
            UpdatePricesDTO priceDTO = new UpdatePricesDTO(
                    rst.getInt("update_prices_id"),
                    rst.getString("update_type"),
                    rst.getString("change_type"),
                    rst.getDouble("previous_value"),
                    rst.getDouble("new_value"),
                    rst.getDouble("change_amount"),
                    rst.getDouble("percentage_change"),
                    rst.getDate("change_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
            pricesList.add(priceDTO);
        }
        return pricesList;
    }

    public boolean saveUpdatePrice(UpdatePricesDTO priceDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Update_Prices(update_type, change_type, previous_value, new_value, " +
                "change_amount, percentage_change, change_date, description, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                priceDTO.getUpdateType(),
                priceDTO.getChangeType(),
                priceDTO.getPreviousValue(),
                priceDTO.getNewValue(),
                priceDTO.getChangeAmount(),
                priceDTO.getPercentageChange(),
                java.sql.Date.valueOf(priceDTO.getChangeDate()),
                priceDTO.getDescription(),
                priceDTO.getCreatedBy()
        );
    }

    public boolean deleteUpdatePrice(String priceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Update_Prices WHERE update_prices_id=?", priceId);
    }

    public UpdatePricesDTO searchUpdatePrice(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Update_Prices WHERE update_prices_id=?", id);

        if (rst.next()) {
            return new UpdatePricesDTO(
                    rst.getInt("update_prices_id"),
                    rst.getString("update_type"),
                    rst.getString("change_type"),
                    rst.getDouble("previous_value"),
                    rst.getDouble("new_value"),
                    rst.getDouble("change_amount"),
                    rst.getDouble("percentage_change"),
                    rst.getDate("change_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
        }
        return null;
    }

    public List<UpdatePricesDTO> searchUpdatePrices(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Update_Prices WHERE update_type LIKE ? OR change_type LIKE ? " +
                "OR description LIKE ? ORDER BY change_date DESC";
        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern);

        List<UpdatePricesDTO> pricesList = new ArrayList<>();

        while (rst.next()) {
            UpdatePricesDTO priceDTO = new UpdatePricesDTO(
                    rst.getInt("update_prices_id"),
                    rst.getString("update_type"),
                    rst.getString("change_type"),
                    rst.getDouble("previous_value"),
                    rst.getDouble("new_value"),
                    rst.getDouble("change_amount"),
                    rst.getDouble("percentage_change"),
                    rst.getDate("change_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
            pricesList.add(priceDTO);
        }
        return pricesList;
    }

    // Calculate change amount based on previous and new values
    public double calculateChangeAmount(double previousValue, double newValue) {
        return newValue - previousValue;
    }

    // Calculate percentage change
    public double calculatePercentageChange(double previousValue, double newValue) {
        if (previousValue == 0) {
            return 0;
        }
        return ((newValue - previousValue) / previousValue) * 100;
    }

    // Get latest price for a specific type (FUEL or TICKET)
    public Double getLatestPrice(String updateType) throws SQLException, ClassNotFoundException {
        String sql = "SELECT new_value FROM Update_Prices WHERE update_type=? " +
                "ORDER BY change_date DESC, update_prices_id DESC LIMIT 1";
        ResultSet rst = CrudUtil.execute(sql, updateType);

        if (rst.next()) {
            return rst.getDouble("new_value");
        }
        return null;
    }
}