package structures.basic;

import java.util.ArrayList;

import Exceptions.NotEnoughCardException;

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
	protected int health;
	protected int mana;
	protected Deck mydeck;
	protected Hand myhand;
	protected int playerID;


	
	public Player() {
		super();
		new Player(20, 2);
		//turn 1 already 2 mana, so basic should be 2
		//original implementation
//		this.health = 20;
//		this.mana = 0;
	}
	
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;		
		myhand = new Hand();
		
	}
	
	public void cardDraw() {
		Card tempCard;
		if (mydeck.isEmpty()) {
			//exception handling for deck is empty
			throw new NotEnoughCardException("Empty deck to draw");
		}else {
			tempCard = mydeck.drawCard();
			myhand.addCard(tempCard);
		}
	}

	public int getHealth() {return health;}
	public void setHealth(int health) {this.health = health;}
	public int getMana() {return mana;}
	public void setMana(int mana) {this.mana = mana;}
	public Deck getMydeck() {return mydeck;}
	public void setMydeck(Deck mydeck) {this.mydeck = mydeck;}
	public int getPlayerID() {return playerID;}
	public void setPlayerID(int playerID) {this.playerID = playerID;}
	public Hand getMyhand() {return myhand;}
	public void setMyhand(Hand myhand) {this.myhand = myhand;}


	
	
}
