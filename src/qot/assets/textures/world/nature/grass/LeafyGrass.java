package qot.assets.textures.world.nature.grass;

import envision.engine.rendering.textureSystem.GameTexture;

public class LeafyGrass extends GameTexture {
	
	private static final String grassDir = tDir + "world\\nature\\grass\\";
	
	public LeafyGrass() {
		super(grassDir, "leafy_grass_0.png");
		
		addChild(grassDir, "leafy_grass_1.png");
	}
	
}
