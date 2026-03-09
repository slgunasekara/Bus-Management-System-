package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.EmployeeSalaryBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.EmployeeSalaryDAO;
import lk.ijse.busmanagementsystem.dto.EmployeeSalaryDTO;
import lk.ijse.busmanagementsystem.entity.EmployeeSalary;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;

import java.sql.SQLException;
import java.util.List;

public class EmployeeSalaryBOImpl implements EmployeeSalaryBO {

    private final EmployeeSalaryDAO employeeSalaryDAO =
            (EmployeeSalaryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.EMPLOYEE_SALARY);

    @Override
    public List<EmployeeSalaryTM> getAllEmployeeSalaries() throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.getAllTM();
    }

    @Override
    public boolean saveEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException {
        if (!isEmployeeExists(dto.getEmpId())) {
            throw new IllegalArgumentException("Employee ID " + dto.getEmpId() + " does not exist!");
        }
        if (dto.getTripId() != null && !isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        return employeeSalaryDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException {
        if (!isEmployeeExists(dto.getEmpId())) {
            throw new IllegalArgumentException("Employee ID " + dto.getEmpId() + " does not exist!");
        }
        if (dto.getTripId() != null && !isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        return employeeSalaryDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteEmployeeSalary(String salaryId) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.delete(salaryId);
    }

    @Override
    public boolean isEmployeeExists(int empId) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.isEmployeeExists(empId);
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.isTripExists(tripId);
    }

    @Override
    public List<Integer> getAllActiveEmployeeIds() throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.getAllActiveEmployeeIds();
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.getAllTripIds();
    }

    @Override
    public List<EmployeeSalaryTM> searchEmployeeSalaries(String keyword) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.searchEmployeeSalaries(keyword);
    }

    @Override
    public String getEmployeeDetails(int empId) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.getEmployeeDetails(empId);
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        return employeeSalaryDAO.getTripDetails(tripId);
    }


    private EmployeeSalary toEntity(EmployeeSalaryDTO dto) {
        EmployeeSalary e = new EmployeeSalary();
        e.setSalaryId(dto.getSalaryId());
        e.setEmpId(dto.getEmpId());
        e.setTripId(dto.getTripId());
        e.setAmount(dto.getAmount());
        e.setDescription(dto.getDescription());
        e.setDate(dto.getDate());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }
}
