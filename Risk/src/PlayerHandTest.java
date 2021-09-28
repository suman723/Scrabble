import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PlayerHandTest {
	
	
	static GoldenCalvary gcu;
	static PlayDeck deck;
	static PlayerHand hand;
	
	@BeforeAll
	static void setup() {
		gcu = new GoldenCalvary();
		deck = new PlayDeck();
		
		hand = new PlayerHand();
	}

	@Test
	void testAddCardAndMustTrade() {
				
		for(int i=0;i<4;i++) {
			hand.addCard(deck.drawCard());
		}
		assertEquals(false,hand.mustTrade());
		assertEquals(4,hand.size());
		hand.addCard(deck.drawCard());
		assertEquals(true,hand.mustTrade());
		assertEquals(5,hand.size());
		
	}
	
	@Test
	void testRemoveCard() {
		
		hand.removeCardAt(0);
		
		assertEquals(4,hand.size());
	}
	
	@Test
	void testCanTrade() {
		
		assertEquals(true,hand.canTrade());
		
	}
	
	@Test
	void testTradeCards() {

		hand.addCard(deck.drawCard());
		hand.addCard(deck.drawCard());
		hand.addCard(deck.drawCard());
		
		assertEquals(4,hand.tradeCards("cia", gcu));
		assertEquals(5,hand.size());
		

	}
	
	
}
