package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.GroupsCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Unit;
import structures.basic.unit.Attack2;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{
	
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		//if(gameState.getUnitMoving()) {return;}
		//unhighlight card
		gameState.unHighlightCard(out);
		gameState.getBoard().unhighlightRedTiles(out);
		gameState.getBoard().unhighlightWhiteTiles(out);
		
		/////////////////// put it b4 changing current player ///////////////////////////
		//turn+1 (if player 2 ends turn)
		if (gameState.getCurrentPlayer()==gameState.getPlayer2()) {
			gameState.setTurn(gameState.getTurn()+1);
		}
		
		//change player control (1->2; 2->1)
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
			gameState.setCurrentPlayer(gameState.getPlayer2());
			BasicCommands.addPlayer1Notification(out, "Your turn now!", 2);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}else {gameState.setCurrentPlayer(gameState.getPlayer1());
			BasicCommands.addPlayer1Notification(out, "My turn now!", 2);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		//////////////////after change current player///////////////
		//refill mana
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
			gameState.getPlayer1().setMana(Math.min(gameState.getTurn()+1, 10));
			BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		}else {
			gameState.getPlayer2().setMana(Math.min(gameState.getTurn()+1, 10));
			BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
		}try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		
		//draw card
		gameState.getCurrentPlayer().cardDraw(out);
		//player 2 no need draw on screen hand (by polymorphism now)
		
		//reset attacked OR moved for all units
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
			for (Unit i: gameState.getBoard().getPlayer1Units()) {

				i.setAttacked(0); i.setMoved(false);
				if(i instanceof Attack2) {i.setAttacked(-1); i.setMoved(false);}
				//i.setAttacked(false); i.setMoved(false);
			}
		}else {
			for (Unit i: gameState.getBoard().getPlayer2Units()) {
				i.setAttacked(0); i.setMoved(false);
				if(i instanceof Attack2) {i.setAttacked(-1); i.setMoved(false);}
				//i.setAttacked(false); i.setMoved(false);
			}
		}
		
	}

}
