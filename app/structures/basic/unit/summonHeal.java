package structures.basic.unit;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Unit;

public class summonHeal extends Unit{

	public void summonEffect(ActorRef out, GameState gameState) {
		Unit player1Avatar = gameState.getBoard().getPlayer1Units().get(0);
		int value = 0;
		if(player1Avatar.getHealth() + 3 > player1Avatar.getMaxHealth()) {
			value = player1Avatar.getMaxHealth();
		} else {value = player1Avatar.getHealth() + 3;}
		gameState.setUnitHealth(out, gameState.getBoard().getPlayer1Units().get(0), value);
	}
}
