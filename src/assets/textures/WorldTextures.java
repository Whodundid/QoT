package assets.textures;

import assets.textures.world.Tex_Grass;
import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
import util.EUtil;
import util.storageUtil.EArrayList;

public class WorldTextures {
	
	// Textures
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture grass = new Tex_Grass();
	public static final GameTexture dirt = new GameTexture("Dirt", "bin/textures/world/dirt.png");
	public static final GameTexture sand = new GameTexture("Sand", "bin/textures/world/sand.png");
	public static final GameTexture water = new GameTexture("Water", "bin/textures/world/water.png");
	public static final GameTexture stone = new GameTexture("Stone", "bin/textures/world/stone.png");
	public static final GameTexture wood = new GameTexture("Wood", "bin/textures/world/wood.png");
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(grass);
		systemIn.registerTexture(dirt);
		systemIn.registerTexture(sand);
		systemIn.registerTexture(water);
		systemIn.registerTexture(stone);
		systemIn.registerTexture(wood);
		
		textures.add(grass);
		textures.add(dirt);
		textures.add(sand);
		textures.add(water);
		textures.add(stone);
		textures.add(wood);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
	/** Returns a copy of the textures in this holder class. */
	public static EArrayList<GameTexture> getTextures() { return new EArrayList(textures); }
	
}
