package assets.textures.world.floors.stone;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class StoneFloorTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final StoneFloorTextures t = new StoneFloorTextures();
	public static StoneFloorTextures instance() { return t; }
	
	// Hide constructor
	private StoneFloorTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\floors\\stone\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	dung_floor = new GameTexture(textureDir, "dung_floor.png"),
	stone_pad = new GameTexture(textureDir, "stone_pad.png"),
	
	clay_pad = new ClayPad();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, dung_floor);
		reg(sys, stone_pad);
		
		reg(sys, clay_pad);
	}
	
}
