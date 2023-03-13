package qot.assets.textures.doodads.ground_clutter;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class GroundClutterTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final GroundClutterTextures t = new GroundClutterTextures();
	public static GroundClutterTextures instance() { return t; }
	
	// Hide constructor
	private GroundClutterTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "doodads\\ground_clutter\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	stones = new StoneClutter(),
	weeds = new WeedClutter(),
	leaves = new LeavesClutter(),
	sticks = new SticksClutter();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stones);
		reg(sys, weeds);
		reg(sys, leaves);
		reg(sys, sticks);
	}
	
}
