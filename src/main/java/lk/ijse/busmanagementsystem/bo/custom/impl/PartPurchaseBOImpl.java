package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.PartPurchaseBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.PartPurchaseDAO;
import lk.ijse.busmanagementsystem.dto.PartPurchaseDTO;
import lk.ijse.busmanagementsystem.entity.PartPurchase;
import lk.ijse.busmanagementsystem.tm.PartPurchaseTM;

import java.sql.SQLException;
import java.util.List;

public class PartPurchaseBOImpl implements PartPurchaseBO {

    private final PartPurchaseDAO partPurchaseDAO =
            (PartPurchaseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PART_PURCHASE);

    @Override
    public List<PartPurchaseTM> getAllPartPurchases() throws SQLException {
        return partPurchaseDAO.getAllTM();
    }

    @Override
    public boolean savePartPurchase(PartPurchaseDTO dto) throws SQLException, ClassNotFoundException {
        dto.setTotalCost(calculateTotalCost(dto.getQuantity(), dto.getUnitPrice()));
        if (dto.getBusId() != null && !isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getMaintId() != null && !isMaintenanceExists(dto.getMaintId())) {
            throw new IllegalArgumentException("Maintenance ID " + dto.getMaintId() + " does not exist!");
        }
        return partPurchaseDAO.save(toEntity(dto));
    }

    @Override
    public boolean updatePartPurchase(PartPurchaseDTO dto) throws SQLException, ClassNotFoundException {
        dto.setTotalCost(calculateTotalCost(dto.getQuantity(), dto.getUnitPrice()));
        if (dto.getBusId() != null && !isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getMaintId() != null && !isMaintenanceExists(dto.getMaintId())) {
            throw new IllegalArgumentException("Maintenance ID " + dto.getMaintId() + " does not exist!");
        }
        return partPurchaseDAO.update(toEntity(dto));
    }

    @Override
    public boolean deletePartPurchase(String purchaseId) throws SQLException, ClassNotFoundException {
        return partPurchaseDAO.delete(purchaseId);
    }

    @Override
    public PartPurchaseDTO searchPartPurchase(String purchaseId) throws SQLException, ClassNotFoundException {
        PartPurchase e = partPurchaseDAO.search(purchaseId);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<PartPurchaseTM> searchPartPurchases(String keyword) throws SQLException {
        return partPurchaseDAO.searchPartPurchases(keyword);
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException {
        return partPurchaseDAO.isBusExists(busId);
    }

    @Override
    public boolean isMaintenanceExists(int maintId) throws SQLException {
        return partPurchaseDAO.isMaintenanceExists(maintId);
    }

    @Override
    public List<Integer> getAllActiveBusIds() throws SQLException {
        return partPurchaseDAO.getAllActiveBusIds();
    }

    @Override
    public List<Integer> getAllMaintenanceIds() throws SQLException {
        return partPurchaseDAO.getAllMaintenanceIds();
    }

    @Override
    public String getBusDetails(int busId) throws SQLException {
        return partPurchaseDAO.getBusDetails(busId);
    }

    @Override
    public String getMaintenanceDetails(int maintId) throws SQLException {
        return partPurchaseDAO.getMaintenanceDetails(maintId);
    }

    @Override
    public double calculateTotalCost(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }


    private PartPurchase toEntity(PartPurchaseDTO dto) {
        PartPurchase e = new PartPurchase();
        e.setPurchaseId(dto.getPurchaseId());
        e.setBusId(dto.getBusId());
        e.setMaintId(dto.getMaintId());
        e.setPartName(dto.getPartName());
        e.setQuantity(dto.getQuantity());
        e.setUnitPrice(dto.getUnitPrice());
        e.setTotalCost(dto.getTotalCost());
        e.setSupplierName(dto.getSupplierName());
        e.setPartDescription(dto.getPartDescription());
        e.setDate(dto.getDate());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private PartPurchaseDTO toDTO(PartPurchase e) {
        PartPurchaseDTO dto = new PartPurchaseDTO();
        dto.setPurchaseId(e.getPurchaseId());
        dto.setBusId(e.getBusId());
        dto.setMaintId(e.getMaintId());
        dto.setPartName(e.getPartName());
        dto.setQuantity(e.getQuantity());
        dto.setUnitPrice(e.getUnitPrice());
        dto.setTotalCost(e.getTotalCost());
        dto.setSupplierName(e.getSupplierName());
        dto.setPartDescription(e.getPartDescription());
        dto.setDate(e.getDate());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }
}
