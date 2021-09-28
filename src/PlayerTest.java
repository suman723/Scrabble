/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 **/

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	void playerGetters() {
		
		Player playerTest = new Player(3,"Kahn",Constants.P3, Constants.P3name,true,36);
		
		try {
			assertEquals(36,playerTest.getRemainingArmies());
			assertEquals("Kahn",playerTest.getName());
			assertEquals(Constants.P3, playerTest.getColor());
			assertEquals(true,playerTest.isHuman());
			assertEquals(3,playerTest.getId());
			
		} catch (IllegalArgumentException ex) {}
		
	}
	
	@Test
	void playerSetters() {
		
		Player playerTest = new Player(3,"Kahn",Constants.P3, Constants.P3name,true,36);
		
		playerTest.setRemainingArmies(5);
		playerTest.updateRemainingArmies(5);
		
		try {
			assertEquals(10,playerTest.getRemainingArmies());
			assertEquals("Kahn",playerTest.getName());
			assertEquals(Constants.P3, playerTest.getColor());
			assertEquals(true,playerTest.isHuman());
			assertEquals(3,playerTest.getId());
			
		} catch (IllegalArgumentException ex) {}

	}
	
	@Test
	void playerToString() {
		
		Player playerTest = new Player(3,"Kahn",Constants.P3, Constants.P3name,true,36);
		try {
			assertEquals("Player 3:\tKahn\nColor:\t\tOrange\nArmies:\t\t36\n",playerTest.toString());
		} catch (IllegalArgumentException ex) {}
		
	}
	
	@Test
	void testArmyUnitBounds() {
		
		try {
			Player playerTest = new Player(3,"Kahn",Constants.P3, Constants.P3name, true,-2);
			Player playerTest2 = new Player(4,"Alexander",Constants.P4, Constants.P4name,true,24);
			playerTest2.updateRemainingArmies(-30);
			playerTest.setRemainingArmies(-50);
			fail("Allowed Armies of less than 0");
		} catch (IllegalArgumentException ex) {}
		
	} 

}
