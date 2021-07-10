import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.EndTurnClicked;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Hand;

public class cardDraw {
	ArrayList<Card> player1Hand;
	ArrayList<Card> player2Hand;
	GameState gameState;
	ObjectNode eventMessage;
	EndTurnClicked processer;
	
	@Test
	//#1 (5,11) draw card
	//#2 (11) overdraw
	public void checkOverDraw() {
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		GameState gameState = new GameState();
		EndTurnClicked processer=  new EndTurnClicked();
		
		ObjectNode eventMessage = Json.newObject();
		processer.processEvent(null, gameState, eventMessage);
		
		player1Hand = gameState.getPlayer1().getMyhand().getMyhand();
		player2Hand = gameState.getPlayer2().getMyhand().getMyhand();
		
		//feels this is clearer than run loop
		helpDrawAssert(4,3); //turn 1, player2
		
		helpDrawAssert(4,4); //turn 2, player1
		helpDrawAssert(5,4); //turn 2, player2
		
		helpDrawAssert(5,5); //turn 3, player1
		helpDrawAssert(6,5); //turn 3, player2

		helpDrawAssert(6,6); //turn 4, player1
		helpDrawAssert(6,6); //turn 4, player2
		helpDrawAssert(6,6); //turn 5, player1
		helpDrawAssert(6,6); //turn 5, player2
		helpDrawAssert(6,6); //turn 6, player1
		helpDrawAssert(6,6); //turn 6, player2
	}
	
	public void helpDrawAssert(int hand1, int hand2) {
		processer.processEvent(null, gameState, eventMessage);
		assertTrue(player1Hand.size()==6);
		assertTrue(player2Hand.size()==6);
	}

}
