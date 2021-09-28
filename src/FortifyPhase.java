
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Handles the fortification phase of a players turn. First compiles a list of possible countries to fortify from then when one is selected
 * we use a method of walking from that country along any links we find belonging to that player to build up a list of countries they can
 * fortify from that country.
 * Main entry point is fortify method. Others are public for unit testing purposes.
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class FortifyPhase
{
	/**
	 * Main entry point of the class. Takes over control of the game until this phase is resolved.
	 *
	 * @param ui userInterface of the game. Allows for communication with player
	 * @param player active player
	 * @param countries the countries object
	 */
	public static void fortify(UserInterface ui, Player player, Countries countries)
	{
		// Store player name
		String pName = player.getName();


		// Check if the player has any valid countries to fortify from
		// get player's countries
		ArrayList<Country> playerCountries = countries.countriesOccupiedBy(player);

		// get list of countries player can fortify from (where at least one link is to another owned country and has more than one army)
		ArrayList<Country> options;
		options = countriesThatCanFortify(playerCountries);

		if (options.size() == 0)
		{
			ui.println("No valid fortification options. Skipping phase.");
			return;
		}

		// Ask player if they wish to fortify
		ui.println(pName + " would you like to fortify a country before passing turn? (Y/N)");
		boolean wantsToFortify = ui.getAffirmation();

		// If player doesn't wish to fortify, just return
		if (!wantsToFortify)
			return;

		// Ask player to choose country to fortify from
		ui.println("Enter country to fortify from:");
		ui.println(countries.names(options).toString());
		Country from = ui.getCountry(options);

		// Generate list of possible countries to fortify given selection
		options = countriesThanCanBeFortifiedFrom(from);

		// Ask player to enter country to fortify
		ui.println("(" + from.getName() + " -> ??) Enter country to fortify: ");
		ui.println(countries.names(options).toString());
		Country to = ui.getCountry(options);

		// Ask for number of troops to move
		int max = from.getNumOfArmies() - 1;
		ui.println(("(" + from.getName() + " -> " + to.getName() + ") Enter number of armies to move: [1 - " + max + "]"));
		int armiesMoving = ui.getNumber(1, max);

		// Handle moving armies and updating map
		ui.println("Fortifying " + to.getName() + " from " + from.getName() + "with " + armiesMoving + "armies.");
		from.updateNumOfArmies(-armiesMoving);
		to.updateNumOfArmies(armiesMoving);
		ui.repaintMap();
	}

	/**
	 * Builds a list of countries that can be fortified from a chosen country. It starts with the selected country and notes any countries it links to that
	 * belong to the same player. Then it visits the next country it noted and repeats this process adding countries if they aren't already in the list and
	 * they belong to the player. When it finishes it returns an array of all countries the player can make path to from the one passed in.
	 * @param from country to take fortifying troops out of
	 * @return list of all possible countries to fortify
	 */
	public static ArrayList<Country> countriesThanCanBeFortifiedFrom(Country from)
	{
		ArrayList<Country> options = new ArrayList<>();
		Countries countries = Countries.getInstance();
		int currentIndex = 0;

		// temporarily add from country to the list then step through the list adding as we go until we visit each country in the list
		// this will prevent other countries pointing back to the from country
		options.add(from);

		Player activePlayer = from.getOccupier();
		while (currentIndex < options.size())
		{
			// get all links from current index and add any that are valid to the list
			// to be valid they have to be occupied by the same player and not already in the list
			ArrayList<Country> links = countries.get(options.get(currentIndex).getLinks());

			// filters the links to a new list with only those that match the two requirements above
			ArrayList<Country> newDestinations = (ArrayList<Country>) links.stream().filter(country -> (country.getOccupier() == activePlayer && !options.contains(country))).collect(Collectors.toList());

			// add new destinations to options and visit next country
			options.addAll(newDestinations);
			currentIndex++;
		}
		// remove the from country as it shouldn't be an option to the player
		options.remove(from);

		return options;
	}

	/**
	 * Returns a list of all countries that can fortify from a list given to it. To qualify the country must link to at least one country with the same
	 * occupant and has more than one armies in it.
	 * @param initialList list of countries to filter down
	 * @return list of potential options to fortify from
	 */
	public static ArrayList<Country> countriesThatCanFortify(ArrayList<Country> initialList)
	{
		return (ArrayList<Country>) initialList.stream().filter(FortifyPhase::countryCanFortify).collect(Collectors.toList());
	}

	/**
	 * Returns true if the country has more than one army and links to at least one country with the same occupant
	 * @param country country to check
	 * @return true or false
	 */
	public static boolean countryCanFortify(Country country)
	{
		// Return false if country has only one army in it
		if (country.getNumOfArmies() == 1)
			return false;

		Countries countries = Countries.getInstance();
		// Get countries linked to from country
		ArrayList<Country> links = countries.get(country.getLinks());
		// Loop over each link and if any match owner return true
		for (Country test : links)
		{
			if (test.getOccupier() == country.getOccupier())
				return true;
		}
		// Otherwise false
		return false;
	}
}
