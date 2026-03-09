package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.UpdatePrices;

import java.sql.SQLException;
import java.util.List;

public interface UpdatePricesDAO extends CrudDAO<UpdatePrices> {

    List<UpdatePrices> searchUpdatePrices(String keyword)
            throws SQLException, ClassNotFoundException;

    Double getLatestPrice(String updateType)
            throws SQLException, ClassNotFoundException;
}
