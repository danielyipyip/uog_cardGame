import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;

public class sampleTest {

	@Test
	public void checkInisitalized() {
		GameState gamestate = new GameState();
		Initalize initializeProcessor = new Initalize();
		
		assertFalse(false);
		
		ObjectNode eventMessage = Json.newObject();
		initializeProcessor.processEvent(null, gamestate, eventMessage);
		assertTrue(true);
	}
}
