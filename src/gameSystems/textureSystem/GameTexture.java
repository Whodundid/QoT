package gameSystems.textureSystem;

import org.lwjgl.opengl.GL11;

public class GameTexture {
	
	private int textureID = -1; //-1 indicates this texutre is currently unregistered
	private String filePath;
	private int width = -1, height = -1;
	private boolean destroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GameTexture(String filePathIn) {
		filePath = filePathIn;
	}
	
	//----------------
	// Public Methods
	//----------------
	
	public boolean destroy() {
		if (textureID != -1) {
			GL11.glDeleteTextures(textureID);
			textureID = -1;
			destroyed = true;
			return true;
		}
		return false;
	}
	
	public boolean hasBeenRegistered() { return textureID != -1; }
	
	//---------------------
	// GameTexture Getters
	//---------------------
	
	public int getTextureID() { return textureID; }
	public String getFilePath() { return filePath; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean hasBeenDestroyed() { return destroyed; }
	
}
