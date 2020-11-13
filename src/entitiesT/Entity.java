package entitiesT;

import util.mathUtil.NumUtil;

public abstract class Entity {
	
	public String name;
	public int level;
	public double maxHealth;
	public double health;
	public double maxMana;
	public double mana;
	public double damage;
	public double gold;
	public boolean isDead;
	
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
	
	// Getters
	public String getName() { return name; }
	public int getLevel() { return level; }
	public double getHealth() { return health; }
	public double getMana() { return mana; }
	public double getGold() { return gold; }
	public double getDamage() { return damage; }
	
	public void hurt(double amount) {
		System.out.println(name + " takes " + amount + " damage!");
		double damage = amount;
		health = NumUtil.clamp(health - damage, 0, health);
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
	
	protected boolean manaCheck(double spellCost) {
		if (mana >= spellCost) {
			drainMana(spellCost);
			return true;
		}
		System.out.println("YOU HAVE NO MANA, BITCH");
		return false;
	}
	
}
