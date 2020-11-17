package entities.player;

import java.util.Scanner;
import main.Game;
import entities.Entity;
import items.Item;
import items.Potion;
import items.Weapon;
import items.types.LesserHealing;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class Player extends Entity {
	
	Scanner reader = new Scanner(System.in);
	
	public double EXP;
	public Inventory inventory;
	public boolean isDead;
	public double flameCost = 3;
	public double healCost = 4.5;
	public double flameDamage;
	public double healAmount;
	public double weaponDamage = 0;
	
	public Player(String name, int level, double maxHealth, double health, double maxMana, double mana, double gold, double damage, double EXPIn) {
		super(name, level, maxHealth, health, maxMana, mana, gold, damage);
		EXP = EXPIn;
		inventory = new Inventory(this);
		flameDamage = 10;
		healAmount = 10;
		
		init(Game.getGameRenderer(), 0, 0);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawCircle(midX, midY - 20, 19, 30, EColors.black);
		drawCircle(midX, midY - 19, 18, 30, EColors.white);
		
		drawRect(midX - 4, midY - 18, midX + 4, midY + 9, EColors.black);
		drawRect(midX - 5, midY - 19, midX + 5, midY + 10, EColors.cyan);
		
		super.drawObject(mXIn, mYIn);
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
	
	public void setDamage(double damageIn) {
		damage = damage + damageIn;
	}
	
	public void setHealth (double healthIn) {
		health = health + healthIn;
	}
	
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
