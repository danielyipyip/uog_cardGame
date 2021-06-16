package events;


import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

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
		
		//------------------------------------Highlight Card(START)------------------------------------//
		Card cardSelected = gameState.getPlayer1().getMyhand().getCard(handPosition);
		
		Card previousCard = gameState.getCardSelected();
		int previousPos = gameState.getcardPos();
		
		if(previousCard != null) {
			BasicCommands.drawCard(out, previousCard, previousPos, 0);
		} 
		BasicCommands.drawCard(out, cardSelected, handPosition, 1);
		gameState.setCardSelected(cardSelected);
		gameState.setcardPos(handPosition);
		//------------------------------------Highlight Card(END)------------------------------------//
		
		//------------------------------------Highlight Tiles(START)------------------------------------//
		int cardID = cardSelected.getId();
		
		ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles();
		ArrayList<Tile> player2UnitTiles = gameState.getBoard().getPlayer2UnitTiles();
		ArrayList<Tile> occupiedTiles = gameState.getBoard().getUnitOccupiedTiles();
		ArrayList<Tile> highlightedWhiteTile = gameState.getBoard().getHighlightedWhiteTiles();
		ArrayList<Tile> highlightedRedTile = gameState.getBoard().getHighlightedRedTiles();
		Tile tile;
		
		/*For unit card, the tiles surrounding ally units will be highlighted
		 *card id 1-8: unit card 
		 *card id 6: Azurite lion
		 */
		if(cardID > 0 && cardID < 9) {		
			gameState.getBoard().unhighlightTiles(out);
			//Select Azurite lion, highlight all unoccupied tiles
			if(cardSelected.getId() == 6) {	
				for(int i=0; i<gameState.getBoard().getX(); i++) {
					for(int j=0; j<gameState.getBoard().getY();j++) {
						tile = gameState.getBoard().getTile(i, j);
						if(occupiedTiles.contains(tile)) continue;
						BasicCommands.drawTile(out, tile, 1);
						highlightedWhiteTile.add(tile);
						try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
					}			
				}
			} 
			//Select other unit cards, highlight unoccupied tiles around ally units
			else {
				for(Tile ally: player1UnitTiles) {
					int x = ally.getTilex() - 1;
					int y = ally.getTiley() - 1;
					for(int i=x; i<=x+2; i++) {
						for(int j=y; j<=y+2; j++) {
							tile = gameState.getBoard().getTile(i, j);
							if(occupiedTiles.contains(tile)) continue;
							BasicCommands.drawTile(out, tile, 1);
							highlightedWhiteTile.add(tile);
							try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
						}
					}
				}
			}
		}
		
		/*For spell card
		 *Truestrike: highlight all enemy units (red)
		 *Sundrop Elixir: highlight all ally units (red)
		 *card id 9: Truestrike
		 *card id 10: Sundrop Elixir
		 */
		if(cardID == 9) {
			gameState.getBoard().unhighlightTiles(out);
			for(Tile i: player2UnitTiles) {
				tile = gameState.getBoard().getTile(i.getTilex(), i.getTiley());
				BasicCommands.drawTile(out, tile, 2);
				highlightedRedTile.add(tile);
			}
		}
		if(cardID == 10) {
			gameState.getBoard().unhighlightTiles(out);
			for(Tile i: player1UnitTiles) {
				tile = gameState.getBoard().getTile(i.getTilex(), i.getTiley());
				BasicCommands.drawTile(out, tile, 2);
				highlightedRedTile.add(tile);
			}
		}
		
		//------------------------------------Highlight Tiles(END)------------------------------------//
		

	}
	
}
