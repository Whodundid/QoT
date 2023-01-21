package game.items;

public enum ItemList {
	
	HEALING_LESSER(0),
	MANA_LESSER(1),
	SWORD_WOODEN(2),
	
	;
	
	public final int ID;
	
	private ItemList(int idIn) {
		ID = idIn;
	}
	
	public ItemList fromID(int idIn) {
		if (idIn < 0) return null;
		int len = values().length;
		if (idIn >= len) return null;
		return values()[idIn];
	}
	
}
