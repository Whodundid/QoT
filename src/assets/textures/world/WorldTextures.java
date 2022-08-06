package assets.textures.world;

import assets.TextureLoader;
import assets.textures.world.farmland.FarmTextures;
import assets.textures.world.floors.FloorTextures;
import assets.textures.world.nature.NatureTextures;
import assets.textures.world.roofs.RoofTextures;
import assets.textures.world.walls.WallTextures;
import engine.renderEngine.textureSystem.TextureSystem;

public class WorldTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WorldTextures t = new WorldTextures();
	public static WorldTextures instance() { return t; }
	
	// Hide constructor
	private WorldTextures() {}
	
	//-------------------------------
	
	public static FarmTextures farmTextures = FarmTextures.instance();
	public static FloorTextures floorTextures = FloorTextures.instance();
	public static NatureTextures natureTextures = NatureTextures.instance();
	public static RoofTextures roofTextures = RoofTextures.instance();
	public static WallTextures wallTextures = WallTextures.instance();
	
	//-------------------------------
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		farmTextures.onRegister(sys);
		floorTextures.onRegister(sys);
		natureTextures.onRegister(sys);
		roofTextures.onRegister(sys);
		wallTextures.onRegister(sys);
	}
	
}