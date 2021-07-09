package structures.basic;

import java.util.ArrayList;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.GroupsCommands;
import events.EventProcessor;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class SpellCard extends Card{
	int middleSleepTime = EventProcessor.middleSleepTime;
	
	
	public SpellCard(){super();};
	
	//nothing new?
	public SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super(id, cardname, manacost, miniCard, bigCard);
	}
	
//	Unit targetUnit = currentTileClicked.getUnit();
	public void playCard(ActorRef out, GameState gameState, String cardName, Tile currentTileClicked) {
		Unit targetUnit = currentTileClicked.getUnit();
		//play the card: separated by which card is played
		if(cardName.equals("Truestrike")) {//deal 2 damage to a unit
			GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getHealth()-2);
			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation) , currentTileClicked);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
		if(cardName.equals("Sundrop Elixir")) { //heal a unit by 5
			int newHealth = targetUnit.getHealth()+5;
			if (newHealth>targetUnit.getMaxHealth()) { //if >max, set to max
				GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getMaxHealth());
			}else {GroupsCommands.setUnitHealth(out, targetUnit, newHealth);} //if NOT > max, set to health+5
			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff) , currentTileClicked);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
		if(cardName.equals("Staff of Y'Kir'")) { //avatar attack +=2
			GroupsCommands.setUnitAttack(out, targetUnit, targetUnit.getAttack()+2);
			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation) , currentTileClicked);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			spellThief(out, gameState); //Only when player2 plays a spell card, check if opponent(player1) has a Pureblade Enforcer
		}
		if(cardName.equals("Entropic Decay")) {  //unit health -> 0
			GroupsCommands.setUnitHealth(out, targetUnit,0); //need other story: trigger death
			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom) , currentTileClicked);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			spellThief(out, gameState); //Only when player2 plays a spell card, check if opponent(player1) has a Pureblade Enforcer
		}
		//after card played, 
		//un-hightlight
		gameState.getBoard().unhighlightRedTiles(out);
		//remove the card from hand (& update display)
		gameState.deleteCard(out);
	}
	
	//Only works on player1 (when player2 play a spell, buff player1 unit)
	//Better to modify it to check "does enemy has PureBlade Enforcer" instead of player1 only
	//(Can it exceed maximum health??)
	public void spellThief(ActorRef out, GameState gameState) {
		ArrayList<Unit> player1Units = gameState.getBoard().getPlayer1Units(); 
		
		for(Unit unit: player1Units) {
			String name = unit.getName();
			if(name == null) {continue;} //For now, some units do not have name, so use this line to prevent error first
			if(name.equals("Pureblade Enforcer")) {
				unit.setAttack(unit.getAttack() + 1);
				BasicCommands.setUnitAttack(out, unit, unit.getAttack());
				try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				unit.setHealth(unit.getHealth() + 1, out);
				BasicCommands.setUnitHealth(out, unit, unit.getHealth());
				try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			} 
		}
	}
}
