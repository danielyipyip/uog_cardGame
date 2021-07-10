import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;

//any better way to know is it shown in screen?

/**
 * Test for story: 
 * 
 * reminder for format: #x (y)
 * x= story from refernece story document (from teacher)
 * y= story from our story list (handed in version in phase 1)
 */

public class InitalizationTest {


	
	@Test
	//#1 (5) start with 3 cards
	public void checkStartCard() {
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		//player 1 & 2 have 3 cards in hand in the start of game
		assertTrue(gameState.getPlayer1().getMyhand().getMyhand().size()==3 & 
				gameState.getPlayer2().getMyhand().getMyhand().size()==3);
	}
	
	@Test
	//#3 (4,7) starting health
	//(7) starting mana
	public void checkStartHealthMana() {
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		assertTrue(gameState.getPlayer1().getHealth()==20 & 
				gameState.getPlayer2().getHealth()==20);
		assertTrue(gameState.getPlayer1().getMana()==2 & 
				gameState.getPlayer2().getMana()==2);
	}
	
	@Test
	//(4) startin position of 2 avatars
	public void checkAvatarStartOfGame() {
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		Unit avatar1 = gameState.getBoard().getPlayer1Units().get(0);
		Unit avatar2 = gameState.getBoard().getPlayer2Units().get(0);
		
		//check class
		assertTrue(avatar1 instanceof Avatar & 
				avatar2 instanceof Avatar);
		//check position
		assertTrue(avatar1.getPosition().getTilex()==1 & 
				avatar1.getPosition().getTiley()==2 &
				avatar2.getPosition().getTilex()==7 & 
				avatar2.getPosition().getTiley()==2);
	}
	

//	teacher's example, please see video
//	@Test
//	public void checkInitalized() {
//		
//		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
//		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
//		
//		GameState gameState = new GameState();
//		Initalize initalizeProcessor =  new Initalize();
//		
//		assertTrue(gameState.gameInitalised);
//		
//		ObjectNode eventMessage = Json.newObject();
//		initalizeProcessor.processEvent(null, gameState, eventMessage);
//		
//		assertTrue(gameState.gameInitalised);
//		
//		Tile tile = BasicObjectBuilders.loadTile(3, 2);
//		BasicCommands.drawTile(null, tile, 0); // draw tile, but will use altTell
//		
//	}
	
}
