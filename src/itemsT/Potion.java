package itemsT;

public class Potion extends Item {
	
	// Heal is the amount of health player will get when using the healing potion
	private double heal;
	// Mana is the amount of mana player will get when using the mana potion
	private double mana;
	// Selling price!
	private double sell;
	
	public Potion(String name, double price, String description, double weight, double healIn, double manaIn, double sellIn) {
		super(name, price, description, weight);
		heal = healIn;
		mana = manaIn;
		sell = sellIn;
	}
	
	// Getters
	
	public String getName() {
		return name;
	}
	
	public double getSellPrice() {
		return sell;
	}
	
	// Setters
	
	public void setSellPrice(double sellIn) {
		sell = sellIn;
	}

	// Methods
	
	
}