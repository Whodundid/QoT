package game.assets.textures.world.nature;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;
import game.assets.textures.world.nature.grass.GrassTextures;
import game.assets.textures.world.nature.rock.RockTextures;
import game.assets.textures.world.nature.sand.SandTextures;
import game.assets.textures.world.nature.water.WaterTextures;

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
