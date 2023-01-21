package game.assets.textures.world.nature.grass;

import envision.renderEngine.textureSystem.GameTexture;

public class BiomeGrass extends GameTexture {
	
	private static final String grassDir = tDir + "world\\nature\\grass\\";
	
	public BiomeGrass() {
		super(grassDir, "biome_grass_0.png");
		
		addChild(grassDir, "biome_grass_1.png");
		addChild(grassDir, "biome_grass_2.png");
	}
	
}
