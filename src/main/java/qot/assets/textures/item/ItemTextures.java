package qot.assets.textures.item;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

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
	wooden_stick = new GameTexture(textureDir, "wooden_stick.png"),
	lesser_healing = new GameTexture(textureDir, "lesser_healing.png"),
	lesser_mana = new GameTexture(textureDir, "lesser_mana.png"),
	major_healing = new GameTexture(textureDir, "major_healing.png"),
	major_mana = new GameTexture(textureDir, "major_mana.png"),
	boots_of_speed = new GameTexture(textureDir, "boots_of_speed.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, iron_sword);
		reg(sys, wooden_stick);
		reg(sys, dragon_shield);
		reg(sys, lesser_healing);
		reg(sys, lesser_mana);
		reg(sys, major_healing);
		reg(sys, major_mana);
		reg(sys, boots_of_speed);
	}
	
}
