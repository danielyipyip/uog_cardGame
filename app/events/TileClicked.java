package events;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.GroupsCommands;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.unit.RangedUnit;
import structures.basic.SpellCard;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.basic.UnitCard;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{
	Tile currentTileClicked;
	int middleSleepTime = EventProcessor.middleSleepTime;
	
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		//if(gameState.getUnitMoving()) {return;}
	//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		//Get the tile with the clicked position
		currentTileClicked = gameState.getBoard().getTile(tilex, tiley);
		//Set gameState.setTileClicked() = currentTile for the first round

		if(gameState.getTileClicked()==null){gameState.setTileClicked(currentTileClicked);}

		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		
		//insert here: play spell card
		//can also insert summon unit here (inside the "gameState.getCardSelected()!=null")
		if (gameState.getCardSelected()!=null) {
			Card currentCard = gameState.getCardSelected();

			if(playCardOnTile(out, gameState, currentCard, currentTileClicked)) {return;} //will return if a card is successfully played
			
			gameState.unHighlightCard(out);
			gameState.getBoard().unhighlightWhiteTiles(out);
			gameState.getBoard().unhighlightRedTiles(out);
		}
		/*4 Possible Scenarios:
		 * 1. Clicking on the Player 1 unit--->Tiles will get highlighted
		 * 2. Clicking on the WhiteHighlighted tiles--->Player 1 unit will move
		 * 3. Clicking on the RedHighlighted tiles--->Player 1 unit will attack
		 * 4. Clicking on the other tiles--->nth happened
		 */
		
		/*Scenario 4:
		 * Check that whether the newclickedTile, is equal to the previous clicked tile 
		 * or whether it belongs to the whiteHighlighted Tiles or the redHighligted Tiles.
		 * if not, the tiles will be unhighlighted and the array are emptied.
		 */

		if(!(currentTileClicked.equals(gameState.getTileClicked())) &&
			(!(gameState.getBoard().getHighlightedWhiteTiles().contains(currentTileClicked))) &&
				(!(gameState.getBoard().getHighlightedRedTiles().contains(currentTileClicked)))){
					gameState.getBoard().unhighlightWhiteTiles(out);
					gameState.getBoard().unhighlightRedTiles(out);
		}
		
		
							
		/*Scenario 1: if the player is clicking on a player1 unit tile, setting the unitClick of gameState.
		 * The tiles will get highlighted
		 */
		if(gameState.getBoard().getPlayer1UnitTiles().contains(currentTileClicked)) {
			gameState.unHighlightCard(out);
			gameState.getBoard().unhighlightWhiteTiles(out);
			gameState.getBoard().unhighlightRedTiles(out);
			gameState.setUnitClicked(currentTileClicked.getUnit());
			
			if((gameState.getUnitClicked().getAttacked()<=0)&&(gameState.getUnitClicked().isMoved()==false)) {
				gameState.getUnitClicked().highlightMoveAndAttackTile(gameState,currentTileClicked);
				gameState.getCurrentPlayer().displayAllTiles(out,gameState);
			}
				
			else if(gameState.getUnitClicked().getAttacked()<=0) {
				gameState.getUnitClicked().highlightAttackTile(gameState,currentTileClicked);
				gameState.getCurrentPlayer().displayRedTile(out,gameState);
			}
				
			else if(gameState.getUnitClicked().isMoved()==false){
				gameState.getUnitClicked().highlightMoveTile(gameState,currentTileClicked);
				gameState.getCurrentPlayer().displayWhiteTile(out,gameState);
			}
			gameState.setTileClicked(currentTileClicked);
			return;
		}

		if(!(gameState.getUnitClicked()==null)) {
			gameState.unHighlightCard(out);} else {return;}	

		
		/*Scenario 2&3 : if the player is clicking on a redTile and the not yet moved before, 
		 * the unit will move and attack.
		 */
		if(gameState.getBoard().getHighlightedRedTiles().contains(currentTileClicked)){
			
			// Ranged unit does not have move and attack method and it can attack far away.

			if(gameState.getUnitClicked() instanceof RangedUnit == false){
				int x = gameState.getUnitClicked().getPosition().getTilex();
				int y = gameState.getUnitClicked().getPosition().getTiley();
		
				if ((tilex-x>=2)||(x-tilex>=2)||(tiley-y>=2)||(y-tiley>=2)&&
					(gameState.getUnitClicked().isMoved()==false) && 
						(gameState.getUnitClicked().getAttacked()<=0)){
				
					
				//The below loop is to find the first tile that is in the whiteTiles
						int x1 = currentTileClicked.getTilex()-1;
						int y1 = currentTileClicked.getTiley()-1;

						Tile moveTile = null;
				
				//Finding the first white tile around the red tile 
						for(int i=x1;i<=x1+2;i++) {
							for(int j=y1;j<= y1+2; j++) {
								Tile tile = gameState.getBoard().getTile(i, j) ;
								if(gameState.getBoard().getHighlightedWhiteTiles().contains(tile)){
									moveTile = tile;
							}	
								if(!(moveTile==null)) {break;}	
							}
							if(!(moveTile==null)) {break;}	
						}	

					gameState.moveUnit(out, gameState.getUnitClicked(), moveTile);
					gameState.getUnitClicked().attackUnit(out, gameState,gameState.getUnitClicked(),currentTileClicked);
				}
			
			}
			
		/*Scenario 2: if the player is clicking on a redTile, but not in move and attack range, only adjacent attack
		 */
			if	(gameState.getUnitClicked().getAttacked()<=0)  {
			
				gameState.getBoard().unhighlightWhiteTiles(out);
				gameState.getBoard().unhighlightRedTiles(out);
		
				gameState.getUnitClicked().attackUnit(out, gameState,gameState.getUnitClicked(),currentTileClicked);
			}
			gameState.setTileClicked(currentTileClicked);
			return;
		}	

		/*Scenario 3: if the player is clicking on a whiteTile, the unit will move
		 */

			
		if((gameState.getUnitClicked().isMoved()==false) && (gameState.getBoard().getHighlightedWhiteTiles().contains(currentTileClicked))){
			gameState.getBoard().unhighlightWhiteTiles(out);
			gameState.getBoard().unhighlightRedTiles(out);
			gameState.moveUnit(out, gameState.getUnitClicked(),currentTileClicked);
			gameState.setTileClicked(currentTileClicked);
			return;
			}
		
		gameState.setTileClicked(currentTileClicked);
		return;
		}


	//Helper method
	public static boolean playCardOnTile(ActorRef out, GameState gameState, Card card, Tile tile) {
//		Tile currTile = gameState.getBoard().getTile(tilex, tiley);
		if(card instanceof UnitCard) { //if a unit card is selected previously
			if(gameState.getBoard().getHighlightedWhiteTiles().contains(tile)) {
				if(gameState.getCurrentPlayer() == gameState.getPlayer1() && card.getManacost() > gameState.getPlayer1().getMana()) {
					BasicCommands.addPlayer1Notification(out, "You have no MANA!", 2);
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					return false;
				}
				gameState.playCard(out, gameState, card, tile);
				gameState.setTileClicked(tile);
				return true;
			}
		}
		if(card instanceof SpellCard) { //if a spell card is selected previously
			//if is valid target -> play the card
			if(gameState.getBoard().getHighlightedRedTiles().contains(tile)) {
				if(gameState.getCurrentPlayer() == gameState.getPlayer1() && card.getManacost() > gameState.getPlayer1().getMana()) {
					BasicCommands.addPlayer1Notification(out, "You have no MANA!", 2);
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					if(tile.getUnit() != null) {
						gameState.unHighlightCard(out); 
						gameState.getBoard().unhighlightRedTiles(out); 
						return true; //not really played, just to stop the process event to prevent running code following}
					} return false;
				}
				gameState.playCard(out, gameState, card, tile);
				gameState.setTileClicked(tile);
				return true;
			}
		} return false;
	}
	

	
	//Helper method

	//Getter and setter
	public Tile getCurrentTileClicked() {return currentTileClicked;}
	public void setCurrentTileClicked(Tile currentTileClicked) {this.currentTileClicked = currentTileClicked;}

}
