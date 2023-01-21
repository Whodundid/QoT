package qot.assets.textures.world.walls.stone;

import envisionEngine.renderEngine.textureSystem.GameTexture;

public class StoneWall extends GameTexture {
	
	private static final String stoneWallDir = tDir + "world\\walls\\stone\\";
	
	public StoneWall() {
		super(stoneWallDir, "stone_wall_0.png");
		
		addChild(stoneWallDir, "stone_wall_1.png");
		addChild(stoneWallDir, "stone_wall_2.png");
		addChild(stoneWallDir, "stone_wall_3.png");
	}
	
}
