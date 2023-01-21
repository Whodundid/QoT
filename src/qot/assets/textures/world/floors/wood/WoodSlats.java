package qot.assets.textures.world.floors.wood;

import envisionEngine.renderEngine.textureSystem.GameTexture;

public class WoodSlats extends GameTexture {
	
	private static final String woodFloorDir = tDir + "world\\floors\\wood\\";
	
	public WoodSlats() {
		super(woodFloorDir, "wood_slats_0.png");
		
		addChild(woodFloorDir, "wood_slats_1.png");
	}
	
}
