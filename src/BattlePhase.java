/**
 * Handles the battle phase of the game. Expects the start method to be called. All other methods are helper methods and are only public to allow testing of those
 * individual parts.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BattlePhase
{
	// Limits how many attackers or defenders in any one battle
	private static final int MAX_ATTACKERS = 3;
	private static final int MAX_DEFENDERS = 2;

	/**
	 * The main entry point of the class. Will handle the complete battle logic of a players turn
	 *
	 * @param game the game object pointer for method calling
	 * @param activePlayer the current player whose turn it is
	 * @param neutralSurrogate the player who makes choices for neutral defenders (basically the other player)
	 * @return
	 */
	public static int start(Game game, Player activePlayer, Player neutralSurrogate)
	{
		// Variables needed
		Countries countries = Countries.getInstance();
		boolean phaseComplete = false;		// While loop flag
		int countriesCaptured = 0;			// Used in later sprints, returns this at end

		// Loop until battle phase has ended
		while (!phaseComplete)
		{
			// Check for game over. If true exit phase.
			if (game.isGameOver())
			{
				phaseComplete = true;
				continue;
			}

			// Get a list of potential attacking countries. This will be all countries with 2 or more armies occupied by the active player
			// that also has at least one link to a country not occupied by active player.
			ArrayList<Country> validAttackCountries = getValidAttackCountries(activePlayer);

			// If no valid options notify user and end phase
			if (validAttackCountries.size() == 0)
			{
				game.getUI().println("No valid countries to attack from. Ending battle phase");
				phaseComplete = true;
				continue;
			}

			// Get attack country or skip
			//game.getUI().println(activePlayer.getName() + " select country to attack from: (enter \"skip\" to end battle phase)");
			game.getUI().print(activePlayer.getName(), activePlayer.getColor());
			game.getUI().println(" select country to attack from: (enter \"skip\" to end battle phase)");

			// prints list of valid countries
			game.getUI().println(countries.names(validAttackCountries).toString());
			Country attackCountry = game.getUI().getCountryWithSkip(validAttackCountries);

			// Check for skip and end phase. AttackCountry will be null if user skipped
			if (attackCountry == null)
			{
				game.getUI().println("Ending battle phase. Thank you.");
				phaseComplete = true;
				continue;
			}

			// Get defending country.

			// This will get a list of all countries with a link to attacking country that are not occupied by active player
			ArrayList<Country> defendingOptions = invadableCountries(attackCountry);

			//game.getUI().println(activePlayer.getName() + " select country to invade from " + attackCountry.getName() + ": (enter \"skip\" to restart from" +
			//		" attacking country selection)");
			game.getUI().print(activePlayer.getName(), activePlayer.getColor());
			game.getUI().println(" select country to invade from " + attackCountry.getName() + ": (enter \"skip\" to restart from" +
					" attacking country selection)");

			game.getUI().println(countries.names(defendingOptions).toString());
			Country defendingCountry = game.getUI().getCountryWithSkip(defendingOptions);

			// Check for skip if so restart loop
			if (defendingCountry == null)
			{
				continue;
			}

			// Handle the conflict in a loop of mini battles (dice rolls)
			boolean conflictResolved = false;		// Control flag for do while loop
			do
			{
				// Will be an array of size 2. With index 0 corresponding to attacking armies and index 1 corresponding to defending army count.
				int[] armiesCommitted = getArmyCounts(game, attackCountry, defendingCountry, activePlayer, neutralSurrogate);

				// Check if attack count is 0 and if so exit (allows attacker to back out if they made a mistake and return to start of main while loop)
				if (armiesCommitted[0] == 0)
				{
					game.getUI().println("Exiting conflict");
					conflictResolved = true;
					continue;
				}

				// Simulate battle and get report. Will roll dice and adjust army counts.
				BattleReport report = simulateBattle(attackCountry, defendingCountry, armiesCommitted[0], armiesCommitted[1]);
				// Print report and update map
				//game.getUI().println(report.toString());
				report.print(game.ui);
				game.getUI().repaintMap();

				// Handle conflict ending by exhausting attack or defense. Else ask if Attacker wants to continue
				if (attackCountry.getNumOfArmies() < 2)			// Can no longer attack
				{
					game.getUI().println("Attacking country, " + attackCountry.getName() + " has been exhausted. Invasion has failed. Ending conflict.");
					conflictResolved = true;
				}
				else if (defendingCountry.getNumOfArmies() == 0)	// Defender has lost
				{
					// Prep units to redeploy to new country variables
					int maxUnitsToRedeploy = attackCountry.getNumOfArmies() - 1;
					int unitsToRedeploy;

					// Gain control of country
					defendingCountry.setOccupier(activePlayer);
					game.getUI().repaintMap();

					// Handle redeployment of attackers troops. If max is 1 we can skip getting a choice from user
					// Also skip input from user if this capture has won the game
					if (maxUnitsToRedeploy == 1 || game.isGameOver())
					{
						game.getUI().println("Invasion has been successful.");
						unitsToRedeploy = 1;
					}
					else
					{
						game.getUI().print("Invasion has been successful. ");
						game.getUI().print(activePlayer.getName(), activePlayer.getColor());
						game.getUI().println(" please enter number of units to redeploy from " + attackCountry.getName() + " to " +
								defendingCountry.getName() + ": [1 - " + maxUnitsToRedeploy + "]");

						unitsToRedeploy = game.getUI().getNumber(1, maxUnitsToRedeploy);
					}
					// Update country with new units
					game.getUI().print(activePlayer.getName(), activePlayer.getColor());
					game.getUI().println(" Redeploying " + unitsToRedeploy + " to your new country, " + defendingCountry.getName());
					attackCountry.updateNumOfArmies(-unitsToRedeploy);
					defendingCountry.setNumOfArmies(unitsToRedeploy);
					game.getUI().repaintMap();

					// Conflict resolved
					conflictResolved = true;
					countriesCaptured++;
				}
				else	// continue conflict?
				{
					game.getUI().repaintMap();
					game.getUI().print(activePlayer.getName(), activePlayer.getColor());
					game.getUI().println(" do you wish to continue this invasion (y/n):");
					boolean continueBattle = game.getUI().getAffirmation();
					conflictResolved = !continueBattle;
				}
			} while (!conflictResolved);

		}
		return countriesCaptured;
	}

	/**
	 * Asks players to enter the number of units the wish to commit to a single battle. Influences the number of dice rolled and casualties of war.
	 *
	 * @param game game object pointer for UI methods
	 * @param attack country that is launching the attack
	 * @param defend defending country.
	 * @param activePlayer current player.
	 * @param neutralSurrogate player who will make choices for neutral defenders if any
	 * @return an array of length 2. First element is the attackers army count and second is the defender's
	 */
	private static int[] getArmyCounts(Game game, Country attack, Country defend, Player activePlayer, Player neutralSurrogate)
	{
		int[] armyCounts = new int[2];
		int min, max;

		// Get army count from attacker. Allow then to enter 0 to cancel conflict
		min = 0;
		max = Math.min(attack.getNumOfArmies() - 1, MAX_ATTACKERS); 		// Must leave one army behind


		game.getUI().print( "(" + attack.getName() + " -> " + defend.getName() + ") ");
		game.getUI().print(activePlayer.getName(), activePlayer.getColor());
		game.getUI().println(" Enter number of armies to attack with: [0 - " + max + "] (enter 0 to end conflict without attacking)");
		armyCounts[0] = game.getUI().getNumber(min, max);

		// If 0 return array and end conflict
		if (armyCounts[0] == 0)
			return armyCounts;

		// Get defense count if ambiguous
		if (defend.getNumOfArmies() == 1)
		{
			armyCounts[1] = 1;
		}
		else
		{
			min = 1;
			max = MAX_DEFENDERS;

			// Determine if the surrogate should be used for choosing defending number of armies
			Player defendingPlayer;
			boolean surrogateInUse;
			if (defend.getOccupier().isHuman())
			{
				defendingPlayer = defend.getOccupier();
				surrogateInUse = false;
			}
			else
			{
				defendingPlayer = neutralSurrogate;
				surrogateInUse = true;
			}

			// Get choice
			if (surrogateInUse)
			{
				game.getUI().print("(" + attack.getName() + " -> " + defend.getName() +") ");
				game.getUI().print(defendingPlayer.getName(), defendingPlayer.getColor());
				game.getUI().print(" Enter number of armies to defend with for ");
				game.getUI().print(defend.getOccupier().getName(), defend.getOccupier().getColor());
				game.getUI().println(": [1 - " + max + "]");
			}
			else
			{
				game.getUI().print("(" + attack.getName() + " -> " + defend.getName() +") ");
				game.getUI().print(defendingPlayer.getName(), defendingPlayer.getColor());
				game.getUI().println(" Enter number of armies to defend with: [1 - " + max + "]");
			}

			armyCounts[1] = game.getUI().getNumber(min, max);
		}

		return armyCounts;
	}

	/**
	 * Returns all countries that player can attack from. The requirements are that there are at least 2 army units in the country and
	 * that it connects to at least one enemy country.
	 * Uses lambda functions for the first time :)
	 *
	 * @param attacker player looking to attack
	 * @return list of viable attack countries
	 */
	public static ArrayList<Country> getValidAttackCountries(Player attacker)
	{
		// Get pointer to countries object
		Countries countries = Countries.getInstance();

		// Get all countries owned by a player
		ArrayList<Country> options = countries.countriesOccupiedBy(attacker);

		// Remove any countries with less than two armies (trying new lambda functions)
		options = (ArrayList<Country>) options.stream().filter(country -> country.getNumOfArmies() >= 2).collect(Collectors.toList());

		// Remove any countries with no valid attack option (i.e when all links out are to countries also occupied by attacker)
		options = (ArrayList<Country>) options.stream().filter(BattlePhase::countryCanInvadeAnother).collect(Collectors.toList());

		return options;
	}

	/**
	 * Returns all countries linked to the passed in country that are occupied by a different player
	 *
	 * @param attacker country attacking
	 * @return list of viable countries to attack
	 */
	public static ArrayList<Country> invadableCountries(Country attacker)
	{
		// Get pointer to countries object
		Countries countries = Countries.getInstance();

		// Get all connected countries
		ArrayList<Country> connected = countries.get(attacker.getLinks());

		// Return all countries connected and not owned by attacker
		return (ArrayList<Country>) connected.stream().filter(country -> country.getOccupier() != attacker.getOccupier()).collect(Collectors.toList());

	}

	/**
	 * Returns true if the country has any viable attack options
	 *
	 * @param country country looking to attack
	 * @return true if and only if there are non zero amount of countries attackable from passed in country
	 */
	public static boolean countryCanInvadeAnother(Country country)
	{
		// If there are any invadable countries it is true
		return invadableCountries(country).size() != 0;
	}

	/**
	 * Handles the dice chucking and army removal as a result of said dice chucking
	 *
 	 * @param attack country attacking
	 * @param defend country defending
	 * @param attackArmies num of attacking units
	 * @param defendArmies num of defending units
	 * @return BattleReport detailing the battle. This was going to be a type of struct but has now become just a string and I may update it to account for that
	 */
	public static BattleReport simulateBattle(Country attack, Country defend, int attackArmies, int defendArmies)
	{
		// Get dice
		Dice die = new Dice();

		// Roll (gets back array list with highest rolls to the start)
		int[] attackingRolls = die.rollDice(attackArmies);
		int[] defendingRolls = die.rollDice(defendArmies);

		// Use the smaller array size for determining number of units lost
		int unitsLost = Math.min(attackArmies, defendArmies);

		// Keep track of units lost
		int attackersLost = 0;
		int defendersLost = 0;

		// Calculate loses
		for (int i = 0; i < unitsLost; i++)
		{
			// attacker has to roll higher than defender
			if (attackingRolls[i] > defendingRolls[i])
			{
				defendersLost++;
			}
			else
			{
				attackersLost++;
			}
		}

		// Adjust army counts in countries
		int attackChange = - attackersLost;
		int defendChange = - defendersLost;
		attack.updateNumOfArmies(attackChange);
		defend.updateNumOfArmies(defendChange);

		return new BattleReport(attack, defend, attackingRolls, defendingRolls, attackersLost, defendersLost);
	}

	/**
	 * Was intended to store various pieces of data about a battle but is only really being used for its toString method now.
	 * Should probably just be refactored to a method that returns said string.
	 */
	private static class BattleReport
	{

		private final Country attack;
		private final Country defense;
		private final int[] attackRolls;
		private final int[] defenseRolls;
		private final int attackersLost;
		private final int defendersLost;

		private BattleReport(Country attack, Country defense, int[] attackRolls, int[] defenseRolls, int attackersLost, int defendersLost)
		{
			this.attack = attack;
			this.defense = defense;
			this.attackRolls = attackRolls;
			this.defenseRolls = defenseRolls;
			this.attackersLost = attackersLost;
			this.defendersLost = defendersLost;
		}

		public void print(UserInterface ui)
		{
			// Colour dice per rules. Red for attacker, white for defender
			Color attackDiceColour = Color.RED;
			Color defendDiceColour = Color.WHITE;

			ui.print("Battle report: Attacking Country: ");
			ui.print(attack.getName());
			ui.print(" Defending Country: ");
			ui.println(defense.getName());

			ui.print(" Attacker Rolls: ");
			ui.print(Arrays.toString(attackRolls), attackDiceColour);
			ui.print(" Defender Rolls: ");
			ui.println(Arrays.toString(defenseRolls), defendDiceColour);
			ui.println("");			// space things out

			if (attackersLost > 0)
			{
				ui.print("Attacker casualties = ");
				ui.print(attackersLost + " ");
			}
			if (defendersLost > 0)
			{
				ui.print("Defender casualties = ");
				ui.println(defendersLost + " ");
			}
		}

		@Override
		public String toString()
		{


			StringBuilder sb = new StringBuilder();

			sb.append("Battle report: Attacking Country: ");
			sb.append(attack.getName());
			sb.append(" Defending Country: ");
			sb.append(defense.getName());
			sb.append(" Attacker Rolls: ");
			sb.append(Arrays.toString(attackRolls));
			sb.append(" Defender Rolls: ");
			sb.append(Arrays.toString(defenseRolls));
			sb.append("\n");
			if (attackersLost > 0)
			{
				sb.append("Attacker casualties = ");
				sb.append(attackersLost);
				sb.append(" ");
			}
			if (defendersLost > 0)
			{
				sb.append("Defender casualties = ");
				sb.append(defendersLost);
				sb.append(" ");
			}

			return sb.toString();
		}
	}

}
