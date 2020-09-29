package datasource;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataDTO.CompoundDTO;
import dataDTO.ElementDTO;

public abstract class CompoundElementTDGRDSTest extends DatabaseTest {
	
	protected CompoundElementTDG gateway;
	
	/**
	 * Gets a singleton.
	 */
	protected abstract CompoundElementTDG getSingletonInstance();
	

	/**
	 * Fills the database with entries to test on.
	 * @throws DatabaseException when things go wrong.
	 */
	@BeforeEach
	void fillDatabase() throws DatabaseException {
		@SuppressWarnings("unused")
		ChemicalRDGRDS chemicalTable = new ChemicalRDGRDS();
		gateway = getSingletonInstance();
		//Inserting 3 elements to make up the compounds
		chemicalTable = new ChemicalRDGRDS(1, "Carbon", "Earth", 6, 12.011, -1, -1);
		chemicalTable = new ChemicalRDGRDS(1, "Oxygen", "Earth", 8, 15.999, -1, -1);
		chemicalTable = new ChemicalRDGRDS(1, "Hydrogen", "Earth", 1, 1.007, -1, -1);
		//Inserting 2 compounds
		chemicalTable = new ChemicalRDGRDS(3, "Water", "Earth", -1, -1, -1, -1);
		chemicalTable = new ChemicalRDGRDS(3, "Carbon Monoxide", "Earth", -1, -1, -1, -1);
		// water is made of hydrogen and oxygen
		gateway.createRow(4, 2);
		gateway.createRow(4, 3);
		// carbon monoxide is made of carbon and oxygen
		gateway.createRow(5, 1);
		gateway.createRow(5, 2);
		// NOTE: might want to make a createRow() that takes in an array
	}

	/**
	 * resets the singleton after every test
	 * @throws DatabaseException
	 */
	@AfterEach
	void resetTable() throws DatabaseException {
		gateway.resetData();
	}
	
	/**
	 * Tests if the gateway is a singleton.
	 */
	@Test
	public void isASingleton()
	{
		CompoundElementTDG x = getSingletonInstance();
		CompoundElementTDG y = getSingletonInstance();
		assertSame(x, y);
		assertNotNull(x);
	}
	
	/**
	 * USES ALTERNATE IMPLEMENTATION
	 * Tests the ability to find a compound by giving an element it is made up of 
	 * @throws DatabaseException
	 */
	@Test
	void testGetCompoundsFromElementID() throws DatabaseException {
		// finds compounds the are oxygen in them
		ElementDTO element = gateway.findCompoundsByElementID(2);
		// checks to make sure that both are gotten and that they are correct
		assertEquals(2, element.partOf().size());
		assertEquals(4, element.partOf().get(0).intValue());
		assertEquals(5, element.partOf().get(1).intValue());
	}
	
	/**
	 * USES ALTERNATE IMPLEMENTATION
	 * Tests the ability to find a compound by giving an compound it is made up of 
	 * @throws DatabaseException
	 */
	@Test
	void testGetElementsFromCompoundID() throws DatabaseException {
		// finds carbon Monoxide
		CompoundDTO compounds = gateway.findElementsByCompoundID(5);
		// checks to make sure that both are gotten and that they are correct
		assertEquals(2, compounds.madeOf().size());
		assertEquals(1, compounds.madeOf().get(0).intValue());
		assertEquals(2, compounds.madeOf().get(1).intValue());
		// NOTE: because it is made of two elements it has to rows this might be a problem
	}
	
	/**
	 * Tests the ability to update a row.
	 * @throws DatabaseException
	 */
	@Test
	void testUpdateCompoundCompoundDTO() throws DatabaseException {
		CompoundDTO compound = gateway.findElementsByCompoundID(5);
		
		assertEquals(5, compound.getCompoundID());
		assertEquals(1, compound.madeOf().get(0).intValue());
		assertEquals(2, compound.madeOf().get(1).intValue());
		
		gateway.updateRow(5, 1, 5, 3);
		
		compound = gateway.findElementsByCompoundID(5);
		
		assertEquals(5, compound.getCompoundID());
		assertEquals(2, compound.madeOf().get(0).intValue());
		assertEquals(3, compound.madeOf().get(1).intValue());
	}
	
	/**
	 * Tests the ability to update a row.
	 * @throws DatabaseException
	 */
	@Test
	void testUpdateCompoundElementInCompoundsDTO() throws DatabaseException {
		ElementDTO element = gateway.findCompoundsByElementID(2);
		
		assertEquals(2, element.getElementID());
		assertEquals(2, element.partOf().size());
		assertEquals(4, element.partOf().get(0).intValue());
		assertEquals(5, element.partOf().get(1).intValue());
		
		gateway.updateRow(4, 2, 4, 1);
		
		element = gateway.findCompoundsByElementID(2);
		assertEquals(1, element.partOf().size());
		assertEquals(2, element.getElementID());
		assertEquals(5, element.partOf().get(0).intValue());
	}
	
	/**
	 * Tests the ability to delete a row.
	 * @throws DatabaseException
	 */
	@Test
	void testDelete() throws DatabaseException {
		ElementDTO element = gateway.findCompoundsByElementID(2);
		
		assertEquals(2, element.getElementID());
		assertEquals(2, element.partOf().size());
		assertEquals(4, element.partOf().get(0).intValue());
		assertEquals(5, element.partOf().get(1).intValue());
		
		gateway.delete(4, 2);
		
		element = gateway.findCompoundsByElementID(2);
		assertEquals(1, element.partOf().size());
		assertEquals(2, element.getElementID());
		assertEquals(5, element.partOf().get(0).intValue());
	}
}