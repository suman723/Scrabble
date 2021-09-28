import java.util.ArrayList;
import java.util.Random;

/**
 * Allows the tester to play a game where the initial deployment phase is randomised and automated.
 * This allows a user to quickly skip into sprint 3's main loop and test by playing.
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class TestGameAfterSprint2 extends Game
{
	public static void main(String[] args)
	{
		TestGameAfterSprint2 risk = new TestGameAfterSprint2();

		// Get names, draw countries and place one army in each
		risk.initialSetup();
		risk.distributeCountries();
		risk.ui.repaintMap();

		// Handle initial deployment of troops
		//risk.printGoFirst();   USED IN NORMAL GAME
		//risk.initialDeployment();  USED IN NORMAL GAME
		risk.randomInitialDeploy();  // SKIPS DEPLOYMENT FOR TESTING
		risk.ui.println("Initial Deployment Complete");

		// Main game
		risk.rollingFor(rollFor.TURN);
		// Pick up for testing
		// add cards to player's hands for testing
		for(int i = 0;i<5;i++) {
//			risk.players[0].getHand().addCard(risk.tradeDeck.drawCard());
			risk.players[1].getHand().addCard(risk.tradeDeck.drawCard());
		}
		
		risk.mainLoop();

		// game over
		risk.handleGameOver();
	}

	/**
	 * This method is solely used for testing in this class. It allows you to randomise the initial deployment of troops to skip ahead and try out
	 * the main game loop
	 */
	protected void randomInitialDeploy()
	{
		Random rand = new Random();
		// Loop over players array and deploy troops
		for (int i = 0; i < players.length; i++)
		{
			// deploy all troops to random countries for this player
			Player current = players[i];
			ArrayList<Country> currentPlayerCountries = countries.countriesOccupiedBy(current);

			while(current.getRemainingArmies() > 0)
			{
				int indexToDeployTo = rand.nextInt(currentPlayerCountries.size());
				int unitsToDeploy = 1;
				// humans will deploy 3. non humans 1
				if (current.isHuman())
					unitsToDeploy = 3;

				// adjust numbers
				current.updateRemainingArmies(-unitsToDeploy);
				currentPlayerCountries.get(indexToDeployTo).updateNumOfArmies(unitsToDeploy);
			}
		}
	}

}
