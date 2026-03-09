package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.TripEmployeeBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.TripEmployeeDAO;
import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.TripEmployeeDTO;
import lk.ijse.busmanagementsystem.entity.TripEmployee;
import lk.ijse.busmanagementsystem.tm.TripEmployeeTM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TripEmployeeBOImpl implements TripEmployeeBO {

    private final TripEmployeeDAO tripEmployeeDAO =
            (TripEmployeeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.TRIP_EMPLOYEE);

    @Override
    public List<TripEmployeeTM> getEmployeesByTrip(int tripId) throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.getEmployeesByTrip(tripId);
    }

    @Override
    public boolean assignEmployeeToTrip(TripEmployeeDTO dto) throws SQLException, ClassNotFoundException {
        if (isEmployeeAssigned(dto.getTripId(), dto.getEmpId(), dto.getRoleInTrip())) {
            throw new IllegalArgumentException(
                    "Employee " + dto.getEmpId() + " is already assigned to this trip with role: " + dto.getRoleInTrip());
        }
        return tripEmployeeDAO.assignEmployeeToTrip(toEntity(dto));
    }


    @Override
    public boolean assignMultipleEmployees(int tripId, List<TripEmployeeDTO> dtoList)
            throws SQLException, ClassNotFoundException {

        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("Employee list cannot be empty!");
        }

        // transaction
        for (TripEmployeeDTO dto : dtoList) {
            if (isEmployeeAssigned(tripId, dto.getEmpId(), dto.getRoleInTrip())) {
                throw new IllegalArgumentException(
                        "Employee " + dto.getEmpId() + " is already assigned to Trip " + tripId
                                + " with role: " + dto.getRoleInTrip());
            }
        }

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<TripEmployee> entityList = dtoList.stream()
                    .peek(dto -> dto.setTripId(tripId))
                    .map(this::toEntity)
                    .collect(Collectors.toList());

            boolean result = tripEmployeeDAO.assignMultipleEmployees(tripId, entityList);
            if (!result) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean removeEmployeeFromTrip(int tripEmpId) throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.delete(tripEmpId);
    }

    @Override
    public boolean removeAllEmployeesFromTrip(int tripId) throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.removeAllEmployeesFromTrip(tripId);
    }

    @Override
    public boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.isEmployeeAssigned(tripId, empId, role);
    }

    @Override
    public List<TripEmployeeTM> getTripsByEmployee(int empId) throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.getTripsByEmployee(empId);
    }

    @Override
    public boolean updateEmployeeRole(int tripEmpId, String newRole)
            throws SQLException, ClassNotFoundException {
        return tripEmployeeDAO.updateEmployeeRole(tripEmpId, newRole);
    }


    private TripEmployee toEntity(TripEmployeeDTO dto) {
        TripEmployee e = new TripEmployee();
        e.setTripEmpId(dto.getTripEmpId());
        e.setTripId(dto.getTripId());
        e.setEmpId(dto.getEmpId());
        e.setRoleInTrip(dto.getRoleInTrip());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }
}
