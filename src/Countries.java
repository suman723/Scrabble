import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a singleton class that constructs a Countries object which wraps around the countries array used in game.
 * This allows for multiple objects to access the array without passing it around and allows us to implement additional
 * methods that filter or search the array in someway.
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */
public class Countries
{
	private static Countries single_instance = null;
	private Country[] countriesArray;

	/**
	 * Private constructor. To get a Countries object use Countries.getInstance() instead.
	 * This creates the initial array of countries from the Constants.java class.
	 */
	private Countries()
	{
		countriesArray = new Country[Constants.NUM_COUNTRIES];
		for (int i = 0; i < Constants.NUM_COUNTRIES; i++)
		{
			countriesArray[i] = new Country(i, Constants.COUNTRY_NAMES[i], Constants.ADJACENT[i], Constants.COUNTRY_COORD[i], Constants.CONTINENT_IDS[i] );
		}
	}

	/**
	 * Returns the instance of the Countries class object if already created. Otherwise it creates a new instance.
	 *
	 * @return Countries object
	 */
	public static Countries getInstance()
	{
		if (single_instance == null)
			single_instance = new Countries();

		return single_instance;
	}

	/**
	 * Returns the internal array if you need to work on it directly.
	 *
	 * @return CountryArray
	 */
	public Country[] asArray()
	{
		return countriesArray;
	}

	/**
	 * Returns the internal array as an arrayList if needed. Useful for making sub lists
	 *
	 * @return Country ArrayList
	 */
	public ArrayList<Country> asList() {return new ArrayList<>(Arrays.asList(countriesArray)); }

	/**
	 * Returns the country at index i.
	 * Throws index exception if i < 0 or i >= size
	 *
	 * @param i	the index of the country to return
	 * @return	the country at index i
	 */
	public Country get(int i)
	{
		if (i < 0 || i >= countriesArray.length)
			throw new ArrayIndexOutOfBoundsException("Tried to access country out of range " + i);

		return countriesArray[i];
	}

	/**
	 * Returns an ArrayList of all countries with idexes that match the elements of the ids array
	 *
	 * @param ids array of ids of desired countries
	 * @return Arraylist of countries
	 */
	public ArrayList<Country> get(int[] ids)
	{
		ArrayList<Country> matches = new ArrayList<>();
		for (int i = 0; i < ids.length; i++)
		{
			// Adds the country with the index stored in the ith element of the ids array
			matches.add(countriesArray[ids[i]]);
		}
		return matches;
	}

	/**
	 * Returns an ArrayList of all country objects within a specific continent.
	 * @param id	the id of the continent required
	 * @return		ArrayList of countries
	 */
	public ArrayList<Country> getCountriesInContinent(int id)
	{
		// Calls the version below with all countries as selection
		return getCountriesInContinent(id, asList());
	}

	public ArrayList<Country> getCountriesInContinent(int id, ArrayList<Country> selection)
	{
		// List to return
		ArrayList<Country> ret = new ArrayList<>();
		// Step through each country in selection and check if it matches the continent id, if so add it to list
		for (Country country : selection)
		{
			if (country.getContinentID() == id)
				ret.add(country);
		}
		return ret;
	}

	/**
	 * Returns an arrayList of countries from options that begin with string name
	 *
	 * @param name search term for country's name
	 * @param options arrayList of potential choices
	 * @return arrayList of all matches
	 */
	public ArrayList<Country> selectCountriesByName(String name, ArrayList<Country> options)
	{
		ArrayList<Country> matches = new ArrayList<>();
		for (Country country : options)
		{
			String cname = country.getName().toLowerCase();
			String lSearch = name.toLowerCase();

			// match if the country name starts with the search string
			if (cname.indexOf(lSearch) == 0)
			{
				matches.add(country);
			}
		}
		return matches;
	}

	/**
	 * Searches all countries for any that start with given name
	 *
	 * @param name name of country to search
	 * @return list of all countries that begin with name
	 */
	public ArrayList<Country> selectCountriesByName(String name)
	{
		return selectCountriesByName(name, asList());
	}

	/**
	 * Filters a list countries to only include the ones occupied by a given player
	 *
	 * @param player player object to filter by
	 * @param options arrayList of country objects
	 * @return filtered arraylist
	 */
	public ArrayList<Country> countriesOccupiedBy(Player player, ArrayList<Country> options)
	{
		ArrayList<Country> owned = new ArrayList<>();
		for (Country country : options)
		{
			if (country.getOccupier() == player)
				owned.add(country);
		}
		return owned;
	}

	/**
	 * Overloaded version of above. Allows caller to not specify options. All countries will be assumed as options
	 *
	 * @param player player object to filter by
	 * @return filtered arrayList
	 */
	public ArrayList<Country> countriesOccupiedBy(Player player)
	{
		// Calls overloaded method with all countries in the countries array as a list
		return countriesOccupiedBy(player, asList());
	}

	/**
	 * Filters a list of countries to EXCLUDE those occupied by the player specified
	 *
	 * @param player player object to filter by
	 * @param options arrayList of potential countries
	 * @return filtered arrayList
	 */
	public ArrayList<Country> countriesNotOccupiedBy(Player player, ArrayList<Country> options)
	{
		ArrayList<Country> notOwned = new ArrayList<>();
		for (Country country : options)
		{
			if (country.getOccupier() != player)
			{
				notOwned.add(country);
			}
		}
		return notOwned;
	}

	/**
	 * Overloaded version of above
	 * Allows caller to not specify countries. All countries will be assumed as potentials
	 *
	 * @param player player object to filter by
	 * @return filtered arrayList
	 */
	public ArrayList<Country> countriesNotOccupiedBy(Player player)
	{
		// Calls overloaded method with all countries in the countries array as a list
		return countriesNotOccupiedBy(player, asList());
	}


	/**
	 * For debugging. Returns a string made of all the countries toString() methods.
	 *
	 * @return String of all countries info
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < countriesArray.length; i++)
		{
			sb.append(countriesArray[i]);
		}
		return sb.toString();
	}

	/**
	 * Returns a smaller list of just country names
	 *
	 * @param countryList list of countries names required by caller
	 * @return list of country names
	 */
	public ArrayList<String> names(ArrayList<Country> countryList)
	{
		ArrayList<String> names = new ArrayList<>();
		for (Country country : countryList)
		{
			names.add(country.getName());
		}
		return names;
	}
	// Debugging
	public static void main(String[] args)
	{
		Countries countries = Countries.getInstance();
		System.out.println(countries);
		//System.out.println(countries.getCountriesInContinent(0));
		ArrayList<Country> si = countries.selectCountriesByName("si");
		System.out.println("Printing countries beginning with Si");
		for (Country c : si)
		{
			System.out.println(c);
		}
	}

}
