package structures;

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
	
	/**
	 * constructor: 
	 * initialize turn, ...
	 */
	public GameState() { //is an object hold by GameActor
		this.turn=1; //start of game, turn =1
	}
	
	
	//to be implemented
	//when end turn, turn +=1
}
