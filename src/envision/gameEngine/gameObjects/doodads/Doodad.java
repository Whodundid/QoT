package envision.gameEngine.gameObjects.doodads;

import envision.renderEngine.GLObject;
import envision.renderEngine.textureSystem.GameTexture;
import eutil.math.EDimension;

public abstract class Doodad extends GLObject {
	
	public GameTexture sprite;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	public EDimension collisionBox = new EDimension();
	public int worldX, worldY;
	protected String name;
	
	protected Doodad(String nameIn, GameTexture textureIn) {
		name = nameIn;
		sprite = textureIn;
	}
	
	public void init(int posX, int posY, int widthIn, int heightIn) {
		startX = posX;
		startY = posY;
		endX = posX + widthIn;
		endY = posY + heightIn;
		midX = posX + (widthIn) / 2;
		midY = posY + (heightIn) / 2;
		width = widthIn;
		height = heightIn;
		collisionBox = new EDimension(startX, startY, endX, endY);
	}
	
}
