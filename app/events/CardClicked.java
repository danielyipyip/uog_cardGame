package events;


import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.SpellCard;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitCard;
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
		//if(gameState.getUnitMoving()) {return;}
		int handPosition = message.get("position").asInt();
		gameState.getBoard().unhighlightRedTiles(out);
		gameState.getBoard().unhighlightWhiteTiles(out);
		gameState.setUnitClicked(null);
		
		//----------------------------------Highlight Card(START)-----------------------------------//
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

		//Highlight Tiles
		cardHighlightTiles(out, gameState, cardSelected);
		
	}

	public static void cardHighlightTiles(ActorRef out, GameState gameState, Card cardSelected) {
		String cardName = cardSelected.getCardname();

		ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles();
		ArrayList<Tile> player2UnitTiles = gameState.getBoard().getPlayer2UnitTiles();
		ArrayList<Tile> occupiedTiles = gameState.getBoard().getUnitOccupiedTiles();
		HashSet<Tile> highlightedWhiteTile = gameState.getBoard().getHighlightedWhiteTiles();
		HashSet<Tile> highlightedRedTile = gameState.getBoard().getHighlightedRedTiles();
		Tile tile;

		/*For unit card, the tiles surrounding ally units will be highlighted
		 *card id 1-8: unit card 
		 *card id 6: Azurite lion
		 */
		//add exception card for player 2: 
		//"Planar Scout"
		//UNIT card
		if(cardSelected instanceof UnitCard) {	//if it is a unit card
			//if "Azurite Lion"/"Planar Scout", highlight all unoccupied tiles
			if(cardName.equals("Azurite Lion") || cardName.equals("Planar Scout")) {	
				for(int i=0; i<gameState.getBoard().getX(); i++) {
					for(int j=0; j<gameState.getBoard().getY();j++) {
						tile = gameState.getBoard().getTile(i, j);
						if(occupiedTiles.contains(tile)) continue;
						//if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
							BasicCommands.drawTile(out, tile, 1); //only draw for player1
						//}
						highlightedWhiteTile.add(tile);
						try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
					}			
				}
			} 
			//Select other unit cards, highlight unoccupied tiles around ally units
			else {
				ArrayList<Tile> unitTiles;
				if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
					unitTiles = player1UnitTiles;
				} else { unitTiles = player2UnitTiles; }
				for(Tile ally: unitTiles) {
					int x = ally.getTilex() - 1;
					int y = ally.getTiley() - 1;
					for(int i=x; i<=x+2; i++) {
						for(int j=y; j<=y+2; j++) {
							tile = gameState.getBoard().getTile(i, j);
							if(occupiedTiles.contains(tile)) continue;
							//if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
								BasicCommands.drawTile(out, tile, 1);
							//}
							highlightedWhiteTile.add(tile);
							try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
						}
					}
				}
			}
		}

		/*For spell card (valid target, red highlight)
		*player 1 spell card: Truestrike (enemy units); Sundrop Elixir: (ally units)
		*player 2 spell card: "Staff of Y'Kir'" (own avatar), "Entropic Decay" (non-avatar unit)
		*/
		if(cardSelected instanceof SpellCard ) {
			if(cardName.equals("Truestrike")) {
				for(Tile i: player2UnitTiles) {
					tile = gameState.getBoard().getTile(i.getTilex(), i.getTiley());
					if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
						BasicCommands.drawTile(out, tile, 2);
					}
					highlightedRedTile.add(tile);
				}
			}
			if(cardName.equals("Sundrop Elixir")) {
				for(Tile i: player1UnitTiles) {
					tile = gameState.getBoard().getTile(i.getTilex(), i.getTiley());
					if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
						BasicCommands.drawTile(out, tile, 2); //change the tile to red
					}
					highlightedRedTile.add(tile); //add in "red-tile" list
				}
			}
			if(cardName.equals("Staff of Y'Kir'")) { 
				//avatar is always first unit; 
				//only player 2 can have this card currently, so hardcoded for player 2 for now
				tile = gameState.getBoard().getPlayer2UnitTiles().get(0); 
				BasicCommands.drawTile(out, tile, 2);
				highlightedRedTile.add(tile);
			}
			if(cardName.equals("Entropic Decay")) { 
				//hightlight unit except at position 1 of array (=avatar)
				//do it separately for player1 & 2 units
				for(Tile i: player1UnitTiles.subList(1, player1UnitTiles.size())){highlightedRedTile.add(i); BasicCommands.drawTile(out, i, 2);}
				for(Tile i: player2UnitTiles.subList(1, player2UnitTiles.size())){highlightedRedTile.add(i); BasicCommands.drawTile(out, i, 2);}
			}
		}
	}
	
}
