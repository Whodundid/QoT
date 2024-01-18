package qot.world_tiles;

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
	DARK_WATER(11, "DARK_WATER", EColors.mc_darkblue),
	
	DUNG_WALL_A(12, "Dung Wall A"),
	DUNG_WALL_B(13, "Dung Wall B"),
	DUNG_FLOOR(14, "Dung Floor"),
	DUNG_FLOOR_Aa(15, "Dung Old Wall A"),
	DUNG_FLOOR_Bb(16, "Dung Old Wall B"),
	FARM_PLOT(17, "Farm Plot"),
	
	COUNTER_FOOD(18, "Counter With Food"),
	COUNTER(19, "Counter"),
	OVEN(20, "Oven"),
	SINK(21, "Sink"),
	
	LEAFY_GRASS(22, "Leafy Grass"),
	DRY_GRASS(23, "Dry Grass"),
	LIGHT_GRASS(24, "Light Grass"),
	
	CLAY_PAD(25, "Clay Pad"),
	STONE_PAD(26, "Stone Pad"),
	WOOD_SLATS(27, "Wood Slats"),
	STONE_WALL(28, "Stone Wall"),
	
	COARSE_SAND(29, "Coarse Sand"),
	STONE_PAVER(30, "Stone Paver"),
	
	COARSE_DIRT(31, "Coarse Dirt"),
	DRY_CRACKED_DIRT(32, "Dry Cracked Dirt"),
	ICY_SNOW(33, "Icy Snow"),
	ROCKY_DIRT(34, "Rocky Dirt"),
	SANDY_DIRT(35, "Sandy Dirt"),
	SMOOTH_DIRT(36, "Smooth Dirt"),
	
	DRY_PLAINS_GRASS(37, "Dry Plains Grass"),
	ROUGH_ROCKY(38, "Rough Rocks"),
	
	WET_SAND(39, "Wet Sand"),
	
	BLUE_DUNG_TILE(40, "Blue Dungeon Tile"),
	DARK_DUNG_FLOOR(41, "Dark Dungeon Floor"),
	
	BLUE_WATER(42, "Blue Water"),
	ROOF(43, "Roof"),
	;
	
	public final int tileID;
	public final int mapColor;
	public final String name;
	
	private TileIDs(int idIn, String nameIn) { this(idIn, nameIn, 0x00000000); }
	private TileIDs(int idIn, String nameIn, EColors mapColorIn) { this(idIn, nameIn, mapColorIn.intVal); }
	private TileIDs(int idIn, String nameIn, int mapColorIn) {
		tileID = idIn;
		mapColor = mapColorIn;
		name = nameIn;
	}
	
	@Override
	public String toString() {
		return tileID + ":" + name;
	}
	
	public static TileIDs getIdFrom(int id) {
		final var vals = values();
		if (id < 0 || id > vals.length) return null;
		for (int i = 0; i < vals.length; i++) {
			var tid = vals[i];
			if (tid.tileID == id) return tid;
		}
		return null;
	}
	
}
