package qot.items;

import envision.game.objects.items.Item;
import envision.game.objects.items.Potion;
import envision.game.objects.items.Weapon;
import eutil.datatypes.util.EList;
import qot.items.potions.healing.Healing_Lesser;
import qot.items.potions.mana.Mana_Lesser;
import qot.items.weapons.melee.swords.Sword_Wooden;

// This will contain every item in the game
public class Items {
	
	// All Weapons
	public static Weapon woodSword = new Sword_Wooden();
	
	// All Potions
	public static Potion lesserHealing = new Healing_Lesser();
	public static Potion lesserMana = new Mana_Lesser();
	
	// All Armor
	
	private static final EList<Item> items = EList.newList();
	static {
		items.add(woodSword);
		items.add(lesserHealing);
		items.add(lesserMana);
	}
	
	public static Item random() {
		return items.getRandom().copy();
	}
	
}
