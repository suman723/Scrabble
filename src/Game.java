import java.util.ArrayList;

/**
 * Game is the entry point of the program. It handles the creation of all other objects and controls the game
 * logic and flow.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class Game
{
	
	
	protected Countries countries;
	protected UserInterface ui;
	protected Player[] players;
	protected Deck deck;
	protected PlayDeck tradeDeck;
	protected Player activePlayer;
	enum rollFor { DEPLOYMENT, TURN }; // determines which type of turn is to be rolled for
	
	// Usually these will be the same but useful to be able to change for testing a shorter setup
	protected int INIT_UNITS_PLAYERS = Constants.INIT_UNITS_PLAYER;
	protected int INIT_UNITS_NEUTRAL = Constants.INIT_UNITS_NEUTRAL;
	
	/**
	 * Creates a game instance
	 */
	public Game()
	{

	}

	/**
	 * gets the user interface
	 * @return ui
	 */
	public UserInterface getUI()
	{
		return ui;
	}

	/**
	 * Returns true if either player has no countries left.
	 * If this is somehow called before the playere are initialised it just returns false. (This shouldn't happen)
	 *
	 * @return whether the game is over or not
	 */
	public boolean isGameOver()
	{
		// if players hasn't been initialised return false
		if (players == null)
			return false;

		int player1CountryCount = countries.countriesOccupiedBy(players[0]).size();
		int player2CountryCount = countries.countriesOccupiedBy(players[1]).size();

		return (player1CountryCount == 0) || (player2CountryCount == 0);
	}

	/**
	 * Sets up the UI, countries and players.
	 * The players will likely get moved to a wrapper class similar to Countries later
	 *
	 */
	protected void initialSetup()
	{
		countries = Countries.getInstance();
		ui = new UserInterface();
		ui.drawUI();
		players = new Player[6];
		deck = new Deck();
		tradeDeck = new PlayDeck();
		deck.shuffle();

		// ***** temporary auto name for debugging uncomment input strings when in use
//				String p1Name = "Player One";
//				String p2Name = "Player Two";
		
		
		// get player names
		boolean validName = true;
		String p1Name, p2Name;
		
		do { // loop asks for name, if it is blank, it will set valid name to false and loop thru again
			//ui.println("Player 1 (" + Constants.P0name + ") Enter Your Name");
			ui.print("Player 1 ");
			ui.print("( " + Constants.P0name + " )", Constants.P0);
			ui.println(" Enter Your Name");

			p1Name = ui.getInput();
			validName = ui.validatePlayerName(p1Name);
		}while(!validName);
		ui.println("Welcome Commander " + p1Name);

		
		do { // loop asks for name, if it is blank, it will set valid name to false and loop thru again
			//ui.println("Player 2 (" + Constants.P1name + ") Enter Your Name");
			ui.print("Player 2 ");
			ui.print("( " + Constants.P1name + " )", Constants.P1);
			ui.println(" Enter Your Name");

			p2Name = ui.getInput();
			validName = ui.validatePlayerName(p2Name);
		}while(!validName);
		ui.println("Welcome Commander " + p2Name);

		// create players, 2 human, 4 neutrals
		players[0] = new Player(0, p1Name, Constants.P0, Constants.P0name, true, INIT_UNITS_PLAYERS);
		players[1] = new Player(1, p2Name, Constants.P1, Constants.P1name, true, INIT_UNITS_PLAYERS);
		players[2] = new Player(2, "Neut1", Constants.P2, Constants.P2name, false, INIT_UNITS_NEUTRAL);
		players[3] = new Player(3, "Neut2", Constants.P3, Constants.P3name,  false, INIT_UNITS_NEUTRAL);
		players[4] = new Player(4, "Neut3", Constants.P4, Constants.P4name, false, INIT_UNITS_NEUTRAL);
		players[5] = new Player(5, "Neut4", Constants.P5, Constants.P5name, false, INIT_UNITS_NEUTRAL);
	
	}

	/**
	 * Distributes initial countries to players.
	 */
	protected void distributeCountries()
	{
		ui.println("Installing Benevolent Leaders To Nations...");
		for (int i = 0; i < players.length; i++)
		{
			int numCountriesToAssign;
			if (players[i].isHuman())
			{
				numCountriesToAssign = 9;
			}
			else
			{
				numCountriesToAssign = 6;
			}

			for (int j = 0; j < numCountriesToAssign; j++)
			{
				Country country = countries.get(deck.drawCard().getCardID());
				country.setOccupier(players[i]);
				country.setNumOfArmies(1);
				players[i].updateRemainingArmies(-1);
			}

		}
	}
	
	/**
	 * Rolls a single die per human player
	 * @return returns a 0 or 1 depending on winner of roll
	 */
	protected int whoGoesFirst() {
		
		Dice die = new Dice();
		int p1Roll = 0;
		int p2Roll = 0;
		do {
			p1Roll = die.rollDice();
			p2Roll = die.rollDice();
			ui.println(players[0].getName() + " rolled a " + p1Roll + " and "
					+ players[1].getName() + " rolled a " + p2Roll);
			if(p1Roll == p2Roll) ui.println("There is a tie. Rolling again...");
		} while (p1Roll == p2Roll);
		
		if(p1Roll > p2Roll) {
			return 0;
		} else {
			return 1;
		}
		
	}
	
	/**
	 * rolls dice to see who goes first for deployment or turn
	 */
	protected void rollingFor(rollFor rollFor) {
		
		switch(rollFor) {
		case DEPLOYMENT: ui.println("Rolling to see who deploys troops first...");
				break;
		case TURN: ui.println("Rolling to see who takes the first turn...");
				break;
		}
		int p = whoGoesFirst();
		
		switch(rollFor) {
		case DEPLOYMENT: ui.println(players[p].getName() + " will place 3 armies on the map first.\n");
				break;
		case TURN: ui.println(players[p].getName() + " will go first.\n");
				break;
		}
		
		activePlayer = players[p];
		
	}

	/**
	 * switch the active player
	 */
	protected void changePlayer()
	{
		if (activePlayer == players[0])
		{
			activePlayer = players[1];
		}
		else
		{
			activePlayer = players[0];
		}
	}

	/**
	 * method to control the initial deployment of troops
	 * completes when both players have deployed all their troops
	 */
	protected void initialDeployment()
	{
		// Loop until both players have deployed their armies
		while (players[0].getRemainingArmies() != 0 || players[1].getRemainingArmies() != 0)
		{
			ArrayList<Country> options;		// holds all options the player can pick from
			Country choice;					// final choice made by player
			// Handle the users armies
			ui.println(activePlayer.getName()  + " (" + activePlayer.getColorName() +  ") , please type in a country to deploy your troops", activePlayer.getColor());
			// options will be a list of all countries that player controls
			options = countries.countriesOccupiedBy(activePlayer);
			// names returns a list of strings of names. This just prints out the options
			ui.println(countries.names(options).toString());
			// choice will return the country user typed. Autocomplete and invalid choices are handled at the UI/Command Panel levels
			choice = ui.getCountry(options);
			ui.println("Deploying troops to " + choice.getName());

			// Add three units to country and redraw map
			choice.updateNumOfArmies(3);
			activePlayer.updateRemainingArmies(-3);
			ui.repaintMap();

			// Loop over neutrals and add one country for each TODO: add helpful country and colour printing here too
			for (int i = 2; i < players.length; i++)
			{
				//ui.println(activePlayer.getName()  + " , please type in a country to deploy an army for " + players[i].getName() + " (" + players[i].getColorName() + ")" );
				ui.print(activePlayer.getName()  + " , please type in a country to deploy an army for ", activePlayer.getColor());
				ui.println(players[i].getName() + " (" + players[i].getColorName() + ")" , players[i].getColor());

				options = countries.countriesOccupiedBy(players[i]);
				// names returns a list of strings of names. This just prints out the options
				ui.println(countries.names(options).toString());

				choice = ui.getCountry(options);
				ui.println("Deploying troops to " + choice.getName());

				// Add one army
				choice.updateNumOfArmies(1);
				players[i].updateRemainingArmies(-1);
				ui.repaintMap();
			}

			// Print a blank line for some text breakup
			ui.println("");

			// Swap player
			changePlayer();

		}
	}

	/**
	 * the main loop runs through game phases per player until
	 * the game is over which is determined by a boolean
	 */
	protected void mainLoop()
	{
		
		GoldenCalvary gcu = new GoldenCalvary();
		while(!isGameOver())
		{
			int countriesCaptured = 0; // create and set counties captured to 0, used for territory cards
			
			// Phase one calculate reinforcements
			ui.println(activePlayer.getName() + " calculating reinforcements: ");
			ui.println(PhaseOne.calcReinforcements(activePlayer, countries));
			
			// Trade Phase
			TradePhase.trade(gcu,activePlayer,this.getUI());

			// Phase two deploy troops
			PlacingArmies.placeArmies(activePlayer, this.getUI());

			// Phase three battle
			Player surg = players[0];
			if (activePlayer == players[0])
				surg = players[1];
			countriesCaptured = BattlePhase.start(this, activePlayer, surg);

			if (! isGameOver())
			{
				// Phase four fortify
				// player gets a card if they captured at least one country
				if(countriesCaptured > 0) {
					activePlayer.getHand().addCard(tradeDeck.drawCard()); 
					ui.print(activePlayer.getName() + ", you successfully invaded " +  countriesCaptured);
					if(countriesCaptured > 1) {
						ui.println(" countries.\nFor your success, you have gained 1 Territory Card.");
					} else {
						ui.println(" country.\nFor your success, you have gained 1 Territory Card.");
					}
				}
				FortifyPhase.fortify(this.getUI(), activePlayer, countries);
			}

			// Next player's turn
			changePlayer();
		}
	}

	/**
	 * method ends the game and prints the winner
	 */
	protected void handleGameOver()
	{
		Player winner; // will hold winning player
		// Check if player 1 has lost
		if (countries.countriesOccupiedBy(players[0]).size() == 0)
			winner = players[1];
		else
			winner = players[0];

		// Print Game over message
		ui.println("Game Over!\n" + winner.getName() + " wins!!!\nThanks for Playing.");
	}

	/**
	 * The entry point of the game
	 *
	 * @param args ignored.
	 */
	public static void main(String[] args)
	{
		Game risk = new Game();
		// TESTING CODE - uncomment to change the number of turns needed to deploy all troops.
		// allows for a faster run through in testing. numTurnsOfSetup should be changed to match how many turns you wish
		// to step through. Changing the formulas below it manually may cause unexpected issues.
//		{
//			int numTurnsOfSetup = 2;
//			risk.INIT_UNITS_PLAYERS = 9 + (3 * numTurnsOfSetup);
//			risk.INIT_UNITS_NEUTRAL = 6 + (2 * numTurnsOfSetup);
//		}
		risk.initialSetup();
		risk.distributeCountries();
		risk.ui.repaintMap();
		risk.rollingFor(rollFor.DEPLOYMENT);
		risk.initialDeployment();
		risk.ui.println("Initial Deployment Complete");

		// Main game loop
		risk.rollingFor(rollFor.TURN);
		risk.mainLoop();

		// game over
		risk.handleGameOver();
	}

}
