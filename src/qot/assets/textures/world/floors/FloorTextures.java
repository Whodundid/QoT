package qot.assets.textures.world.floors;

import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;

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
