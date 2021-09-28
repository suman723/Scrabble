/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CardTest {

	@Test
	void cardGetters() {
		
		Card tCard = new Card(10);
		Card tCard2 = new Card(43);
		
		try {
			assertEquals(10,tCard.getCardID());
			assertEquals("Europe",tCard.getContinent());
			assertEquals("W Europe",tCard.getCountry());
			assertEquals("infantry",tCard.getInsignia());
			assertEquals(false,tCard.isWild());
			
		} catch (IllegalArgumentException ex) {}
		
		try {
			assertEquals(true,tCard2.isWild());
		} catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	void cardSetters() {
		
		Card testCard = new Card(2);

		testCard.setCardID(12); testCard.setCountry(12);
		testCard.setContinent(12);

		
		try {
			assertEquals(12,testCard.getCardID());
			assertEquals("Europe",testCard.getContinent());
			assertEquals("Ukraine",testCard.getCountry());
			
		} catch (IllegalArgumentException ex) {}
		
	}
	
	@Test
	void cardToString() {
		
		Card testCard = new Card(2);
		Card testCard2 = new Card(43);
		Card testCard3 = new Card(44);
		
		try {
			assertEquals("Card Number:\t2\nCountry:\tNW Territory\nContinent:\tN America\nInsignia:\tartillery\n",testCard.toString());
			assertEquals("Card Number:\t43\nWild Card\n",testCard2.toString());
			assertEquals("Card Number:\t44\nWild Card\n",testCard3.toString());
		} catch (IllegalArgumentException ex) {}

		
	}
	
	@Test
	void testCardIDBounds() {
		try {
			Card testCard = new Card(-10);
			fail("Allowed out of bounds id to be entered");
		} catch (IllegalArgumentException ex) {}
		
		try {
			Card testCard2 = new Card(45);
			fail("Allowed out of bounds id to be entered");
		} catch (IllegalArgumentException ex) {}
		
	} 

}
