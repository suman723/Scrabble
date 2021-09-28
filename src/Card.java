
import java.io.IOException;

/**
 * Creates a card object with getters, setters and toString methods
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */


public class Card{
	
	private int cardID; // will match country ID from constants class
	private String country; // name of the country
	private String continent; // continent name the country belongs to
	private String insignia; // infantry, cavalry, artillery
	private boolean wild;
	
	
	// an insignia and potentially a location variable may be added later
	// the location variable could display whether the card is in the deck, in a players hand or out of play
	
	// Card Constructor
//	public Card(int cardID,String country, String continent) {
//		this.cardID = cardID;
//		this.country = country;
//		this.continent = continent;
//	}
	
	// new Card constructor which only requires a countryID input to make the card
	public Card(int countryID) {
		verifyIDBounds(countryID);
		if(countryID>=Constants.NUM_COUNTRIES) {
			this.cardID = countryID;
			this.country = null;
			this.continent = null;
			this.insignia = "wild";
			this.wild = true;
		} else {
			this.cardID = countryID;
			this.country = Constants.COUNTRY_NAMES[cardID];
			this.continent = Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[cardID]];
			this.insignia = INSIGNIAS[COUNTRY_INSIGNIA[cardID]];
			this.wild = false;
		}
	}
	
	private void verifyIDBounds(int countryID) {
		if(countryID < 0 || countryID > Constants.NUM_COUNTRIES+2) {
			throw new IllegalArgumentException("Cannot make card. ID is out of bounds.");
		}
	}
	
	// getters and setters
	
	public String getInsignia() {
		return insignia;
	}
	
	public boolean isWild() {
		return wild;
	}
	
	public int getCardID() {
		return cardID;
	}


	public void setCardID(int cardID) {
		verifyIDBounds(cardID);
		this.cardID = cardID;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(int cardID) {
		this.country = Constants.COUNTRY_NAMES[cardID];
	}


	public String getContinent() {
		return continent;
	}

	
	public void setContinent(int cardID) {
		this.continent = Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[cardID]];
	}
	
	// toString method for user display
	public String toString() {
		if(this.wild) {
			return "Card Number:\t" + this.cardID + "\n"
					+ "Wild Card\n";
		} else {
		return "Card Number:\t" + this.cardID + "\n" 
				+ "Country:\t" + this.country +  "\n"
				+ "Continent:\t" + this.continent + "\n"
				+ "Insignia:\t" + this.insignia + "\n"; }
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Card[] deck = new Card[Constants.NUM_COUNTRIES];
		
		for(int i = 0;i<42;i++) {
			deck[i] = new Card(i);
		}
		
		System.out.println(deck[5].toString());
		
	}
	
	public static final String[] INSIGNIAS= {"infantry", "cavalry", "artillery"};
	public static final int[] COUNTRY_INSIGNIA= {
			1,//"Ontario",
			2,//"Quebec",
			2,//"NW Territory",
			0,//"Alberta",
			1,//"Greenland",
			2,//"E United States",
			0,//"W United States",
			1,//"Central America",
			0,//"Alaska",
			1,//"Great Britain",
			0,//"W Europe",
			1,//"S Europe",
			2,//"Ukraine",
			1,//"N Europe",
			1,//"Iceland",
			2,//"Scandinavia",
			0,//"Afghanistan",
			0,//"India",
			2,//"Middle East",
			0,//"Japan",
			1,//"Ural",
			1,//"Yakutsk",
			1,//"Kamchatka",
			2,//"Siam",
			0,//"Irkutsk",
			2,//"Siberia",
			2,//"Mongolia",
			1,//"China",
			0,//"E Australia",
			1,//"New Guinea",
			2,//"W Australia",
			1,//"Indonesia",
			2,//"Venezuela",
			1,//"Peru",
			2,//"Brazil",
			0,//"Argentina",
			1,//"Congo",
			0,//"N Africa",
			2,//"S Africa",
			0,//"Egypt",
			2,//"E Africa",
			0};//"Madagascar"};

}
