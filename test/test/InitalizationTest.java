import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

public class InitalizationTest {

	@Test
	public void checkInitalized() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		assertFalse(gameState.gameInitalised);
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		assertTrue(gameState.gameInitalised);
		
		Tile tile = BasicObjectBuilders.loadTile(3, 2);
		BasicCommands.drawTile(null, tile, 0); // draw tile, but will use altTell
		
	}
	
}
