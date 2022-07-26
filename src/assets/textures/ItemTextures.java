package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

public class ItemTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture dragon_shield;
	public static final GameTexture iron_sword;
	public static final GameTexture wooden_stick;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\items\\";
	
	static {
		dragon_shield = new GameTexture("Dragon Shield", textureDir, "dragon_sheild.png");
		iron_sword = new GameTexture("Iron Sword", textureDir, "iron_sword.png");
		wooden_stick = new GameTexture("Wooden Stick", textureDir, "wooden_stick.jpg");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(iron_sword);
		systemIn.registerTexture(wooden_stick);
		systemIn.reg(dragon_shield);
		
		textures.add(iron_sword);
		textures.add(wooden_stick);
		textures.add(dragon_shield);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
