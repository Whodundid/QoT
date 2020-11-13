package entities.enemy.types;

import entities.enemy.Enemy;

public class Goblin extends Enemy {
	public int treasure;
	public double loot;
	
	public Goblin() {
		super("Goblin", 1, 20, 20, 0, 0, 2, 0);
	}
	
	// This gets the loot and returns it
	public void getLoot() {
		treasure = (int) (Math.random() * 30);
		loot = (double) (treasure);
	}
}
