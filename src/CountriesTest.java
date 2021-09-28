/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CountriesTest
{
	// Setup countries. As a wrapper class we can only use one copy
	static Countries countries;
	static Player testPlayer;
	@BeforeAll
	static void setup()
	{
		countries = Countries.getInstance();
		testPlayer = new Player(1,"Bob", Color.black, "Black", false, 10);
		countries.get(2).setOccupier(testPlayer); // NW Territory
		countries.get(21).setOccupier(testPlayer); // Yakutsk
	}

	@Test
	void testMultipleCountriesObject()
	{
		Countries otherCountries = Countries.getInstance();
		assertSame(countries, otherCountries);
	}

	@Test
	void asArrayAllCountriesAccountedFor()
	{
		Country[] arr = countries.asArray();
		assertEquals(Constants.NUM_COUNTRIES, arr.length);

		for (int i = 0; i < arr.length; i++)
		{
			assertEquals(i, arr[i].getId());
			assertEquals(Constants.COUNTRY_NAMES[i], arr[i].getName());
		}
	}

	@Test
	void asList()
	{
		ArrayList<Country> countriesList = countries.asList();
		assertEquals(Constants.NUM_COUNTRIES, countriesList.size());
		assertSame(countriesList.get(3), countries.get(3));
	}

	@Test
	void get()
	{
		assertEquals(5, countries.get(5).getId());
		assertEquals(1, countries.get(1).getId());
		assertEquals(0, countries.get(0).getId());
		assertEquals(41, countries.get(41).getId());
		try
		{
			countries.get(-1);
			fail("Allowed access to negative country");
		} catch (ArrayIndexOutOfBoundsException ex_) {}
		try
		{
			countries.get(Constants.NUM_COUNTRIES);
			fail("Allowed access to range outside array");
		} catch (ArrayIndexOutOfBoundsException ex_) {}


	}

	@Test
	void testGetCountriesInContinent()
	{
		ArrayList<Country> inContinent0 = countries.getCountriesInContinent(0);
		for (Country country : inContinent0)
		{
			assertEquals(0, country.getContinentID());
		}

		// Continent 0 is N.America should be 9 countries
		assertEquals(9, inContinent0.size());

		// should be size 0 if unknown continentID
		ArrayList<Country> notAnActualContinent = countries.getCountriesInContinent(42);
		assertEquals(0, notAnActualContinent.size());
	}

	@Test
	void testSelectCountriesByName()
	{
		String name = "s";
		ArrayList<Country> countriesList = countries.selectCountriesByName(name); //[S Europe, Scandinavia, Siam, Siberia, S Africa]
		assertEquals(5, countriesList.size());
		assertEquals("S Europe", countriesList.get(0).getName());
		assertEquals("Siam", countriesList.get(2).getName());

		name = "SIAM";
		countriesList = countries.selectCountriesByName(name); // [Siam]
		assertEquals(1,countriesList.size());
		assertEquals("Siam", countriesList.get(0).getName());

		name = "Not a real country test";
		countriesList = countries.selectCountriesByName(name); // []
		assertEquals(0, countriesList.size());

		// Test names with spaces
		name = "W uniTeD s";
		countriesList = countries.selectCountriesByName(name); // [W United States]
		assertEquals(1, countriesList.size());
		assertEquals("W United States", countriesList.get(0).getName());

	}

	@Test
	void testCountriesOccupiedBy()
	{
		ArrayList<Country> countriesOccupiedByTest = countries.countriesOccupiedBy(testPlayer);
		assertEquals(2, countriesOccupiedByTest.size());
		assertEquals("Yakutsk", countriesOccupiedByTest.get(1).getName());
	}

	@Test
	void testCountriesNotOccupiedBy()
	{
		ArrayList<Country> countriesNotOccupied = countries.countriesNotOccupiedBy(testPlayer);
		assertEquals(Constants.NUM_COUNTRIES - 2, countriesNotOccupied.size());
		for (Country country : countriesNotOccupied)
		{
			assertNotEquals("Yakutsk", country.getName());
		}
	}

	@Test
	void testNames()
	{
		ArrayList<String> names = countries.names(countries.asList());
		assertEquals(countries.asArray().length, names.size());
		assertEquals(names.get(3), countries.get(3).getName());
		assertEquals(names.get(6), countries.get(6).getName());
		assertEquals(names.get(22), countries.get(22).getName());
	}
}