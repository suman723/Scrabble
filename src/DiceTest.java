/**
 * This DiceTest class created to test the Dice class functionality
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 **/

import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

// DiceTest class created to test the Dice class all functionality
public class DiceTest {

    // new object dice
    Dice dice = new Dice();

    @Test // for checking the Random value become true
    public void rollValue() {
        dice = new Dice();

        IntStream.range(0, 100).map(i -> dice.rollDice()).forEach(result -> {
            assertTrue(result >= 1);
            assertTrue(result <= 6);
        });
    }

}// end Dicetest