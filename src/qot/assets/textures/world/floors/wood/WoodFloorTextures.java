package game.assets.textures.world.floors.wood;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;

public class WoodFloorTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WoodFloorTextures t = new WoodFloorTextures();
	public static WoodFloorTextures instance() { return t; }
	
	// Hide constructor
	private WoodFloorTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\floors\\wood\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	wood = new GameTexture(textureDir, "wood.png"),
	wood_floor = new GameTexture(textureDir, "wood_floor.png"),
	wood_siding = new GameTexture(textureDir, "wood_siding.png"),
	
	wood_slats = new WoodSlats();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, wood);
		reg(sys, wood_floor);
		reg(sys, wood_siding);
		
		reg(sys, wood_slats);
	}
	
}
