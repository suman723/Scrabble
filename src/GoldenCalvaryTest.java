import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * GoldenCalvary Junit test created to test all functionality of the GoldenCalvary class
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

class GoldenCalvaryTest { // start test class

    @Test
    void reinforcementsGetter(){ //start reinforcementGetter method
        GoldenCalvary test = new GoldenCalvary();

        //It should return 4 on the first call
        int reinforcements = test.getNumberReinforcements();
        assertEquals(4, reinforcements);

        // It should return 6 on the second call
        reinforcements = test.getNumberReinforcements();
        assertEquals(6, reinforcements);

        //It should return 8 on the third call
        reinforcements = test.getNumberReinforcements();
        assertEquals(8, reinforcements);

        //It should return 10 on the fourth call
        reinforcements = test.getNumberReinforcements();
        assertEquals(10, reinforcements);

        //It should return 12 on the fifth call
        reinforcements = test.getNumberReinforcements();
        assertEquals(12, reinforcements);

        // It should return 15 on the sixth call
        reinforcements = test.getNumberReinforcements();
        assertEquals(15, reinforcements);

        // It should 20 (15 + 5) on the seventh call and so on
        reinforcements = test.getNumberReinforcements();
        assertEquals(20, reinforcements);

        //It should return 4 on the first call
        reinforcements = test.getNumberReinforcements();
        assertEquals(25, reinforcements);
    } // end reinforcementGetter method
} // end program