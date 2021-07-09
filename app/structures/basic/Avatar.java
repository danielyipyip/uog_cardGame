package structures.basic;

public class Avatar extends Unit{
	
	Player player;
	
	public Avatar() {
		super();
		this.attack = 2;
		this.health = 20;
		this.maxHealth = 20;
	}
	
	public void setHealth(int health) {
		if (health<0) {this.health = 0;} else {this.health = health;}
		this.player.setHealth(this.health);
		//If new health < previous health, trigger Silverguard Knight's passive
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
