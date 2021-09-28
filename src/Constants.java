import java.awt.Color;

public class Constants
{
	public static final int F_WIDTH = 1000;
	public static final int F_HEIGHT = 800;
	public static final int NUM_PLAYERS = 2;
	public static final int NUM_NEUTRALS = 4;
	public static final int NUM_PLAYERS_PLUS_NEUTRALS = NUM_PLAYERS + NUM_NEUTRALS;
	public static final int NUM_COUNTRIES = 42;
	public static final int INIT_COUNTRIES_PLAYER = 9;
	public static final int INIT_COUNTRIES_NEUTRAL = 6;
	public static final int INIT_UNITS_PLAYER = 36;
	public static final int INIT_UNITS_NEUTRAL = 24;
	public static final String[] COUNTRY_NAMES = {
			"Ontario","Quebec","NW Territory","Alberta","Greenland","E United States","W United States","Central America","Alaska",
			"Great Britain","W Europe","S Europe","Ukraine","N Europe","Iceland","Scandinavia",
			"Afghanistan","India","Middle East","Japan","Ural","Yakutsk","Kamchatka","Siam","Irkutsk","Siberia","Mongolia","China",
			"E Australia","New Guinea","W Australia","Indonesia",
			"Venezuela","Peru","Brazil","Argentina",
			"Congo","N Africa","S Africa","Egypt","E Africa","Madagascar"};  // for reference
	public static final int[][] ADJACENT = {
			{4,1,5,6,3,2},			//  0 Ontario
			{4,5,0},				//  1 Quebec  
			{4,0,3,8},				//  2 NW Territory
			{2,0,6,8},				//  3 Alberta
			{14,1,0,2},				//  4 Greenland
			{0,1,7,6},				//  5 E United States
			{3,0,5,7},				//  6 W United States
			{6,5,32},				//  7 Central America
			{2,3,22},				//  8 Alaska
			{14,15,13,10},			//  9 Great Britain
			{9,13,11,37},			// 10 W Europe
			{13,12,18,39,10,37},	// 11 S Europe
			{20,16,18,11,13,15},	// 12 Ukraine
			{15,12,11,10,9},		// 13 N Europe
			{15,9,4},				// 14 Iceland
			{9,12,13,14},			// 15 Scandinavia
			{20,27,17,18,12},		// 16 Afghanistan
			{16,27,23,18},			// 17 India
			{12,16,17,40,39,11},	// 18 Middle East
			{26,22},				// 19 Japan
			{25,27,16,12},    		// 20 Ural
			{22,24,25},				// 21 Yakutsk
			{8,19,26,24,21},		// 22 Kamchatcka
			{27,31,17},				// 23 Siam
			{21,22,26,25},			// 24 Irkutsk
			{21,24,26,27,20},		// 25 Siberia
			{24,22,19,27,25},		// 26 Mongolia
			{26,23,17,16,20,25},	// 27 China
			{29,30},				// 28 E Australia
			{28,30,31},				// 29 New Guinea
			{29,28,31},      		// 30 W Australia
			{23,29,30},				// 31 Indonesia
			{7,34,33},				// 32 Venezuela
			{32,34,35},				// 33 Peru
			{32,37,35,33},			// 34 Brazil
			{33,34},				// 35 Argentina
			{37,40,38},				// 36 Congo
			{10,11,39,40,36,34},	// 37 N Africa
			{36,40,41},				// 38 S Africa
			{11,18,40,37},			// 39 Egypt
			{39,18,41,38,36,37},  	// 40 E Africa
			{38,40}					// 41 Madagascar
	};
	public static final int NUM_CONTINENTS = 6;
	public static final String[] CONTINENT_NAMES = {"N America","Europe","Asia","Australia","S America","Africa"};  // for reference
	public static final int[] CONTINENT_IDS = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,5,5};
	public static final int[] CONTINENT_SIZES = {9, 7, 12, 4, 4, 6};
	public static final int[] CONTINENT_VALUES = {5,5,7,2,2,3};
	public static final int FRAME_WIDTH = 1000;    // must be even
	public static final int FRAME_HEIGHT = 600;
	public static final int[][] COUNTRY_COORD = {
			{191,150},     // 0
			{255,161},
			{146,86},
			{123,144},
			{314,61},
			{205,235},
			{135,219},
			{140,299},
			{45,89},
			{370,199},
			{398,280},      // 10
			{465,270},
			{547,180},
			{460,200},
			{393,127},
			{463,122},
			{628,227},
			{679,332},
			{572,338},
			{861,213},
			{645,152},      // 20
			{763,70},
			{827,94},
			{751,360},
			{750,140},
			{695,108},
			{760,216},
			{735,277},
			{889,537},
			{850,429},
			{813,526},       // 30
			{771,454},
			{213,352},
			{221,426},
			{289,415},
			{233,523},
			{496,462},
			{440,393},
			{510,532},
			{499,354},
			{547,432},        // 40
			{586,545}
	};
	
	// colors to represent each of the Countries and Players
	public static final Color ASIA = new Color(212,226,180);
	public static final Color AUS = new Color(238,211,232);
	public static final Color EU = new Color(211,217,230);
	public static final Color NA = new Color(243,223,209);
	public static final Color AF = new Color(232,211,180);
	public static final Color SA = new Color(245,238,202);

	public static final Color P0 = new Color(220,20,20);
	public static final String P0name = "Red";
	public static final Color P1 = new Color(20,240,20);
	public static final String P1name = "Green";
	public static final Color P2 = new Color(20,20,240);
	public static final String P2name = "Blue";
	public static final Color P3 = new Color(250,150,50);
	public static final String P3name = "Orange";
	public static final Color P4 = new Color(72, 68, 68);
	public static final String P4name = "Black";
	public static final Color P5 = new Color(150,50,250);
	public static final String P5name = "Purple";

	public static final Color matrixBlack = new Color(13,2,8);
	public static final Color matrixDarkGreen = new Color(0,59,0);
	public static final Color matrixMidGreen = new Color(0,143,17);
	public static final Color matrixLightGreen = new Color(0, 255,65);
	public static final Color errorRed = new Color(255, 0, 33);
	

}
