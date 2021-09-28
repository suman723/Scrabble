import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Used to create the deck of cards. Allows players to create a new deck, shuffle the deck and draw cards from the top of the deck
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class PlayDeck
{
	private ArrayList<Card> cards;

	/**
	 * Constructor
	 */
	public PlayDeck()
	{
		cards = new ArrayList<>();
		build();
		this.shuffle();

	}

	private void build()
	{
		for (int i = 0; i < Constants.NUM_COUNTRIES+2; i++)
		{
			cards.add(new Card(i));
		}
	}

	public void shuffle()
	{
		Random rand = new Random();
		for (int i = 1; i < cards.size(); i++)
		{
			int r = rand.nextInt(i + 1);	// Gets a number from 0 - i inclusive
			Collections.swap(cards, i, r);
		}
	}

	public int size()
	{
		return cards.size();
	}

	public Card drawCard()
	{
		// If deck is empty throw exception
		if (size() == 0)
			throw new ArrayIndexOutOfBoundsException("Empty deck, cannot draw a card");
		
		return cards.remove(0);
	}
	
	
	// added to get country id array out of remaining deck
	public int[] getCountryIDs() {
		int[] c = new int[cards.size()];
		for(int i=0;i<cards.size();i++) {
			c[i] = cards.get(i).getCardID();
		}
		return c;
	}

	public static void main(String[] args)
	{
		PlayDeck deck = new PlayDeck();
//		deck.shuffle();
		int spot = 1;
		while(deck.size() > 0)
		{
			System.out.println("Card " + spot++);
			System.out.println(deck.drawCard());
			System.out.println("");
		}

	}

}
