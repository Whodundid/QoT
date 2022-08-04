package assets.textures.world.floors;

import assets.TextureLoader;
import assets.textures.world.floors.stone.StoneFloorTextures;
import assets.textures.world.floors.wood.WoodFloorTextures;
import engine.renderEngine.textureSystem.TextureSystem;

public class FloorTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final FloorTextures t = new FloorTextures();
	public static FloorTextures instance() { return t; }
	
	// Hide constructor
	private FloorTextures() {}
	
	//-------------------------------
	
	public static StoneFloorTextures stone_floors = StoneFloorTextures.instance();
	public static WoodFloorTextures wood_floors = WoodFloorTextures.instance();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		stone_floors.onRegister(sys);
		wood_floors.onRegister(sys);
	}
	
}
