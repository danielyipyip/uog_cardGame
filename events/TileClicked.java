package events;




import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Position;
import structures.basic.Tile;

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
	
	
	public TileClicked() {
		
	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		player1Position = gameState.getBoard().getPlayer1Avatar().getPosition();
		int player1PositionX = player1Position.getTilex();
		int player1PositionY = player1Position.getTiley();
		if(tilex==player1PositionX && tiley==player1PositionY) {
			BasicCommands.addPlayer1Notification(out,"Avatar Tile", 5);
			highlightMoveTile(out,gameState.getBoard());		}
		
		//unhighlight card
		gameState.unHighlightCard(out);
		
		}
	
	public void highlightMoveTile (ActorRef out,Board board) {
		
		int player1PositionX = player1Position.getTilex();
		int player1PositionY = player1Position.getTiley();
		
		int x = player1PositionX-1;
		int y = player1PositionY-1;
		
		for(int i=x;i<= player1PositionX+1;i++) {
			
			for(int j=y;j<=player1PositionY+1;j++) {
				
			if(!(i==player1PositionX && j==player1PositionY)) {
			Tile tile = board.getTile(i, j) ;
			//if the tile does not have any unit....then draw Tile?
			BasicCommands.drawTile(out, tile, 1);
			}
			}
		}
		
		for(int i=x-1;i<= player1PositionX+2;i++) {
				
			if(!(i==player1PositionX)) {
			Tile tile = board.getTile(i, player1PositionY) ;
			//if the tile does not have any unit....then draw Tile?
			BasicCommands.drawTile(out, tile, 1);
			}
			}
		
		for(int i=y-1;i<= player1PositionY+2;i++) {
			
			if(!(i==player1PositionY)) {
			Tile tile = board.getTile(player1PositionX, i) ;
			//if the tile does not have any unit....then draw Tile?
			BasicCommands.drawTile(out, tile, 1);
			}
			}
		}
		
		
		
		
	
	
	    
	
}

