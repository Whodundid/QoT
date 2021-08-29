package assets.worldTiles;

import assets.textures.WorldTextures;
import eutil.colors.EColors;
import renderEngine.textureSystem.GameTexture;

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
