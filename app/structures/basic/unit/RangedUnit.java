package structures.basic.unit;

import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;

public class RangedUnit extends Unit{


	@JsonIgnore
	int shortSleepTime = EventProcessor.shortSleepTime;
	
	public RangedUnit() {
		super();
	}
	
	@Override
	public void highlightAttackTile(GameState gameState,Tile tileClicked) {//Range Attack highlight
		
		ArrayList<Tile> opponentUnitTiles = gameState.getBoard().getPlayer2UnitTiles();
		if(gameState.getCurrentPlayer().equals(gameState.getPlayer2())) 
		{opponentUnitTiles = gameState.getBoard().getPlayer1UnitTiles();}
		
		for(Tile i : opponentUnitTiles) {
			gameState.getBoard().addHighlightRedTiles(i);
		}
	}
	
	@Override
	public void attackUnit (ActorRef out, GameState gameState,Unit unit, Tile target) {//attack without counter attack
		//unit = attacker, target = being attacked
		Unit attackTarget = target.getUnit();
		int targetNewHealth = attackTarget.getHealth() - unit.getAttack();
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setUnitHealth(out, attackTarget, targetNewHealth);
		unit.setAttacked(unit.getAttack()+1);
	}
}


