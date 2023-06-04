package envision.game;

import java.util.concurrent.atomic.AtomicInteger;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.game.effects.animations.AnimationHandler;
import envision.game.util.IDrawable;
import envision.game.world.IGameWorld;
import eutil.colors.EColors;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.Rotation;

public abstract class GameObject extends RenderingManager implements IDrawable {
	
	public IGameWorld world;
	public GameTexture tex;
	public double startX, startY, endX, endY;
	public double midX, midY;
	public double width, height;
	public Dimension_d collisionBox = new Dimension_d();
	public int worldX, worldY;
	public String name;
	public Rotation facing = Rotation.LEFT;
	
	/** The internal ID that is exclusive to this entity regardless of world. */
	public final int objectID;
	/** The ID of this entity within the world. -1 by default. */
	public int worldID = -1;
	
	/** The animation handler of this entity. */
	public AnimationHandler animationHandler;
	
	/** The layer that this object is rendered at. Zero by default. */
	public int renderLayer = 0;
	
	private static AtomicInteger internalIDCounter = new AtomicInteger();
	public static int nextObjectID() { return internalIDCounter.getAndIncrement(); }
	
	//--------------
	// Constructors
	//--------------
	
	protected GameObject() { this(null); }
	protected GameObject(String nameIn) {
		name = nameIn;
		objectID = nextObjectID();
		
		if (name == null) {
			name = "NO_NAME_" + objectID;
		}
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
		collisionBox = new Dimension_d(startX, startY, endX, endY);
	}
	
	//@Override
	//public abstract void draw(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver);
	
	public void onGameTick(float dt) {
		onLivingUpdate(dt);
	}
	
	/** Can be overridden in child classes to denote specific entity behavior. */
	public void onLivingUpdate(float dt) {}
	
	/** Called from the world whenever this entity is removed from it. */
	public void onRemovedFromWorld() {
		if (animationHandler != null) {
			animationHandler.unloadAnimation();
		}
	}
	
	/** Returns the hard-coded ID of this object -- used for saving and loading map data. */
	public abstract int getInternalSaveID();
	
	/** Returns the ID of this object in the world it is in. */
	public int getWorldID() { return worldID; }
	public void setWorldID(int idIn) { worldID = idIn; }
	
	/** Returns the internal unique ID of this object. */
	public int getObjectID() { return objectID; }
	
	public String getName() { return name; }
	public GameTexture getTexture() { return tex; }
	public Rotation getFacing() { return facing; }
	public int getRenderLayer() { return renderLayer; }
	
	public Dimension_d getDimensions() { return new Dimension_d(startX, startY, endX, endY); }
	
	public GameObject setName(String nameIn) { name = nameIn; return this; }
	public GameObject setTexture(GameTexture in) { tex = in; return this; }
	public GameObject setFacing(Rotation dir) { facing = dir; return this; }
	
	public Dimension_d getCollision() { return collisionBox; }
	public Dimension_d getCollisionDims() {
		return new Dimension_d(startX + collisionBox.startX,
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
		var world = Envision.theWorld;
		if (world == null || !world.isUnderground())
			return 0xffffffff;
		
		int viewDist = 18;
		int lightx = x, lighty = y;
		var obj = world.getCamera().getFocusedObject();
		
		if (obj != null) {
			var p = obj;
			
			double cmx = p.collisionBox.midX; //collision mid x
			double cmy = p.collisionBox.midY; //collision mid y
			double tw = Envision.theWorld.getTileWidth(); //tile width
			double th = Envision.theWorld.getTileHeight(); //tile height
			
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
