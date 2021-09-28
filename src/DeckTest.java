/**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 **/


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest
{
	private Deck testDeck;
	@BeforeEach
	void setupDeck()
	{
		testDeck = new Deck();
	}

	@Test
	void shuffleNoLostOrChangedCardsTest()
	{
		int sizeBefore = testDeck.size();
		testDeck.shuffle();
		assertEquals(sizeBefore, testDeck.size());

		// Make sure each card is still in the deck and only occurs once (using pigeon hole)
		// initial value in array of booleans is false

		boolean[] found = new boolean[testDeck.size()];
		while(testDeck.size() != 0)
		{
			Card testCard = testDeck.drawCard();
			// Make sure ID is between 0 and NUM_COUNTRIES - 1
			if (testCard.getCardID() < 0 || testCard.getCardID() >= Constants.NUM_COUNTRIES)
				fail("Card ID now outside range");

			// Make sure ID has not been seen before and set it to seen
			if (found[testCard.getCardID()])
				fail("Card exists twice in deck now");

			found[testCard.getCardID()] = true;
		}
		// Passed test
	}

	@Test
	void shuffleChangesDeckTest()
	{
		// Using 5 decks to make the odds of shuffling them in order increasingly unlikely
		Deck deck1 = new Deck();
		Deck deck2 = new Deck();
		Deck deck3 = new Deck();
		Deck deck4 = new Deck();
		Deck deck5 = new Deck();

		// Shuffle all but first deck
		deck2.shuffle();
		deck3.shuffle();
		deck4.shuffle();
		deck5.shuffle();

		// Loop and draw card from each deck until deck is empty or if the same card is not on top of each deck
		boolean allSame = true;
		while (deck1.size() != 0 && allSame)
		{
			int id1, id2, id3, id4, id5;
			id1 = deck1.drawCard().getCardID();
			id2 = deck2.drawCard().getCardID();
			id3 = deck3.drawCard().getCardID();
			id4 = deck4.drawCard().getCardID();
			id5 = deck5.drawCard().getCardID();

			// If any of the cards do not match id1 then shuffle has changed at least one deck
			if ( !(id1 == id2 && id1 == id3 && id1 == id4 && id1 == id5) )
				allSame = false;
		}
		// If there's still cards left to draw shuffle has changed at least one deck from the original
		if (deck1.size() == 0)
			fail("All decks were the same");

		//Pass
	}

	@Test
	void sizeTest()
	{
		assertEquals(Constants.NUM_COUNTRIES, testDeck.size());
		testDeck.drawCard();
		testDeck.drawCard();
		testDeck.drawCard();
		testDeck.drawCard();
		assertEquals(Constants.NUM_COUNTRIES - 4, testDeck.size());

	}

	@Test
	void drawCardTest()
	{
		assertEquals(0, testDeck.drawCard().getCardID());
		assertEquals(1, testDeck.drawCard().getCardID());
		// draw all cards
		while(testDeck.size() > 0)
		{
			testDeck.drawCard();
		}
		// try and draw card from empty deck
		try
		{
			testDeck.drawCard();
			fail("Drew from empty deck");
		} catch (ArrayIndexOutOfBoundsException ex) {}

	}
}