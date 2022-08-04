package assets.textures.doodads;

import assets.TextureLoader;
import assets.textures.doodads.ground_clutter.GroundClutterTextures;
import assets.textures.doodads.house.HouseTextures;
import assets.textures.doodads.trees.TreeTextures;
import engine.renderEngine.textureSystem.TextureSystem;

public class DoodadTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final DoodadTextures t = new DoodadTextures();
	public static DoodadTextures instance() { return t; }
	
	// Hide constructor
	private DoodadTextures() {}
	
	//-------------------------------
	
	public static GroundClutterTextures groundClutterTextures = GroundClutterTextures.instance();
	public static HouseTextures houseTextures = HouseTextures.instance();
	public static TreeTextures treeTextures = TreeTextures.instance();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		groundClutterTextures.onRegister(sys);
		houseTextures.onRegister(sys);
		treeTextures.onRegister(sys);
	}
	
}
