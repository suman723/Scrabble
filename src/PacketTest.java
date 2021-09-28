 /**
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 **/

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PacketTest {

	
	@Test
	void packetGetters() {
		
		String s = "error testing";
		Player playerTest = new Player(3,"Kahn",Constants.P3, Constants.P3name,true,36);
		Packet<Player> packetTest= new Packet<Player>(playerTest,Packet.Type.SUCCESS,s);
		
		assertEquals(playerTest,packetTest.getData());
		assertEquals(Packet.Type.SUCCESS,packetTest.getType());
		assertEquals(s,packetTest.getMessage());
		
		
	}
	
	@Test
	void packetToString() {
		
		String s = "error testing";
		Integer x = 4;
		Country c = new Country(0,Constants.COUNTRY_NAMES[0], Constants.ADJACENT[0], Constants.COUNTRY_COORD[0], Constants.CONTINENT_IDS[0] );
		Packet<Integer> packetTest= new Packet<Integer>(x,Packet.Type.SUCCESS,s);
		Packet<Country> cpTest = new Packet<Country>(c,Packet.Type.ERROR,s);
		assertEquals("Data: 4 Type: SUCCESS Message: error testing",packetTest.toString());
		assertEquals("Data: Name: Ontario ID: 0 Links: {4, 1, 5, 6, 3, 2} "
				+ "Map Coords: X: 191 Y: 150 ContinentID: 0 Unoccupied  Army count: 0"
				+ " Type: ERROR Message: error testing",cpTest.toString());
		
	}

}
