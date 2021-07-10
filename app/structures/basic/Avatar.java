package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Exceptions.AvatarException;
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

	@Override
	//If new health < previous health, trigger Silverguard Knight's passive
	public void setHealth(int health, ActorRef out) throws AvatarException{
		if (health<=0) {this.health = 0;
		player.lose(out); //player lost
		}else {this.health = health;}

		//also update health on board
		BasicCommands.setUnitHealth(out, this, health);
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}

		//also update health in player status
		this.player.setHealth(this.health);

		throw new AvatarException("");
		//do outside
//		if (playerIndex==1) {BasicCommands.setPlayer1Health(out, player);}
//		else {BasicCommands.setPlayer2Health(out, player);}
	}

	public Player getPlayer() {return player;}
	public void setPlayer(Player player) {this.player = player;}


}
