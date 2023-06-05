package qot.assets.textures.doodads.ground_clutter;

import envision.engine.rendering.textureSystem.GameTexture;

public class LeavesClutter extends GameTexture {
	
	private static final String dir = tDir + "doodads/ground_clutter/";
	
	public LeavesClutter() {
		super(dir, "leaves_0.png");
		
		addChild(dir, "leaves_1.png");
	}
}
