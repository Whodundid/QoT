package Entities;

import Inventory.Inventory;
import Swords.Item;
import Swords.Weapon;
import Aspects.Utility;

public class Player extends Entity {
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
		double damage = Utility.getRoll(0, 3);
		if (damage == 0) {
			System.out.println(name + " missed!");
		}
		
		return (damage == 0) ? 0 : (damage + weaponDamage);
	}
	
	// Setters
	
	public void setDamage(double damageIn) {
		damage = damage + damageIn;
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
	
	public void useFlame(Enemy enemy) {
		if (enemy != null && manaCheck(flameCost)) {
			System.out.println("You cast Flame!");
			enemy.hurt(flameDamage);
		}
	}
	
	// Use or get rid of a weapon within your inventory!
	public void equipWeapon(Weapon weapon) {
		weaponDamage = weaponDamage + weapon.getDamage();
	}
	
	public void unequipWeapon(Weapon weapon) {
		weaponDamage = weaponDamage - weapon.getDamage();
	}
	
	
	// Player Getters
	
	public Inventory getInventory() {
		return inventory;
	}
	
	
	
	
}
