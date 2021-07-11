package structures.basic.unit;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;

public class WindShrike extends Unit{
	
int shortSleepTime = EventProcessor.shortSleepTime;
	
	public WindShrike() {
		super();
	}
	
	@Override
	public void highlightMoveTile (GameState gameState ,Tile tileClicked) {
	
		for(int i=0; i<gameState.getBoard().getX(); i++) {
			for(int j=0; j<gameState.getBoard().getY();j++) {
				Tile tile = gameState.getBoard().getTile(i, j);
				if(gameState.getBoard().getUnitOccupiedTiles().contains(tile)) continue;
				gameState.getBoard().addHighlightWhiteTiles(tile);
			}			
		} return;
	}

	@Override
	public void deathTrigger(Player player, ActorRef out) {
		// TODO Auto-generated method stub
		player.cardDraw(out);
	}	

}
	
	//public void flyAbility (ActorRef out,GameState gameState ,Tile tileClicked) {
		
		//int positionX = tileClicked.getTilex();
		//int positionY = tileClicked.getTiley();
		//Flying ability
		//if(gameState.getBoard().getTile(positionX, positionY).getUnit().getName().equals("Windshrike")){
			
			//for(int i=0; i<gameState.getBoard().getX(); i++) {
				//for(int j=0; j<gameState.getBoard().getY();j++) {
					//Tile tile = gameState.getBoard().getTile(i, j);
					//if(gameState.getBoard().getUnitOccupiedTiles().contains(tile)) continue;
					//if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
						//BasicCommands.drawTile(out, tile, 1); //only draw for player1
					//}
					//gameState.getBoard().addHighlightWhiteTiles(tile);
					//try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
				//}			
			//} return;
		//}
	//}

	

	

//}
