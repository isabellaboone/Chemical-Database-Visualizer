package model;

import java.util.ArrayList;
import java.util.List;

import dataDTO.ChemicalDTO;
import dataENUM.ChemicalEnum;
import datasource.ChemicalRowDataGatewayRDS;
import datasource.ChemicalTableDataGatewayRDS;
import datasource.DatabaseException;

/**
 * A mapper for Base objects.
 * 
 * @author andrewjanuszko
 *
 */
public class BaseDataMapper implements BaseDataMapperInterface {

  private ChemicalTableDataGatewayRDS chemicalTableDataGateway;

  /**
   * Empty constructor for BaseDataMapper.
   */
  public BaseDataMapper() {
    // EMPTY.
  }

  /**
   * @see model.BaseDataMapperInterface#create(String, double, int).
   */
  @Override
  public Base create(String name, double inventory, int solute) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(ChemicalEnum.BASE.getIntValue(), name, inventory, 0,
          0, 0, 0, solute);
      return new Base(row.getID(), name, inventory, solute);
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to create a Base.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#read(int).
   */
  @Override
  public Base read(int id) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(id);
      return new Base(row.getID(), row.getName(), row.getInventory(), row.getSolute());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to read a Base with ID '" + id + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#update(Base).
   */
  @Override
  public void update(Base base) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(base.getID());
      row.setName(base.getName());
      row.setInventory(base.getInventory());
      row.setSolute(base.getSolute());
      row.update();
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to update a Base with ID '" + base.getID() + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#delete(Base).
   */
  @Override
  public void delete(Base base) throws DomainModelException {
    try {
      ChemicalRowDataGatewayRDS row = new ChemicalRowDataGatewayRDS(base.getID());
      row.delete();
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to delete a Base with ID '" + base.getID() + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#getAll().
   */
  @Override
  public List<Base> getAll() throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBases().executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Bases.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#filterByWildCardName(String).
   */
  @Override
  public List<Base> filterByNameLike(String nameLike) throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBases().filterByNameLike(nameLike).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Bases with name '" + nameLike + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#filterByInventory(double).
   */
  @Override
  public List<Base> filterByInventory(double inventory) throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBases().filterByInventory(inventory).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Bases with inventory of '" + inventory + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#filterByInventoryRange(double, double).
   */
  @Override
  public List<Base> filterByInventoryBetween(double min, double max) throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBases().filterByInventoryBetween(min, max).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException(
          "Failed to get all Bases with inventory between '" + min + "' < x < '" + max + "'.", e);
    }
  }

  /**
   * @see model.BaseDataMapperInterface#filterBySolute(int).
   */
  @Override
  public List<Base> filterBySolute(int chemicalID) throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBases().filterBySolute(chemicalID).executeQuery());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Bases with solute '" + chemicalID + "'.", e);
    }
  }
  
  /**
   * @see model.BaseDataMapperInterface#filterByLowInventory().
   */
  @Override
  public List<Base> filterByLowInventory() throws DomainModelException {
    try {
      return convertToBase(chemicalTableDataGateway.getBasesWithLowInventory());
    } catch (DatabaseException e) {
      throw new DomainModelException("Failed to get all Bases with low inventory.", e);
    }
  }

  /**
   * Converts a ChemicalDTO to a Base.
   * 
   * @param chemicalDTOs the List of ChemicalDTO to convert.
   * @return a List of Bases.
   */
  private List<Base> convertToBase(List<ChemicalDTO> chemicals) throws DomainModelException {
    ArrayList<Base> bases = new ArrayList<Base>();
    for (ChemicalDTO chemical : chemicals) {
      bases.add(new Base(chemical.getID(), chemical.getName(), chemical.getInventory(), chemical.getSolute()));
    }
    return bases;
  }

}
