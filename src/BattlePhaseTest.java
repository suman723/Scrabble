import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BattlePhase class
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

import java.awt.*;
import java.util.ArrayList;

class BattlePhaseTest
{
	static Countries countries;
	static Player mainPlayer;
	static Player enemy;

	/**
	 * Setup all countries and 2 players for the testing
	 */
	@BeforeAll
	static void instantiateCountriesAndPlayers()
	{
		countries = Countries.getInstance();
		mainPlayer = new Player(0, "Player1", Color.black, "Black", true, 0);
		enemy = new Player(1, "Enemy", Color.BLUE, "Blue", true, 0);
	}

	/**
	 * Assigns all countries back to enemy controlled before each test
	 */
	@BeforeEach
	void setAllCountriesToEnemy()
	{
		ArrayList<Country> all = countries.asList();
		for (Country c : all)
		{
			c.updateNumOfArmies(1);
			c.setOccupier(enemy);
		}
	}

	@Test
	void testGetValidAttackCountries()
	{
		ArrayList<Country> options;
		options = BattlePhase.getValidAttackCountries(mainPlayer);  // should be 0 (all countries owned by enemy)
		assertEquals(0, options.size());

		// Add some countries for main player
		countries.get(0).setOccupier(mainPlayer);		// cannot attack from country with 1 army
		countries.get(0).setNumOfArmies(1);

		countries.get(1).setOccupier(mainPlayer); 		// should be able to attack from here
		countries.get(1).setNumOfArmies(5);

		countries.get(19).setOccupier(mainPlayer);		// only has links to [22,26] which we are going to give main player, so not valid
		countries.get(19).setNumOfArmies(5);
		countries.get(22).setOccupier(mainPlayer);
		countries.get(22).setNumOfArmies(1);

		/* mid point 19 should be valid */
		assertTrue(BattlePhase.getValidAttackCountries(mainPlayer).contains(countries.get(19)));
		assertEquals(2, BattlePhase.getValidAttackCountries(mainPlayer).size());

		countries.get(26).setOccupier(mainPlayer);
		countries.get(26).setNumOfArmies(1);

		/* should be false now */
		assertFalse(BattlePhase.getValidAttackCountries(mainPlayer).contains(countries.get(19)));

		// Only country[1] should be a valid attack option
		assertEquals(1, BattlePhase.getValidAttackCountries(mainPlayer).size());
		assertEquals(countries.get(1), BattlePhase.getValidAttackCountries(mainPlayer).get(0));
	}

	@Test
	void testInvadableCountries()
	{
		ArrayList<Country> invadables;
		Country siam = countries.get(23);		// Has 3 links to other countries [17, 27, 31]
		// all links are owned by enemy as well as Siam itself so list should be size 0
		invadables = BattlePhase.invadableCountries(siam);
		assertEquals(0, invadables.size());

		// hand over control to main player and all 3 links should be invadable
		siam.setOccupier(mainPlayer);
		invadables = BattlePhase.invadableCountries(siam);
		assertEquals(3, invadables.size());
		// check India (17) is in list
		Country india = countries.get(17);
		assertTrue(invadables.contains(india));

		// give player control of india (17) and check list decreases and it is no longer in the list
		india.setOccupier(mainPlayer);
		invadables = BattlePhase.invadableCountries(siam);
		assertEquals(2, invadables.size());
		// check India (17) is NOT in list
		assertFalse(invadables.contains(india));
	}

	@Test
	void testCountryCanInvadeAnother()
	{
		Country alaska = countries.get(8);		// Links [2,3,22]
		// currently all owned by enemy so should not be able to invade
		assertFalse(BattlePhase.countryCanInvadeAnother(alaska));

		// change ownership to player
		alaska.setOccupier(mainPlayer);
		// should be able to attack
		assertTrue(BattlePhase.countryCanInvadeAnother(alaska));

	}

}