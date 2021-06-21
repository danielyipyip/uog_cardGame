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




	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();

		//Get the tile with the clicked position
		Tile tile= gameState.getBoard().getTile(tilex, tiley);
		ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles(); 
		
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
			gameState.getBoard().unhighlightWhiteTiles(out);
			GroupsCommands.moveUnit(out, gameState, gameState.getUnitClicked(), tile);
		}
	}

	public boolean checkTile (Tile tile, ArrayList<Tile> a) {//check whether the tile is in a arraylist.
		if(a.contains(tile)) {return true;}
		return false;
	}


}