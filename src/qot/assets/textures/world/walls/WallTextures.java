package qot.assets.textures.world.walls;

import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.assets.textures.world.walls.stone.StoneWallTextures;

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
