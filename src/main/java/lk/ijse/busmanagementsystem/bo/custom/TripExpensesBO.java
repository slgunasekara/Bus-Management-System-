package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;

import java.sql.SQLException;
import java.util.List;

public interface TripExpensesBO extends SuperBO {
    List<TripExpensesDTO> getAllTripExpenses() throws SQLException, ClassNotFoundException;
    boolean saveTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException;
    boolean updateTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteTripExpense(String expenseId) throws SQLException, ClassNotFoundException;
    boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException;
    List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException;
    List<TripExpensesDTO> searchTripExpenses(String keyword) throws SQLException, ClassNotFoundException;
    String getTripDetails(int tripId) throws SQLException, ClassNotFoundException;
}