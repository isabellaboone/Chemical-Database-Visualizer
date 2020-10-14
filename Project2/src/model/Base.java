package model;

/**
 * Class for creating a Base.
 * 
 * @author andrewjanuszko & morgan williams-burrell
 */
public class Base extends Chemical {

  private int solute;

  /**
   * Constructor for creating a Base object.
   * 
   * @param id        the ID of the Base.
   * @param name      the name of the Base.
   * @param inventory the inventory of the Base.
   * @param solute    the Chemical that creates a solution.
   */
  public Base(int id, String name, double inventory, int solute) {
    super(id, name, inventory);
    setSolute(solute);
  }

  /**
   * Get the solute for the Base.
   * 
   * @return the solute for the Base.
   */
  public int getSolute() {
    return solute;
  }

  /**
   * Set the solute for the Base.
   * 
   * @param solute the solute for the Base.
   */
  public void setSolute(int solute) {
    this.solute = solute;
  }

}
