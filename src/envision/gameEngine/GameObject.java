package envision.gameEngine;

import envision.gameEngine.effects.animations.AnimationHandler;
import envision.gameEngine.world.gameWorld.IGameWorld;
import envision.renderEngine.GLObject;
import envision.renderEngine.textureSystem.GameTexture;
import envision.util.IDrawable;
import eutil.math.EDimension;
import eutil.misc.Rotation;

public abstract class GameObject extends GLObject implements IDrawable {
	
	public IGameWorld world;
	protected GameTexture sprite;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	public EDimension collisionBox = new EDimension();
	public int worldX, worldY;
	protected String name;
	protected Rotation facing = Rotation.LEFT;
	
	/** The ID of this entity within the world. -1 by default. */
	private int objectID = -1;
	
	/** The animation handler of this entity. */
	protected AnimationHandler animationHandler;
	
	//--------------
	// Constructors
	//--------------
	
	protected GameObject() {}
	protected GameObject(String nameIn) {
		name = nameIn;
	}
	
	//------------------
	// Back-end Methods
	//------------------
	
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
	
	@Override
	public void draw(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		boolean flip = facing == Rotation.RIGHT || facing == Rotation.DOWN;
		
		drawTexture(sprite, x, y, w, h, flip, brightness);
	}
	
	public void onGameTick() {
		onLivingUpdate();
	}
	
	/** Can be overridden in child classes to denote specific entity behavior. */
	public void onLivingUpdate() {}
	
	/** Called from the world whenever this entity is removed from it. */
	public void onRemovedFromWorld() {
		if (animationHandler != null) {
			animationHandler.unloadAnimation();
		}
	}
	
	/** Returns the hard-coded ID of this object -- used for saving and loading map data. */
	public abstract int getInternalSaveID();
	
	public int getObjectID() { return objectID; }
	public void setObjectID(int idIn) { objectID = idIn; }
	
	public String getName() { return name; }
	public GameTexture getTexture() { return sprite; }
	public Rotation getFacing() { return facing; }
	
	public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	public GameObject setName(String nameIn) { name = nameIn; return this; }
	public GameObject setTexture(GameTexture in) { sprite = in; return this; }
	public GameObject setFacing(Rotation dir) { facing = dir; return this; }
	
	public EDimension getCollision() { return collisionBox; }
	public EDimension getCollisionDims() {
		return new EDimension(startX + collisionBox.startX,
							  startY + collisionBox.startY,
							  startX + collisionBox.startX + collisionBox.width,
							  startY + collisionBox.startY + collisionBox.height);
	}
	
	@Override
	public double getSortPoint() {
		return startY + collisionBox.endY;
	}
	
}
