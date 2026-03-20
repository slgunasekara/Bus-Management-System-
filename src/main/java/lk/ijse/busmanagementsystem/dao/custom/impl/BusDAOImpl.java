package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.BusDAO;
import lk.ijse.busmanagementsystem.entity.Bus;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAOImpl implements BusDAO {

    @Override
    public boolean save(Bus bus) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Bus (bus_brand_name, bus_number, bus_type, no_of_seats, bus_status, " +
                        "manufacture_date, insurance_Expiry_Date, license_Renewal_Date, current_Mileage, created_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                bus.getBusBrandName(),
                bus.getBusNumber(),
                bus.getBusType(),
                bus.getNoOfSeats(),
                bus.getBusStatus(),
                bus.getManufactureDate() != null ? bus.getManufactureDate().toString() : null,
                bus.getInsuranceExpiryDate() != null ? bus.getInsuranceExpiryDate().toString() : null,
                bus.getLicenseRenewalDate() != null ? bus.getLicenseRenewalDate().toString() : null,
                bus.getCurrentMileage(),
                bus.getCreatedBy()
        );
    }

    @Override
    public boolean update(Bus bus) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Bus SET bus_brand_name=?, bus_number=?, bus_type=?, no_of_seats=?, " +
                        "bus_status=?, manufacture_date=?, insurance_Expiry_Date=?, license_Renewal_Date=?, " +
                        "current_Mileage=?, updated_at=NOW() WHERE bus_id=?",
                bus.getBusBrandName(),
                bus.getBusNumber(),
                bus.getBusType(),
                bus.getNoOfSeats(),
                bus.getBusStatus(),
                bus.getManufactureDate() != null ? bus.getManufactureDate().toString() : null,
                bus.getInsuranceExpiryDate() != null ? bus.getInsuranceExpiryDate().toString() : null,
                bus.getLicenseRenewalDate() != null ? bus.getLicenseRenewalDate().toString() : null,
                bus.getCurrentMileage(),
                bus.getBusId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Bus WHERE bus_id=?", Integer.parseInt(id));
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Bus WHERE bus_id=?", id);
    }

    @Override
    public boolean exists(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", Integer.parseInt(id));
        return rs.next();
    }

    @Override
    public boolean exists(int id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", id);
        return rs.next();
    }

    @Override
    public Bus search(String id) throws SQLException, ClassNotFoundException {
        return get(Integer.parseInt(id));
    }

    @Override
    public Bus get(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id WHERE b.bus_id=?", id);
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public List<Bus> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id " +
                        "ORDER BY b.bus_id ASC"
        );
        List<Bus> list = new ArrayList<>();
        while (rs.next()) list.add(mapResultSet(rs));
        return list;
    }

    @Override
    public Integer getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(bus_id) FROM Bus");
        if (rs.next()) return rs.getInt(1) + 1;
        return 1;
    }

    @Override
    public List<String> getAllBusNumbers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT bus_number FROM Bus WHERE bus_status = 'Active' ORDER BY bus_number ASC"
        );
        List<String> list = new ArrayList<>();
        while (rs.next()) list.add(rs.getString("bus_number"));
        return list;
    }

    @Override
    public Bus getByBusNumber(String busNumber) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id WHERE b.bus_number=?",
                busNumber
        );
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public List<Bus> searchBuses(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id " +
                        "WHERE b.bus_brand_name LIKE ? OR b.bus_number LIKE ? " +
                        "OR b.bus_type LIKE ? OR b.bus_status LIKE ? " +
                        "ORDER BY b.bus_id ASC",
                p, p, p, p
        );
        List<Bus> list = new ArrayList<>();
        while (rs.next()) list.add(mapResultSet(rs));
        return list;
    }

    @Override
    public int getCountByStatus(String status) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Bus WHERE bus_status = ?", status);
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    @Override
    public int getTotalCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Bus");
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    private Bus mapResultSet(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        bus.setBusId(rs.getInt("bus_id"));
        bus.setBusBrandName(rs.getString("bus_brand_name"));
        bus.setBusNumber(rs.getString("bus_number"));
        bus.setBusType(rs.getString("bus_type"));
        bus.setNoOfSeats(rs.getInt("no_of_seats"));
        bus.setBusStatus(rs.getString("bus_status"));

        java.sql.Date mfgDate = rs.getDate("manufacture_date");
        bus.setManufactureDate(mfgDate != null ? mfgDate.toLocalDate() : null);

        java.sql.Date insDate = rs.getDate("insurance_Expiry_Date");
        bus.setInsuranceExpiryDate(insDate != null ? insDate.toLocalDate() : null);

        java.sql.Date licDate = rs.getDate("license_Renewal_Date");
        bus.setLicenseRenewalDate(licDate != null ? licDate.toLocalDate() : null);

        int mileage = rs.getInt("current_Mileage");
        bus.setCurrentMileage(rs.wasNull() ? 0 : mileage);

        bus.setCreatedBy(rs.getInt("created_by"));

        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        bus.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
        bus.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return bus;
    }
}