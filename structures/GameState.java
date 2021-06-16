package structures;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import akka.actor.ActorRef;
import commands.BasicCommands;

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
	
	
	//constructor
	public GameState() { //is an object hold by GameActor
		this.turn=1; //start of game, turn =1
		
		//create new players
		//starting mana =2 (turn 1)
		player1 = new HumanPlayer(20,2);
		player2 = new AIPlayer(20,2);
		board = new Board();
		
		//Default cardSelected is null and cardPos is -1
		cardSelected = null; 
		cardPos = -1;
	}
	
	
	
	//Getter and Setter Method
	public int getTurn() {return turn;}
	public void setTurn(int turn) {this.turn = turn;}
	public Player getPlayer1() {return player1;}
	public Player getPlayer2() {return player2;}
	public Board getBoard() {return board;}
	public void setCardSelected(Card cardClicked) {this.cardSelected = cardClicked;}
	public Card getCardSelected() {return cardSelected;}
	public void setcardPos(int pos) {this.cardPos = pos;}
	public int getcardPos() {return cardPos;}

	
	//Method to unhighlight card and set instance variables to default value
	public void unHighlightCard(ActorRef out) {
		BasicCommands.drawCard(out, cardSelected, cardPos, 0);
		cardSelected = null; 
		cardPos = -1;
	}
	
	
	
	//to be implemented
	//when end turn, turn +=1
}
