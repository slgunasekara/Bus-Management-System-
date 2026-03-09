package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.TripExpenses;

import java.sql.SQLException;
import java.util.List;

public interface TripExpensesDAO extends CrudDAO<TripExpenses> {

    boolean isTripExists(int tripId)
            throws SQLException, ClassNotFoundException;

    List<Integer> getAllTripIds()
            throws SQLException, ClassNotFoundException;

    List<TripExpenses> searchTripExpenses(String keyword)
            throws SQLException, ClassNotFoundException;

    String getTripDetails(int tripId)
            throws SQLException, ClassNotFoundException;
}
