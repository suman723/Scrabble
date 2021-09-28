import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

/**
 * Handles the drawing and display of all information on the game map.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class MapPanel extends JPanel{
	
	// create space for an image, the Risk Map
	private BufferedImage mapImage;
	
	/**
	 * MapPanel constructor
	 */
	public MapPanel(int width, int height) {
		
		// Call JPanel constructor
		super();
		
		//setPreferredSize(new Dimension(1000,650));
		setPreferredSize(new Dimension(width, height));
		// load the background image into mapImage
		// the file must be stored in the src folder to work properly
		// an exception is thrown if fails
		try {
			mapImage = ImageIO.read(this.getClass().getResource("RiskWorldMapImage.jpg"));
		} catch (IOException ex) {
			System.out.println("Could not find the image file " + ex.toString());
		}
	}
	
	/**
	 * the paintComponent method does all the drawing
	 */
	public void paintComponent(Graphics g) {
		
		// get an instance of the countries to pull from 
		// used for army units and country color
		Countries countries = Countries.getInstance();
		
		// turn on the graphics
		Graphics2D g2 = (Graphics2D) g; // for all graphics except connection lines
		
		// place the background image into the panel
		// -25 offset to compensate for position, 999*775 is the image resolution
		g2.drawImage(mapImage, 0, -25, 999, 775, this);
		
		//** disabled country lines as they are now apart of the bg image
		// draw the country lines
//		drawCountryLines(g);
		
		// for loop contains all other drawing components
		for(int i = 0;i < Constants.NUM_COUNTRIES;i++) { // loop start
			
			// get the current countries information
			Country country = countries.get(i);

			// set the x and y coordinates of the country currently being drawn
			// pulls coords from the country instance
			int xCoord = (country.getMapCoordinate())[0];
			int yCoord = (country.getMapCoordinate())[1];
			
			// create the country dot to be drawn
			Ellipse2D.Double mapCoord = new Ellipse2D.Double(xCoord,yCoord,20,20);
						
			// change color of stroke to match player color or White if neutral
			if(country.getOccupier() != null) {
				g2.setColor(country.getOccupier().getColor());
				g2.setStroke(new BasicStroke(8));
			} else {
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(6));
			}
						
			// draw the country outer circle
			g2.draw(mapCoord);
			
			// draw the country name and unit numbers under the country coordinate
			g2.setColor(Color.BLACK);
			g2.setFont(new Font(g2.getFont().getFontName(), Font.BOLD, 16));
			
			
			// disabled the drawing of the country names as they are a part of the bg image
//			g2.drawString(Constants.COUNTRY_NAMES[i], xCoord-25,yCoord+25);
			
			// draw the fill			
			drawFill(g,Constants.CONTINENT_IDS[i], mapCoord);
			
			// set color back to black
			g2.setColor(Color.BLACK);
			
			Integer numArmies = country.getNumOfArmies();
			
			// draw the number armies in the current country
			if(numArmies>19) { // reduce font size and center point if armies over 19
				g2.setFont(new Font(g2.getFont().getFontName(), Font.BOLD, 14));
				g2.drawString(numArmies.toString(), xCoord+3, yCoord+15);
			} else if(numArmies>9) { // adjust the center point if armies over 9
				g2.drawString(numArmies.toString(), xCoord+1, yCoord+16); 
			} else { // set center point if armies under 10
				g2.drawString(numArmies.toString(), xCoord+6, yCoord+16); 
			}
			
			
		}
		
	}

	/**
	 * sets and draws the fill color of the country based on continent
	 * @param g
	 * @param ci
	 * @param mapCoord
	 */
	private void drawFill(Graphics g, int ci, Ellipse2D.Double mapCoord) {
		Graphics2D fills = (Graphics2D) g;
		
		// change the draw color to match the continent
		switch(ci) {
			case 0: fills.setColor(Constants.NA); break;
			case 1: fills.setColor(Constants.EU); break;
			case 2: fills.setColor(Constants.ASIA); break;
			case 3: fills.setColor(Constants.AUS); break;
			case 4: fills.setColor(Constants.SA); break;
			default: fills.setColor(Constants.AF);
		}
		
		// fill in the country coordinate
		fills.fill(mapCoord);	
	}

	/**
	 * method to draw the lines between countries, uses Constants for coordinates
	 * currently not in use, could be used to light up adjacent armies during the attack phase
	 * 
	 * @param g
	 */
	public void drawCountryLines(Graphics g) {
		
		Graphics2D cLines = (Graphics2D) g; // for connection lines
		
		// for loop contains all the drawing components
		for(int i = 0;i < Constants.NUM_COUNTRIES;i++) { // loop start
					
			// set the x1 and y1 coordinates of the country currently being drawn
			int x1 = Constants.COUNTRY_COORD[i][0];
			int y1 = Constants.COUNTRY_COORD[i][1];
					
			// create an array to hold the ending coordinates for drawing connection lines
			int x2[] = new int[Constants.ADJACENT[i].length];
			int y2[] = new int[Constants.ADJACENT[i].length];
					
			// change draw color and set stroke
			cLines.setColor(Color.GRAY);
			cLines.setStroke(new BasicStroke(2));
					
			// loop through the adjacent countries of the country currently being drawn
			for(int j=0;j<Constants.ADJACENT[i].length;j++) {
				// set the x2/y2 coordinates to draw connection lines
				x2[j] = Constants.COUNTRY_COORD[Constants.ADJACENT[i][j]][0];
				y2[j] = Constants.COUNTRY_COORD[Constants.ADJACENT[i][j]][1];
						
				// draw the connection line from the center of the country dot
				// the plus 10 is used because the dots have a radius of 10 so that
				// the connection lines get drawn to/from the center
				if(Constants.CONTINENT_IDS[j]>Constants.CONTINENT_IDS[i]) {}
				else if(i == 8 && j == 2) { // the Alaska special connection
					cLines.drawLine(x1+10, y1+10, x1 - 40, y1+10);
				} else if(i == 22 && j == 0) { // the Kamchatka special connection
					cLines.drawLine(x1+10, y1+10, x1 + 150, y1+10);
				} else {
					cLines.drawLine(x1+10, y1+10, x2[j]+10, y2[j]+10);
				}
						
			}
						
		}
	}
}
