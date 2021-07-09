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
	
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		
		//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		
		//Get the tile with the clicked position
		currentTileClicked = gameState.getBoard().getTile(tilex, tiley);
	
		//Set gameState.setTileClicked() = currentTile for the first round
		if(gameState.getTileClicked()==null){		
			gameState.setTileClicked(currentTileClicked);
		}

		try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
		
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
			(!(checkTile(currentTileClicked,gameState.getBoard().getHighlightedWhiteTiles()))) &&
				(!(checkTile(currentTileClicked,gameState.getBoard().getHighlightedRedTiles())))){
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
			
			if((gameState.getUnitClicked().isAttacked()==false)&&(gameState.getUnitClicked().isMoved()==false)) {
				GroupsCommands.highlightMoveAndAttackTile(out,gameState,currentTileClicked);
			}
				
			else if(gameState.getUnitClicked().isAttacked()==false) {
				//if(gameState.getUnitClicked()== fireSpritter.id) {..I dont know where to find the ID of the unit
					//GroupsCommands.rangeAttackHighLight(out,gameState);
				//}else
				GroupsCommands.highlightAttackTile(out,gameState,currentTileClicked);
			}
				
			else if(gameState.getUnitClicked().isMoved()==false){
				GroupsCommands.highlightMoveTile(out,gameState,currentTileClicked);
			}
			gameState.setTileClicked(currentTileClicked);
			return;
		}
		
		/*Scenario 2&3 : if the player is clicking on a redTile and the not yet moved before, 
		 * the unit will move and attack.
		 */
		if(!(gameState.getUnitClicked()==null)) {
			gameState.unHighlightCard(out);
		if(gameState.getUnitClicked().isMoved()==false&&gameState.getUnitClicked().isAttacked()==false) {		
			if
			//((!(gameState.getUnitClicked().getId()==99))&&...rangeAttack unit doesnt do moveandattack...
				(checkTile(currentTileClicked, gameState.getBoard().getHighlightedRedTiles())){
				//The below loop is to find the first tile that is in the whiteTiles
				int x = currentTileClicked.getTilex()-1;
				int y = currentTileClicked.getTiley()-1;
				Tile moveTile = null;
				
				//Finding the first white tile around the red tile 
				for(int i=x;i<= x+2;i++) {
					for(int j=y;j<= y+2; j++) {
							Tile tile = gameState.getBoard().getTile(i, j) ;
							if(checkTile(tile,gameState.getBoard().getHighlightedWhiteTiles())){
								moveTile = tile;
							}	
						if(!(moveTile==null)) {break;}	
						}
					if(!(moveTile==null)) {break;}	
				}
				GroupsCommands.moveUnit(out, gameState,gameState.getUnitClicked(),moveTile);
				GroupsCommands.attackUnitWithCounter(out, gameState,gameState.getUnitClicked(),currentTileClicked);
			}
		}
		
		/*Scenario 2: if the player is clicking on a whiteTile, the unit will move
		 */
		if((gameState.getUnitClicked().isMoved()==false)&& ((checkTile(currentTileClicked ,gameState.getBoard().getHighlightedWhiteTiles())))){
			gameState.getBoard().unhighlightWhiteTiles(out);
			gameState.getBoard().unhighlightRedTiles(out);
			GroupsCommands.moveUnit(out, gameState, gameState.getUnitClicked(),currentTileClicked);
			gameState.setTileClicked(currentTileClicked);
			return;
		}
		
		
		/*Scenario 3: if the player is clicking on a redTile, the unit will attack
		 */
		
		if(gameState.getUnitClicked().isAttacked()==false) {
			if((checkTile(currentTileClicked ,gameState.getBoard().getHighlightedRedTiles())))
					//&&if((gameState.getUnitClicked().getId()==99))..
					//Range attack only apply on fire spitter, and no counter attack....
					{
				gameState.getBoard().unhighlightWhiteTiles(out);
				gameState.getBoard().unhighlightRedTiles(out);
				GroupsCommands.attackUnit(out, gameState,gameState.getUnitClicked(),currentTileClicked);
			}

			else if(checkTile(currentTileClicked, gameState.getBoard().getHighlightedRedTiles())){
			gameState.getBoard().unhighlightWhiteTiles(out);
			gameState.getBoard().unhighlightRedTiles(out);
			GroupsCommands.attackUnitWithCounter(out, gameState,gameState.getUnitClicked(),currentTileClicked);
			}	
			gameState.setTileClicked(currentTileClicked);
			return;
		}
		gameState.setTileClicked(currentTileClicked);
		return;
		}
		
		//insert here: play spell card
		//can also insert summon unit here (inside the "gameState.getCardSelected()!=null")
		if (gameState.getCardSelected()!=null) {
			Card currentCard = gameState.getCardSelected();
			String cardName = currentCard.getCardname();
			if(currentCard instanceof UnitCard) { //if a unit card is selected previously
				if(checkTile(currentTileClicked,gameState.getBoard().getHighlightedWhiteTiles())) {
					GroupsCommands.playUnitCard(out, gameState, currentCard, currentTileClicked);
				}
			}
			if(currentCard instanceof SpellCard) { //if a spell card is selected previously
				//if is valid target -> play the card
				if(checkTile(currentTileClicked,gameState.getBoard().getHighlightedRedTiles())) {
					GroupsCommands.playSpellCard(out, gameState, cardName, currentTileClicked); //see GroupsCommands...
				}
			} 
			gameState.unHighlightCard(out);
			gameState.setTileClicked(currentTileClicked);
			return;
		}
				
	}

	//Helper method
	public boolean checkTile (Tile tile, HashSet<Tile> hashSet) {//check whether the tile is in a arraylist.
		if(hashSet.contains(tile)) {return true;}
		return false;
	}

	//Getter and setter
	public Tile getCurrentTileClicked() {return currentTileClicked;}
	public void setCurrentTileClicked(Tile currentTileClicked) {this.currentTileClicked = currentTileClicked;}

}