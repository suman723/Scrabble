/**
 * Sets up a game where one player has only one country left and the other has almost the rest of the map.
 * Allows tester to quickly check for end game handling including making sure a neutral army does not stop a successful game over.
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class TestGameFinalTurn extends Game
{
	public static void main(String[] args)
	{
		TestGameFinalTurn risk = new TestGameFinalTurn();
		// TESTING CODE - uncomment to change the number of turns needed to deploy all troops.
		// allows for a faster run through in testing. numTurnsOfSetup should be changed to match how many turns you wish
		// to step through. Changing the formulas below it manually may cause unexpected issues.
//		{
//			int numTurnsOfSetup = 2;
//			risk.INIT_UNITS_PLAYERS = 9 + (3 * numTurnsOfSetup);
//			risk.INIT_UNITS_NEUTRAL = 6 + (2 * numTurnsOfSetup);
//		}
		risk.initialSetup();
		//risk.distributeCountries();
		//risk.ui.repaintMap();
		//risk.rollingFor(deployment);
		//risk.initialDeployment();
		//risk.ui.print("Initial Deployment Complete");

		// TESTING create map where player 1 has almost won
		risk.removeDefaultArmiesFromPlayers();
		risk.setupMap();


		// Main game loop
		//risk.rollingFor(rollFor.TURN);
		// set player 2 to first player to test edge cases
		risk.activePlayer = risk.players[1];

		risk.mainLoop();

		// game over
		risk.handleGameOver();
	}

	protected void removeDefaultArmiesFromPlayers()
	{
		for (Player p : players)
			p.setRemainingArmies(0);
	}

	protected void setupMap()
	{
		Countries countries = Countries.getInstance();

		// Give all countries to player 1 and 3 armies
		for (Country c : countries.asArray())
		{
			c.setOccupier(players[0]);
			c.setNumOfArmies(3);
		}

		// Give a couple of countries to neutrals to make sure they don't stop game over
		Country ontario = countries.get(0);
		Country gb = countries.get(9);

		ontario.setOccupier(players[2]);
		gb.setOccupier(players[3]);

		// Give player 2 a single country with one army in it
		Country china = countries.get(27);
		china.setOccupier(players[1]);
		china.setNumOfArmies(1);

		// draw
		ui.repaintMap();
	}
}
