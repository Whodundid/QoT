package envision.gameEngine;

import envision.gameEngine.effects.animations.AnimationHandler;
import envision.gameEngine.world.gameWorld.IGameWorld;
import envision.gameEngine.world.worldUtil.WorldCamera;
import envision.renderEngine.GLObject;
import envision.renderEngine.textureSystem.GameTexture;
import envision.util.IDrawable;
import eutil.colors.EColors;
import eutil.math.EDimension;
import eutil.math.ENumUtil;
import eutil.misc.Rotation;
import game.QoT;

public abstract class GameObject extends GLObject implements IDrawable {
	
	public IGameWorld world;
	protected GameTexture tex;
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
	
	/** The layer that this object is rendered at. Zero by default. */
	protected int renderLayer = 0;
	
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
	public abstract void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY);
	
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
	public GameTexture getTexture() { return tex; }
	public Rotation getFacing() { return facing; }
	public int getRenderLayer() { return renderLayer; }
	
	public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	public GameObject setName(String nameIn) { name = nameIn; return this; }
	public GameObject setTexture(GameTexture in) { tex = in; return this; }
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
	
	protected int calcBrightness() { return calcBrightness(worldX, worldY); }
	public int calcBrightness(int x, int y) {
		var world = QoT.theWorld;
		if (world == null || !world.isUnderground())
			return 0xffffffff;
		
		int viewDist = 18;
		int lightx = x, lighty = y;
		var obj = world.getCamera().getFocusedObject();
		
		if (obj != null) {
			var p = obj;
			
			double cmx = p.collisionBox.midX; //collision mid x
			double cmy = p.collisionBox.midY; //collision mid y
			double tw = QoT.theWorld.getTileWidth(); //tile width
			double th = QoT.theWorld.getTileHeight(); //tile height
			
			//offset world coordinates to middle of collision box
			int mwcx = (int) Math.ceil(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			//light render position
			lightx = p.worldX + mwcx;
			lighty = p.worldY + mwcy;
		}
		else {
			lightx = world.getCamera().getWorldX();
			lighty = world.getCamera().getWorldY();
		}
		
		int distToPlayer = viewDist - (int) (ENumUtil.distance(x, y, lightx, lighty));
		distToPlayer = ENumUtil.clamp(distToPlayer, 0, distToPlayer);
		int ratio = (distToPlayer * 255) / viewDist;
		return EColors.changeBrightness(0xffffffff, ratio);
	}
	
	public void setRenderLayer(int layerIn) { renderLayer = layerIn; }
	
}
