/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 * 
 * 
 */

import java.util.ArrayList;

public class PhaseOne {
	
	
	/**
	 * Public facing method which updates a players remaining armies
	 * @param activePlayer
	 * @param countries
	 */
	public static String calcReinforcements(Player activePlayer, Countries countries) {
		StringBuilder s = new StringBuilder();
		int a[] = calculateReinforcements(activePlayer,countries);
		activePlayer.updateRemainingArmies(a[1]+a[2]);
		s.append("*Receiving Reinforcements*\n");
		s.append("You are occupying " + a[0] + " countries.\n" +
					"You will get " + a[1] + " reinforcements.\n");
		if(a[2]>0) {
			s.append("You are occupying the following continents:\t");
			for(int i = 3;i<a.length;i++) {
				if(a[i]==1) {
					s.append(Constants.CONTINENT_NAMES[i-3] + " ");
				}
			}
			s.append("\nYour reinforcement bonus is: " + a[2] + "\nTotal reinforcements: " + (a[1]+a[2]) + "\n");
		}
		return s.toString();
	}
	
	/**
	 * Calculates Reinforcements based on active players owned countries
	 * @param activePlayer
	 * @param countries
	 * @return
	 */
	private static int[] calculateReinforcements(Player activePlayer, Countries countries) {
		int[] a = new int[9];
			// array values by position
			// 0 = number of countries owned by active player
			// 1 = the number of reinforcements gained based on owned countries
			// 2 = the number of bonus reinforcements if continents are occupied, 0 if none
			// 3 - 8 = 0 or 1 based on if the active player owns that particular continent
		    //         uses the same order as the Constants.CONTINENT_NAMES array
		ArrayList<Country> ownedCountries = countries.countriesOccupiedBy(activePlayer);
		int owned = ownedCountries.size(); 
		a[0] = owned; // set a0 to the number of owned countries
		int r = owned/3; 
		if(r<3) r = 3;
		a[1] = r; // set a1 to the number of reinforcement based on owned countries only
		r += ownsContinentBonus(ownedCountries);
		a[2] = r-a[1]; // set a2 to the number of reinforcements gained from the continent bonus
		if(a[2]>0) { // if at least one continent is owned, set the according value to 1
			if(ownsNAmerica(ownedCountries)) { a[3] = 1; }
			if(ownsEurope(ownedCountries)) { a[4] = 1; }
			if(ownsAsia(ownedCountries)) { a[5] = 1; }
			if(ownsAustralia(ownedCountries)) { a[6] = 1; }
			if(ownsSAmerica(ownedCountries)) { a[7] = 1; }
			if(ownsAfrica(ownedCountries)) { a[8] = 1; }
		}
		return a;
	}
	
	/**
	 * ownsContintentBonus calculates the total bonus for occupying a continent
	 * @param ownedCountries
	 * @return int bonus
	 */
	private static int ownsContinentBonus(ArrayList<Country> ownedCountries) {
		int bonus = 0,count = 0; // set initial bonus and count values
		
		// loop through each continent id
		for(int id=0;id<6;id++) {
			// loop through each owned country
			for(int i=0;i<ownedCountries.size();i++)
				// increment counter if the country id matches
				if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == id) count++;
			
			// if the counter matches the continent size, increase bonus
			if(count == Constants.CONTINENT_SIZES[id]) 
				bonus += Constants.CONTINENT_VALUES[id];
			
			// reset counter
			count = 0;
		}
		
		// return the bonus
		return bonus;
	}
	
	
	// these were my original methods to determine country ownership and therefore
	// determine the bonus reinforcements received... however I combined them into
	// a single method above... however the following methods may prove useful in 
	// the future so I am not deleting them as of right now.
	public static boolean ownsNAmerica(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 0) count++;
		}
		return count == Constants.CONTINENT_SIZES[0];
	}
	
	public static boolean ownsEurope(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 1) count++;
		}
		return count == Constants.CONTINENT_SIZES[1];
	}
	
	public static boolean ownsAsia(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 2) count++;
		}
		return count == Constants.CONTINENT_SIZES[2];
	}
	
	public static boolean ownsAustralia(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 3) count++;
		}
		return count == Constants.CONTINENT_SIZES[3];
	}

	public static boolean ownsSAmerica(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 4) count++;
		}
		return count == Constants.CONTINENT_SIZES[4];
	}
	
	public static boolean ownsAfrica(ArrayList<Country> ownedCountries) {
		int count = 0;
		for(int i=0;i<ownedCountries.size();i++) {
			if(Constants.CONTINENT_IDS[ownedCountries.get(i).getId()] == 5) count++;
		}
		return count == Constants.CONTINENT_SIZES[5];
	}

}
