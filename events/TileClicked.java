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

	public TileClicked() {

	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		//orig code
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();

		Tile tile = gameState.getBoard().getTile(tilex, tiley);
		//if an unit is clicked b4
		if (gameState.getUnitClicked()!=null) {
			if (tile.getUnit()!=null) {
				if(tile.getUnit().equals(gameState.getUnitClicked())) {
					//i.e. clicked on itself -> trigger unclick ((a) variable; (b) unhighlight)
					gameState.setUnitClicked(null);
					gameState.getBoard().unhighlightWhiteTiles(out);
				}
			}
			else if(gameState.getBoard().getHighlightedWhiteTiles().indexOf(tile)!=-1) {
				//i.e. this tile is found in HighlightedWhiteTiles -> move (see group commands for steps)
				GroupsCommands.moveUnit(out, gameState, gameState.getUnitClicked(), tile);
			}
		}else if(!(tile.getUnit()==null)){
			Unit unit = tile.getUnit();
			gameState.setUnitClicked(unit); //newly added (Jun16)
			GroupsCommands.highlightMoveTile(out, gameState, unit);
		}


	}
}


