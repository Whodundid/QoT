package entities;

import eWindow.windowTypes.WindowObject;
import entities.player.Player;
import gameScreens.BattleScreen;
import gameSystems.questSystem.RouteTracker;
import main.Game;
import particles.DamageSplash;
import util.mathUtil.Direction;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public abstract class Entity extends WindowObject {
	
	protected String name;
	protected int level;
	protected double maxHealth;
	protected double health;
	protected double maxMana;
	protected double mana;
	protected double damage;
	protected double gold;
	protected boolean isDead;
	protected boolean drawHitbox = false;
	
	public Entity(String nameIn, int levelIn, double maxHealthIn, double healthIn, double maxManaIn, double manaIn, double damageIn, double goldIn) {
		name = nameIn;
		level = levelIn;
		maxHealth = maxHealthIn;
		health = healthIn;
		maxMana = maxManaIn;
		mana = manaIn;
		damage = damageIn;
		gold = goldIn;
		isDead = false;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawHitbox) { drawHRect(EColors.yellow); }
		super.drawObject(mXIn, mYIn);
	}
	
	//---------
	// Methods
	//---------
	
	public void move(Direction dir) {
		switch (dir) {
		case N: move(0, -1); break;
		case NE: move(1, -1); break;
		case E: move(1, 0); break;
		case SE: move(1, 1); break;
		case S: move(0, 1); break;
		case SW: move(-1, 1); break;
		case W: move(-1, 0); break;
		case NW: move(-1, -1); break;
		default: break;
		}
	}
	
	public void hurt(double amount) {
		System.out.println(name + " takes " + amount + " damage!");
		double damage = amount;
		health = NumUtil.clamp(health - damage, 0, health);
		
		Game.getGameRenderer().addObject(new DamageSplash(midX, startY, damage));
		
		if (health <= 0) { kill(); }
	}
	
	public void heal(double amount) {
		System.out.println(name + " heals for " + amount + " damage?!");
		health = NumUtil.clamp(health + amount, 0, maxHealth);
	}
	
	public void drainMana(double amount) {
		mana = NumUtil.clamp(mana - amount, 0, Double.MAX_VALUE);
	}
	
	public void kill() {
		health = 0;
		isDead = true;
	}
	
	public void kill(Player player) {
		health = 0;
		isDead = true;
		player.getBackgroundStats().setEnemiesKilled(1);
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return name; }
	public int getLevel() { return level; }
	public double getHealth() { return health; }
	public double getMana() { return mana; }
	public double getGold() { return gold; }
	public double getDamage() { return damage; }
	public boolean isDead() { return isDead; }
	
	//---------
	// Setters
	//---------
	
	public Entity setDrawHitbox(boolean val) { drawHitbox = val; return this; }
	
	//public void setHealth(double healthIn) {
	//	health = health + healthIn;
	//}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	/** Returns true if this entity had enough mana to use the given spell. */
	protected boolean manaCheck(double spellCost) {
		if (mana >= spellCost) {
			drainMana(spellCost);
			return true;
		}
		System.out.println("YOU HAVE NO MANA, BITCH");
		return false;
	}

	public abstract RouteTracker getBackgroundStats();

	
}
