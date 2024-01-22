package qot.assets.textures.ability;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class AbilityTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final AbilityTextures t = new AbilityTextures();
	public static AbilityTextures instance() { return t; }
	
	// Hide constructor
	private AbilityTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "abilities\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	cast_heal = new GameTexture(textureDir, "Heal.png"),
	cast_fireball = new GameTexture(textureDir, "Cast_Fireball.png"),
	cast_kick = new GameTexture(textureDir, "kick.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, cast_heal);
		reg(sys, cast_fireball);
		reg(sys, cast_kick);
	}
	
}
