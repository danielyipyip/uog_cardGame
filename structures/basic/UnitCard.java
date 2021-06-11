package structures.basic;

public class UnitCard extends Card{
	int attack; 
	int health;
	//ability?
//	String[] abilities;
	
	public UnitCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super(id, cardname, manacost, miniCard, bigCard);
		this.attack = this.getBigCard().getAttack();
		this.health = this.getBigCard().getHealth();
	}

	public int getAttack() {return attack;}
	public void setAttack(int attack) {this.attack = attack;}

	public int getHealth() {return health;}
	public void setHealth(int health) {this.health = health;}
	
}
