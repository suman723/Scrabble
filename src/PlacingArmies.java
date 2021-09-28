import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Class created for allowing users to place their reinforcements on their territory
 * Updating the map accordingly and provide error messages if anything wrong
 * And allowing re-entry instructions if needed
 * Also making sure all the reinforcements are placed
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class PlacingArmies { //start program

    /**
     * Method for asking user to place his armies until there are remaining army units
     *
     * @param player to ask for placing
     * @param ui     for user interaction
     */
    public static void placeArmies(Player player, UserInterface ui) {
        // getting countries
        Countries countries = Countries.getInstance();
        // getting country options for current player
        ArrayList<Country> options = countries.countriesOccupiedBy(player);

        // keep asking user until he has remaining army units
        while (player.getRemainingArmies() > 0) {
            // print statement to ask user to enter country to reinforce
            ui.println(player.getName() + " Enter country to reinforce");

            try {
                // printing player options
                ui.println(countries.names(options).toString());

                // getting player's country choice
                Country choice = ui.getCountry(options);
                //printing statement to ask user to enter number of armies to reinforce
                ui.println("Enter number of armies to reinforce: [ 1 - " + player.getRemainingArmies() +
                        "]");

                // asking how many units user wants to place
                int armies = ui.getNumber(1, player.getRemainingArmies());

                // calling helper method to update logic state
                placeArmiesHelper(player, choice, armies);

                // showing info message
                ui.println("Deploying troops to " + choice.getName());
                ui.repaintMap();
                if (player.getRemainingArmies() > 0)
                    ui.println(player.getRemainingArmies() + " armies left to place...");
            } catch (Exception e) {
                // printing error message if anything occurred
                ui.println(e.getMessage());
            }
        }
    }

    /**
     * Helper method for update state of logic classes
     *
     * @param player  to place army
     * @param country to place army to
     * @param armies  number of army units to place
     */
    public static void placeArmiesHelper(Player player, Country country, int armies) {
        // check if current country is occupied by this player
        if (country.getOccupier() != player) {
            throw new IllegalArgumentException(MessageFormat.format("Country {0} is not occupied by player {1}.",
                    country.getName(), player.getName()));
        }

        // check if current player has enough army units
        if (player.getRemainingArmies() < armies) {
            throw new IllegalArgumentException(MessageFormat.format("Player {0} does not have {1} armies.",
                    player.getName(), armies));
        }

        // updating country units
        country.updateNumOfArmies(armies);

        // updating user units
        player.updateRemainingArmies(-armies);
    }

} //end program
