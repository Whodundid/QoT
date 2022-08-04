package assets.textures.world.walls.stone;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

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
