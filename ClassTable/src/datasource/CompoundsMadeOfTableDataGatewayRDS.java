package datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompoundsMadeOfTableDataGatewayRDS implements CompoundsMadeOfTableDataGateway {

  private int compoundId;
  private List<Integer> madeOf; // Element ids
  private String name;
  private String inhabits;
  
  public CompoundsMadeOfTableDataGatewayRDS(int compoundId, List<Integer> madeOf, String name, String inhabits) {
    createTableCompoundMadeFrom();
    try {
      PreparedStatement insertChemical = DatabaseManager.getSingleton().getConnection()
        .prepareStatement("INSERT INTO Chemical (chemicalId, name, inhabits)" + "VALUES (?, ?, ?);");
      insertChemical.setInt(1, compoundId);
      insertChemical.setString(2, name);
      insertChemical.setString(3, inhabits);
      
      insertChemical.execute();
      
      for(int i = 0; i < madeOf.size(); i++) {
        PreparedStatement insert = DatabaseManager.getSingleton().getConnection()
            .prepareStatement("INSERT INTO CompoundMadeFromElement (compoundId, elementId)" + "VALUES (?, ?);");
        
        insert.setInt(1, compoundId);
        insert.setInt(2, madeOf.get(i));
        insert.execute();
      }
      
      this.compoundId = compoundId;
      this.madeOf = madeOf;
      this.name = name;
      this.inhabits = inhabits; 
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Problem inserting CompoundsMadeOfTableDataGatewayRDS");
    }
  }

  public CompoundsMadeOfTableDataGatewayRDS() {
    this.dropTableCompoundMadeFromElement();
    this.createTableCompoundMadeFrom();
  }
  
  public CompoundsMadeOfTableDataGatewayRDS(int compoundId) {
    this.createTableCompoundMadeFrom();
    String sql = "SELECT * FROM Chemical WHERE chemicalId = "
        + compoundId + ";"; 
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      
      rs.next();
      
      this.name = rs.getString("name");
      this.inhabits = rs.getString("inhabits");
      this.compoundId = compoundId;
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void createTableCompoundMadeFrom() {
    String createTable = "CREATE TABLE IF NOT EXISTS CompoundMadeFromElement(" + "compoundId INT NOT NULL, "
        + "elementId INT NOT NULL, " + "FOREIGN KEY (compoundId) REFERENCES Chemical(chemicalId), "
        + "FOREIGN KEY (elementId) REFERENCES Element(elementId));";

    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();

      // Drop the table if exists first
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      //
      statement.executeUpdate(createTable);

    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to create/drop CompoundMadeFromElement");
    }
  }

  @Override
  public void dropTableCompoundMadeFromElement() {
    String dropTable = "DROP TABLE IF EXISTS CompoundMadeFromElement";
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      statement.executeUpdate(dropTable);
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error dropping Compound table");
    }
  }

  /**
   * Get all compoundId's of elementId
   * 
   * @param elementId
   *          to search for
   */
  @Override
  public List<Integer> findSetCompoundId(int elementId) {
    String sql = "SELECT * FROM CompoundMadeFromElement WHERE elementId = " + elementId + ";";
    List<Integer> compounds = new ArrayList<>();
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      // While there are still results to search through
      while (rs.next()) {
        // Add each result to compound list
        compounds.add(rs.getInt("compoundId"));
      }
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
    return compounds;
  }

  /**
   * Get all elementId's of of compoundId
   * 
   * @param compoundId
   *          to search for
   */
  @Override
  public List<Integer> findSetElementId(int compoundId) {
    String sql = "SELECT * FROM CompoundMadeFromElement WHERE compoundId = " + compoundId + ";";
    List<Integer> compounds = new ArrayList<>();

    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      // While there are still results to search through
      while (rs.next()) {
        // Add each result to compound list
        compounds.add(rs.getInt("elementId"));
      }
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
    return compounds;
  }

  /**
   * Insert a given compoundId and elementId into the CompoundsMadeOfElement
   * table.
   * 
   * @param compoundId
   *          to insert
   * @param elementId
   *          to insert
   */
  @Override
  public void insert(int compoundId, int elementId) {
    try {

      PreparedStatement insert = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO CompoundMadeFromElement (compoundId, elementId)" + "VALUES (?, ?);");
      insert.setInt(1, compoundId); // Set compoundId
      insert.setInt(2, elementId); // Set elementId

      insert.execute(); // Insert into table

    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to insert compoundmadeof");
    }
  }

  /**
   * Get compound name from compoundId
   * 
   * @param compoundId
   *          compoundId of id searching for
   */
  @Override
  public String getCompoundName() {
    return this.name;
  }

  /**
   * Get inhabits from the Chemical table of a given chemicalId
   * 
   * @param chemicalId
   *          to search for
   */
  @Override
  public String getInhabits() {
    return this.inhabits;
  }

  @Override
  public void delete(int id) {
    try {
      PreparedStatement sql = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("DELETE FROM CompoundMadeFromElement WHERE compoundId = " + id + ";");
      sql.execute();
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
  }

}
