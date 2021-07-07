package structures;


import java.util.ArrayList;
import java.util.HashMap;
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
			this.unSelectCard(); //(1)
			this.currentPlayer.removeCard(out, cardPos); //(2,3)
		}
	} 
	
	//other than 4 things to remove when a unit died, 2 more things
	//(5)show dead animation (6)remove from front end display 
	public void setUnitHealth(ActorRef out, Unit unit, int newHealth) {
		int pos;
		//unit died
		if (newHealth<=0) {
			//only one of the indexOf will find
			pos=board.getPlayer1Units().indexOf(unit); 
			pos=board.getPlayer2Units().indexOf(unit);
			//unit died
			if (pos!=-1) {
				board.removeUnit(unit); //(1-4)
				//(5)
				BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death); 
				try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				//(6) just redraw empty tile?
				BasicCommands.drawTile(out, board.unit2Tile(unit), 0);
				try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			} //avatar died
			else{
				//add win/lose logic
			}
		}
		//unit NOT died, just do normally
		else {unit.setHealth(newHealth);} 
	}
	
	//unhighlight card and set instance variables to default value
	public void unHighlightCard(ActorRef out) {
		BasicCommands.drawCard(out, cardSelected, cardPos, 0);
		unSelectCard();
	}
	
	//to be implemented
	//when end turn, turn +=1 OR change player
}