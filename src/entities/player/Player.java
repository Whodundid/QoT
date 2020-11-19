package entities.player;

import java.util.Scanner;
import main.Game;
import entities.Entity;
import gameSystems.questSystem.PathFinder;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import items.Item;
import items.Potion;
import items.Weapon;
import items.types.LesserHealing;
import util.mathUtil.NumUtil;

public class Player extends Entity {
	
	Scanner reader = new Scanner(System.in);
	
	public double EXP;
	public Inventory inventory;
	public double flameCost = 3;
	public double healCost = 4.5;
	public double flameDamage;
	public double healAmount;
	public double weaponDamage = 0;
	private RouteTracker routes = new RouteTracker();
	private PathFinder findPath = new PathFinder(null);
	
	public Player(String nameIn) { this(nameIn, 0, 0); }
	public Player(String nameIn, int posX, int posY) {
		super(nameIn, 0, 200, 200, 50, 50, 0, 10);
		
		EXP = 0;
		inventory = new Inventory(this);
		flameDamage = 10;
		healAmount = 10;
		
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
	
	// Getters
	
	public double getTrueDamage() {
		return damage;
	}
	
	public double getDamage() {
		double damage = NumUtil.getRoll(0, 3);
		if (damage == 0) {
			System.out.println(name + " missed!");
		}
		
		return (damage == 0) ? 0 : (damage + weaponDamage);
	}
	
	// Setters
	
	public void setDamage(double damageIn) { damage = damageIn; }
	public void setHealth(double healthIn) { health = healthIn; }
	
	// Player Methods
	
	public void displayInventory() {
		for (Item i : inventory.getItems()) {
			System.out.println(i.getName() + " - " + i.getDescription());
		}
	}
	
	public void useHeal() {
		if (manaCheck(healCost)) {
			System.out.println("You cast Heal!");
			heal(healAmount);
		}
	}
	
	public void useFlame(Entity enemy1) {
		if (enemy1 != null && manaCheck(flameCost)) {
			System.out.println("You cast Flame!");
			enemy1.hurt(flameDamage);
		}
	}
	
	// Use or get rid of a weapon in your hand!
	public void equipWeapon(Weapon weapon) {
		damage = damage + weapon.getDamage();
	}
	
	public void unequipWeapon(Weapon weapon) {
		damage = damage - weapon.getDamage();
	}
	
	public void statusOfWeapon() {
		
	}
	
	
	// Player Getters
	
	public Inventory getInventory() {
		return inventory;
	}
	
	@Override
	public RouteTracker getBackgroundStats() {
		return routes;
	}
	
	public void utilizeInventory(String nameIn) {
		System.out.println("What item would you like to use?");
		displayInventory();
		String slot = reader.nextLine();
		Item itemInUse = inventory.getItem(slot);
		if (itemInUse instanceof Potion) {
			if (itemInUse instanceof LesserHealing) {
				System.out.println("Temporary version of Healing and removing the potion from the inventory! :D");
				System.out.println("This is hard coded in Player.java rather than LesserHealing.java so I need to fix that somehow. ): But it's okay it works for now! :D");
				if (maxHealth <= (health + 30)) {
					health = maxHealth;
				}
				else {
					setHealth(30);
				}
			}
			inventory.removeItem(slot);
		}
		else {
			System.out.println("The item you specified is not usable.");
		}
	}
	
}
