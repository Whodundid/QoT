package game.assets.textures.doodads;

import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;
import game.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import game.assets.textures.doodads.house.HouseTextures;
import game.assets.textures.doodads.trees.TreeTextures;

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
