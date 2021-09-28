import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Creates bot for RiskGame
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */


public class DasKlos implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example

	private BoardAPI board;
	private PlayerAPI player;
	private final int opponent;
	private String lastCommand;
	private int commandCounter = 0;  // should not be needed, but helpful when testing. It stops BOT from infinite loops
	// Adjustable aggressiveness of bot
	private static final int MINIMUM_ATTACKING_ARMIES = 7;
	private static final int MINIMUM_ATTACK_GAP = 2;

	// array declaration of neighbour, own , opponent and neutral
	private ArrayList<Integer> neighbours;
	private ArrayList<Integer> myCountries;
	private ArrayList<Integer> opponentCountries;
	private ArrayList<Integer> neutralCountries;

	DasKlos(BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;
		player = inPlayer;
		// put your code here
		opponent = (player.getId() == 0) ? 1 : 0;	// If we are bot0 opponent is bot1 and vice versa
		neighbours = new ArrayList<>();
		myCountries = new ArrayList<>();
		opponentCountries = new ArrayList<>();
		neutralCountries = new ArrayList<>();
	}

	/**
	 * When called returns our bots name
	 *
	 * @return bot name
	 */
	public String getName () {
		String command = "";
		// put your code here
		command = "DasKlos";
		return(command);
	}

	/**
	 * Returns country to reinforce and units.
	 * Tactically attempts to pile all troops onto one aggro country to attack from
	 * Tries to pick a country that neighbours an opponent's one
	 *
	 * @return
	 */
	public String getReinforcement () {
		String command = "";
		// put your code here
		command = getBotsCountryToReinforce();
		command = command.replaceAll("\\s", "");
		command += " ";
		command += player.getNumUnits();
		return(command);
	}

	/**
	 * Calls our placement strategy for the player, generally trying to reinforce countries next
	 * to opponent's country.
	 *
	 * @param forPlayer
	 * @return
	 */
	public String getPlacement (int forPlayer) {
		String command = "";
		// put your code here
		command = placementStrategy(forPlayer);
		return(command);
	}

	/**
	 * Bot tries to exchange if it can
	 * @return
	 */
	public String getCardExchange () {
		String command = "";
		// put your code here
		
		if(player.isForcedExchange() || canTrade()) {
			return changeCardExchange();
		}
		command = "skip";
		return(command);
	}

	/**
	 * Bot tries to attack from the tall country with all the armies it can until it has less armies than required by
	 * the constants declared at the top. Tries to attack opponent's country if possible.
	 * @return
	 */
	public String getBattle () {
		String command = "";
		// put your code here
		int countryToAttackFrom = getMostUnitsInCountryByID();
		int neighbourToAttack = getNeighbourToAttack(countryToAttackFrom);
		// no attacks
		if (isStoppingAttack(countryToAttackFrom,neighbourToAttack))
		{
			return "skip";
		}

		// Try and attack with the maximum units
		int attackUnits = board.getNumUnits(countryToAttackFrom);

		String from = "";
		String to = "";
		String with = "";

		switch(attackUnits) {
			case 1: return "skip";          
			case 2: with += "1"; break;
			case 3: with += "2"; break;
			default: with += "3"; break;
		}
		
		from += GameData.COUNTRY_NAMES[countryToAttackFrom];
		to = GameData.COUNTRY_NAMES[neighbourToAttack];
		command = convertToCommand(from,to,with);
		command = commandBreaker(command);		// shouldn't be needed but protects from errors
		return(command);
	}
	
	/**
	 * Defense Strategy
	 * always use 2 units to defend unless it is not available
	 */
	public String getDefence (int countryId) {
		String command = "";
		// put your code here
		if(board.getNumUnits(countryId)>1) {
			command = "2";
		} else {
			command = "1";
		}
		return(command);
	}
	
	/**
	 * Movement Strategy
	 * move all available unit to invaded country
	 */
	public String getMoveIn (int attackCountryId) {
		StringBuilder command = new StringBuilder();
		// put your code here
		command.append(board.getNumUnits(attackCountryId)-1);
		return(command.toString());
	}

	/**
	 * In continuing with our go tall strategy the bot tries to reinforce our tall country if it is still connected to an opponent
	 * country. If it doesn't have an opponent country it tries to find a linked one that does to move all troops to it
	 *
	 * @return
	 */
	public String getFortify () {
		String command = "";
		// put code here
		command = fortifyBot();
		command = commandBreaker(command);
		return(command);
	}

	// privates

	/**
	 * Filters the countries by playerID
	 *
	 * @param playerID
	 * @return arraylist of countries
	 */
	private ArrayList<Integer> countryIDsOwnedByPlayer(int playerID) {
		ArrayList<Integer> countries = new ArrayList<>();

		for(int i = 0;i<GameData.NUM_COUNTRIES;i++) {
			if (board.getOccupier(i) == playerID)
				countries.add(i);
		}
		return countries;
	}

	/**
	 *
	 * @param countryToAttackFrom
	 * @return
	 */
	private int getNeighbourToAttack(int countryToAttackFrom) {

		// get neighbouring countries
		ArrayList<Integer> neighbours = neighbourCountries(countryToAttackFrom);
		// remove player country from neighbours
		neighbours = filterCountriesWithoutPlayer(player.getId(), neighbours);

		// this list will only have neighbouring countries owned by opponent
		ArrayList<Integer> filterOutNeutrals = filterCountriesByPlayer(opponent,neighbours);

		// Pick the weakest country, prioritise opponent over neutral if we can
		if(filterOutNeutrals.isEmpty()) {
			return getWeakestNeighbour(neighbours);
		} else {
			return getWeakestNeighbour(filterOutNeutrals);
		}

	}

	/**
	 * Given an array of neighbouring countries, returns the country id of least occupied neighbour
	 * @param neighbours
	 * @return
	 */
	private int getWeakestNeighbour(ArrayList<Integer> neighbours) {
		//edge case if passed in is empty
		if (neighbours.size() == 0) return -1;

		int neighbour = neighbours.get(0);
		int count = 999;
		for(int i=0;i<neighbours.size();i++) {
			if(count>board.getNumUnits(neighbours.get(i))) {
				neighbour = neighbours.get(i);

				//update count
				count = board.getNumUnits(neighbours.get(i));
			}
		}
		return neighbour;
	}

	/**
	 * Returns an arraylist of all countries linked to passed in country
	 * @param country
	 * @return
	 */
	private ArrayList<Integer> neighbourCountries(int country)
	{
		ArrayList<Integer> neighbours = new ArrayList<>();
		for(int i = 0;i<GameData.NUM_COUNTRIES;i++) {
			if (board.isAdjacent(country, i))
				neighbours.add(i);
		}
		return neighbours;

	}

	/**
	 * Takes a list of countries and a player. Builds and returns a new list without any of those countries that are
	 * owned by that player.
	 *
	 * @param playerID
	 * @param countries
	 * @return
	 */
	private ArrayList<Integer> filterCountriesWithoutPlayer(int playerID, ArrayList<Integer> countries)
	{
		ArrayList<Integer> filteredCountries = new ArrayList<>();

		for (Integer country : countries)
		{
			if (board.getOccupier(country) != playerID)
				filteredCountries.add(country);
		}
		return filteredCountries;
	}

	/**
	 * Takes a list of countries and a player and builds and returns a new list of only countries in the passed in list that
	 * belongs to that player.
	 * @param playerID
	 * @param countries
	 * @return
	 */
	private ArrayList<Integer> filterCountriesByPlayer(int playerID, ArrayList<Integer> countries)
	{
		ArrayList<Integer> filteredCountries = new ArrayList<>();

		for (Integer country : countries)
		{
			if (board.getOccupier(country) == playerID)
				filteredCountries.add(country);
		}
		return filteredCountries;
	}

	/**
	 * Returns the id of country owned by bot with most units
	 * @return country id
	 */
	private int getMostUnitsInCountryByID() {
		int mostUnits = -1;
		int country = 0;
		ArrayList<Integer> myCountries = countryIDsOwnedByPlayer(player.getId());
		for(int i = 0; i < myCountries.size(); i++) {
			if(mostUnits < board.getNumUnits(myCountries.get(i))) {
				country = myCountries.get(i);
				mostUnits = board.getNumUnits(myCountries.get(i));
			}
		}
		return country;
	}

	private String getBotsCountryToReinforce() {

		int mostUnits = getMostUnitsInCountryByID();
		// updates global arraylists with relation to the country with most
		refreshAll(mostUnits);

		// checks that that country has no opponent country connected
		if(hasNoEnemys()) {
			for (Integer country : myCountries)
			{
				int most = board.getNumUnits(mostUnits);
				// if this is the tall country reinforce it (can fortify later to move the troops)
				if(most > 1) {
					return GameData.COUNTRY_NAMES[country];
				}
							
				if (neighbours.size() > 0)
					return GameData.COUNTRY_NAMES[country];
			}
		}
		
		// fallback
		return GameData.COUNTRY_NAMES[mostUnits];
	}
	
	/**
	 * method takes in three strings, formats and concatenates them
	 * @param from
	 * @param to
	 * @param units
	 * @return a formated String
	 */
	private String convertToCommand(String from, String to, String units) {
		from = from.replaceAll("\\s", "");
		to = to.replaceAll("\\s", "");
		return from + " " + to + " " + units;
	}
	
	/**
	 * DEBUGGING METHOD
	 *
	 * method maintains a counter to prevent a bad command from being
	 * entered more than 99 times
	 * if the counter reaches 100, the counter is reset and returns "skip"
	 * @param command
	 * @return a formated String
	 */
	private String commandBreaker(String command) {
		if(commandCounter == 0) {
			lastCommand = command;
			commandCounter++;
			return command;
		}
		
		if(lastCommand.compareTo(command) != 0) {
			lastCommand = "";
			commandCounter = 0;
		} else {
			commandCounter++;
		}
		
		if(commandCounter > 99) {
			commandCounter = 0;
			return "skip";
		}
		
		return command;
		
	}

	/**
	 * takes in a country and a list of countries and returns the ones that are linked to that country
	 * Used for fortifying
	 * @param country
	 * @param myCountries
	 * @return
	 */
	private ArrayList<Integer> getLinkedCountries(int country, ArrayList<Integer> myCountries){
		ArrayList<Integer> linked = new ArrayList<>();
		for(Integer c : myCountries) {
			if(board.isConnected(country, c)) {
				linked.add(c);
			}
		}
		return linked;
	}

	
	/**
	 * method called when bot wishes and can trade
	 * Prefers to trade a mix of cards, then any group of 3.
	 * Tries to keep wilds if it has any
	 *
	 * @return a formatted String for card exchange
	 */
	private String changeCardExchange() {
		ArrayList<Card> hand = player.getCards();
		String mixed = "";
		String artillery = "";
		String infantry = "";
		String cavalry = "";

		for (Card card : hand) {
			// grab first letter of insignia (lowercase)
			char tradeLetter = card.getInsigniaName().toLowerCase().charAt(0);

			// don't attempt to add wilds first
			if (tradeLetter == 'w') continue;

			// add to correct string
			switch(tradeLetter) {
				case 'a':
					artillery+= tradeLetter;
					break;
				case 'i':
					infantry+= tradeLetter;
					break;
				case 'c':
					cavalry+= tradeLetter;
					break;
				default:
					System.out.println("Error handling trade letter");
			}

			// attempt to add to mixed
			if (mixed.indexOf(tradeLetter) == -1) mixed += tradeLetter;
		}

		// look for largest string, prefer mixed where possible
		String largest = mixed;
		largest = (artillery.length() > largest.length()) ? artillery : largest;
		largest = (infantry.length() > largest.length()) ? infantry : largest;
		largest = (cavalry.length() > largest.length()) ? cavalry : largest;

		// add any wilds needed
		while(largest.length() < 3)
			largest += 'w';

		// return
		return largest;

	}
	
	/**
	 * method checks to see if the player has a matching set of cards
	 * in order to make a trade
	 * @return
	 */
	private boolean canTrade() {
		
		// ints to keep track of card types and to determine trade-ability
		int infantry = 0;
		int cavalry = 0;
		int artillery = 0;
		ArrayList<Card> hand = player.getCards();
		
		// loop through the players hand, checking insignias
		for(int j=0;j<hand.size();j++) {
			if(hand.get(j).getInsigniaName().equals("Artillery")) {
				artillery++;
			} else if(hand.get(j).getInsigniaName().equals("Infantry")) {
				infantry++;
			} else if(hand.get(j).getInsigniaName().equals("Cavalry")) {
				cavalry++;
			} else if(hand.get(j).getInsigniaName().equals("Wild Card")) {
				artillery++;
				infantry++;
				cavalry++;
			}
		}
		
		// if the player has 3 of the same type of card or
		// if the player has at least one of each card
		// then the player can make a trade
		if(infantry >= 3 || cavalry >= 3 || artillery >= 3 
				|| (infantry>0 && artillery>0 && cavalry>0)) {
			return true;
		}
		
		// if the player does not have at least three of one type or one of each
		// then the player cannot make a trade
		return false;
	}
	
	/**
	 * method to determine when the bot should stop an attack
	 * @param myCountry
	 * @param enemyCountry
	 * @return
	 */
	private boolean isStoppingAttack(int myCountry,int enemyCountry) {
		// no attack option
		if(enemyCountry == -1) {
			return true;
		}
		int myUnits = board.getNumUnits(myCountry);
		int enemyUnits = board.getNumUnits(enemyCountry);

		// Stop if armies are of similar size
		if(enemyUnits > myUnits + MINIMUM_ATTACK_GAP) {
			return true;
		}

		// Stop if our tower of armies gets too small
		if(myUnits < MINIMUM_ATTACKING_ARMIES) {
			if(enemyUnits == 1 && myUnits >= MINIMUM_ATTACK_GAP + 1) {
				return false;
			} else {
				return true;
			}
		}

		// otherwise carry on
		return false;
	}

	/**
	 * Updates neighbour arraylist to relate to passed in country
	 */
	private void refreshNeighbours(int country) {
		if(!neighbours.isEmpty()) {
			neighbours.clear();
		}
		for(int i = 0; i < GameData.ADJACENT[country].length; i++) {
			neighbours.add(GameData.ADJACENT[country][i]);
		}
	}

	/**
	 * updates player's country arraylist
	 *
	 * @param country
	 */
	private void refreshMyCountries(int country) {
		if(!myCountries.isEmpty()) {
			myCountries.clear();
		}
		for(int i = 0;i<GameData.NUM_COUNTRIES;i++) {
			if (board.getOccupier(i) == player.getId())
				myCountries.add(i);
		}
	}

	/**
	 * Updates opponent country arraylist
	 */
	private void refreshOpponentCountries() {
		if(!opponentCountries.isEmpty()) {
			opponentCountries.clear();
		}
		for(int i = 0;i<GameData.NUM_COUNTRIES;i++) {
			if (board.getOccupier(i) == opponent)
				opponentCountries.add(i);
		}
	}

	/**
	 * Updates arraylist of all neutral countries
	 */
	private void refreshNeutralCountries() {
		if(!neutralCountries.isEmpty()) {
			neutralCountries.clear();
		}
		
		for(int i = 0; i < GameData.NUM_COUNTRIES; i++) {
			if(board.getOccupier(i) != player.getId() && board.getOccupier(i) != opponent)
				neutralCountries.add(i);
		}

	}
	
	/**
	 * refresh all Array Lists
	 * @param country
	 */
	private void refreshAll(int country) {
		refreshNeighbours(country);
		refreshMyCountries(country);
		refreshOpponentCountries();
		refreshNeutralCountries();
	}
	
	/**
	 * find a country to fortify and a country to fortify from
	 * returns skip if no such set is found
	 * @return a formated String to fortify the bot
	 */
	private String fortifyBot() {
		int mostUnits = getMostUnitsInCountryByID();
		int countryFrom;
		int countryTo;

		// update arraylists
		refreshAll(mostUnits);
		// fortify our tower if it neighbours an opponent's country otherwise fortify one from it that does
		if(hasNoEnemys()) {
			countryFrom = mostUnits;
			countryTo = getCountryToFortify(countryFrom);
		} else {
			countryTo = mostUnits;
			countryFrom = getCountryFromFortify(countryTo);
		}

		// If neither of the above make sense just skip
		if(countryFrom == -1 || countryTo == -1)
			return "skip";
		
		String command = "";
		String from = "";
		String to = "";
		String units = "";
		
		from += GameData.COUNTRY_NAMES[countryFrom];
		to += GameData.COUNTRY_NAMES[countryTo];
		units += board.getNumUnits(countryFrom)-1;
		command = convertToCommand(from,to,units);
		return command;
		
	}
	
	/**
	 * based on a destination, loops through the linked country list to
	 * find a country from which to move units
	 * returns -1 if one is not found
	 * @param countryTo
	 * @return
	 */
	private int getCountryFromFortify(int countryTo) {
		ArrayList<Integer> linked = getLinkedCountries(countryTo, myCountries);
				
		if(linked.isEmpty()) {
			return -1;
		}
		
		int[] unitValues = new int[linked.size()];
		for(int i = 0;i<unitValues.length;i++) {
			unitValues[i] = board.getNumUnits(linked.get(i));
		}
		
		for(int i = 0;i<unitValues.length;i++) {
			if(unitValues[i] != 1)
				return linked.get(i);
		}
		
		return -1;
	}
	
	/**
	 * based upon a source country, this method returns a country id
	 * which has an enemy next to it
	 * if it cannot find an enemy within the linked country list, returns -1
	 * @param countryFrom
	 * @return
	 */
	private int getCountryToFortify(int countryFrom) {
		ArrayList<Integer> linked = new ArrayList<>();
		for(Integer c : myCountries) {
			if(countryFrom != c && board.isConnected(countryFrom, c))
				linked.add(c);
		}
		
		if(linked.isEmpty()) {
			return -1;
		}
		
		/* loop through the linked country list to look for an enemy country
		 * which belongs to the opponent */
		for(Integer c : linked) {
			ArrayList<Integer> adj = new ArrayList<>();
			if(c != countryFrom) {
				for(int i = 0;i<GameData.ADJACENT[c].length;i++) {
					adj.add(GameData.ADJACENT[c][i]);
				}
			}
			
			for(Integer ac : adj) {
				if(board.isAdjacent(c,ac) && board.getOccupier(ac) == opponent)
					return c;
			}
		}
		
		/* if an opponent country cannot be found,
		 * loop through the linked country list to look for an enemy country
		 * which is neutral */
		for(Integer c : linked) {
			ArrayList<Integer> adj = new ArrayList<>();
			if(c != countryFrom) {
				for(int i = 0;i<GameData.ADJACENT[c].length;i++) {
					adj.add(GameData.ADJACENT[c][i]);
				}
			}
			
			for(Integer ac : adj) {
				if(board.isAdjacent(c,ac) && board.getOccupier(ac) != opponent
						&& board.getOccupier(ac) != player.getId())
					return c;
			}
		}
		
		/* if no enemy can be found within the linked list
		 * return -1 */
		return -1;
	}
	
	/**
	 * loops thru the neighbours list
	 * if the last contains at least one country which does not 
	 * belong to the player, return false
	 * otherwise returns true
	 * @return
	 */
	private boolean hasNoEnemys() {
		for(Integer c : neighbours) {
			if(board.getOccupier(c) != player.getId())
				return false;
		}
		return true;
	}
	
	/**
	 * placement strategy
	 * looks for a country owned by one of the neutral opponents and checks to see if
	 * it is adjacent to an opponent country 
	 * @return a formated String of a single country name
	 */
	private String placementStrategy(int player) {
		String command = "";
		
		refreshNeutralCountries();
		refreshOpponentCountries();

		ArrayList<Integer> currentNeutralsCountries = countryIDsOwnedByPlayer(player);

		// Will hold any country owned by neutral that neighbours our opponent
		ArrayList<Integer> neighboursOpponents = new ArrayList<>();
		
		for(Integer c : currentNeutralsCountries) {
			ArrayList<Integer> neutralNeighbours = this.neighbourCountries(c);
			neutralNeighbours = this.filterCountriesByPlayer(opponent, neutralNeighbours);
			if(!neutralNeighbours.isEmpty()) {
				neighboursOpponents.add(c);
			}
		}
		
		// Randomly select a country, prefer one from the ones that neighbour our opponent if any
		Random rand = new Random();
		if(!neighboursOpponents.isEmpty()) {
			command = GameData.COUNTRY_NAMES[neighboursOpponents.get(rand.nextInt(neighboursOpponents.size()))];
		} else {
			command = GameData.COUNTRY_NAMES[currentNeutralsCountries.get(rand.nextInt(currentNeutralsCountries.size()))];
		}

		command = command.replaceAll("\\s", "");
		return command;
	}
}
