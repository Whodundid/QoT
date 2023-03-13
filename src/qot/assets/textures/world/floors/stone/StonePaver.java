package qot.assets.textures.world.floors.stone;

import envision.engine.rendering.textureSystem.GameTexture;

public class StonePaver extends GameTexture {
	
	private static final String stoneFloorDir = tDir + "world\\floors\\stone\\";
	
	public StonePaver() {
		super(stoneFloorDir, "stone_paver_0.png");
		
		addChild(stoneFloorDir, "stone_paver_1.png");
		addChild(stoneFloorDir, "stone_paver_2.png");
		addChild(stoneFloorDir, "stone_paver_3.png");
		addChild(stoneFloorDir, "stone_paver_4.png");
	}
}
