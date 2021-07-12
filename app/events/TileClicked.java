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
	Tile clickedTile;
	int middleSleepTime = EventProcessor.middleSleepTime;

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		//if(gameState.getUnitMoving()) {return;}
		//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		//Get the tile with the clicked position
		clickedTile = gameState.getBoard().getTile(tilex, tiley);

		//if no prev clicked tile: 
		if(gameState.getTileClicked()==null){gameState.setTileClicked(clickedTile);}
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}

		//play card
		if (gameState.getCardSelected()!=null) {
			if(playCardOnTile(out, gameState, gameState.getCardSelected(), clickedTile)) {return;} //will return if a card is successfully played
			gameState.unHighlightCard(out);
			gameState.unhightlightWhiteRedTiles(out);
		}
		
		/*4 Possible Scenarios:
		 * 1. Clicking on the Player 1 unit--->Tiles will get highlighted
		 * 2. Clicking on the WhiteHighlighted tiles--->Player 1 unit will move
		 * 3. Clicking on the RedHighlighted tiles--->Player 1 unit will attack
		 * 4. Clicking on the other tiles--->nth happened
		 */

		//scenario 4: 
		//if NOT clicked, white HL, red HL -> un HL all
		if(!(clickedTile.equals(gameState.getTileClicked())) &&
				(!(gameState.getBoard().getHighlightedWhiteTiles().contains(clickedTile))) &&
				(!(gameState.getBoard().getHighlightedRedTiles().contains(clickedTile)))){
			gameState.unhightlightWhiteRedTiles(out);
////////////////need to set new clicked???
		}

		//Scenario 1: if the player is clicking on a player1 unit tile, setting the unitClick of gameState.
		//The valid move/atk tiles (of that unit) will get highlighted
		if(gameState.getBoard().getPlayer1UnitTiles().contains(clickedTile)) {
			gameState.unHighlightCard(out);
			gameState.unhightlightWhiteRedTiles(out);
			gameState.setUnitClicked(clickedTile.getUnit());
			gameState.setTileClicked(clickedTile);
			
			Unit currUnit = gameState.getUnitClicked();
			//if able to move &/atk
			if((currUnit.getAttacked()<=0)&&(currUnit.isMoved()==false)) {
				currUnit.highlightMoveAndAttackTile(gameState,clickedTile);
				gameState.getCurrentPlayer().displayAllTiles(out,gameState);
			}
			//if can atk (=cannot move)
			else if(gameState.getUnitClicked().getAttacked()<=0) {
				currUnit.highlightAttackTile(gameState,clickedTile);
				gameState.getCurrentPlayer().displayRedTile(out,gameState);
			}
			//if can move (=cannot atk)
			else if(gameState.getUnitClicked().isMoved()==false){
				currUnit.highlightMoveTile(gameState,clickedTile);
				gameState.getCurrentPlayer().displayWhiteTile(out,gameState);
			}
			return; //after display -> end
		}

////////////if no unit was clicked?? 
		if(gameState.getUnitClicked()!=null) {gameState.unHighlightCard(out);}
///////////if unit was clicked??
		else {return;}	


		//Scenario 2&3 : if the player is clicking on a redTile and the not yet moved before, 
		 //the unit will move and attack.
		Unit currUnit = gameState.getUnitClicked();
		int x = currUnit.getPosition().getTilex();
		int y = currUnit.getPosition().getTiley();
		if(gameState.getBoard().getHighlightedRedTiles().contains(clickedTile)
				&& (tilex-x>=2)||(x-tilex>=2)||(tiley-y>=2)||(y-tiley>=2)){
			// Ranged unit does not have move and attack method and it can attack far away
			if(currUnit instanceof RangedUnit == false){
				if (currUnit.isMoved()==false && currUnit.getAttacked()<=0){
					//do move & atk
					int x_pos = clickedTile.getTilex();
					int y_pos = clickedTile.getTiley();
					Tile moveTile = null;
					for (int i=x_pos-1; i<=x_pos+1; i++) {
						for (int j=y_pos-1; j<=y_pos+1; j++) {
							Tile tile = gameState.getBoard().getTile(i, j);
							if (gameState.getBoard().getHighlightedWhiteTiles().contains(tile)) {
								if (moveTile==null) {moveTile=tile;}
								else if (moveTile.absdiff(clickedTile)>tile.absdiff(clickedTile)) {
									moveTile=tile;
								}
							}
						}
					}
					gameState.moveUnit(out, currUnit, moveTile);
					currUnit.attackUnit(out, gameState,currUnit,clickedTile);
				}
			}//Scenario 2: clicked redTile, NOT move & attack range, only adjacent attack
			else if	(currUnit.getAttacked()<=0)  {
				gameState.unhightlightWhiteRedTiles(out);
				currUnit.attackUnit(out, gameState, currUnit, clickedTile);
			}
			gameState.setTileClicked(null);
			return; //sucessful atk -> return
		}

		//Scenario 3: clicked on a whiteTile, 
		//check condi for unit move
		if((currUnit.isMoved()==false) && (gameState.getBoard().getHighlightedWhiteTiles().contains(clickedTile))){
			gameState.unhightlightWhiteRedTiles(out);
			gameState.moveUnit(out, currUnit,clickedTile);
			gameState.setTileClicked(null);
			return; //sucessful move -> return
		}

		gameState.setTileClicked(clickedTile);
		return;
	}


	//Helper method
	public static boolean playCardOnTile(ActorRef out, GameState gameState, Card card, Tile tile) {
		//play unit card
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
		//play spell card
		if(card instanceof SpellCard) { //if a spell card is selected previously
			//if is valid target -> play the card
			if(gameState.getBoard().getHighlightedRedTiles().contains(tile)) {
				if(gameState.getCurrentPlayer() == gameState.getPlayer1() && card.getManacost() > gameState.getPlayer1().getMana()) {
					BasicCommands.addPlayer1Notification(out, "You have no MANA!", 2);
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					//cannot return false here
					if(tile.getUnit() != null) {
						gameState.unHighlightCard(out); 
						gameState.getBoard().unhighlightRedTiles(out); 
						return true; //not really played, just to stop the process event to prevent running code following}
					}
					return false;
				}
				gameState.playCard(out, gameState, card, tile);
				gameState.setTileClicked(tile);
				return true;
			}
		}
		return false;
	}



	//Helper method

	//Getter and setter
	public Tile getCurrentTileClicked() {return clickedTile;}
	public void setCurrentTileClicked(Tile currentTileClicked) {this.clickedTile = currentTileClicked;}

}
