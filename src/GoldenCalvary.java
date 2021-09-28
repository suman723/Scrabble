/**
 * GoldenCalvary class created to calculate the number of reinforcements to be traded
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */
public class GoldenCalvary { // start class
    // declaration variables
    private int incCounter;
    private int reinforcements;

    public GoldenCalvary() { // Constructor for the class
        incCounter = 0; // Holds number of times the getNumberReinforcements function is called for
        reinforcements = 4; // Initial number of reinforcements
    }
    // Will return updated number of reinforcements
    public int getNumberReinforcements() { //start getNumberReinforcements method
        if (incCounter == 0) { // If called for the first time, set number to 4 for next number and return reinforcements
            reinforcements = 4; // initial numbers
            incCounter++; // Increment counter after first call
            return reinforcements;
        }
        else if (incCounter == 1) { // if called for the second time
            reinforcements = 6; // Update number to 6 for next number
            incCounter++; // increment the counter
            return reinforcements;
        }
        else if (incCounter == 2) { // If called for the third time
            reinforcements = 8; // Set the number of reinforcements to 8 for next number
            incCounter++; // Increment counter
            return reinforcements;
        }
        else if (incCounter == 3) { // If called for the fourth time
            reinforcements = 10; // Set number of reinforcements to 10 for next number
            incCounter++;// Increment the counter to keep track
            return reinforcements;
        }
        else if (incCounter == 4) { // If called for the fifth time
            reinforcements = 12; // Set number of reinforcements to 12 for next number
            incCounter++;// Increment the counter to keep track
            return reinforcements;
        }
        else if (incCounter == 5) {
            reinforcements = 15; // Set number of reinforcements to 15 for next number
            incCounter++;// Increment the counter to keep track
            return reinforcements;
        }
        else {
            reinforcements += 5; // Add 5 to the number of reinforcements
            incCounter++;// Increment the counter to keep track
            return reinforcements;
        }
    }// end NumberReinforcement method

    public int getCurrentReinforcements() { // start method without increment which return reinforcements
        return reinforcements;
    }// end method

} // end program
