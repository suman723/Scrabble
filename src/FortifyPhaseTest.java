import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.IconUIResource;
import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for Fortify
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

class FortifyPhaseTest
{
	static Countries countries;
	static Player active;
	static Player enemy;

	@BeforeAll
	static void setup()
	{
		// Setup initial countries and players
		countries = Countries.getInstance();
		active = new Player(1,"Active", Color.BLUE, "Blue", true, 0);
		enemy = new Player(2, "Enemy", Color.BLACK, "Black", true, 0);

		// initially set all countries owned by enemy and all have one troop
		for (Country c : countries.asArray())
		{
			c.setOccupier(enemy);
			c.updateNumOfArmies(1);
		}

		// Give active player an initial list of countries
		int[] activesCountries = {
				35,			// Argentina
				34,			// Brazil
				32,			// Venezuela
				7,			// C. America
				41,			// Madagascar
				28,			// E Australia
				30, 		// W Australia
		};

		for (int id : activesCountries)
		{
			countries.get(id).setOccupier(active);
		}

		// add troops to some of the active players countries
		countries.get(35).setNumOfArmies(5);		// Argentina
		countries.get(32).setNumOfArmies(2);		// Venezuela
		countries.get(7).setNumOfArmies(3);			// C America
		countries.get(41).setNumOfArmies(9);		// Madagascar
	}

	@Test
	void testCountriesThanCanBeFortifiedFrom()
	{
		// Testing with Argentina (should be able to fortify Brazil, Venezuela and C America
		Country argentina = countries.get(35);
		ArrayList<Country> options = FortifyPhase.countriesThanCanBeFortifiedFrom(argentina);

		// list should contain 3 countries
		assertEquals(3, options.size());

		// check correct 3
		assertTrue(options.contains(countries.get(34)));		// Brazil
		assertTrue(options.contains(countries.get(32)));		// Venezuela
		assertTrue(options.contains(countries.get(7)));			// C America

		// Test with Madagascar which should return empty list
		Country madagascar = countries.get(41);
		options = FortifyPhase.countriesThanCanBeFortifiedFrom(madagascar);

		assertEquals(0, options.size());

	}

	@Test
	void testCountriesThatCanFortify()
	{
		// Grab all the players countries
		ArrayList<Country> testList = countries.countriesOccupiedBy(active);

		// Get the list that can fortify
		// Can: Argentina, Venezuela, C America
		// Cant: Brazil (1 army) W australia (1 army) E australia (1 army) Madagascar (No links)
		ArrayList<Country> result = FortifyPhase.countriesThatCanFortify(testList);

		assertEquals(3, result.size());
		assertTrue(testList.contains(countries.get(35)));		// Argentina
		assertTrue(testList.contains(countries.get(32)));		// Venezuela
		assertTrue(testList.contains(countries.get(7)));		// C America

		// Empty list should return empty list
		result = FortifyPhase.countriesThatCanFortify(new ArrayList<>());
		assertEquals(0, result.size());
	}

	@Test
	void testCountryCanFortify()
	{
		assertTrue(FortifyPhase.countryCanFortify(countries.get(35)));
		assertFalse(FortifyPhase.countryCanFortify(countries.get(41)));
	}
}