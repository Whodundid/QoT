package envision.game.entity.classes;

public enum ClassType {
	
	SCOUT(0, "Scout"),
	ARCHER(1, "Archer"),
	MAGE(2, "Mage"),
	DRUID(3, "Druid"),
	SWORDSMAN(4, "Swordsman"),
	ROGUE(5, "Rogue"),
	PALADIN(6, "Paladin"),
	BERSERKER(7, "Berserker");
	
	public final int typeInt;
	public final String name;
	
	private ClassType(int type, String nameIn) {
		typeInt = type;
		name = nameIn;
	}
	
}
