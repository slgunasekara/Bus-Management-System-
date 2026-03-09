package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.PartPurchaseDAO;
import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.entity.PartPurchase;
import lk.ijse.busmanagementsystem.tm.PartPurchaseTM;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartPurchaseDAOImpl implements PartPurchaseDAO {

    private static final String TM_SQL =
            "SELECT pp.purchase_id, pp.bus_id, b.bus_number, pp.maint_id, pp.part_name, " +
                    "pp.quantity, pp.unit_price, pp.total_cost, pp.supplier_name, " +
                    "pp.part_description, pp.date, u.username " +
                    "FROM Part_Purchases pp " +
                    "LEFT JOIN Bus b ON pp.bus_id = b.bus_id " +
                    "LEFT JOIN User u ON pp.created_by = u.user_id ";

    @Override
    public List<PartPurchase> getAll() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM Part_Purchases ORDER BY purchase_id DESC");
        ResultSet rs = pstm.executeQuery();
        List<PartPurchase> list = new ArrayList<>();
        while (rs.next()) list.add(mapResultSet(rs));
        return list;
    }

    @Override
    public List<PartPurchaseTM> getAllTM() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(TM_SQL + "ORDER BY pp.purchase_id DESC");
        return mapTMResultSet(pstm.executeQuery());
    }

    @Override
    public boolean save(PartPurchase pp) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "INSERT INTO Part_Purchases(bus_id, maint_id, part_name, quantity, unit_price, total_cost, supplier_name, part_description, date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        pstm.setObject(1, pp.getBusId()); pstm.setObject(2, pp.getMaintId());
        pstm.setString(3, pp.getPartName()); pstm.setInt(4, pp.getQuantity());
        pstm.setDouble(5, pp.getUnitPrice()); pstm.setDouble(6, pp.getTotalCost());
        pstm.setString(7, pp.getSupplierName()); pstm.setString(8, pp.getPartDescription());
        pstm.setDate(9, Date.valueOf(pp.getDate())); pstm.setInt(10, pp.getCreatedBy());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(PartPurchase pp) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "UPDATE Part_Purchases SET bus_id=?, maint_id=?, part_name=?, quantity=?, unit_price=?, total_cost=?, supplier_name=?, part_description=?, date=? WHERE purchase_id=?");
        pstm.setObject(1, pp.getBusId()); pstm.setObject(2, pp.getMaintId());
        pstm.setString(3, pp.getPartName()); pstm.setInt(4, pp.getQuantity());
        pstm.setDouble(5, pp.getUnitPrice()); pstm.setDouble(6, pp.getTotalCost());
        pstm.setString(7, pp.getSupplierName()); pstm.setString(8, pp.getPartDescription());
        pstm.setDate(9, Date.valueOf(pp.getDate())); pstm.setInt(10, pp.getPurchaseId());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String purchaseId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM Part_Purchases WHERE purchase_id=?");
        pstm.setInt(1, Integer.parseInt(purchaseId));
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(int purchaseId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM Part_Purchases WHERE purchase_id=?");
        pstm.setInt(1, purchaseId);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean exists(String purchaseId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT purchase_id FROM Part_Purchases WHERE purchase_id=?");
        pstm.setInt(1, Integer.parseInt(purchaseId));
        return pstm.executeQuery().next();
    }

    @Override
    public boolean exists(int purchaseId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT purchase_id FROM Part_Purchases WHERE purchase_id=?");
        pstm.setInt(1, purchaseId);
        return pstm.executeQuery().next();
    }

    @Override
    public PartPurchase search(String purchaseId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM Part_Purchases WHERE purchase_id=?");
        pstm.setInt(1, Integer.parseInt(purchaseId));
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public List<PartPurchaseTM> searchPartPurchases(String keyword) throws SQLException {
        String p = "%" + keyword + "%";
        PreparedStatement pstm = conn().prepareStatement(
                TM_SQL + "WHERE pp.purchase_id LIKE ? OR pp.part_name LIKE ? OR pp.supplier_name LIKE ? OR b.bus_number LIKE ? ORDER BY pp.purchase_id DESC");
        pstm.setString(1, p); pstm.setString(2, p); pstm.setString(3, p); pstm.setString(4, p);
        return mapTMResultSet(pstm.executeQuery());
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT bus_id FROM Bus WHERE bus_id=?");
        pstm.setInt(1, busId);
        return pstm.executeQuery().next();
    }

    @Override
    public boolean isMaintenanceExists(int maintId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT maint_id FROM Maintenance WHERE maint_id=?");
        pstm.setInt(1, maintId);
        return pstm.executeQuery().next();
    }

    @Override
    public List<Integer> getAllActiveBusIds() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT bus_id FROM Bus WHERE bus_status='Active' ORDER BY bus_id");
        ResultSet rs = pstm.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) ids.add(rs.getInt("bus_id"));
        return ids;
    }

    @Override
    public List<Integer> getAllMaintenanceIds() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT maint_id FROM Maintenance ORDER BY maint_id DESC");
        ResultSet rs = pstm.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) ids.add(rs.getInt("maint_id"));
        return ids;
    }

    @Override
    public String getBusDetails(int busId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT bus_number, bus_brand_name, bus_type FROM Bus WHERE bus_id=?");
        pstm.setInt(1, busId);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return rs.getString("bus_number") + " - " + rs.getString("bus_brand_name") + " (" + rs.getString("bus_type") + ")";
        return "Bus not found";
    }

    @Override
    public String getMaintenanceDetails(int maintId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "SELECT m.maintenance_type, m.date, b.bus_number FROM Maintenance m JOIN Bus b ON m.bus_id=b.bus_id WHERE m.maint_id=?");
        pstm.setInt(1, maintId);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return rs.getString("maintenance_type") + " - " + rs.getDate("date") + " (Bus: " + rs.getString("bus_number") + ")";
        return "Maintenance not found";
    }

    private Connection conn() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    private PartPurchase mapResultSet(ResultSet rs) throws SQLException {
        PartPurchase pp = new PartPurchase();
        pp.setPurchaseId(rs.getInt("purchase_id"));
        pp.setBusId(rs.getObject("bus_id", Integer.class));
        pp.setMaintId(rs.getObject("maint_id", Integer.class));
        pp.setPartName(rs.getString("part_name"));
        pp.setQuantity(rs.getInt("quantity"));
        pp.setUnitPrice(rs.getDouble("unit_price"));
        pp.setTotalCost(rs.getDouble("total_cost"));
        pp.setSupplierName(rs.getString("supplier_name"));
        pp.setPartDescription(rs.getString("part_description"));
        pp.setDate(rs.getDate("date").toLocalDate());
        pp.setCreatedBy(rs.getInt("created_by"));
        return pp;
    }

    private List<PartPurchaseTM> mapTMResultSet(ResultSet rs) throws SQLException {
        List<PartPurchaseTM> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new PartPurchaseTM(
                    rs.getInt("purchase_id"), rs.getObject("bus_id", Integer.class),
                    rs.getString("bus_number"), rs.getObject("maint_id", Integer.class),
                    rs.getString("part_name"), rs.getInt("quantity"),
                    rs.getDouble("unit_price"), rs.getDouble("total_cost"),
                    rs.getString("supplier_name"), rs.getString("part_description"),
                    rs.getDate("date").toLocalDate(), rs.getString("username")
            ));
        }
        return list;
    }
}
