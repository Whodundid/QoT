package item.items.potions;

import Entities.Entity;

public class LesserHealingPotion extends Potion {
	
	private int healingAmount = 25;
	
	// LesserHealingPotion Constructors 
	protected LesserHealingPotion(int costIn, int weightIn, String nameIn) {
		super(costIn, weightIn, nameIn, PotionType.HEALING);
	}
	
	// LesserHealingPotion Methods
	
	@Override
	public String getDescription() {
		return "A small health potion that heals a little bit of health";
	}
	
	@Override
	public void consume(Entity characterIn) {
		characterIn.health += healingAmount;
	}
	
	// LesserHealingPotion Getters
	
	public int getHealingAmount() {
		return healingAmount;
	}
	
	
}
