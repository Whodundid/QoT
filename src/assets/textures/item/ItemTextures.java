package assets.textures.item;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import main.settings.QoTSettings;

public class ItemTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final ItemTextures t = new ItemTextures();
	public static ItemTextures instance() { return t; }
	
	// Hide constructor
	private ItemTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\items\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	dragon_shield = new GameTexture(textureDir, "dragon_shield.png"),
	iron_sword = new GameTexture(textureDir, "iron_sword.png"),
	wooden_stick = new GameTexture(textureDir, "wooden_stick.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, iron_sword);
		reg(sys, wooden_stick);
		reg(sys, dragon_shield);
	}
	
}
