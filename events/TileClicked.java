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
	
	
	Position player1Position;
	Position player2Position;
	ArrayList <Tile> highlightedTile; //to stored the tile that are highlighted so we can unhighlight later
	
	public TileClicked() {
		
		highlightedTile = new ArrayList<Tile>(); //to keep track of movable tile.
		
	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		Tile tile = gameState.getBoard().getTile(tilex, tiley);
		if(!(tile.getUnit()==null)){
			Unit unit = tile.getUnit();
			highlightMoveTile(out,gameState,unit);
		}
		
		
		
		
		//Unhighlighted Tiles. Below Doesnt work. Still working on it
		if(!(tilex==tile.getTilex()&&tiley==tile.getTiley())){ 
			unhighlightTiles(out);
			
		}
		
		
		
		
}


	public void highlightMoveTile (ActorRef out,GameState gameState ,Unit unit) {
		
		int positionX = unit.getPosition().getTilex();
		int positionY = unit.getPosition().getTiley();
		
		//Highlighted the tiles surrounding the avatar first.
		int x = positionX-1;
		int y = positionY-1;
		
		ArrayList<Tile> occupiedTiles = gameState.getOccupiedTiles();
		
		for(int i=x;i<= unit.getPosition().getTilex()+1;i++) {
			
			for(int j=y;j<=unit.getPosition().getTiley()+1;j++) {
				
			Tile tile = gameState.getBoard().getTile(i, j) ;
			if(!(occupiedTiles.contains(tile))) {
			//if the tile does not have any unit...then draw Tile.
			BasicCommands.drawTile(out, tile, 1);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			highlightedTile.add(tile);
			}
			}
		}
		//drawing the x+2 and x-2 square
		
		for(int i=x-1;i<=unit.getPosition().getTilex()+2;i++) {
				
			if(!(i==unit.getPosition().getTilex())) {
			Tile tile = gameState.getBoard().getTile(i, unit.getPosition().getTiley()) ;
			if(!(occupiedTiles.contains(tile))) {
			//if the tile does not have any unit
			BasicCommands.drawTile(out, tile, 1);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			highlightedTile.add(tile);
			}
			}
		}
		//drawing the y+2 and y-2 square
		for(int i=y-1;i<= unit.getPosition().getTiley()+2;i++) {
			
			if(!(i==unit.getPosition().getTiley())) {
			Tile tile = gameState.getBoard().getTile(unit.getPosition().getTilex(), i) ;
			if(!(occupiedTiles.contains(tile))) {
			//if the tile does not have any unit
			BasicCommands.drawTile(out, tile, 1);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			highlightedTile.add(tile);
			}
			}
		}
	}
	
	public void unhighlightTiles(ActorRef out) {
		
		Iterator<Tile> iter = highlightedTile.iterator();
		while (iter.hasNext()) {BasicCommands.drawTile(out, iter.next(), 0); }
		
	}

		
		
	
	
	    
	
}

