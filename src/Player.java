import java.awt.*;
import java.io.IOException;

/**
 * Allows for the construction of Player objects.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class Player
{
	private int id;
	private String name;
	private Color color;
	private String colorName;
	private boolean human;
	private int remainingArmies;
	private PlayerHand hand;

	/**
	 * Creates a Player object
	 *
	 * @param id number to identify player
	 * @param name player's name
	 * @param color colour for player on map
	 * @param colorName user friendly text for colour
	 * @param human if the player is human (or a neutral passive)
	 * @param remainingArmies armies left to place by the player onto the map
	 */
	public Player(int id, String name, Color color, String colorName, boolean human, int remainingArmies)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.colorName = colorName;
		this.human = human;
		this.remainingArmies = remainingArmies;
		hand = new PlayerHand();
	}

	// Getters

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Color getColor()
	{
		return color;
	}

	public boolean isHuman()
	{
		return human;
	}

	public int getRemainingArmies()
	{
		return remainingArmies;
	}

	public String getColorName()
	{
		return colorName;
	}

	public PlayerHand getHand() { return hand;}

	// Setters

	// TODO: validate armies are not negative

	public void setRemainingArmies(int remainingArmies)
	{
		if(remainingArmies < 0) {
			throw new IllegalArgumentException("Error: Armies less than zero");
		}
		this.remainingArmies = remainingArmies;
	}

	/**
	 *  Instead of setting the remaining armies we can use this change the number
	 *  change can be a positive or negative integer.
	 *
	 * @param change
	 */
	public void updateRemainingArmies(int change)
	{
		if(change + this.remainingArmies < 0) {
			this.remainingArmies = 0;
		}
		this.remainingArmies += change;
	}
	
	public String toString() { 
		
		StringBuilder playerColor = new StringBuilder();
		
		switch(this.id) {
			case 0: playerColor.append("Red"); break;
			case 1: playerColor.append("Green"); break;
			case 2: playerColor.append("Blue"); break;
			case 3: playerColor.append("Orange"); break;
			case 4: playerColor.append("Light Blue"); break;
			default: playerColor.append("Violet"); break;
		}
		return "Player " + this.id
				+ ":\t" + this.name + "\n"
				+ "Color:\t\t" + playerColor.toString() + "\n"
				+ "Armies:\t\t" + this.remainingArmies + "\n";
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Player p1 = new Player(3,"Kahn",Constants.P3,true,36);
		//System.out.print(p1.toString());
	}
}
