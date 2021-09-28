import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class extends JPanel and is used in the creation of the command terminal for user input.
 * When input is requested from an outside source it activates the terminal and watches for an action.
 * It has different modes to handle different types of expected input which are set by calling the correct method
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class CommandPanel extends JPanel implements ActionListener, DocumentListener
{
	// Keeps track of what type of input in is currently polling for
	private enum Mode {OFF, TEXT, COUNTRY, NUMBER, CONFIRMATION, TRADING}
	private Mode mode;
	private boolean skipEnabled;		// Sub mode for country

	// Components
	private final JTextField commandField;
	private final JLabel commandLabel;

	// Buffer that stores user input
	private final LinkedList<String> commandBuffer = new LinkedList<>();

	// Store requested data options. Will determine text colour and output type
	private final Countries countries = Countries.getInstance();
	private ArrayList<Country> countryOptions;
	private PlayerHand playerHand;
	private int min, max;

	/**
	 * Constructor.
	 * GridBagLayout seemed to work best for this panel but it is open to change.
	 * Creates a label and textField and disables the field until input is required.
	 */
	public CommandPanel(int width, int height)
	{
		// Call JPanel constructor
		super();
		// https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html

		setPreferredSize(new Dimension(width, height));

		// Layout
		//this.setLayout(new GridBagLayout());
		//GridBagConstraints c = new GridBagConstraints();
		this.setLayout(new BorderLayout());
		this.setBackground(Constants.matrixBlack);

		// Construct Label
		commandLabel = new JLabel("Command Terminal:");
		commandLabel.setForeground(Constants.matrixMidGreen);

		// Construct the TextField and disable until needed
		commandField = new JTextField(50);
		commandField.setBackground(Constants.matrixBlack);
		commandField.setForeground(Constants.matrixLightGreen);
		Font font = new Font("Verdana",Font.BOLD,12);
		commandField.setFont(font);
		// Add listeners; action when user hits enter, document when the text changes
		commandField.addActionListener(this);
		commandField.getDocument().addDocumentListener(this);
		commandField.setCaretColor(Constants.matrixDarkGreen);
		//commandField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		// Disable terminal until polled for data
		disableTerminal();

		// Add components to the panel
		add(commandLabel, BorderLayout.NORTH);
		add(commandField, BorderLayout.CENTER);
		//c.gridx = 0;
		//c.gridy = 0;
		//c.anchor = GridBagConstraints.NORTHWEST;
		//add(commandLabel, c);
		//c.ipadx = 200;
		//c.gridy = 1;
		//c.weighty = 1;		// Pushes it to the top along with everything above it
		//add(commandField, c);
	}

	/**
	 * This is called when the user submits a command.
	 * It updates the command Buffer to ready for output.
	 *
	 * @param actionEvent unused.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		synchronized (commandBuffer)
		{
			commandBuffer.add(commandField.getText());
			disableTerminal();
			commandBuffer.notify();
		}
	}

	// Document Listeners. Will fire whenever the text changes in the textField
	// For now all three just call textChanged method
	@Override
	public void insertUpdate(DocumentEvent documentEvent)
	{
		//System.out.println("Inserted");
		textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent documentEvent)
	{
		//System.out.println("Removed");
		textChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent documentEvent)
	{
		//System.out.println("Ch-Ch-Changes");
		textChanged();
	}

	/**
	 * Will handle changing the look of the panel depending on the current text in the textField
	 * Things like text colour will be updated depending on various conditions
	 */
	private void textChanged()
	{
		switch (mode)
		{
			case OFF:
				break;
			case TEXT:
				commandField.setForeground(Constants.matrixLightGreen);
				break;
			case COUNTRY:
				// If there is only one country that starts with the user entered data set text to green. Otherwise red.
				// Also allow for "skip" if that is an option afforded to the player
				// Visual indicator that the game knows what they are trying to enter
				if (countries.selectCountriesByName(commandField.getText(), countryOptions).size() == 1)
				{
					commandField.setForeground(Constants.matrixLightGreen);
				}
				else if (skipEnabled && commandField.getText().equalsIgnoreCase("skip"))	// Checks for skip
				{
					commandField.setForeground(Constants.matrixLightGreen);
				}
				else
				{
					commandField.setForeground(Constants.errorRed);
				}
				break;
			case NUMBER:
				// If number is between min and max set colour to green. Otherwise red.
				int numSoFar;
				// Try and convert text.
				try
				{
					numSoFar = Integer.parseInt(commandField.getText());
					// If successful check if in range and adjust accordingly
					if (numSoFar >= min && numSoFar <= max)
					{
						commandField.setForeground(Constants.matrixLightGreen);
					}
					else
					{
						commandField.setForeground(Constants.errorRed);
					}
				}	catch (NumberFormatException nfe) { commandField.setForeground(Constants.errorRed); }	// Non number in command field should always error
				break;
			case CONFIRMATION:
				// should only be the letter y or n
				if (commandField.getText().equalsIgnoreCase("y") || commandField.getText().equalsIgnoreCase("n"))
				{
					commandField.setForeground(Constants.matrixLightGreen);
				}
				else
				{
					commandField.setForeground(Constants.errorRed);
				}
				break;
			case TRADING:
				// set red unless legal trade
				if (legalTrade(commandField.getText()))
				{
					commandField.setForeground(Constants.matrixLightGreen);
				}
				else
				{
					commandField.setForeground(Constants.errorRed);
				}
				break;
			default:
				System.out.println("Something has gone very wrong");
		}
	}

	/**
	 * Handles the internal async input for all the public facing methods. Returns user entered string.
	 * @param inputMode mode to set the command panel to
	 * @return user entered string
	 */
	private String getInput(Mode inputMode)
	{
		// Allow input
		enableTerminal();
		// Set mode
		mode = inputMode;
		// Waits for user input
		String command;
		synchronized (commandBuffer)
		{
			while (commandBuffer.isEmpty())
			{
				try {
					commandBuffer.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		// Gets input
		command = commandBuffer.pop();
		// Disables Terminal and resets mode
		disableTerminal();

		// Returns user input
		return command;
	}

	/**
	 * Method called to initiate getting user input.
	 * Used when the caller doesn't care what output it gets.
	 * Waits for the commandBuffer to be non empty and returns the first element
	 *
	 * @return user inputted string.
	 */
	public String getUserInput()
	{
		return getInput(Mode.TEXT);
	}

	public Packet<Country> getCountryWithSkip(ArrayList<Country> options)
	{
		// setup
		countryOptions = options;
		skipEnabled = true;

		String command = getInput(Mode.COUNTRY);

		// Packet to return
		Packet<Country> ret;

		// Check for "skip"
		if(command.toLowerCase().equals("skip"))
		{
			return new Packet<>(null, Packet.Type.SUCCESS, "skipped");
		}

		// Matching countries. Gets back all countries that start with the text user entered.
		ArrayList<Country> matches = countries.selectCountriesByName(command, countryOptions);

		// Handle different sizes
		if (matches.size() == 0)	// None found
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Unknown selection " + command + ".",command);
		}
		else if (matches.size() == 1)	// Desired result
		{
			ret = new Packet<>(matches.get(0), Packet.Type.SUCCESS, command);
		}
		else 	// Multiple countries match entry
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Unambiguous selection " + command + ".", command);
		}

		return ret;
	}

	/**
	 * Called to get a country from the user. Takes a list of potential options in the form of arraylist
	 * Allows for partial entry of name. Returns a packet with a signal whether a unique country name was
	 * supplied and if successful puts that country into the packet as its data member.
	 *
	 * @param options ArrayList of potential country choices
	 * @return packet that countains selected country if possible and a signal whether or not polling was successful
	 */
	public Packet<Country> getCountry(ArrayList<Country> options)
	{
		// setup
		countryOptions = options;
		skipEnabled = false;

		String command = getInput(Mode.COUNTRY);

		// Packet to return
		Packet<Country> ret;

		// Matching countries. Gets back all countries that start with the text user entered.
		ArrayList<Country> matches = countries.selectCountriesByName(command, countryOptions);

		// Handle different sizes
		if (matches.size() == 0)	// None found
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Unknown selection " + command + ".", command);
		}
		else if (matches.size() == 1)	// Desired result
		{
			ret = new Packet<>(matches.get(0), Packet.Type.SUCCESS, command);
		}
		else 	// Multiple countries match entry
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Unambiguous selection " + command + ".", command);
		}

		return ret;

	}

	public Packet<Integer> getNumber(int min, int max)
	{
		// Sets min and max. They are used outside this method in the text update method
		this.min = min;
		this.max = max;

		String command = getInput(Mode.NUMBER);

		// Packet to return
		Packet<Integer> ret;

		// Try and convert command to integer
		try
		{
			int numEntered = Integer.parseInt(command);
			// If successful check within range and return appropriate packet
			if (numEntered >= min && numEntered <= max)
			{
				ret = new Packet<>(numEntered, Packet.Type.SUCCESS, command);
			}
			else
			{
				ret = new Packet<>(null, Packet.Type.ERROR, "Number is out of range. Expected between " + min + " - " + max +
						". Got " + numEntered, command);
			}
		} catch (NumberFormatException n)
		{
			// Text entered wasn't a number
			ret = new Packet<>(null, Packet.Type.ERROR, "Expected a number but got text instead: " + command, command);
		}

		// Return
		return ret;
	}

	public Packet<Boolean> getAffirmation()
	{
		String command = getInput(Mode.CONFIRMATION);

		// Packet to return
		Packet<Boolean> ret;

		if (command.equalsIgnoreCase("y"))
		{
			ret = new Packet<>(true, Packet.Type.SUCCESS, command);
		}
		else if (command.equalsIgnoreCase("n"))
		{
			ret = new Packet<>(false, Packet.Type.SUCCESS, command);
		}
		else
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Unknown response. Expected: Y/N Got: " + command, command);
		}

		return ret;

	}

	public Packet<String> getTrade(PlayerHand hand)
	{
		// Setup
		this.playerHand = hand;

		String command = getInput(Mode.TRADING);

		// Packet to return
		Packet<String> ret;

		// Check legal trade and return based on result
		if (legalTrade(command))
		{
			ret = new Packet<>(command, Packet.Type.SUCCESS, command);
		}
		else
		{
			ret = new Packet<>(null, Packet.Type.ERROR, "Cannot trade in for " + command, command);
		}

		return ret;
	}

	/**
	 * Disables the text field and stops users giving it focus
	 */
	private void disableTerminal()
	{
		// Set mode to off
		mode = Mode.OFF;
		commandField.setText("");
		commandField.setEditable(false);
		commandField.setFocusable(false);

	}

	/**
	 * Checks if a string is legal AND the player has the cards to trade
	 * @param tradeText text that the user has typed into the command field
	 * @return true if player can trade the matching string
	 */
	private boolean legalTrade(String tradeText)
	{
		// false if not length 3
		if (tradeText.length() != 3) return false;

		// lowercase
		tradeText = tradeText.toLowerCase();

		// set up counts of all four options (iacw)
		int i = 0;
		int a = 0;
		int c = 0;
		int w = 0;

		// loop over string and add up
		for (int j = 0; j < tradeText.length(); j++)
		{
			switch (tradeText.charAt(j))
			{
				case 'i':
					i++;
					break;
				case 'a':
					a++;
					break;
				case 'c':
					c++;
					break;
				case 'w':
					w++;
					break;
				default:
					return false;		// different letter not allowed
			}
		}

		// fail states: if wilds are 3, if any of i/a/c are 2 and wild is not set to 1
		if (w == 3 || ( ( i == 2 || a == 2 || c == 2) && w != 1)) return false;

		// ensure players hand can match
		if (playerHand.insigniaCount("infantry") < i) return false;
		if (playerHand.insigniaCount("artillery") < a) return false;
		if (playerHand.insigniaCount("cavalry") < c) return false;
		if (playerHand.insigniaCount("wild") < w) return false;

		// all conditions met
		return true;
	}

	/**
	 * Enables the text field and allows entry
	 */
	private void enableTerminal()
	{
		commandField.setEditable(true);
		commandField.setFocusable(true);
		commandField.grabFocus();
	}


	public static void main(String[] args) throws InterruptedException
	{
		// Simple frame to add my panel to
		JFrame myFrame = new JFrame();
		myFrame.setLayout(new BorderLayout());
		// Create panel
		CommandPanel cmd = new CommandPanel(400,250);
		// Add to frame and set some values on the frame
		myFrame.setSize(400,250);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.add(cmd, BorderLayout.CENTER);
		myFrame.setVisible(true);
//		Countries countries = Countries.getInstance();
//		while(true)
//		{
//			Packet<Country> test = cmd.getCountry(countries.asList());
//			System.out.println(test);
//		}
//		while(true)
//		{
//			Packet<Integer> test = cmd.getNumber(0, 6);
//			System.out.println(test);
//		}
		// Construct player hand
		Card ontario = new Card(0);
		Card greenland = new Card(4);
		Card cAmerica = new Card(7);
		Card wild = new Card(42);

		PlayerHand hand = new PlayerHand();
		hand.addCard(ontario);
		hand.addCard(greenland);
		hand.addCard(cAmerica);
		hand.addCard(wild);

		System.out.println("cavalry count = " + hand.insigniaCount("cavalry"));

		while(true)
		{
			Packet<String> test = cmd.getTrade(hand);
			System.out.println(test);
		}
	}


}
