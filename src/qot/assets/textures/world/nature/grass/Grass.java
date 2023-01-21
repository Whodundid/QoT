package qot.assets.textures.world.nature.grass;

import envisionEngine.renderEngine.textureSystem.GameTexture;

public class Grass extends GameTexture {
	
	private static final String grassDir = tDir + "world\\nature\\grass\\";
	
	public Grass() {
		super(grassDir, "grass_0.png");
		
		addChild(grassDir, "grass_1.png");
		addChild(grassDir, "grass_2.png");
	}
	
}
