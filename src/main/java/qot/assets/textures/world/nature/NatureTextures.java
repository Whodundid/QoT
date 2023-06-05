package qot.assets.textures.world.nature;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.assets.textures.world.nature.water.WaterTextures;

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
	
	dirt 				= new GameTexture(textureDir, "dirt.png"),
	cracked_dirt 		= new GameTexture(textureDir, "cracked_dirt.png"),
	mud 				= new GameTexture(textureDir, "mud.png"),
	coarse_dirt			= new GameTexture(textureDir, "coarse_dirt.png"),
	icy_snow 			= new GameTexture(textureDir, "icy_snow.png"),
	rocky_dirt 			= new GameTexture(textureDir, "rocky_dirt.png"),
	sandy_dirt 			= new GameTexture(textureDir, "sandy_dirt.png"),
	smooth_dirt			= new GameTexture(textureDir, "smooth_dirt.png"),
	dry_cracked_dirt 	= new GameTexture(textureDir, "dry_cracked_dirt.png");
	
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
		reg(sys, coarse_dirt);
		reg(sys, icy_snow);
		reg(sys, rocky_dirt);
		reg(sys, sandy_dirt);
		reg(sys, smooth_dirt);
		reg(sys, dry_cracked_dirt);
		
	}
	
}
