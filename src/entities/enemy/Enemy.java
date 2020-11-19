package entities.enemy;

import entities.Entity;
import entities.player.Player;

public abstract class Enemy extends Entity {

	public Enemy(String nameIn, int levelIn, double maxHealthIn, double healthIn, double maxManaIn, double manaIn, double damageIn, double goldIn) {
		super(nameIn, levelIn, maxHealthIn, healthIn, maxManaIn, manaIn, damageIn, goldIn);
	}
	

}
