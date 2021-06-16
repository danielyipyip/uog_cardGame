package events;




import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;

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
		if(!(tile.getUnit()==null)){
			Unit unit = tile.getUnit();
			gameState.getBoard().highlightMoveTile(out, gameState, unit);

		}
		}
}

