package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.PartPurchaseDTO;
import lk.ijse.busmanagementsystem.dto.PartPurchaseTM;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartPurchaseModel {

    // Get all active Bus IDs for ComboBox
    public List<Integer> getAllActiveBusIds() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT bus_id FROM Bus WHERE bus_status = 'Active' ORDER BY bus_id";

        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<Integer> busIds = new ArrayList<>();
        while (rs.next()) {
            busIds.add(rs.getInt("bus_id"));
        }
        return busIds;
    }

    // Get all Maintenance IDs for ComboBox
    public List<Integer> getAllMaintenanceIds() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT maint_id FROM Maintenance ORDER BY maint_id DESC";

        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<Integer> maintIds = new ArrayList<>();
        while (rs.next()) {
            maintIds.add(rs.getInt("maint_id"));
        }
        return maintIds;
    }

    // Get Bus Details to display below ComboBox
    public String getBusDetails(int busId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT bus_number, bus_brand_name, bus_type FROM Bus WHERE bus_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, busId);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return String.format("%s - %s (%s)",
                    rs.getString("bus_number"),
                    rs.getString("bus_brand_name"),
                    rs.getString("bus_type"));
        }
        return "Bus not found";
    }

    // Get Maintenance Details to display below ComboBox
    public String getMaintenanceDetails(int maintId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT m.maintenance_type, m.date, b.bus_number " +
                "FROM Maintenance m " +
                "JOIN Bus b ON m.bus_id = b.bus_id " +
                "WHERE m.maint_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, maintId);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return String.format("%s - %s (Bus: %s)",
                    rs.getString("maintenance_type"),
                    rs.getDate("date"),
                    rs.getString("bus_number"));
        }
        return "Maintenance not found";
    }

    // Check if Bus exists
    public boolean isBusExists(int busId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT bus_id FROM Bus WHERE bus_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, busId);
        ResultSet rs = pstm.executeQuery();

        return rs.next();
    }

    // Check if Maintenance exists
    public boolean isMaintenanceExists(int maintId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT maint_id FROM Maintenance WHERE maint_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, maintId);
        ResultSet rs = pstm.executeQuery();

        return rs.next();
    }

    // Save Part Purchase
    public boolean savePartPurchase(PartPurchaseDTO dto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO Part_Purchases (bus_id, maint_id, part_name, quantity, unit_price, " +
                "total_cost, supplier_name, part_description, date, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, dto.getBusId());
        pstm.setObject(2, dto.getMaintId());
        pstm.setString(3, dto.getPartName());
        pstm.setInt(4, dto.getQuantity());
        pstm.setDouble(5, dto.getUnitPrice());
        pstm.setDouble(6, dto.getTotalCost());
        pstm.setString(7, dto.getSupplierName());
        pstm.setString(8, dto.getPartDescription());
        pstm.setDate(9, Date.valueOf(dto.getDate()));
        pstm.setInt(10, dto.getCreatedBy());

        return pstm.executeUpdate() > 0;
    }

    // Update Part Purchase
    public boolean updatePartPurchase(PartPurchaseDTO dto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE Part_Purchases SET bus_id = ?, maint_id = ?, part_name = ?, " +
                "quantity = ?, unit_price = ?, total_cost = ?, supplier_name = ?, " +
                "part_description = ?, date = ? WHERE purchase_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, dto.getBusId());
        pstm.setObject(2, dto.getMaintId());
        pstm.setString(3, dto.getPartName());
        pstm.setInt(4, dto.getQuantity());
        pstm.setDouble(5, dto.getUnitPrice());
        pstm.setDouble(6, dto.getTotalCost());
        pstm.setString(7, dto.getSupplierName());
        pstm.setString(8, dto.getPartDescription());
        pstm.setDate(9, Date.valueOf(dto.getDate()));
        pstm.setInt(10, dto.getPurchaseId());

        return pstm.executeUpdate() > 0;
    }

    // Delete Part Purchase
    public boolean deletePartPurchase(String purchaseId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM Part_Purchases WHERE purchase_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, Integer.parseInt(purchaseId));

        return pstm.executeUpdate() > 0;
    }

    // Get All Part Purchases with TM (for table display)
    public List<PartPurchaseTM> getAllPartPurchases() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT pp.purchase_id, pp.bus_id, b.bus_number, pp.maint_id, pp.part_name, " +
                "pp.quantity, pp.unit_price, pp.total_cost, pp.supplier_name, " +
                "pp.part_description, pp.date, u.username " +
                "FROM Part_Purchases pp " +
                "LEFT JOIN Bus b ON pp.bus_id = b.bus_id " +
                "LEFT JOIN User u ON pp.created_by = u.user_id " +
                "ORDER BY pp.purchase_id DESC";

        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<PartPurchaseTM> partPurchaseList = new ArrayList<>();

        while (rs.next()) {
            PartPurchaseTM tm = new PartPurchaseTM(
                    rs.getInt("purchase_id"),
                    rs.getObject("bus_id", Integer.class),
                    rs.getString("bus_number"),
                    rs.getObject("maint_id", Integer.class),
                    rs.getString("part_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("total_cost"),
                    rs.getString("supplier_name"),
                    rs.getString("part_description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("username")
            );
            partPurchaseList.add(tm);
        }

        return partPurchaseList;
    }

    // Search Part Purchase by ID (returns DTO for form filling)
    public PartPurchaseDTO searchPartPurchase(String purchaseId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Part_Purchases WHERE purchase_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, Integer.parseInt(purchaseId));
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return new PartPurchaseDTO(
                    rs.getInt("purchase_id"),
                    rs.getObject("bus_id", Integer.class),
                    rs.getObject("maint_id", Integer.class),
                    rs.getString("part_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("total_cost"),
                    rs.getString("supplier_name"),
                    rs.getString("part_description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("created_by")
            );
        }
        return null;
    }

    // Search Part Purchases by keyword (returns TM list for table)
    public List<PartPurchaseTM> searchPartPurchases(String keyword) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT pp.purchase_id, pp.bus_id, b.bus_number, pp.maint_id, pp.part_name, " +
                "pp.quantity, pp.unit_price, pp.total_cost, pp.supplier_name, " +
                "pp.part_description, pp.date, u.username " +
                "FROM Part_Purchases pp " +
                "LEFT JOIN Bus b ON pp.bus_id = b.bus_id " +
                "LEFT JOIN User u ON pp.created_by = u.user_id " +
                "WHERE pp.purchase_id LIKE ? OR pp.part_name LIKE ? OR " +
                "pp.supplier_name LIKE ? OR b.bus_number LIKE ? " +
                "ORDER BY pp.purchase_id DESC";

        PreparedStatement pstm = connection.prepareStatement(sql);
        String searchPattern = "%" + keyword + "%";
        pstm.setString(1, searchPattern);
        pstm.setString(2, searchPattern);
        pstm.setString(3, searchPattern);
        pstm.setString(4, searchPattern);

        ResultSet rs = pstm.executeQuery();

        List<PartPurchaseTM> partPurchaseList = new ArrayList<>();

        while (rs.next()) {
            PartPurchaseTM tm = new PartPurchaseTM(
                    rs.getInt("purchase_id"),
                    rs.getObject("bus_id", Integer.class),
                    rs.getString("bus_number"),
                    rs.getObject("maint_id", Integer.class),
                    rs.getString("part_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDouble("total_cost"),
                    rs.getString("supplier_name"),
                    rs.getString("part_description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("username")
            );
            partPurchaseList.add(tm);
        }

        return partPurchaseList;
    }
}