package events;




import java.util.ArrayList;
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
		currentTileClicked= gameState.getBoard().getTile(tilex, tiley);
		ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles(); 

		//Set gameState.setTileClicked() = currentTile for the first round
		if(gameState.getTileClicked()==null){		
			gameState.setTileClicked(currentTileClicked);}

		//insert here: play spell card
		//can also insert summon unit here (inside the "gameState.getCardSelected()!=null")
		if (gameState.getCardSelected()!=null) {
			Card currentCard = gameState.getCardSelected();
			String cardName = currentCard.getCardname();
			if(currentCard instanceof SpellCard) { //if a spell card is selected previously
				//if is valid target -> play the card
				if(checkTile(currentTileClicked,gameState.getBoard().getHighlightedRedTiles())) {
					GroupsCommands.playSpellCard(out, cardName, currentTileClicked); //see GroupsCommands...
			}
		}


		/*First checking whether the unit can move or not
		  if move and attack is true, there is no valid move tile and attack unit. Emptying the two arrays */

		if(gameState.isAttack()==true) {gameState.getBoard().unhighlightRedTiles(out);}
		if(gameState.isMove()==true)  {gameState.getBoard().unhighlightWhiteTiles(out);}

		/*Second, check that whether the newclickedTile, is equal to the previous clicked tile 
		 * or whether it belongs to the whiteHighlighted Tiles or the redHighligted Tiles.
		 * if not, the tiles will be unhighlighted.
		 */

		if(!(currentTileClicked.equals(gameState.getTileClicked())) &&
				(!(checkTile(currentTileClicked,gameState.getBoard().getHighlightedWhiteTiles()))) &&
				(!(checkTile(currentTileClicked,gameState.getBoard().getHighlightedRedTiles())))){
			gameState.getBoard().unhighlightRedTiles(out);
			gameState.getBoard().unhighlightWhiteTiles(out);}

		/* Third, use helper method to check the newtileClick belongs to player1 unit or not.
		 * If true, then check whether attack or move is applicable.
		 * call the highlightmoveTile to highlight the valid move tiles and attack tiles
		 */

		if(checkTile(currentTileClicked, player1UnitTiles)) {	
			gameState.setUnitClicked(currentTileClicked.getUnit());
			if(gameState.isAttack()==false) {GroupsCommands.highlightAttackTile(out,gameState,gameState.getUnitClicked());}
			if(gameState.isMove()==false) {GroupsCommands.highlightMoveTile(out,gameState,gameState.getUnitClicked()); }}	
		/*
		 * Use helped method to check the tileClick belongs to player1 highlightedTiles List.
		 * If true, unhighlight the tiles and then call the moveUnit method.*/

		if(checkTile(currentTileClicked,gameState.getBoard().getHighlightedWhiteTiles())){
			gameState.getBoard().unhighlightWhiteTiles(out);
			GroupsCommands.moveUnit(out, gameState, gameState.getUnitClicked(),currentTileClicked);}

		if(checkTile(currentTileClicked, gameState.getBoard().getHighlightedRedTiles())){
			gameState.getBoard().unhighlightRedTiles(out);
			GroupsCommands.attackUnit(out, gameState,gameState.getUnitClicked(),currentTileClicked);}	

		//Setting the gameState's TileClicked with the current tile clicked at the end.
		gameState.setTileClicked(currentTileClicked);
	}

	//Helper method
	public boolean checkTile (Tile tile, ArrayList<Tile> a) {//check whether the tile is in a arraylist.
		if(a.contains(tile)) {return true;}
		return false;
	}

	//Getter and setter
	public Tile getCurrentTileClicked() {return currentTileClicked;}
	public void setCurrentTileClicked(Tile currentTileClicked) {this.currentTileClicked = currentTileClicked;}

}