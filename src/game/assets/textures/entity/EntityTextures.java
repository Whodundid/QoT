package game.assets.textures.entity;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;
import game.settings.QoTSettings;

public class EntityTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final EntityTextures t = new EntityTextures();
	public static EntityTextures instance() { return t; }
	
	// Hide constructor
	private EntityTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\entities\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	goblin = new GameTexture(textureDir, "goblin/goblin_base.png"),
	player = new GameTexture(textureDir, "player/player_base.png"),
	whodundid = new GameTexture(textureDir, "whodundid/whodundid_base.png"),
	trollboar = new GameTexture(textureDir, "tollboar/tollboar_base.png"),
	thyrah = new GameTexture(textureDir, "thyrah/thyrah_base.png"),
	
	whobro = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base.png"),
	whobro1 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base2.png"),
	whobro2 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base3.png"),
	whobro3 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base4.png"),
	
	whobro_blink0 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink0.png"),
	whobro_blink1 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink1.png"),
	whobro_blink2 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink2.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, goblin);
		reg(sys, player);
		reg(sys, whodundid);
		reg(sys, trollboar);
		reg(sys, thyrah);
		
		reg(sys, whobro);
		reg(sys, whobro1);
		reg(sys, whobro2);
		reg(sys, whobro3);
		
		reg(sys, whobro_blink0);
		reg(sys, whobro_blink1);
		reg(sys, whobro_blink2);
	}
	
}
