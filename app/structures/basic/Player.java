package structures.basic;

import java.util.ArrayList;

import Exceptions.NotEnoughCardException;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;

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
	
	//imported
	int middleSleepTime = EventProcessor.middleSleepTime;

	public Player() {
		super();
		new Player(20, 2);
	}
	
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;		
		myhand = new Hand();
		
	}
	

	public void playCard(ActorRef out, GameState gameState, Card card, Tile currentTileClicked) {
		card.playCard(out, gameState, currentTileClicked);
	}
	
	//draw a card
	public void cardDraw() {
		Card tempCard;
		if (mydeck.isEmpty()) {//exception handling for deck is empty
			throw new NotEnoughCardException("Empty deck to draw");
		}else {
			tempCard = mydeck.drawCard();
			myhand.addCard(tempCard);
		}
	}
	public void cardDraw(ActorRef out) {;}
	
	public void drawHand(ActorRef out) {;}
	public void removeCard(ActorRef out, int n) {}
	public void lose(ActorRef out) {}
	
	public void setHealth(int health) {this.health = health;}

	public int getHealth() {return health;}
	public int getMana() {return mana;}
	public void setMana(int mana) {this.mana = mana;}
	public Deck getMydeck() {return mydeck;}
	public void setMydeck(Deck mydeck) {this.mydeck = mydeck;}
	public int getPlayerID() {return playerID;}
	public void setPlayerID(int playerID) {this.playerID = playerID;}
	public Hand getMyhand() {return myhand;}
	public void setMyhand(Hand myhand) {this.myhand = myhand;}

}
