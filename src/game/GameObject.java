package game;

import engine.renderEngine.GLObject;
import engine.renderEngine.textureSystem.GameTexture;
import eutil.math.EDimension;
import eutil.misc.Rotation;

public abstract class GameObject extends GLObject {
	
	protected GameTexture sprite;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	public EDimension collisionBox = new EDimension();
	public int worldX, worldY;
	protected String name;
	protected Rotation facing = Rotation.RIGHT;
	
	protected GameObject(String nameIn) {
		name = nameIn;
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
	
	public void renderObject(double x, double y, double w, double h) {
		boolean flip = facing == Rotation.RIGHT || facing == Rotation.DOWN;
		
		drawTexture(sprite, x, y, w, h, flip);
	}
	
	/** Returns the hard-coded ID of this object -- used for saving and loading map data. */
	public abstract int getObjectID();
	
	public String getName() { return name; }
	public GameTexture getTexture() { return sprite; }
	public Rotation getFacing() { return facing; }
	
	public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	public GameObject setName(String nameIn) { name = nameIn; return this; }
	public GameObject setTexture(GameTexture in) { sprite = in; return this; }
	public GameObject setFacing(Rotation dir) { facing = dir; return this; }
	
}
