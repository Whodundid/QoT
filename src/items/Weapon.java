package items;

public class Weapon extends Item {
	
	private double damage;
	private String equipt = "[EQUIPT]";
	
	public Weapon(String name, double price, String description, int weight, double damageIn) {
		super(name, price, description, weight);
		damage = damageIn;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public void isEquipt (Weapon weapon) {
		
	}
	
}
