package structures.basic.unit;

import akka.actor.ActorRef;
import events.EventProcessor;
import structures.GameState;
import structures.basic.Unit;

public class summonDraw extends Unit{
	int middleSleepTime = EventProcessor.middleSleepTime;
	
	public void summonEffect(ActorRef out, GameState gameState) {
		gameState.getPlayer1().cardDraw(out);
		gameState.getPlayer2().cardDraw(out);
	}
}
