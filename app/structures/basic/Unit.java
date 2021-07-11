package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import Exceptions.UnitDieException;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile. 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {
	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	int id;
	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;

	//we added
	@JsonIgnore
	int attack;
	@JsonIgnore
	int health;
	@JsonIgnore
	//boolean attacked = false; change to int..indicate the number of attacked.
	protected int attacked = 0;
	@JsonIgnore
	boolean moved = false; 
	@JsonIgnore
	int maxHealth;
	@JsonIgnore
	static int player1index = 2;
	@JsonIgnore
	static int player2index = -2;
	//For ability
	@JsonIgnore
	String name;
	@JsonIgnore
	int shortSleepTime = EventProcessor.shortSleepTime;
	@JsonIgnore
	int middleSleepTime= EventProcessor.middleSleepTime;
	@JsonIgnore
	int longSleepTime= EventProcessor.longSleepTime;
	@JsonIgnore
	int sleepTime = EventProcessor.sleepTime;
	
	
	//different constructors
	public Unit() {}
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(0,0,0,0);
		this.correction = correction;
		this.animations = animations;
	}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}
	
	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
	}

	//set up initial parameter for a card
	//parameter: (1)attack (2)health (3)maxHealth (4)cardName
	//(5)Attacked=true (6)moved=True (so cannot move/attack the turn summoned)
	public void setup(Card card) {
		this.initSetAttack(card.getBigCard().getAttack());
		this.initSetHealth(card.getBigCard().getHealth());
		this.setMaxHealth(this.health);
		this.setName(card.getCardname());
		this.setAttacked(1);
		//this.setAttacked(true);
		this.setMoved(true);
	}
	
	///////////////set atk, set health///////////
	public void setAttack(int attack, ActorRef out) {
		if (attack<0) {this.attack = 0;} else {this.attack = attack;}
		BasicCommands.setUnitAttack(out, this, attack);
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//(1) set unit health, front-end, back-end
	public void setHealth(int health, ActorRef out) throws UnitDieException{
		//back-end
		if (health<=0) {this.health = 0; this.die(out);
		}else {this.health = health;}
		//front-end
		BasicCommands.setUnitHealth(out, this, health);
		try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	//handle what happens when a unit dies
	//(1) dead animation
	//(2)remove: front-end, 
	//(3)remove: back-end by throwing exception (handle in gameState)
	public void die(ActorRef out) throws UnitDieException{
		//play animation
		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.death);
		try {Thread.sleep(longSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//remove it (front-end)
		BasicCommands.deleteUnit(out, this);
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//remove it (back-end)
		throw new UnitDieException("");
	}
	
	
	public void windShrikeAbility() {
		
	}

	//////////////////move ///////////////////
	//6 steps to move a unit: 
	//(1)swap unit's associated tiles (2)change player1/2UnitTiles & unitOccupiedTiles
	//(3) unhightlight (4) move animation; (5) actual moving (6) set UnitClicked to null; 
	//(7)set moved to true
	public void moveUnit(ActorRef out, Tile targetTile) {
		//gameState.switchUnitMoving();
		//(4) move animation
		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.move);
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//(5) moveUnitToTile
		BasicCommands.moveUnitToTile(out, this, targetTile);
		this.setPositionByTile(targetTile); 
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//(7) set move to true
		this.setMoved(true);
	}
	
	//for sub-class
	public void summonEffect(ActorRef out, GameState gameState) {;}
	
	/**
	 * This command sets the position of the Unit to a specified
	 * tile.
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
	}
	
	//get a new unit id for object builder
	public static int newid(int n){if (n==1)return player1index++; else return player2index--;}
	
	//For ability
	public void setName(String name) {this.name = name;}
	public String getName() {return this.name;}

	//Normal attack with counter
	public void attackUnit (ActorRef out, GameState gameState,Unit unit, Tile target) {
		//unit = attacker, target = being attacked
		Unit attackTarget = target.getUnit();
		int targetNewHealth = attackTarget.getHealth() - unit.getAttack();
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setUnitHealth(out, attackTarget, targetNewHealth);
		if(targetNewHealth >0) {counterAttack(out,gameState,unit,target);}
		unit.setAttacked(this.attacked+1);
	}
	
	public void counterAttack (ActorRef out, GameState gameState,Unit unit, Tile target){
		int attackerNewHealth = unit.getHealth() - target.getUnit().getAttack();
		BasicCommands.playUnitAnimation(out, target.getUnit(), UnitAnimationType.attack);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setUnitHealth(out, unit, attackerNewHealth);
	}
	//combine attack with counter attack
	//public void attackWithCounter (ActorRef out, GameState gameState,Unit unit, Tile target) {
		//attackUnit(out,gameState,unit,target);
		//if(target.getUnit().getHealth()>=0) {counterAttack(out,gameState,unit,target);}
		//unit.setAttacked(attacked+1);
//}

	
	//getter setter
	public void initSetAttack(int atk) {this.attack=atk;}
	public void initSetHealth(int health) {this.health=health;}
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public UnitAnimationType getAnimation() {return animation;}
	public void setAnimation(UnitAnimationType animation) {this.animation = animation;}
	public ImageCorrection getCorrection() {return correction;}
	public void setCorrection(ImageCorrection correction) {this.correction = correction;}
	public Position getPosition() {return position;}
	public void setPosition(Position position) {this.position = position;}
	public UnitAnimationSet getAnimations() {return animations;}
	public void setAnimations(UnitAnimationSet animations) {this.animations = animations;}
	public int getAttack() {return attack;}
	public int getHealth() {return health;}
	public int getAttacked() {return attacked;}
	public void setAttacked(int attacked) {this.attacked = attacked;}
	public boolean isMoved() {return moved;}
	public void setMoved(boolean moved) {this.moved = moved;}
	public int getMaxHealth() {return maxHealth;}
	public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
}
