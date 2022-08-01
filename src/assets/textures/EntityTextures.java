package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

public class EntityTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture goblin;
	public static final GameTexture player;
	public static final GameTexture whodundid;
	public static final GameTexture trollboar;
	public static final GameTexture thyrah;
	
	public static final GameTexture whodundidsbrother;
	public static final GameTexture whodundidsbrother2;
	public static final GameTexture whodundidsbrother3;
	public static final GameTexture whodundidsbrother4;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\entities\\";
	
	static {
		goblin = new GameTexture("goblin", textureDir, "goblin/goblin_base.png");
		player = new GameTexture("player", textureDir, "player/player_base.png");
		whodundid = new GameTexture("whodundid", textureDir, "whodundid/whodundid_base.png");
		trollboar = new GameTexture("trollboar", textureDir, "tollboar/tollboar_base.png");
		thyrah = new GameTexture("thyrah", textureDir, "thyrah/thyrah_base.png");
		
		var wbPath = "whodundidsbrother\\";
		whodundidsbrother = new GameTexture("who_brother", textureDir + wbPath, "whodundidsbrother_base.png");
		whodundidsbrother2 = new GameTexture("who_brother2", textureDir + wbPath, "whodundidsbrother_base2.png");
		whodundidsbrother3 = new GameTexture("who_brother3", textureDir + wbPath, "whodundidsbrother_base3.png");
		whodundidsbrother4 = new GameTexture("who_brother4", textureDir + wbPath, "whodundidsbrother_base4.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.reg(textures.addR(goblin));
		systemIn.reg(textures.addR(player));
		systemIn.reg(textures.addR(whodundid));
		systemIn.reg(textures.addR(trollboar));
		systemIn.reg(textures.addR(thyrah));
		
		systemIn.reg(textures.addR(whodundidsbrother));
		systemIn.reg(textures.addR(whodundidsbrother2));
		systemIn.reg(textures.addR(whodundidsbrother3));
		systemIn.reg(textures.addR(whodundidsbrother4));
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
