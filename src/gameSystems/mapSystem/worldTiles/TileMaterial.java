package gameSystems.mapSystem.worldTiles;

public enum TileMaterial {
	GRASS("grass"),
	DIRT("dirt"),
	STONE("stone"),
	WOOD("wood"),
	WATER("water"),
	VOID("void");
	
	public String name;
	
	TileMaterial(String nameIn) {
		name = nameIn;
	}
	
	public static TileMaterial getMaterial(String name) {
		for (TileMaterial t : values()) {
			if (t.name.equals(name)) { return t; }
		}
		return null;
	}
	
}
