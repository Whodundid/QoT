package item.items.armor;

import item.Item;

public abstract class ArmorItem extends Item {
	
	protected double physicalArmor;
	protected double magicArmor;
	
	public ArmorItem(int costIn, int weightIn, String nameIn, double physicalIn, double magicIn) {
		super(costIn, weightIn, nameIn);
			physicalArmor = physicalIn;
			magicArmor = magicIn;
	}
	
	public double getPhysicalArmor() {
		return physicalArmor;
	}
	
	public double getMagicArmor() {
		return magicArmor;
	}
}
