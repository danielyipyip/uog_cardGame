package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;

public class Avatar extends Unit{
	@JsonIgnore
	Player player;
	@JsonIgnore
	int shortSleepTime = EventProcessor.shortSleepTime;
	
	public Avatar() {
		super();
		this.attack = 2;
		this.health = 20;
		this.maxHealth = 20;
		}
	
	public void setHealth(int health, ActorRef out) {
		if (health<=0) {this.health = 0;
			player.lose(out); //player lost
		}else {this.health = health;}
		
		//also update health in player status
		this.player.setHealth(this.health); 
		BasicCommands.setUnitHealth(out, this, health);
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	public Player getPlayer() {return player;}
	public void setPlayer(Player player) {this.player = player;}
	
}
