/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
public class PhaseOneTest {
	
	@Test
	void calculatesReinforcements() {
		
		
		String s1,s2,s3;
		Countries countries = Countries.getInstance();
		Player p1 = new Player(0, "Test Player 1", Constants.P0, Constants.P0name, true, 0);
		Player p2 = new Player(1, "Test Player 2", Constants.P0, Constants.P0name, true, 0);
		Player p3 = new Player(2, "Test Player 3", Constants.P0, Constants.P0name, true, 0);
		Country[] c = countries.asArray();
		for(int i=0;i<15;i++) { c[i].setOccupier(p1);}
		for(int i=15;i<20;i++) { c[i].setOccupier(p2);}
		for(int i=20;i<42;i++) { c[i].setOccupier(p3);}
		s1 = PhaseOne.calcReinforcements(p1,countries);
		s2 = PhaseOne.calcReinforcements(p2,countries);
		s3 = PhaseOne.calcReinforcements(p3,countries);
		assertEquals(10,p1.getRemainingArmies());
		assertEquals(3,p2.getRemainingArmies());
		assertEquals(14,p3.getRemainingArmies());
		assertEquals("*Receiving Reinforcements*\n" +
					"You are occupying 15 countries.\n" +
					"You will get 5 reinforcements.\n" +
					"You are occupying the following continents:\tN America \n" +
					"Your reinforcement bonus is: 5\n" +
					"Total reinforcements: 10\n",s1);
		assertEquals("*Receiving Reinforcements*\n" +
					"You are occupying 5 countries.\n" +
					"You will get 3 reinforcements.\n",s2);
		assertEquals("*Receiving Reinforcements*\n" +
						"You are occupying 22 countries.\n" +
					    "You will get 7 reinforcements.\n" +
						"You are occupying the following continents:\tAustralia S America Africa \n" +
					    "Your reinforcement bonus is: 7\n" +
						"Total reinforcements: 14\n",s3);
		
		
	}
}
