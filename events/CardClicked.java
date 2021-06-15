package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int handPosition = message.get("position").asInt();
		
		//------------------------------------highlight card------------------------------------//
		Card cardSelected = gameState.getPlayer1().getMyhand().getCard(handPosition);
		
		Card previousCard = gameState.getCardSelected();
		int previousPos = gameState.getcardPos();
		
		if(previousCard != null) {
			BasicCommands.drawCard(out, previousCard, previousPos, 0);
		} 
		BasicCommands.drawCard(out, cardSelected, handPosition, 1);
		gameState.setCardSelected(cardSelected);
		gameState.setcardPos(handPosition);
		//----------------------------------^^highlight card^^----------------------------------//
	}

}
