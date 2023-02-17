package qot.assets.textures.world.walls.stone;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class StoneWallTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final StoneWallTextures t = new StoneWallTextures();
	public static StoneWallTextures instance() { return t; }
	
	// Hide constructor
	private StoneWallTextures() {}
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	stone_wall = new StoneWall();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stone_wall);
	}
	
}
