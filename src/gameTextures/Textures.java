package gameTextures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;

public class Textures {
	
	// Textures
	
	public static final GameTexture debug;
	
	static {
		debug = new GameTexture("bin/textures/stump_1.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(debug);
	}
	
}
