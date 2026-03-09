package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAOImpl {

    public List<BusDTO> getAllBuses() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Bus ORDER BY bus_id DESC");
        List<BusDTO> busList = new ArrayList<>();

        while (rst.next()) {
            BusDTO busDTO = new BusDTO(
                    rst.getInt("bus_id"),
                    rst.getString("bus_brand_name"),
                    rst.getString("bus_number"),
                    rst.getString("bus_type"),
                    rst.getInt("no_of_seats"),
                    rst.getString("bus_status"),
                    rst.getDate("manufacture_date") != null ? rst.getDate("manufacture_date").toLocalDate() : null,
                    rst.getDate("insurance_Expiry_Date") != null ? rst.getDate("insurance_Expiry_Date").toLocalDate() : null,
                    rst.getDate("license_Renewal_Date") != null ? rst.getDate("license_Renewal_Date").toLocalDate() : null,
                    rst.getInt("current_Mileage"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
            busList.add(busDTO);
        }
        return busList;
    }

    public boolean saveBus(BusDTO busDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Bus(bus_brand_name, bus_number, bus_type, no_of_seats, bus_status, " +
                "manufacture_date, insurance_Expiry_Date, license_Renewal_Date, current_Mileage, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                busDTO.getBusBrandName(),
                busDTO.getBusNumber(),
                busDTO.getBusType(),
                busDTO.getNoOfSeats(),
                busDTO.getBusStatus(),
                busDTO.getManufactureDate() != null ? java.sql.Date.valueOf(busDTO.getManufactureDate()) : null,
                busDTO.getInsuranceExpiryDate() != null ? java.sql.Date.valueOf(busDTO.getInsuranceExpiryDate()) : null,
                busDTO.getLicenseRenewalDate() != null ? java.sql.Date.valueOf(busDTO.getLicenseRenewalDate()) : null,
                busDTO.getCurrentMileage(),
                busDTO.getCreatedBy()
        );
    }

    public boolean updateBus(BusDTO busDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Bus SET bus_brand_name=?, bus_number=?, bus_type=?, no_of_seats=?, " +
                "bus_status=?, manufacture_date=?, insurance_Expiry_Date=?, license_Renewal_Date=?, " +
                "current_Mileage=? WHERE bus_id=?";

        return CrudUtil.execute(sql,
                busDTO.getBusBrandName(),
                busDTO.getBusNumber(),
                busDTO.getBusType(),
                busDTO.getNoOfSeats(),
                busDTO.getBusStatus(),
                busDTO.getManufactureDate() != null ? java.sql.Date.valueOf(busDTO.getManufactureDate()) : null,
                busDTO.getInsuranceExpiryDate() != null ? java.sql.Date.valueOf(busDTO.getInsuranceExpiryDate()) : null,
                busDTO.getLicenseRenewalDate() != null ? java.sql.Date.valueOf(busDTO.getLicenseRenewalDate()) : null,
                busDTO.getCurrentMileage(),
                busDTO.getBusId()
        );
    }

    public boolean deleteBus(String busId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Bus WHERE bus_id=?", busId);
    }

    public BusDTO searchBus(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Bus WHERE bus_id=?", id);

        if (rst.next()) {
            return new BusDTO(
                    rst.getInt("bus_id"),
                    rst.getString("bus_brand_name"),
                    rst.getString("bus_number"),
                    rst.getString("bus_type"),
                    rst.getInt("no_of_seats"),
                    rst.getString("bus_status"),
                    rst.getDate("manufacture_date") != null ? rst.getDate("manufacture_date").toLocalDate() : null,
                    rst.getDate("insurance_Expiry_Date") != null ? rst.getDate("insurance_Expiry_Date").toLocalDate() : null,
                    rst.getDate("license_Renewal_Date") != null ? rst.getDate("license_Renewal_Date").toLocalDate() : null,
                    rst.getInt("current_Mileage"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
        }
        return null;
    }

    public boolean isBusNumberExists(String busNumber) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_number=?", busNumber);
        return rst.next();
    }

    public boolean isBusNumberExistsForUpdate(String busNumber, int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_number=? AND bus_id!=?", busNumber, busId);
        return rst.next();
    }

    public List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Bus WHERE bus_brand_name LIKE ? OR bus_number LIKE ? OR bus_type LIKE ? OR bus_status LIKE ? ORDER BY bus_id DESC";
        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern, searchPattern);

        List<BusDTO> busList = new ArrayList<>();

        while (rst.next()) {
            BusDTO busDTO = new BusDTO(
                    rst.getInt("bus_id"),
                    rst.getString("bus_brand_name"),
                    rst.getString("bus_number"),
                    rst.getString("bus_type"),
                    rst.getInt("no_of_seats"),
                    rst.getString("bus_status"),
                    rst.getDate("manufacture_date") != null ? rst.getDate("manufacture_date").toLocalDate() : null,
                    rst.getDate("insurance_Expiry_Date") != null ? rst.getDate("insurance_Expiry_Date").toLocalDate() : null,
                    rst.getDate("license_Renewal_Date") != null ? rst.getDate("license_Renewal_Date").toLocalDate() : null,
                    rst.getInt("current_Mileage"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
            busList.add(busDTO);
        }
        return busList;
    }
}