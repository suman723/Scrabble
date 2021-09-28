/*
 *	Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 * 	Country class will store all information about a single country.
 * 	Each country has a name, links to other countries, an occupier, number of armies in it,
 * 	as well as map coordinates and a continentID for the display
 */

import java.util.ArrayList;

/**
 * This class allows for the creation of a country object. Each country keeps track of all of its information for use when drawing the map
 * and when needed for game logic.
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class Country
{
	// Data
	private int id;			// Matches the country's index in the Constants
	private String name;
	// Each int will be an index that matches another country's index in the countries array. We can change this to an ArrayList of those countries if that is needed
	private int[] links;
	private Player occupier;
	private int numOfArmies;
	// mapCoordinate[0] will be the x-coordinate and mapCoordinate[1] will be the y-coordinate on the map
	private int[] mapCoordinate;
	private int continentID;

	/**
	 *	Constructor
	 *
	 * Sets up id, name, links, mapCoordinate and continentID. These are fixed at time of creation and shouldn't need to be changed
	 * so not setters are available.
	 * Initialises numOfArmies to 0 and occupier to NULL. These can be updated as game logic demands.
	 */
	public Country(int id, String name, int[] links, int[] mapCoordinate, int continentID)
	{
		this.id = id;
		this.name = name;
		this.links = links;
		this.mapCoordinate = mapCoordinate;
		this.continentID = continentID;
		occupier = null;
		numOfArmies = 0;
	}

	// Getters
	public int getId() {return id;}

	public String getName()
	{
		return name;
	}

	public int[] getLinks()
	{
		return links;
	}

	public int getNumOfArmies()
	{
		return numOfArmies;
	}

	public int[] getMapCoordinate()
	{
		return mapCoordinate;
	}

	public int getContinentID()
	{
		return continentID;
	}

	public Player getOccupier() { return occupier;}

	// Setters
	public void setNumOfArmies(int numOfArmies)
	{
		// Validate armies will not be negative
		if (numOfArmies < 0)
		{
			throw new IllegalArgumentException("Negative armies not allowed");
		}
		this.numOfArmies = numOfArmies;
	}

	public void setOccupier(Player player) {this.occupier = player; }

	public void updateNumOfArmies(int change)
	{
		// Validate armies will not be negative
		if (this.numOfArmies + change < 0)
		{
			throw new IllegalArgumentException("Negative armies not allowed");
		}
		// Update
		this.numOfArmies += change;
	}

	/**
	 * toString for debugging any details of the country.
	 *
	 * @return a string representation of the country's data
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ");
		sb.append(name);
		sb.append(" ID: ");
		sb.append(id);
		sb.append(" Links: {");
		for(int i = 0; i < links.length; i++)
		{
			sb.append(links[i]);
			sb.append(", ");
		}
		// Deletes the last comma
		sb.delete(sb.lastIndexOf(", "), sb.length());

		sb.append("} Map Coords: X: ");
		sb.append(mapCoordinate[0]);
		sb.append(" Y: ");
		sb.append(mapCoordinate[1]);
		sb.append(" ContinentID: ");
		sb.append(continentID);
		sb.append(" ");
		if (occupier == null)
		{
			sb.append("Unoccupied ");
		}
		else
		{
			sb.append("Occupied by: ");
			sb.append(occupier.getName());
		}
		sb.append(" Army count: ");
		sb.append(numOfArmies);

		return sb.toString();
	}

	// Debugging
//	public static void main(String[] args)
//	{
//		// Test a build of countries using constants. This will end up in the game class eventually I feel
//		Country[] countries = new Country[Constants.NUM_COUNTRIES];
//		for (int i = 0; i < Constants.NUM_COUNTRIES; i++)
//		{
//			countries[i] = new Country(i, Constants.COUNTRY_NAMES[i], Constants.ADJACENT[i], Constants.COUNTRY_COORD[i], Constants.CONTINENT_IDS[i] );
//		}
//		System.out.println(countries[0]);
//	}
}
