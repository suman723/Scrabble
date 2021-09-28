/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class CountryTest
{
	Country basicTestCountry;

	@BeforeEach
	void setup()
	{
		int[] links = {2,3,4};
		int[] coord = {100,250};
		basicTestCountry = new Country(1, "TestCountry", links , coord, 2 );
	}

	// Getters

	@Test
	void testGetID()
	{
		assertEquals(1,basicTestCountry.getId());
	}

	@Test
	void testGetName()
	{
		assertEquals("TestCountry", basicTestCountry.getName());
	}

	@Test
	void testGetLinks()
	{
		int [] expectedLinks = {2,3,4};
		for (int i = 0; i < expectedLinks.length; i++)
		{
			assertEquals(expectedLinks[i], basicTestCountry.getLinks()[i]);
		}
	}

	@Test
	void testGetMapCoordinates()
	{
		int [] expectedCords = {100, 250};
		assertEquals(expectedCords[0], basicTestCountry.getMapCoordinate()[0]);
		assertEquals(expectedCords[1], basicTestCountry.getMapCoordinate()[1]);
	}

	@Test
	void testGetContinentId()
	{
		assertEquals(2, basicTestCountry.getContinentID());
	}

	@Test
	void testGetNumArmies()
	{
		assertEquals(0, basicTestCountry.getNumOfArmies());
	}

	@Test
	void testGetOccupier()
	{
		assertEquals(null, basicTestCountry.getOccupier());
	}

	// Setters

	@Test
	void testSetNumArmies()
	{
		basicTestCountry.setNumOfArmies(100);
		assertEquals(100, basicTestCountry.getNumOfArmies());
		basicTestCountry.setNumOfArmies(0);
		assertEquals(0, basicTestCountry.getNumOfArmies());

		try
		{
			basicTestCountry.setNumOfArmies(-1);
			fail("Should not allow negative army count");
		} catch (IllegalArgumentException ex) {}
	}

	@Test
	void testSetOccupier()
	{
		Player test = new Player(1, "Kang", Color.BLACK, "Black",  false, 10);
		basicTestCountry.setOccupier(test);
		assertEquals(test, basicTestCountry.getOccupier());

		Player test2 = new Player(2, "Kodos", Color.GREEN, "Green", false, 22);
		basicTestCountry.setOccupier(test2);
		assertEquals(test2, basicTestCountry.getOccupier());
		assertNotEquals(test, basicTestCountry.getOccupier());
	}

	@Test
	void testUpdateNumOfArmies()
	{
		basicTestCountry.setNumOfArmies(100);
		assertEquals(100, basicTestCountry.getNumOfArmies());

		basicTestCountry.updateNumOfArmies(50);
		assertEquals(150, basicTestCountry.getNumOfArmies());

		basicTestCountry.updateNumOfArmies(-140);
		assertEquals(10, basicTestCountry.getNumOfArmies());

		try
		{
			basicTestCountry.updateNumOfArmies(-11); // Sets to total to -1 which should error
			fail("Negative army count allowed");
		} catch (IllegalArgumentException ex) {}


	}
}
