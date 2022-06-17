package game.worldTiles;

import assets.textures.WorldTextures;
import engine.renderEngine.textureSystem.GameTexture;
import eutil.colors.EColors;

/** A global enum to keep track of every tile's ID.
 *  These are hardcoded to maintain consistency between versions. */
public enum TileIDs {
	
	STONE(0, "Stone", EColors.gray, WorldTextures.stone),
	GRASS(1, "Grass", EColors.lgreen, WorldTextures.grass),
	DIRT(2, "Dirt", 0xffad6637, WorldTextures.dirt),
	SAND(3, "Sand", 0xffd8b668, WorldTextures.sand),
	WATER(4, "Water", EColors.blue, WorldTextures.water),
	WOOD(5, "Wood", 0xff91642b, WorldTextures.wood),
	DARK_GRASS(6, "Dark Grass", null),
	CRACKED_DIRT(7, "Cracked Dirt", null),
	RED_SAND(8, "Red Sand", null),
	ROCKY_STONE(9, "Rocky Stone", null),
	MUD(10, "Mud", null),
	BUSH0(11, "Tree", WorldTextures.bush0),
	DUNG_WALL_A(12, "Dung Wall A", WorldTextures.dungWallA),
	DUNG_WALL_B(13, "Dung Wall B", WorldTextures.dungWallB),
	DUNG_FLOOR(14, "Dung Floor", WorldTextures.dungFloor),
	DUNG_FLOOR_Aa(15, "Dung Old Wall A", WorldTextures.dungOldWallA),
	DUNG_FLOOR_Bb(16, "Dung Old Wall B", WorldTextures.dungOldWallB),
	
	FARM0(17, "Farm 0", WorldTextures.farm0),
	FARM1(18, "Farm 1", WorldTextures.farm1),
	FARM2(19, "Farm 2", WorldTextures.farm2),
	FARM3(20, "Farm 3", WorldTextures.farm3),
	;
	
	public final int id;
	public final int mapColor;
	public final String name;
	public final GameTexture texture;
	
	private TileIDs(int idIn, String nameIn, GameTexture textureIn) { this(idIn, nameIn, 0x00000000, textureIn); }
	private TileIDs(int idIn, String nameIn, EColors mapColorIn, GameTexture textureIn) { this(idIn, nameIn, mapColorIn.intVal, textureIn); }
	private TileIDs(int idIn, String nameIn, int mapColorIn, GameTexture textureIn) {
		id = idIn;
		mapColor = mapColorIn;
		name = nameIn;
		texture = textureIn;
	}
	
	@Override
	public String toString() {
		return id + ":" + name;
	}
	
}
