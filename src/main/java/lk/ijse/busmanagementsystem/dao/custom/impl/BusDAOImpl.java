package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.BusDAO;
import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAOImpl implements BusDAO {

    // ── Table name matches your DB exactly ───────────────────────────────────────
    private static final String TABLE = "Bus";
    private static final String USER_TABLE = "User";

    @Override
    public boolean save(BusDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Bus (bus_brand_name, bus_number, bus_type, no_of_seats, bus_status, " +
                        "manufacture_date, insurance_Expiry_Date, license_Renewal_Date, current_Mileage, created_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                dto.getBusBrandName(),
                dto.getBusNumber(),
                dto.getBusType(),
                dto.getNoOfSeats(),
                dto.getBusStatus(),
                dto.getManufactureDate() != null ? dto.getManufactureDate().toString() : null,
                dto.getInsuranceExpiryDate() != null ? dto.getInsuranceExpiryDate().toString() : null,
                dto.getLicenseRenewalDate() != null ? dto.getLicenseRenewalDate().toString() : null,
                dto.getCurrentMileage(),
                dto.getCreatedBy()
        );
    }

    @Override
    public boolean update(BusDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Bus SET bus_brand_name=?, bus_number=?, bus_type=?, no_of_seats=?, " +
                        "bus_status=?, manufacture_date=?, insurance_Expiry_Date=?, license_Renewal_Date=?, " +
                        "current_Mileage=?, updated_at=NOW() WHERE bus_id=?",
                dto.getBusBrandName(),
                dto.getBusNumber(),
                dto.getBusType(),
                dto.getNoOfSeats(),
                dto.getBusStatus(),
                dto.getManufactureDate() != null ? dto.getManufactureDate().toString() : null,
                dto.getInsuranceExpiryDate() != null ? dto.getInsuranceExpiryDate().toString() : null,
                dto.getLicenseRenewalDate() != null ? dto.getLicenseRenewalDate().toString() : null,
                dto.getCurrentMileage(),
                dto.getBusId()
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

    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
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
    public BusDTO search(String id) throws SQLException, ClassNotFoundException {
        // Delegate to get() method
        return get(Integer.parseInt(id));
    }

    public BusDTO get(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id WHERE b.bus_id=?", id);
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    @Override
    public List<BusDTO> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id " +
                        "ORDER BY b.bus_id ASC"
        );
        List<BusDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    @Override
    public Integer getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(bus_id) FROM Bus");
        if (rs.next()) {
            int maxId = rs.getInt(1);
            return maxId + 1;
        }
        return 1;
    }

    @Override
    public List<String> getAllBusNumbers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT bus_number FROM Bus WHERE bus_status = 'Active' ORDER BY bus_number ASC"
        );
        List<String> busNumbers = new ArrayList<>();
        while (rs.next()) {
            busNumbers.add(rs.getString("bus_number"));
        }
        return busNumbers;
    }

    @Override
    public BusDTO getByBusNumber(String busNumber) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id WHERE b.bus_number=?",
                busNumber
        );
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    @Override
    public List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException {
        String searchTerm = "%" + keyword + "%";
        ResultSet rs = CrudUtil.execute(
                "SELECT b.*, u.name as created_by_name FROM Bus b " +
                        "LEFT JOIN User u ON b.created_by = u.user_id " +
                        "WHERE b.bus_brand_name LIKE ? OR b.bus_number LIKE ? " +
                        "OR b.bus_type LIKE ? OR b.bus_status LIKE ? " +
                        "ORDER BY b.bus_id ASC",
                searchTerm, searchTerm, searchTerm, searchTerm
        );
        List<BusDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    @Override
    public int getCountByStatus(String status) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute(
                "SELECT COUNT(*) FROM Bus WHERE bus_status = ?", status
        );
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public int getTotalCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Bus");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // ─── mapResultSet — column names DB එකට exact match ────────────────────────
    private BusDTO mapResultSet(ResultSet rs) throws SQLException {
        BusDTO dto = new BusDTO();
        dto.setBusId(rs.getInt("bus_id"));
        dto.setBusBrandName(rs.getString("bus_brand_name"));
        dto.setBusNumber(rs.getString("bus_number"));
        dto.setBusType(rs.getString("bus_type"));
        dto.setNoOfSeats(rs.getInt("no_of_seats"));
        dto.setBusStatus(rs.getString("bus_status"));

        // DB column: manufacture_date
        java.sql.Date mfgDate = rs.getDate("manufacture_date");
        dto.setManufactureDate(mfgDate != null ? mfgDate.toLocalDate() : null);

        // DB column: insurance_Expiry_Date (capital E and D — DB ekata exact)
        java.sql.Date insDate = rs.getDate("insurance_Expiry_Date");
        dto.setInsuranceExpiryDate(insDate != null ? insDate.toLocalDate() : null);

        // DB column: license_Renewal_Date (capital R and D — DB ekata exact)
        java.sql.Date licDate = rs.getDate("license_Renewal_Date");
        dto.setLicenseRenewalDate(licDate != null ? licDate.toLocalDate() : null);

        // DB column: current_Mileage (capital M — DB ekata exact)
        int mileage = rs.getInt("current_Mileage");
        dto.setCurrentMileage(rs.wasNull() ? null : mileage);

        dto.setCreatedBy(rs.getInt("created_by"));

        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        dto.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
        dto.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return dto;
    }
}