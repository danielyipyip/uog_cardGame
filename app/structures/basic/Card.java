package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;
import utils.BasicObjectBuilders;

/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Card {
	
	int id;
	String cardname;
	int manacost;
	MiniCard miniCard;
	BigCard bigCard;
	
	//I make
	@JsonIgnore
	int shortSleepTime = EventProcessor.shortSleepTime;
	@JsonIgnore
	int middleSleepTime = EventProcessor.middleSleepTime;
	
	public Card() {};
	public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super();
		this.id = id;
		this.cardname = cardname;
		this.manacost = manacost;
		this.miniCard = miniCard;
		this.bigCard = bigCard;
	}
	
	public void playCard(ActorRef out, GameState gameState, Tile currentTileClicked) {
		int n;
		String[] nameWord = this.getCardname().split(" ");
		Unit unit = null;
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {n=1;}
		else {n=2;}
		String unitConfigName = "conf/gameconfs/units/"+nameWord[0]+"_"+nameWord[1]+".json";
		unit = BasicObjectBuilders.loadUnit(unitConfigName, Unit.newid(n), Unit.class);
		unit.setup(this); //do anything needed to init a unit

		//Add the unit to the relevant array
		gameState.getBoard().addTileAndAvatarToPlayerArray(currentTileClicked, unit, gameState);
		
		//Draw out Unit and it's stats on the browser
		BasicCommands.drawUnit(out, unit, currentTileClicked);
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit, unit.getAttack());
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit , unit.getHealth());
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		
		//Unit ability on summon
		if(unit.getName().equals("Azure Herald")) {
			azureHeraldPassive(out,gameState);
		} 
		if(unit.getName().equals("Blaze Hound")) {
			blazeHoundPassive(out,gameState);
		}
		
		/*
		Can we do something like unit.skillOnSummon();
		Then, Azure Herald and Blaze Hound are extended from unit.
		And our code will call their own skill method?
		
		Or other ways, such that we do not need to hard code the abilities on summon like the above.
		*/
		

	}
	
	//works for player1 only for now
	public void azureHeraldPassive(ActorRef out, GameState gameState) {
		Unit player1Avatar = gameState.getBoard().getPlayer1Units().get(0);
		int value = 0;
		if(player1Avatar.getHealth() + 3 > player1Avatar.getMaxHealth()) {
			value = player1Avatar.getMaxHealth();
		} else {
			value = player1Avatar.getHealth() + 3;
		}
		player1Avatar.setHealth(value, out);
		gameState.getPlayer1().setHealth(value);
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, player1Avatar, player1Avatar.getHealth());
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void blazeHoundPassive(ActorRef out, GameState gameState) {
		gameState.getPlayer1().cardDraw();
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.getPlayer2().cardDraw();
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	//getter setter
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public String getCardname() {return cardname;}
	public void setCardname(String cardname) {this.cardname = cardname;}
	public int getManacost() {return manacost;}
	public void setManacost(int manacost) {this.manacost = manacost;}
	public MiniCard getMiniCard() {return miniCard;}
	public void setMiniCard(MiniCard miniCard) {this.miniCard = miniCard;}
	public BigCard getBigCard() {return bigCard;}
	public void setBigCard(BigCard bigCard) {this.bigCard = bigCard;}
}
