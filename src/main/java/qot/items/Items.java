package qot.items;

import envision.game.items.Item;
import envision.game.items.Potion;
import envision.game.items.Weapon;
import eutil.datatypes.util.EList;
import qot.items.armor.BootsOfSpeed;
import qot.items.armor.DragonShield;
import qot.items.potions.healing.Healing_Lesser;
import qot.items.potions.healing.Healing_Major;
import qot.items.potions.mana.Mana_Lesser;
import qot.items.potions.mana.Mana_Major;
import qot.items.weapons.magic.WoodenStick;
import qot.items.weapons.melee.swords.Sword_Wooden;

// This will contain every item in the game
public class Items {
	
	// All Weapons
	public static Weapon woodSword = new Sword_Wooden();
	public static Weapon woodenStick = new WoodenStick();
	
	// All Potions
	public static Potion lesserHealing = new Healing_Lesser();
	public static Potion lesserMana = new Mana_Lesser();
	public static Potion majorHealing = new Healing_Major();
	public static Potion majorMana = new Mana_Major();
		
	// All Armor
	public static BootsOfSpeed bootsOfSpeed = new BootsOfSpeed();
	public static DragonShield dragonShield = new DragonShield();
	
	private static final EList<Item> items = EList.newList();
	static {
		items.add(woodSword);
		items.add(woodenStick);
		
		items.add(lesserHealing);
		items.add(lesserMana);
		items.add(majorHealing);
		items.add(majorMana);
		
		items.add(bootsOfSpeed);
		items.add(dragonShield);
	}
	
	public static EList<Item> getAllItems() { return items.toUnmodifiableList(); }
	
	public static Item random() {
		return items.getRandom().copy();
	}
	
}
