package game.worldTiles;

import eutil.colors.EColors;

/** A global enum to keep track of every tile's ID.
 *  These are hardcoded to maintain consistency between versions. */
public enum TileIDs {
	
	STONE(0, "Stone", EColors.gray),
	GRASS(1, "Grass", EColors.lgreen),
	DIRT(2, "Dirt", 0xffad6637),
	SAND(3, "Sand", 0xffd8b668),
	WATER(4, "Water", EColors.blue),
	WOOD(5, "Wood", 0xff91642b),
	DARK_GRASS(6, "Dark Grass"),
	CRACKED_DIRT(7, "Cracked Dirt"),
	RED_SAND(8, "Red Sand"),
	ROCKY_STONE(9, "Rocky Stone"),
	MUD(10, "Mud"),
	BUSH0(11, "Tree"),
	DUNG_WALL_A(12, "Dung Wall A"),
	DUNG_WALL_B(13, "Dung Wall B"),
	DUNG_FLOOR(14, "Dung Floor"),
	DUNG_FLOOR_Aa(15, "Dung Old Wall A"),
	DUNG_FLOOR_Bb(16, "Dung Old Wall B"),
	FARM_PLOT(17, "Farm Plot");
	;
	
	public final int id;
	public final int mapColor;
	public final String name;
	
	private TileIDs(int idIn, String nameIn) { this(idIn, nameIn, 0x00000000); }
	private TileIDs(int idIn, String nameIn, EColors mapColorIn) { this(idIn, nameIn, mapColorIn.intVal); }
	private TileIDs(int idIn, String nameIn, int mapColorIn) {
		id = idIn;
		mapColor = mapColorIn;
		name = nameIn;
	}
	
	@Override
	public String toString() {
		return id + ":" + name;
	}
	
}
