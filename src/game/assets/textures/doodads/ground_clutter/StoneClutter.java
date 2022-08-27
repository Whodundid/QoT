package game.assets.textures.doodads.ground_clutter;

import envision.renderEngine.textureSystem.GameTexture;

public class StoneClutter extends GameTexture {
	
	private static final String dir = tDir + "doodads/ground_clutter/";
	
	public StoneClutter() {
		super(dir, "stone_0.png");
		
		addChild(dir, "stone_1.png");
		addChild(dir, "stone_2.png");
		addChild(dir, "stone_3.png");
		addChild(dir, "stone_4.png");
	}
}
