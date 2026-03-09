package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.TripExpensesBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.TripExpensesDAO;
import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;
import lk.ijse.busmanagementsystem.entity.TripExpenses;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TripExpensesBOImpl implements TripExpensesBO {

    private final TripExpensesDAO tripExpensesDAO =
            (TripExpensesDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.TRIP_EXPENSES);

    @Override
    public List<TripExpensesDTO> getAllTripExpenses() throws SQLException, ClassNotFoundException {
        return tripExpensesDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException {
        if (!isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        if (dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero!");
        }
        return tripExpensesDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException {
        if (!isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        if (dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero!");
        }
        return tripExpensesDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteTripExpense(String expenseId) throws SQLException, ClassNotFoundException {
        return tripExpensesDAO.delete(expenseId);
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        // FIX: was tripExpensesDAO.exists(tripId) which checks trip_exp_id — correct is isTripExists()
        return tripExpensesDAO.isTripExists(tripId);
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        return tripExpensesDAO.getAllTripIds();
    }

    @Override
    public List<TripExpensesDTO> searchTripExpenses(String keyword) throws SQLException, ClassNotFoundException {
        return tripExpensesDAO.searchTripExpenses(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        return tripExpensesDAO.getTripDetails(tripId);
    }


    private TripExpenses toEntity(TripExpensesDTO dto) {
        TripExpenses e = new TripExpenses();
        e.setTripExpId(dto.getTripExpId());
        e.setTripId(dto.getTripId());
        e.setTripExpType(dto.getTripExpType());
        e.setAmount(dto.getAmount());
        e.setDescription(dto.getDescription());
        e.setDate(dto.getDate());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private TripExpensesDTO toDTO(TripExpenses e) {
        TripExpensesDTO dto = new TripExpensesDTO();
        dto.setTripExpId(e.getTripExpId());
        dto.setTripId(e.getTripId());
        dto.setTripExpType(e.getTripExpType());
        dto.setAmount(e.getAmount());
        dto.setDescription(e.getDescription());
        dto.setDate(e.getDate());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }
}
