package game.items;

import envision.gameEngine.gameObjects.items.Potion;
import envision.gameEngine.gameObjects.items.Weapon;
import game.items.potions.healing.Healing_Lesser;
import game.items.weapons.melee.swords.Sword_Wooden;

public class Items {
	
	// This will contain every item in the game
	// All Weapons
	public static Weapon woodSword = new Sword_Wooden();
	
	// All Potions
	public static Potion lesserHealing = new Healing_Lesser();
	
	
	// All Armor
}
