package structures.basic;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 *added deck for player
 *
 *@author Dainel Yip
 */
public class Player {

	int health;
	int mana;
	Deck mydeck;
	Hand myhand;
	int playerID;
	
	public Player() {
		super();
		new Player(20, 2); //turn 1 already 2 mana, so basic should be 2
		//original implementation
//		this.health = 20;
//		this.mana = 0;
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
//		this.playerID=playerID;
//		mydeck = new Deck(1);
		//card shuffled when player is created (should be start of game?
		//should this functionality be here OR in init?
//		mydeck.shuffleCard(); 
		//card draw in start of game
		
	}

	public int getHealth() {return health;	}
	public void setHealth(int health) {this.health = health;}
	public int getMana() {return mana;}
	public void setMana(int mana) {this.mana = mana;}
	public Deck getMydeck() {return mydeck;}
	public void setMydeck(Deck mydeck) {this.mydeck = mydeck;}
	public int getPlayerID() {return playerID;}
	public void setPlayerID(int playerID) {this.playerID = playerID;}
	
}
