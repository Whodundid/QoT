package assets.textures.world;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import main.settings.QoTSettings;

public class Tex_Grass extends GameTexture {
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\";
	private static final String world = textureDir + "world\\";
	
	public final GameTexture grass_1;
	public final GameTexture grass_2;
	//public final GameTexture grass_3;
	
	public Tex_Grass() {
		super("Grass_0", world + "\\grass\\grass_0.png");
		
		addChild(grass_1 = new GameTexture("Grass_1", world + "\\grass\\grass_1.png"));
		addChild(grass_2 = new GameTexture("Grass_2", world + "\\grass\\grass_2.png"));
		//addChild(grass_3 = new GameTexture("Grass_3", world + "\\grass\\grass_3.png"));
		
		grass_1.setChildID(1);
		grass_2.setChildID(2);
		//grass_3.setChildID(3);
	}
	
	@Override
	public void registerChildTextures(TextureSystem systemIn) {
		for (GameTexture t : children.getAVals()) {
			systemIn.registerTexture(t);
		}
	}
	
}
