package assets.textures.doodads.trees;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class TreeTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final TreeTextures t = new TreeTextures();
	public static TreeTextures instance() { return t; }
	
	// Hide constructor
	private TreeTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "doodads\\trees\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	tree_pine_0 = new GameTexture(textureDir, "tree_pine_0.png"),
	tree_pine_1 = new GameTexture(textureDir, "tree_pine_1.png"),
	birch_0 = new GameTexture(textureDir, "birch_0.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, tree_pine_0);
		reg(sys, tree_pine_1);
		reg(sys, birch_0);
	}
	
}
