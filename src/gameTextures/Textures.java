package gameTextures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;

public class Textures {
	
	// Textures
	
	public static final GameTexture stump_1;
	public static final GameTexture dirt_1;
	public static final GameTexture dirt_2;
	public static final GameTexture dirt_3;
	public static final GameTexture dirt_grass_1;
	public static final GameTexture dirt_grass_2;
	public static final GameTexture dirt_grass_3;
	public static final GameTexture rocks_1;
	public static final GameTexture farmland_1;
	public static final GameTexture potatoes_1_seed;
	public static final GameTexture potatoes_1_grow;
	public static final GameTexture potatoes_1_done;
	
	static {
		stump_1 = new GameTexture("bin/textures/stump_1.png");
		dirt_1 = new GameTexture("bin/textures/dirt_1.png");
		dirt_2 = new GameTexture("bin/textures/dirt_2.png");
		dirt_3 = new GameTexture("bin/textures/dirt_3.png");
		dirt_grass_1 = new GameTexture("bin/textures/dirt_grass_1.png");
		dirt_grass_2 = new GameTexture("bin/textures/dirt_grass_2.png");
		dirt_grass_3 = new GameTexture("bin/textures/dirt_grass_3.png");
		rocks_1 = new GameTexture("bin/textures/rocks_1.png");
		farmland_1 = new GameTexture("bin/textures/farmland_1.png");
		potatoes_1_seed = new GameTexture("bin/textures/potatoes_1_seed.png");
		potatoes_1_grow = new GameTexture("bin/textures/potatoes_1_grow.png");
		potatoes_1_done = new GameTexture("bin/textures/potatoes_1_done.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(stump_1);
		systemIn.registerTexture(dirt_1);
		systemIn.registerTexture(dirt_2);
		systemIn.registerTexture(dirt_3);
		systemIn.registerTexture(dirt_grass_1);
		systemIn.registerTexture(dirt_grass_2);
		systemIn.registerTexture(dirt_grass_3);
		systemIn.registerTexture(rocks_1);
		systemIn.registerTexture(farmland_1);
		systemIn.registerTexture(potatoes_1_seed);
		systemIn.registerTexture(potatoes_1_grow);
		systemIn.registerTexture(potatoes_1_done);
	}
	
}
