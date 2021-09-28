import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * User Interface handles all UI elements of the game and acts as a go between for the game object
 * and the various panels.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class UserInterface{
	private final int width, height;
	private JFrame frame;
	private MessagePanel msg;
	private MapPanel map;
	private CommandPanel terminal;
	private Countries countries;
	
	public UserInterface() {
		frame = new JFrame();

		JPanel messages = new JPanel();
		JLabel messageLabel = new JLabel("Instructions");

		// Set dimensions of of ui
		this.width = 1300;
		this.height = 650;

		// Map takes up 100% height
		final int mapWidth = 1000;
		MapPanel map = new MapPanel(mapWidth, height);

		// Output and input panels
		msg = new MessagePanel(width - mapWidth, 570);
		terminal = new CommandPanel(width-mapWidth, 50);

		this.map = map;
		frame = new JFrame();
		
	    	
	}
	
	public void drawUI() {
		frame.setSize(width, height);
		frame.setTitle("Das Klos Farrelly: Risk");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		
		// draw the map in the west panel
		frame.add(map, BorderLayout.WEST);

		// panel that holds both input and output panels
		JPanel textPane = new JPanel();
		textPane.setPreferredSize(new Dimension(300, 650));
		textPane.setLayout(new BorderLayout());
		textPane.add(msg, BorderLayout.NORTH);
		textPane.add(terminal, BorderLayout.SOUTH);
		frame.add(textPane,BorderLayout.EAST);

	}

	public void repaintMap()
	{
		map.revalidate();
		map.repaint();
	}

	/**
	 * Prints text to message panel. Does not add new line
	 * @param text text to print
	 */
	public void print(String text) {msg.print(text);}

	/**
	 * Prints text to message panel and adds new line
	 * @param text text to print
	 */
	public void println(String text)
	{
		msg.println(text);
	}

	/**
	 * Prints text to message panel in colour specified. Does not add a new line
	 * @param text text to print
	 * @param color colour to set text
	 */
	public void print(String text, Color color) {msg.print(text, color);}

	/**
	 * Prints text to message panel in colour specified and adds new line
	 * @param text text to print
	 * @param color colour to set text
	 */
	public void println(String text, Color color) {msg.println(text, color);};

	/**
	 * Gets user input string and returns it, no validation done on string.
	 * @return user entered string
	 */
	public String getInput()
	{
		String input = terminal.getUserInput();
		// Print entry
		msg.printInputLine(input);
		return input;
	}

	/**
	 * Takes a list of potential countries and repeatedly asks user until one is
	 * successfully chosen.
	 * @param options countries user can pick from
	 * @return chosen country
	 */
	public Country getCountry(ArrayList<Country> options)
	{
		Packet<Country> response = null;

		do
		{
			// If response is not null then error occurred on last loop
			if (response != null)
			{
				println("Error selecting country:");
				println(response.getMessage());
			}
			response = terminal.getCountry(options);
			// print user input
			msg.printInputLine(response.getUserInput());
		} while (response.getType() == Packet.Type.ERROR);

		// Return selected country
		return response.getData();
	}

	/**
	 * Takes a list of potential countries and repeatedly asks user until one is
	 * successfully chosen. User also has option to choose to skip which will return null
	 * @param options countries user can pick from
	 * @return chosen country or null if skip is entered
	 */
	public Country getCountryWithSkip(ArrayList<Country> options)
	{
		Packet<Country> response = null;

		do
		{
			// If response is not null then error occurred on last loop
			if (response != null)
			{
				println("Error selecting country:");
				println(response.getMessage());
			}
			response = terminal.getCountryWithSkip(options);
			// print user input
			msg.printInputLine(response.getUserInput());
		} while (response.getType() == Packet.Type.ERROR);

		// Return selected country
		return response.getData();
	}

	/**
	 * Asks the user to enter y/n and returns matching boolean
	 * repeats the request until one is chosen
	 * @return true if user enters y, false if n
	 */
	public boolean getAffirmation()
	{
		Packet<Boolean> response = null;
		do
		{
			// If response is not null then error occurred on last loop
			if (response != null)
			{
				// handle error
				println("Error:");
				println(response.getMessage());
			}
			response = terminal.getAffirmation();
			// print user input
			msg.printInputLine(response.getUserInput());
		} while (response.getType() == Packet.Type.ERROR);

		// returns true or false
		return response.getData();
	}

	/**
	 * Gets a number entered by the user. Repeats the request until a number between min and max is returned
	 * @param min inclusive minimum int
	 * @param max inclusive maximum int
	 * @return verified selected number
	 */
	public int getNumber(int min, int max)
	{
		// Set up response object from command panel
		Packet<Integer> response = null;

		// Loop until response type is not an error
		do
		{
			// If response is not null then error occurred on last loop
			if (response != null)
			{
				println("Error selecting number:");
				println(response.getMessage());
			}
			// get input from user
			response = terminal.getNumber(min, max);
			// print user input
			msg.printInputLine(response.getUserInput());

		} while (response.getType() == Packet.Type.ERROR);

		return response.getData();
	}

	/**
	 * Gets a choice from the player in the form of "aaa" "iac" "acw" etc..
	 *
	 * @param hand the players hand they are trying to trade in
	 * @return string representing the trade
	 */
	public String getTrade(PlayerHand hand)
	{
		// Set up response object from command panel
		Packet<String> response = null;

		// Loop until response type is not an error
		do
		{
			// If response is not null then error occurred on last loop
			if (response != null)
			{
				println("Error entering trade:");
				println(response.getMessage());
			}
			// get input from user
			response = terminal.getTrade(hand);
			// print user input
			msg.printInputLine(response.getUserInput());

		} while (response.getType() == Packet.Type.ERROR);

		return response.getData();
	}
	
	/**
	  *Assign Countries: method to assign starting countries to players and ai
	  *@param hands
	  *@param players
	  *@param countries instance
	  *@param shuffledDeck
	 */ 
	public void assignCountries(PlayerHand[] hands, Player[] players, Countries countries, Deck shuffledDeck) {
		
		
		// a very convoluted way to make a 2D array of the country id integers
		int[] p1 = hands[0].getCountryIDs();
		int[] p2 = hands[1].getCountryIDs();
		
		int[][] pc = new int[2][9]; // pc for player characters
		
		for(int i=0;i<2;i++) {
			for(int j=0;j<9;j++) {
				if(i==0) {
					pc[i][j] = p1[j];
				} else {
					pc[i][j] = p2[j];
				}
			}
		}
		
		// a just as convuloted way to make a 2D array of non player character country ids
		// I absolutely believe there is a better way to do this but this is what my brain spat out, it does work
		int[] neutrals = shuffledDeck.getCountryIDs();		
		int[][] npc = new int[4][6]; // npc for non player characters
		
		for(int i=0;i<4;i++) {
			for(int j=0;j<6;j++) {
				     if(i<1) { npc[i][j] = neutrals[j];}
				else if(i<2) { npc[i][j] = neutrals[j+6];}
				else if(i<3) { npc[i][j] = neutrals[j+12];}
				else if(i<4) { npc[i][j] = neutrals[j+18];}
			}
			
		}
		
		
		// run through the pc and npc arrays to assign countries and update armies on the map
		for(int i=0;i<6;i++) {
			if(i<2) {
				for(int j=0;j<9;j++) {
					Country country = countries.get(pc[i][j]);
					country.setOccupier(players[i]);
					country.setNumOfArmies(1);
					players[i].updateRemainingArmies(-1);
				} 
			} else {
				for(int j=0;j<6;j++) {
					Country country = countries.get(npc[i-2][j]);
					country.setOccupier(players[i]);
					country.setNumOfArmies(1);
					players[i].updateRemainingArmies(-1);
				} 
			
			}
		}
	} // end of method assignCountries
	
	public boolean validatePlayerName(String playerName) {
		if(playerName.trim().isEmpty()) {
			this.println("Your name cannot be blank.");
			return false;
		}
		return true;
	}
	

	
	
}