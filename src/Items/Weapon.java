package Items;

public class Weapon extends Item {
	public double damage;
	
	public Weapon(String name, double price, String description, int weight, double damageIn) {
		super(name, price, description, weight);
		damage = damageIn;
	}
	
	public double getDamage() {
		return damage;
	}
}
