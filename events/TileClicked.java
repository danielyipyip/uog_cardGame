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
import structures.basic.Position;
import structures.basic.SpellCard;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;

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
<<<<<<< HEAD




=======
	
	Tile currentTileClicked;
	
	
>>>>>>> b1ce9f65bb04b4493ad99af68ec97d7cc131bd8f
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
<<<<<<< HEAD

		//Get the tile with the clicked position
		Tile tile= gameState.getBoard().getTile(tilex, tiley);
=======
		currentTileClicked= gameState.getBoard().getTile(tilex, tiley);
>>>>>>> b1ce9f65bb04b4493ad99af68ec97d7cc131bd8f
		ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles(); 
	
		//Set gameState.setTileClicked() = currentTile for the first round
		if(gameState.getTileClicked()==null){		
			gameState.setTileClicked(currentTileClicked);}
	
		/*First checking whether the unit can move or not
		  if move and attack is true, there is no valid move tile and attack unit. Emptying the two arrays */
		
<<<<<<< HEAD
		//insert here: play spell card
		if(gameState.getCardSelected() instanceof SpellCard) { //if a spell card is selected previously
			if(checkTile(tile,gameState.getBoard().getHighlightedRedTiles())) {
				
			}
		}
		
		
		/*Check that whether the clickedTile, is equal to the previous clicked tile 
		 * or whether it belongs to the whiteHighlighted Tiles.
		 * if not, the tiles will be unhighlighted. And update gameState tileClicked variable
		 */
		if(!(gameState.getTileClicked()==tile)){
			if(!(checkTile(tile,gameState.getBoard().getHighlightedWhiteTiles()))){
				gameState.getBoard().unhighlightRedTiles(out);
				gameState.getBoard().unhighlightWhiteTiles(out);
				gameState.setTileClicked(tile);
			}
		}

		/*
		 * Use helped method to check the tileClick belongs to player1 unit or not.
		 * If true, call the highlightmoveTile to highlight the tiles.
		 */
		if(checkTile(tile, player1UnitTiles)) {
			gameState.setUnitClicked(tile.getUnit());
			GroupsCommands.highlightMoveTile(out,gameState,tile.getUnit());	
			gameState.setTileClicked(tile);
		}

		/*
		 * Use helped method to check the tileClick belongs to player1 highlightedTiles List.
		 * 
		 * If true, unhighlight the tiles and then call the moveUnit method.
		 */
		if((checkTile(tile,gameState.getBoard().getHighlightedWhiteTiles()))){
=======
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
>>>>>>> b1ce9f65bb04b4493ad99af68ec97d7cc131bd8f
			gameState.getBoard().unhighlightWhiteTiles(out);
			GroupsCommands.moveUnit(out, gameState, gameState.getUnitClicked(),currentTileClicked);}
			
		if(checkTile(currentTileClicked, gameState.getBoard().getHighlightedRedTiles())){
			gameState.getBoard().unhighlightRedTiles(out);
			GroupsCommands.attackUnit(out, gameState,gameState.getUnitClicked(),currentTileClicked);}	
		
		//Setting the gameState's TileClicked with the current tile clicked at the end.
		gameState.setTileClicked(currentTileClicked);
	}
<<<<<<< HEAD

	public boolean checkTile (Tile tile, ArrayList<Tile> a) {//check whether the tile is in a arraylist.
		if(a.contains(tile)) {return true;}
		return false;
	}
=======
			
	
	//Helper method
	
	public boolean checkTile (Tile tile, ArrayList<Tile> a) {//check whether the tile is in an arraylist.
		if(a.contains(tile)) {
		return true;	
		}return false;}
>>>>>>> b1ce9f65bb04b4493ad99af68ec97d7cc131bd8f

	
	
	
	//Getter and setter
	
	public Tile getCurrentTileClicked() {return currentTileClicked;}
	public void setCurrentTileClicked(Tile currentTileClicked) {this.currentTileClicked = currentTileClicked;}
	

	
	
}