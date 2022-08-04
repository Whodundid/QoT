package assets.textures.world.walls;

import assets.TextureLoader;
import assets.textures.world.walls.dungeon.DungeonWallTextures;
import assets.textures.world.walls.stone.StoneWallTextures;
import engine.renderEngine.textureSystem.TextureSystem;

public class WallTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WallTextures t = new WallTextures();
	public static WallTextures instance() { return t; }
	
	// Hide constructor
	private WallTextures() {}
	
	//-------------------------------
	
	public static DungeonWallTextures dung_walls = DungeonWallTextures.instance();
	public static StoneWallTextures stone_walls = StoneWallTextures.instance();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		dung_walls.onRegister(sys);
		stone_walls.onRegister(sys);
	}
	
}
