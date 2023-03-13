package qot.assets.textures.doodads.ground_clutter;

import envision.engine.rendering.textureSystem.GameTexture;

public class WeedClutter extends GameTexture {
	
	private static final String dir = tDir + "doodads/ground_clutter/";
	
	public WeedClutter() {
		super(dir, "weed_0.png");
		
		addChild(dir, "weed_1.png");
		addChild(dir, "weed_2.png");
	}
}
