package envisionEngine.gameEngine.gameObjects.items;

public abstract class Weapon extends Item {
	
	private int baseDamage;
	
	public Weapon(String name, int idIn) {
		super(name, idIn);
	}
	
	public int getBaseDamage() { return baseDamage; }
	public void setBaseDamage(int val) { baseDamage = val; }
	
}
