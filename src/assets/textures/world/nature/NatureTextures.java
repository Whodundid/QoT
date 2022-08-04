package assets.textures.world.nature;

import assets.TextureLoader;
import assets.textures.world.nature.grass.GrassTextures;
import assets.textures.world.nature.rock.RockTextures;
import assets.textures.world.nature.sand.SandTextures;
import assets.textures.world.nature.water.WaterTextures;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class NatureTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final NatureTextures t = new NatureTextures();
	public static NatureTextures instance() { return t; }
	
	// Hide constructor
	private NatureTextures() {}
	
	//-------------------------------
	
	public static GrassTextures grass = GrassTextures.instance();
	public static RockTextures rock = RockTextures.instance();
	public static SandTextures sand = SandTextures.instance();
	public static WaterTextures water = WaterTextures.instance();
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\nature\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	dirt			= new GameTexture(textureDir, "dirt.png"),
	cracked_dirt	= new GameTexture(textureDir, "cracked_dirt.png"),
	mud				= new GameTexture(textureDir, "mud.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		grass.onRegister(sys);
		rock.onRegister(sys);
		sand.onRegister(sys);
		water.onRegister(sys);
		
		reg(sys, dirt);
		reg(sys, cracked_dirt);
		reg(sys, mud);
	}
	
}
