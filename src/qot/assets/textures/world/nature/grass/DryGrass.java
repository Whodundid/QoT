package game.assets.textures.world.nature.grass;

import envision.renderEngine.textureSystem.GameTexture;

public class DryGrass extends GameTexture {
	
	private static final String grassDir = tDir + "world\\nature\\grass\\";
	
	public DryGrass() {
		super(grassDir, "dry_grass_0.png");
		
		addChild(grassDir, "dry_grass_1.png");
		addChild(grassDir, "dry_grass_2.png");
		addChild(grassDir, "dry_grass_3.png");
		addChild(grassDir, "dry_grass_4.png");
	}
	
}
