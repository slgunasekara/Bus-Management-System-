package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.UpdatePricesDAO;
import lk.ijse.busmanagementsystem.entity.UpdatePrices;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdatePricesDAOImpl implements UpdatePricesDAO {

    @Override
    public List<UpdatePrices> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Update_Prices ORDER BY change_date DESC");
        List<UpdatePrices> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(UpdatePrices up) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Update_Prices(update_type, change_type, previous_value, new_value, change_amount, percentage_change, change_date, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                up.getUpdateType(), up.getChangeType(), up.getPreviousValue(), up.getNewValue(),
                up.getChangeAmount(), up.getPercentageChange(),
                java.sql.Date.valueOf(up.getChangeDate()), up.getDescription(), up.getCreatedBy()
        );
    }

    @Override
    public boolean update(UpdatePrices up) throws SQLException, ClassNotFoundException {
        return false; // Price records are immutable — no updates allowed
    }

    @Override
    public boolean delete(String priceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Update_Prices WHERE update_prices_id=?", priceId);
    }

    @Override
    public boolean delete(int priceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Update_Prices WHERE update_prices_id=?", priceId);
    }

    @Override
    public boolean exists(String priceId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT update_prices_id FROM Update_Prices WHERE update_prices_id=?", priceId);
        return rst.next();
    }

    @Override
    public boolean exists(int priceId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT update_prices_id FROM Update_Prices WHERE update_prices_id=?", priceId);
        return rst.next();
    }

    @Override
    public UpdatePrices search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Update_Prices WHERE update_prices_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<UpdatePrices> searchUpdatePrices(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM Update_Prices WHERE update_type LIKE ? OR change_type LIKE ? OR description LIKE ? ORDER BY change_date DESC",
                p, p, p);
        List<UpdatePrices> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public Double getLatestPrice(String updateType) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT new_value FROM Update_Prices WHERE update_type=? ORDER BY change_date DESC, update_prices_id DESC LIMIT 1",
                updateType);
        return rst.next() ? rst.getDouble("new_value") : null;
    }

    private UpdatePrices mapResultSet(ResultSet rst) throws SQLException {
        UpdatePrices up = new UpdatePrices();
        up.setUpdatePricesId(rst.getInt("update_prices_id"));
        up.setUpdateType(rst.getString("update_type"));
        up.setChangeType(rst.getString("change_type"));
        up.setPreviousValue(rst.getDouble("previous_value"));
        up.setNewValue(rst.getDouble("new_value"));
        up.setChangeAmount(rst.getDouble("change_amount"));
        up.setPercentageChange(rst.getDouble("percentage_change"));
        up.setChangeDate(rst.getDate("change_date").toLocalDate());
        up.setDescription(rst.getString("description"));
        up.setCreatedBy(rst.getInt("created_by"));
        return up;
    }
}
