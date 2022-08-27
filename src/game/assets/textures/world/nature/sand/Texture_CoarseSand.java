package game.assets.textures.world.nature.sand;

import envision.renderEngine.textureSystem.GameTexture;

public class Texture_CoarseSand extends GameTexture {
	
	private static final String sandDir = tDir + "world\\nature\\sand\\";
	
	public Texture_CoarseSand() {
		super(sandDir, "coarse_sand_0.png");
		
		addChild(sandDir, "coarse_sand_1.png");
		addChild(sandDir, "coarse_sand_2.png");
	}
	
}