import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PlacingArmiesTest class created to test the functionalities of placingArmies have
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

class PlacingArmiesTest { //start program
    // Setup countries. As a wrapper class we can only use one copy
    static Countries countries;
    static Player testPlayer1;
    static Player testPlayer2;

    @BeforeAll
    static void setup() {
        // creating countries instance
        countries = Countries.getInstance();

        // creating two players for testing purposes
        testPlayer1 = new Player(1, "Bob", Color.black, "Black", false, 10);
        testPlayer2 = new Player(2, "Alice", Color.white, "White", false, 10);

        // interchanging countries between two test players
        for (int i = 0; i < countries.asList().size(); i++) {
            if (i % 2 == 0) {
                countries.get(i).setOccupier(testPlayer1);
            } else {
                countries.get(i).setOccupier(testPlayer2);
            }
        }
    }

    /**
     * Test method for trying to place army on the opponents country
     */
    @Test
    void testIllegalOccupy() {
        try {
            // country at index 1 is occupied by testPlayer2. This method must throw exception
            PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(1), 5);
            fail();
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * Test method for trying to place army too many units of army
     */
    @Test
    void testIllegalArmySize() {
        try {
            // testPlayer1 does not have enough units (testPlayer1.getRemainingArmies() + 1) to set.
            // This method must throw exception
            PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(0), testPlayer1.getRemainingArmies() + 1);
            fail();
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    void testPlaceArmy() {
        // storing number of army units for both players

        // units of player 1
        int armies1 = testPlayer1.getRemainingArmies();

        // units of player 2
        int armies2 = testPlayer2.getRemainingArmies();

        // number of units to place by testPlayer1
        int army11 = 1;
        // placing armies
        PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(0), army11);
        // decreasing expected army units of testPlayer1
        armies1 -= army11;

        // comparing expected value of number of armies at country(0) with actual value
        assertEquals(army11, countries.get(0).getNumOfArmies());

        // comparing expected number of remaining testPlayer1 army units with actual value
        assertEquals(armies1, testPlayer1.getRemainingArmies());


        // number of units to place by testPlayer2
        int army21 = testPlayer2.getRemainingArmies();
        // placing armies
        PlacingArmies.placeArmiesHelper(testPlayer2, countries.get(1), army21);
        // decreasing expected army units of testPlayer2
        armies2 -= army21;

        // comparing expected value of number of armies at country(1) with actual value
        assertEquals(army21, countries.get(1).getNumOfArmies());
        // comparing expected number of remaining testPlayer2 army units with actual value
        assertEquals(armies2, testPlayer2.getRemainingArmies());

        int army12 = 2;
        //placing armies
        PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(2), army12);
        //decreasing expected army units of test player 1
        armies1 -= army12;
        //comparing expected value of number of armies at country(2) with actual value
        assertEquals(army12, countries.get(2).getNumOfArmies());
        assertEquals(armies1, testPlayer1.getRemainingArmies());

        int army13 = 3;
        //placing armies
        PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(4), army13);
        //decreasing expected army units of test player 1
        armies1 -= army13;
        //comparing expected value of number of armies at country(4) with actual value
        assertEquals(army13, countries.get(4).getNumOfArmies());
        //comparing expected number of remaining testPlayer1 army units with actual value
        assertEquals(armies1, testPlayer1.getRemainingArmies());

        int army14 = testPlayer1.getRemainingArmies();
        //placing armies
        PlacingArmies.placeArmiesHelper(testPlayer1, countries.get(0), army14);
        //decreasing expected army units of test player 1
        armies1 -= army14;
        //comparing expected value of number of armies at country(0) with actual value
        assertEquals(army11 + army14, countries.get(0).getNumOfArmies());
        //comparing expected number of remaining testPlayer1 army units with actual value
        assertEquals(armies1, testPlayer1.getRemainingArmies());
    }
}//end program
