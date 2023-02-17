package qot.assets.textures.world.walls.dungeon;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class DungeonWallTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final DungeonWallTextures t = new DungeonWallTextures();
	public static DungeonWallTextures instance() { return t; }
	
	// Hide constructor
	private DungeonWallTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\walls\\dungeon\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	dung_wall_a 		= new GameTexture(textureDir, "dung_wall_a.png"),
	dung_wall_b 		= new GameTexture(textureDir, "dung_wall_b.png"),
	dung_wall_m_a 		= new GameTexture(textureDir, "dung_wall_m_a.png"),
	dung_wall_m_b 		= new GameTexture(textureDir, "dung_wall_m_b.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, dung_wall_a);
		reg(sys, dung_wall_b);
		reg(sys, dung_wall_m_a);
		reg(sys, dung_wall_m_b);
	}
	
}
