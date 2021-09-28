import java.util.ArrayList;

public class PlayerHand {

	// the drawCard method from PlayDeck will be used when adding a card
	// cards will be added one at a time if the player successfully takes over
	// a new country
	
	private ArrayList<Card> cards; 
	
	// constructor for the players hand which initially starts out empty
	public PlayerHand() {
		cards = new ArrayList<>();
	} 
	
	// returns the size of the players hand
	public int size() {
		return cards.size();
	}
	
	// returning an array of country ids is currentyly not necessary but is still here just in case
	// ** delete if never used **
	public int[] getCountryIDs() {
		int[] c = new int[cards.size()];
		for(int i=0;i<cards.size();i++) {
			c[i] = cards.get(i).getCardID();
		}
		return c;
	}
	
	/**
	 * Method returns true if the player has more than five cards
	 * @return
	 */
	public boolean mustTrade() {
		// if the players hand is 5 or more, the player must make a trade
		if(this.size()>4) {
			return true;
		}
		return false;
	}
	
	public String mustTradeMessage(Player activePlayer) {
		return activePlayer.getName() + ", you have " + this.size() + " cards.\n"
				+ "You must make a trade.\n";
	}
	
	/**
	 * Method returns true if the player has at least one possible trade option
	 * @return
	 */
	public boolean canTrade() {
		
		// if the players hand is less then three then there are not enough to trade with
		if(this.size()<3) {
			return false;
		}
		
		// ints to keep track of card types and to determine trade-ability
		int infantry = 0;
		int cavalry = 0;
		int artillery = 0;
		
		// loop through the players hand, checking insignias
		for(int j=0;j<this.size();j++) {
			if(this.cards.get(j).getInsignia().equals("artillery")) {
				artillery++;
			} else if(this.cards.get(j).getInsignia().equals("infantry")) {
				infantry++;
			} else if(this.cards.get(j).getInsignia().equals("cavalry")) {
				cavalry++;
			} else if(this.cards.get(j).isWild()) {
				artillery++;
				infantry++;
				cavalry++;
			}
		}
		
		// if the player has 3 of the same type of card or
		// if the player has at least one of each card
		// then the player can make a trade
		if(infantry >= 3 || cavalry >= 3 || artillery >= 3 
				|| (infantry>0 && artillery>0 && cavalry>0)) {
			return true;
		}
		

		// if the player does not have at least three of one type or one of each
		// then the player cannot make a trade
		return false;
		
	}

	/**
	 * Counts the number of a specific type of insignia in the hand. Treats wild as its own insignia
	 *
	 * @param insignia one of: "infantry", "artillery", "cavalry", or "wild"
	 * @return count of said cards in hand
	 */
	public int insigniaCount(String insignia)
	{
		int count = 0;
		if (insignia.equals("wild"))
		{
			for (Card card : cards)
			{
				if (card.isWild())
					count++;
			}
		}
		else
		{
			// loop over cards and add any that match to count. Check insignia exists first due to wilds
			for (Card card : cards)
			{
				if (card.getInsignia() != null && card.getInsignia().equals(insignia))
					count++;
			}
		}
		return count;
	}

	/**
	 * Calls insignia count on all 4 types. Builds an array from the results with indexing as follows:
	 * 0: infantry 1: cavalry 2: artillery 3: wild
	 * @return size 4 array of counts
	 */
	public int[] allInsigniaCounts()
	{
		int[] counts = new int[4];

		counts[0] = insigniaCount("infantry");
		counts[1] = insigniaCount("cavalry");
		counts[2] = insigniaCount("artillery");
		counts[3] = insigniaCount("wild");

		return counts;
	}

	/**
	 * Adds a card to the hand
	 * @param card card object to add
	 */
	public void addCard(Card card)
	{
		cards.add(card);
	}

	/**
	 * Removes card at index from hand
	 * @param index index of card to remove
	 */
	public void removeCardAt(int index)
	{
		cards.remove(index);
	}

	/**
	 * Removes the card object from the hand if
	 * it is in it.
	 * @param card card to remove from hand
	 */
	public void removeCard(Card card)
	{
		cards.remove(card);
	}
	
	/**
	 * method to display a list of the players cards
	 * only displays the first letter of the insignia
	 * output should look like this [a,a,a,c,w]
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append('[');
		for(int i=0;i<this.size();i++) {
			if(i==this.size()-1) {
				s.append(this.cards.get(i).getInsignia().charAt(0) + "]");
			} else {
				s.append(this.cards.get(i).getInsignia().charAt(0) + ",");
			}
		}
		return s.toString();
	}
	
	/**
	 * Method takes in a string such as "iii" or "aic" or "ccw"
	 * also takes in a Golden Calvary Unit to determine the return value
	 * as part of the trade, the cards are removed from the players hand
	 * @param trade
	 * @param gcu
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int tradeCards(String trade,GoldenCalvary gcu) throws IllegalArgumentException {
		if(trade.length()>3) {
			throw new IllegalArgumentException("trade String is too long");
		}
		
		for(int i = 0;i<trade.length();i++) {
			tradeCardsHelper(trade.charAt(i));
		}
		
		
		return gcu.getNumberReinforcements(); 
	}
	
	/**
	 * method updates the players hand by removing the traded cards
	 * @param trade
	 */
	public void tradeCardsHelper(char trade) {
		for(int i = 0; i<this.size();i++) {
			if(this.cards.get(i).getInsignia().charAt(0) == trade) {
				this.removeCardAt(i);
			}
		}
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoldenCalvary gcu = new GoldenCalvary();
		PlayDeck deck = new PlayDeck();
		
		PlayerHand hand = new PlayerHand();
		
		for(int i=0;i<5;i++) {
			hand.addCard(deck.drawCard());
		}

		System.out.println(hand.toString()+"\n"+hand.size());
		hand.removeCardAt(0);
		System.out.println(hand.toString()+"\n"+hand.size());

	}

}
