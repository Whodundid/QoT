package assets.textures.world.nature.grass;

import engine.renderEngine.textureSystem.GameTexture;

public class LightGrass extends GameTexture {
	
	private static final String grassDir = tDir + "world\\nature\\grass\\";
	
	public LightGrass() {
		super(grassDir, "light_grass_0.png");
		
		addChild(grassDir, "light_grass_1.png");
		addChild(grassDir, "light_grass_2.png");
		addChild(grassDir, "light_grass_3.png");
	}
	
}
