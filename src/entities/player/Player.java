package entities.player;

import main.Game;
import entities.Entity;
import gameSystems.questSystem.PathFinder;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import items.Item;
import items.Weapon;
import util.mathUtil.NumUtil;

public class Player extends Entity {
	
	public double EXP;
	public Inventory inventory;
	public double flameCost = 3;
	public double healCost = 4.5;
	public double flameDamage;
	public double healAmount;
	public double weaponDamage = 0;
	private RouteTracker routes;
	private PathFinder findPath;
	
	public Player(String nameIn) { this(nameIn, 0, 0); }
	public Player(String nameIn, int posX, int posY) {
		super(nameIn, 0, 200, 200, 50, 50, 0, 10);
		
		EXP = 0;
		flameDamage = 10;
		healAmount = 10;
		
		inventory = new Inventory(this);
		routes = new RouteTracker(this);
		findPath = new PathFinder(null);
		
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.player);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void kill() {
		Game.getGameRenderer().removeObject(this);
		super.kill();
	}
	
	
	// Leveling up
	public void levelUp() {
		if (EXP == 1000 * level) {
			// LOOK AT THIS
			// Add upgrades in here
			level++;
		}
	}
	
	//---------
	// Getters
	//---------
	
	public double getTrueDamage() { return damage; }
	
	public double getDamage() {
		double damage = NumUtil.getRoll(0, 3);
		return (damage == 0) ? 0 : (damage + weaponDamage);
	}
	
	public double getMaxHealth() { return maxHealth; }
	public double getMaxMana() { return maxMana; }
	
	//---------
	// Setters
	//---------
	
	public void setDamage(double damageIn) { damage = damageIn; }
	public void setHealth(double healthIn) { health = healthIn; }
	
	//----------------
	// Player Methods
	//----------------
	
	public boolean useHeal() {
		if (manaCheck(healCost)) {
			heal(healAmount);
			return true;
		}
		return false;
	}
	
	public boolean useFlame(Entity enemy1) {
		if (enemy1 != null && manaCheck(flameCost)) {
			enemy1.hurt(flameDamage);
			return true;
		}
		return false;
	}
	
	// Use or get rid of a weapon in your hand!
	public void equipWeapon(Weapon weapon) {
		damage = damage + weapon.getDamage();
	}
	
	public void unequipWeapon(Weapon weapon) {
		damage = damage - weapon.getDamage();
	}
	
	public Item getEquipedItem() { return null; }
	
	public void statusOfWeapon() {
		
	}
	
	//----------------
	// Player Getters
	//----------------
	
	public Inventory getInventory() { return inventory; }
	@Override public RouteTracker getBackgroundStats() { return routes; }
	
}
