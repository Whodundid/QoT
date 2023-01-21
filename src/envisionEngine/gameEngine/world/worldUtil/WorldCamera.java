package envision.gameEngine.world.worldUtil;

import java.util.Objects;

import envision.gameEngine.GameObject;
import envision.gameEngine.world.gameWorld.GameWorld;
import eutil.datatypes.Box2;
import eutil.math.ENumUtil;

public class WorldCamera {
	
	//--------
	// Fields
	//--------
	
	/** The world that this camera is viewing. */
	private GameWorld theWorld;
	/** The object this camera is following. */
	private GameObject focusedObject;
	/** The world coordinates that this camera is focused around. */
	private final Box2<Integer, Integer> focusedCoords = new Box2<>(0, 0);
	/** The pixel world coordinates that this camera is precisely focused on. */
	private final Box2<Integer, Integer> focusedPoint = new Box2<>(0, 0);
	/** The zoom of the camera. Higher values are more zoomed in. */
	private double zoom = 1.0;
	
	/** The number of pixels in the X dimension that the camera is offset by in world coordinates. */
	private double offsetX;
	/** The number of pixels in the Y dimension that the camera is offset by in world coordinates. */
	private double offsetY;
	
	private long panStartTime;
	private long panDuration;
	private boolean isPanning;
	
	//--------------
	// Constructors
	//--------------
	
	public WorldCamera(GameWorld worldIn) {
		//preventing this from being null for right now..
		theWorld = Objects.requireNonNull(worldIn);
	}
	
	//------------------------
	// Implementation Methods
	//------------------------
	
	/**
	 * Called each frame to update camera position.
	 */
	public synchronized void onRenderTick() {
		if (focusedCoords.anyNull()) focusedCoords.set(0, 0);
		if (focusedPoint.anyNull()) focusedPoint.set(0, 0);
		
		if (focusedObject != null) {
			focusedCoords.setA(focusedObject.worldX);
			focusedCoords.setB(focusedObject.worldY);
			focusedPoint.setA(focusedObject.startX);
			focusedPoint.setB(focusedObject.startY);
			offsetX = (focusedObject.startX % theWorld.getTileWidth()) * zoom;
			offsetY = (focusedObject.startY % theWorld.getTileHeight()) * zoom;
		}
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the exact X pixel coordinate that camera is focused on in the world. */
	public int getX() { return focusedPoint.getA(); }
	/** Returns the exact Y pixel coordinate that camera is focused on in the world. */
	public int getY() { return focusedPoint.getB(); }
	/** Returns the X world coordinate that camera is focused on. */
	public int getWorldX() { return focusedCoords.getA(); }
	/** Returns the Y world coordinate that camera is focused on. */
	public int getWorldY() { return focusedCoords.getB(); }
	/** Returns the number of pixels in the X dimension that the camera is offset by in world coordinates. */
	public double getOffsetX() { return offsetX; }
	/** Returns the number of pixels in the Y dimension that the camera is offset by in world coordinates. */
	public double getOffsetY() { return offsetY; }
	/** Returns the current zoom of the camera. Higher values are more zoomed in! */
	public double getZoom() { return zoom; }
	/** Returns the object the camera is currently focused on. (Could be null) */
	public GameObject getFocusedObject() { return focusedObject; }
	
	//---------
	// Setters
	//---------
	
	/**
	 * Sets this camera's zoom.
	 * Range: [0.25, 10]
	 * 
	 * @param zoomIn The zoom to set
	 */
	public void setZoom(double zoomIn) {
		zoom = zoomIn;
		zoom = ENumUtil.clamp(zoom, 0.25, 10);
	}
	
	/**
	 * Sets an object that the camera will focus in on and follow.
	 * <p>
	 * If the given object is null, then the camera will be set to (0, 0)
	 * instead.
	 * 
	 * @param object The object to focus
	 */
	public synchronized void setFocusedObject(GameObject object) {
		focusedObject = object;
		if (focusedObject == null) {
			focusedPoint.set(0, 0);
			offsetX = (focusedPoint.getA() % theWorld.getTileWidth()) * zoom;
			offsetY = (focusedPoint.getB() % theWorld.getTileHeight()) * zoom;
		}
		else {
			focusedCoords.set(focusedObject.worldX, focusedObject.worldY);
			focusedPoint.set(focusedObject.startX, focusedObject.startY);
			offsetX = (focusedObject.startX % theWorld.getTileWidth()) * zoom;
			offsetY = (focusedObject.startY % theWorld.getTileHeight()) * zoom;
		}
	}
	
	/**
	 * Specifies a location in world pixel coordinates that the camera will focus in on.
	 * <p>
	 * Note: The camera will not move from this point.
	 * 
	 * @param x The X pixel coordinate
	 * @param y The Y pixel coordinate
	 */
	public synchronized void setFocusedPoint(int x, int y) {
		focusedObject = null;
		focusedCoords.set(x / theWorld.getTileWidth(), y / theWorld.getTileHeight());
		focusedPoint.set(x, y);
		offsetX = (focusedPoint.getA() % theWorld.getTileWidth()) * zoom;
		offsetY = (focusedPoint.getB() % theWorld.getTileHeight()) * zoom;
	}
	
	/**
	 * Specifies a position in world coordinates that the camera will focus in on.
	 * <p>
	 * Note: The camera will not move from this point.
	 * 
	 * @param x The X pixel coordinate
	 * @param y The Y pixel coordinate
	 */
	public synchronized void setFocusedCoords(int x, int y) {
		focusedObject = null;
		focusedCoords.set(x, y);
		var tw = theWorld.getTileWidth();
		var th = theWorld.getTileHeight();
		focusedPoint.set(x * tw + tw / 2, y * th + th / 2);
		offsetX = (focusedPoint.getA() % tw) * zoom;
		offsetY = (focusedPoint.getB() % th) * zoom;
		//System.out.println(focusedCoords + " : " + focusedPoint + " : " + offsetX + " : " + offsetY);
	}
	
}
