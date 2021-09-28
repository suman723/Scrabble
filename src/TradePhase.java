/**
* Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
*/

public class TradePhase{
	
	/**
	 * method returns the current trade in value based on the gcu position
	 * @param gcu
	 * @return
	 */
	public int getTradeValue(GoldenCalvary gcu) {
		return gcu.getNumberReinforcements();
	}
	
	/**
	 * Method to regulate territory card trades
	 * 
	 * @param gcu
	 * @param activePlayer
	 * @param ui
	 */
	public static void trade(GoldenCalvary gcu, Player activePlayer, UserInterface ui) {
			
		PlayerHand hand = activePlayer.getHand();
		boolean mustTrade = activePlayer.getHand().mustTrade();
		boolean canTrade = activePlayer.getHand().canTrade();
		
		ui.println("*Trade Territory Cards* \n");
		
		// check hand size, print hand if not zero, print cannot trade message and return if zero
		if(hand.size()!=0) {
			ui.println(activePlayer.getName() + 
				 	", these are your territory cards: " + hand.toString());
		} else {
			ui.println(activePlayer.getName() + ", you have " + hand.size() + 
					" territory cards\n and are unable to make a trade at this time.");
			return;
		}
		
		
		// checks if player can make a trade. if so it asks player to make a trade
		if(canTrade || mustTrade) {
			int numOfPossibleReinforcements = gcu.getCurrentReinforcements();
			ui.println("Trading in is worth " + numOfPossibleReinforcements + " reinforcements");
			if(!mustTrade) ui.println(activePlayer.getName() + ", would you like to trade in territory cards?\n(Enter y/n)");
			// check to see if the player must make a trade and prints a message if so
			if(mustTrade) ui.println(hand.mustTradeMessage(activePlayer));
			// display players hand and ask for cards to trade
			if(mustTrade || ui.getAffirmation()) {
				ui.println("Your hand is: " + hand.toString());
				String trade = ui.getTrade(hand);
				activePlayer.updateRemainingArmies(hand.tradeCards(trade, gcu));
				ui.println(activePlayer.getName() + ", you now have " + activePlayer.getRemainingArmies() + " armies to deploy.");
			}	
		} else {
			ui.println(activePlayer.getName() + ", you have " + hand.size() + 
					" territory cards.\n" + hand.toString() + "\n.However, you are unable to make a trade at this time.");
		}
		
	}
	

}