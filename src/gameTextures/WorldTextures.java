package gameTextures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
import util.EUtil;
import util.storageUtil.EArrayList;

public class WorldTextures {
	
	// Textures
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture grass = new GameTexture("Grass", "bin/textures/world/grass.png");
	public static final GameTexture dirt = new GameTexture("Dirt", "bin/textures/world/dirt.png");
	public static final GameTexture sand = new GameTexture("Sand", "bin/textures/world/sand.png");
	public static final GameTexture water = new GameTexture("Water", "bin/textures/world/water.png");
	public static final GameTexture stone = new GameTexture("Stone", "bin/textures/world/stone.png");
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(grass);
		systemIn.registerTexture(dirt);
		systemIn.registerTexture(sand);
		systemIn.registerTexture(water);
		systemIn.registerTexture(stone);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
