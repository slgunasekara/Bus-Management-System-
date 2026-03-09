package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.EmployeeBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.EmployeeDAO;
import lk.ijse.busmanagementsystem.dto.EmployeeDTO;
import lk.ijse.busmanagementsystem.entity.Employee;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeBOImpl implements EmployeeBO {

    private final EmployeeDAO employeeDAO =
            (EmployeeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.EMPLOYEE);

    @Override
    public List<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        return employeeDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        if (isNicExists(dto.getNicNo())) {
            throw new IllegalArgumentException("NIC '" + dto.getNicNo() + "' already exists!");
        }
        return employeeDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        if (isNicExistsForOther(dto.getNicNo(), dto.getEmpId())) {
            throw new IllegalArgumentException("NIC '" + dto.getNicNo() + "' already exists for another employee!");
        }
        return employeeDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteEmployee(String empId) throws SQLException, ClassNotFoundException {
        return employeeDAO.delete(empId);
    }

    @Override
    public EmployeeDTO searchEmployee(String id) throws SQLException, ClassNotFoundException {
        Employee e = employeeDAO.search(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<EmployeeDTO> searchEmployees(String keyword) throws SQLException, ClassNotFoundException {
        return employeeDAO.searchEmployees(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean isNicExists(String nic) throws SQLException, ClassNotFoundException {
        return employeeDAO.exists(nic);
    }

    @Override
    public boolean isNicExistsForOther(String nic, int empId) throws SQLException, ClassNotFoundException {
        return employeeDAO.isNicExistsForOther(nic, empId);
    }


    private Employee toEntity(EmployeeDTO dto) {
        Employee e = new Employee();
        e.setEmpId(dto.getEmpId());
        e.setEmpCategory(dto.getEmpCategory());
        e.setEmpName(dto.getEmpName());
        e.setAddress(dto.getAddress());
        e.setContactNo(dto.getContactNo());
        e.setNicNo(dto.getNicNo());
        e.setNtcNo(dto.getNtcNo());
        e.setDrivingLicenceNo(dto.getDrivingLicenceNo());
        e.setJoinDate(dto.getJoinDate());
        e.setExitDate(dto.getExitDate());
        e.setEmpStatus(dto.getEmpStatus());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpId(e.getEmpId());
        dto.setEmpCategory(e.getEmpCategory());
        dto.setEmpName(e.getEmpName());
        dto.setAddress(e.getAddress());
        dto.setContactNo(e.getContactNo());
        dto.setNicNo(e.getNicNo());
        dto.setNtcNo(e.getNtcNo());
        dto.setDrivingLicenceNo(e.getDrivingLicenceNo());
        dto.setJoinDate(e.getJoinDate());
        dto.setExitDate(e.getExitDate());
        dto.setEmpStatus(e.getEmpStatus());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }
}
