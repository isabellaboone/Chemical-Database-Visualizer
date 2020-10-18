package model;

import java.util.ArrayList;
import java.util.List;
import dataDTO.ChemicalDTO;
import dataDTO.ElementCompoundDTO;
import dataENUM.ChemicalEnum;
import datasource.ChemicalRowDataGatewayRDS;
import datasource.ChemicalTableDataGatewayRDS;
import datasource.DatabaseException;
import datasource.ElementCompoundTableDataGatewayRDS;

/**
 * A mapper for Metal objects.
 * 
 * @author andrewjanuszko
 *
 */
public class MetalDataMapper implements MetalDataMapperInterface {

  private ChemicalTableDataGatewayRDS chemicalTableDataGateway;
  private ElementCompoundTableDataGatewayRDS ecTableDataGateway;

  /**
   * Empty constructor for MetalDataMapper.
   */
  public MetalDataMapper() {
    // EMPTY
  }

  /**
   * @see model.MetalDataMapperInterface#create(String, double, int, double,
   *      double).
   */
  @Override
  public Metal create(String name, double inventory, int atomicNumber, double atomicMass, double acidAmount)
      throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(ChemicalEnum.METAL.getIntValue(), name, inventory,
          atomicNumber, atomicMass, 0, acidAmount, 0);
      return new Metal(row.getID(), name, inventory, atomicNumber, atomicMass, acidAmount);
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to create Metal.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#read(int).
   */
  @Override
  public Metal read(int id) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(id);
      return new Metal(id, row.getName(), row.getInventory(), row.getAtomicNumber(), row.getAtomicMass(),
          row.getAcidAmount());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to read Metal with ID '" + id + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#update(Metal).
   */
  @Override
  public void update(Metal metal) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(metal.getID());
      row.setName(metal.getName());
      row.setInventory(metal.getInventory());
      row.setAtomicNumber(metal.getAtomicNumber());
      row.setAtomicMass(metal.getAtomicMass());
      row.setAcidAmount(metal.getAcidAmount());
      row.update();
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to update Metal with ID '" + metal.getID() + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#delete(Metal).
   */
  @Override
  public void delete(Metal metal) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(metal.getID());
      row.delete();
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to delete Metal with ID '" + metal.getID() + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#getAll().
   */
  @Override
  public ArrayList<Metal> getAll() throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to find all Metals.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByWildCardName(String).
   */
  @Override
  public ArrayList<Metal> filterByNameLike(String nameLike) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByNameLike(nameLike).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Metals with name '" + nameLike + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByInventory(double).
   */
  @Override
  public ArrayList<Metal> filterByInventory(double inventory) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByInventory(inventory).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Metals with inventory of '" + inventory + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByInventoryRange(double, double).
   */
  @Override
  public ArrayList<Metal> filterByInventoryBetween(double min, double max) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByInventoryBetween(min, max).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException(
          "Failed to get all Metals with inventory between '" + min + "' < x < '" + max + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByAtomicNumber(int).
   */
  @Override
  public ArrayList<Metal> filterByAtomicNumber(int atomicNumber) throws DomainModelException {
    try {
      return convertToMetal(
          chemicalTableDataGateway.getMetals().filterByAtomicNumber(atomicNumber).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Metals with atomic number of '" + atomicNumber + "'.", e);
    }
  }
  
  /**
   * 
   */
  @Override
  public List<Metal> filterByAtomicNumberBetween(int min, int max) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByAtomicNumberBetween(min, max).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException(
          "Failed to get all Metals with atomic number between '" + min + "' < x < '" + max + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByAtomicMass(double).
   */
  @Override
  public ArrayList<Metal> filterByAtomicMass(double atomicMass) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByAtomicMass(atomicMass).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Metals with atomic mass of '" + atomicMass + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByAtomicMassRange(double, double).
   */
  @Override
  public ArrayList<Metal> filterByAtomicMassBetween(double min, double max) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByAtomicMassBetween(min, max).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException(
          "Failed to get all Metals with atomic mass between '" + min + "' < x < '" + max + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByAcidRequired(double).
   */
  @Override
  public ArrayList<Metal> filterByAcidAmount(double acidAmount) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByAcidAmount(acidAmount).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Metals with an acid amount of '" + acidAmount + "'.", e);
    }
  }

  /**
   * @see model.MetalDataMapperInterface#filterByAcidRequiredRange(double,
   *      double).
   */
  @Override
  public ArrayList<Metal> filterByAcidAmountBetween(double min, double max) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByAcidAmountBetween(min, max).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException(
          "Failed to get all Metals with acid amount between '" + min + "' < x < '" + max + "'.", e);
    }
  }

  /**
   * 
   */
  @Override
  public ArrayList<Metal> filterByDissolvedBy(int acidID) throws DomainModelException {
    try {
      return convertToMetal(chemicalTableDataGateway.getMetals().filterByDissolvedBy(acidID).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to find Metals dissolved by ID '" + acidID + "'.", e);
    }
  }

  /**
   * 
   */
  @Override
  public List<Metal> filterByPartOfCompound(int compoundID) throws DomainModelException {
    try {
      List<ChemicalDTO> temp = new ArrayList<>();
      ElementCompoundDTO compound = ecTableDataGateway.findElementsByCompoundID(compoundID);
      for (ChemicalDTO element : compound.getRelations()) {
        if (element.getType() == ChemicalEnum.METAL.getIntValue()) {
          temp.add(element);
        }
      }
      return convertToMetal(temp);
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get Metals in Compound with ID '" + compoundID + "'.", e);
    }
  }

  /**
   * 
   * @param chemicals
   * @return
   */
  private ArrayList<Metal> convertToMetal(List<ChemicalDTO> chemicals) throws DomainModelException {
    ArrayList<Metal> metals = new ArrayList<Metal>();
    for (ChemicalDTO dto : chemicals) {
      metals.add(new Metal(dto.getID(), dto.getName(), dto.getInventory(), dto.getAtomicNumber(), dto.getAtomicMass(),
          dto.getAcidAmount()));
    }
    return metals;
  }

}
