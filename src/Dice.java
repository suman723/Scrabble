import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class allows the user to roll the dice randomly
 * It has two rollDice method, one of them return single number between 1 nad 6
 * Another one will return array of number or values
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 **/

// create Dice class
public class Dice {


    // create constructor of Dice class
    public Dice() {

    }

    // Method when calling return one number between 1 nad 6
    public int rollDice() {
        Random Die = new Random();
        return Die.nextInt(5) + 1;
    }


    /**
     * This method is used to return the integer values of arrays between 1 and 6
     * representing the outcome of rolling dice
     * And the value in array  should be between 1 and 3 depending on
     * the number of dice rolled by the player. The number of dice rolled by the player is determined
     * by the argument of number_of_Dice
     **/
    public int[] rollDice(int numOfDice) {

        int[] arrayDice = new int[numOfDice];

        //for loop used to determine the roll within the bound
        for (int i = 0; i < arrayDice.length; i++) {
            Random Die = new Random();
            int rollDice = Die.nextInt(5) + 1;
            arrayDice[i] = rollDice;
        }
        // java library function Array.sort has been used
        Arrays.sort(arrayDice);
        //Collections.reverse(Arrays.asList(arrayDice));
        arrayDice = reverse(arrayDice);

        return arrayDice; // return statement of arrayDice
    }

    public int[] reverse(int[] diceRolls)
    {
        int[] reversed = new int[diceRolls.length];
        for (int i = 0; i < diceRolls.length; i++)
        {
            reversed[i] = diceRolls[(diceRolls.length - 1) - i];
        }
        return reversed;
    }

    // Main method to test the program
    public static void main(String[] args) {
        Dice dice = new Dice();
        System.out.println(" The result after calling the first method is:"+ dice.rollDice());

        int[] result = dice.rollDice(4);
        for (int i = 0; i < result.length; i++) {
            System.out.println(" The result after calling the second method is:" +result[i]);
        }

    }
} //end the program