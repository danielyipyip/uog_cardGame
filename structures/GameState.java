package structures;

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
	Position player1Position;
	Position player2Position;
	
	//constructor
	public GameState() { //is an object hold by GameActor
		this.turn=1; //start of game, turn =1
		
		//create new players
		//starting mana =2 (turn 1)
		player1 = new HumanPlayer(20,2);
		player2 = new AIPlayer(20,2);
		board = new Board();
		player1Position = board.getPlayer1Avatar().getPosition();
		player2Position = board.getPlayer2Avatar().getPosition();
	}

	public int getTurn() {return turn;}
	public void setTurn(int turn) {this.turn = turn;}
	public Player getPlayer1() {return player1;}
	public void setPlayer1(Player player1) {this.player1 = player1;}
	public Player getPlayer2() {return player2;}
	public void setPlayer2(Player player2) {this.player2 = player2;}
	public Board getBoard() {return board;}
	public Position getPlayer1Position() {return player1Position;}
	public void setPlayer1Position(Position player1Position) {this.player1Position = player1Position;}
	public Position getPlayer2Position() {return player2Position;}
	public void setPlayer2Position(Position player2Position) {this.player2Position = player2Position;}
	
	
	
	//to be implemented
	//when end turn, turn +=1
}
