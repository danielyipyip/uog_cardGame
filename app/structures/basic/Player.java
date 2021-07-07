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
	
	//draw the hand in display
	public void drawHand(ActorRef out) {
		int pos=0;	
		for (Card i:myhand.getMyhand()) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	//delete a card both in front-end (display) and back-end (Hand)
	//(2) delete card from player's Hand (back-end)
	//(3) delect card from front-end display and shift the remaining cards to the left
	public void removeCard(ActorRef out, int n) {
		myhand.removeCard(n);
		this.drawHand(out);//change the displays:redraw previous card
		BasicCommands.deleteCard(out, myhand.getMyhand().size());//change the displays: remove last card (will not be redraw)
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
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
