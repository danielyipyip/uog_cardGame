package structures;


import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.AvatarException;
import Exceptions.UnitDieException;

import java.util.*;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.basic.*;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	//instance variables
	int turn;
	Player player1;
	Player player2;
	Board board;
	
	/*cardSelected = x when card x is clicked
	 *cardPos = n when card on hand position n is clicked
	 *if not selecting anyCard, n=-1 */
	Card cardSelected; 
	int cardPos; 
	Tile tileClicked; //to store the previous clicked tile;
	Unit unitClicked;
	Player currentPlayer;
	//Boolean unitMoving;
	
	
	int middleSleepTime = EventProcessor.middleSleepTime;
	
	//constructor
	public GameState() { //is an object hold by GameActor
		this.turn=1; //start of game, turn =1
		
		//create new players
		//starting mana =2 (turn 1)
		player1 = new HumanPlayer(20,2);
		player2 = new AIPlayer(20,2);
		board = new Board();
		tileClicked= null;
		//Default cardSelected is null and cardPos is -1
		cardSelected = null; 
		cardPos = -1;

		//start from player 1
		currentPlayer=player1;
		
		//unitMoving = false;
	}
	
	//helper method
	//just to group 2 steps in once
	public void unSelectCard() {this.setcardPos(-1); this.setCardSelected(null);} //unselect the card
	
	//Getter and Setter Method
	public int getTurn() {return turn;}
	public void setTurn(int turn) {this.turn = turn;}
	public Player getPlayer1() {return player1;}
	public Player getPlayer2() {return player2;}
	public Board getBoard() {return board;}
	public void setCardSelected(Card cardClicked) {this.cardSelected = cardClicked;}
	public Card getCardSelected() {return cardSelected;}
	public void setcardPos(int pos) {this.cardPos = pos;}
	
	public Tile getTileClicked() {return tileClicked;}
	public void setTileClicked(Tile tileClicked) {this.tileClicked = tileClicked;}

	/*public void switchUnitMoving() {
		this.unitMoving = !this.unitMoving;
	}
	public boolean getUnitMoving() {
		return this.unitMoving;
	}*/
	
	public int getcardPos() {return cardPos;}	
	public Unit getUnitClicked() {return unitClicked;}
	public void setUnitClicked(Unit unitClicked) {this.unitClicked = unitClicked;}
	public Player getCurrentPlayer() {return currentPlayer;}
	public void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer;}

	////////////// cards related /////////////////
	//draw hand (of cards) on display
	public void drawHand(ActorRef out) {currentPlayer.drawHand(out);}

	//delete a card both in front-end (display) and back-end (Hand)
	//few things it does: (1)unselect cards: gameState.cardPos=-1 gameState.CardSelected=null
	//(2) delete card from player's Hand (back-end)
	//(3) delect card from front-end display and shift the remaining cards to the left
	public void deleteCard(ActorRef out) {
		this.currentPlayer.removeCard(out, cardPos); //(2,3)
		this.unSelectCard(); //(1)
	} 
	
	//play a card: go to player then card
	public void playCard(ActorRef out, GameState gameState, Card card, Tile currentTileClicked) {
		currentPlayer.playCard(out, gameState, card, currentTileClicked);
		//after card played, 
		//un-hightlight
		gameState.getBoard().unhighlightRedTiles(out);
		//Unhighlight Tiles and Delete Cards
		gameState.getBoard().unhighlightWhiteTiles(out);
		//remove the card from hand (& update display)
		gameState.deleteCard(out);
	}
	
	///////////////////unit related/////////////////
	
	//other than 4 things to remove when a unit died, 2 more things
	//(5)show dead animation (6)remove from front end display 
	public void setUnitHealth(ActorRef out, Unit unit, int newHealth) {
		try{unit.setHealth(newHealth, out);} //will 
		catch(UnitDieException e) {this.getBoard().removeUnit(unit);}
		catch(AvatarException f) {
			if(this.getCurrentPlayer()==this.getPlayer1()) {
				BasicCommands.setPlayer1Health(out, this.getCurrentPlayer());
			}else {
				BasicCommands.setPlayer2Health(out, this.getCurrentPlayer());
			}
		}
	}
	
	public void setUnitAttack(ActorRef out, Unit targetUnit, int attack) {
		targetUnit.setAttack(attack, out);
	}
	
	/////////////////unit action related (unit move/attack) ///////////////////
	public void unitMove(ActorRef out, GameState gameState, Unit unit, Tile targetTile) {
		int player;
		if (gameState.getCurrentPlayer().equals(gameState.getPlayer1())){player=1;}
		else {player=2;}
		//(1) get unit's tile b4 moving; swap unit's associated tile to new tile
		Tile previousTile = gameState.getTileClicked();
		previousTile.setUnit(null);
		targetTile.setUnit(unit);
		//(2) change player1/2UnitTiles & unitOccupiedTiles
		gameState.getBoard().removeUnit(previousTile);
		gameState.getBoard().addUnit(targetTile, player);
		//(3) un-hightlight tiles
		gameState.getBoard().unhighlightWhiteTiles(out);
		gameState.getBoard().unhighlightRedTiles(out);
		//(6) set seleted unit to null & pos to -1
		gameState.unSelectCard();
	}
	
	
	//unhighlight card and set instance variables to default value
	public void unHighlightCard(ActorRef out) {
		BasicCommands.drawCard(out, cardSelected, cardPos, 0);
		unSelectCard();
	}
		public int currentPlayer() {
			if (this.getCurrentPlayer()==this.getPlayer1()) {return 1;}
			else {return 2;}
		}
}


//other than 4 things to remove when a unit died, 2 more things
//(5)show dead animation (6)remove from front end display 
//public void setUnitHealth(ActorRef out, Unit unit, int newHealth) {
//	int pos;
//	//unit died
//	if (newHealth<=0) {
//		//only one of the indexOf will find
//		pos=board.getPlayer1Units().indexOf(unit); 
//		pos=board.getPlayer2Units().indexOf(unit);
//		//unit died
//		if (pos!=-1) {
//			board.removeUnit(unit); //(1-4)
//			//(5)
//			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death); 
//			try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//			//(6) just redraw empty tile?
//			BasicCommands.drawTile(out, board.unit2Tile(unit), 0);
//			try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		} //avatar died
//		else{
//			//add win/lose logic
//		}
//	}
//	//unit NOT died, just do normally
//	else {unit.setHealth(newHealth);} 
//}