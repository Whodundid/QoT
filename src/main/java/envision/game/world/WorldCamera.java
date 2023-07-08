package envision.game.world;

import java.util.Objects;

import envision.Envision;
import envision.game.GameObject;
import eutil.datatypes.points.Point2d;
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
	private final Point2d focusedCoords = new Point2d(0.0, 0.0);
	/** The pixel world coordinates that this camera is precisely focused on. */
	private final Point2d focusedPoint = new Point2d(0.0, 0.0);
	/** The zoom of the camera. Higher values are more zoomed in. */
	private double zoom = 1.0;
	
	private double minZoom = 0.25;
	private double maxZoom = 10;
	
	/** The number of pixels in the X dimension that the camera is offset by in world coordinates. */
	private double offsetX;
	/** The number of pixels in the Y dimension that the camera is offset by in world coordinates. */
	private double offsetY;
	
	private long panStartTime;
	private long panDuration;
	private boolean isPanning;
	private boolean isEdgeLocked = true;
	
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
	public synchronized void onRenderTick(float partialTicks) {
		if (focusedObject == null) return;
		
		double xCoordToSet = focusedObject.worldX;
        double yCoordToSet = focusedObject.worldY;
        double xToSet = focusedObject.startX;
        double yToSet = focusedObject.startY;
        
        final double ew = focusedObject.width;
        final double eh = focusedObject.height;
        final int ww = theWorld.getWidth(); // world width
        final int wh = theWorld.getHeight(); // world height
        final int tw = theWorld.getTileWidth(); // tile width
        final int th = theWorld.getTileHeight(); // tile height
        final int gameWidth = Envision.getWidth();
        final int gameHeight = Envision.getHeight();
        
        double halfTileWidth = tw  / 2;
        double halfTileHeight = th / 2;

        double halfEntityWidth = ew * 0.5;
        double halfEntityHeight = eh * 0.5;
        
        double scaledTileWidth = zoom * tw;
        double scaledTileHeight = zoom * th;
        
        // center the entity on the middle of the tile accounting for tile offsets
        
        // (entityWidth - tilePixelWidth) * zoom * 0.5
        double entityOffsetX = halfEntityWidth - halfTileWidth;
        // (entityHeight - tilePixelHeight) * zoom * 0.5
        double entityOffsetY = halfEntityHeight - halfTileHeight;
        
        double magicalAdjustmentX = (tw - ew) * zoom;
        double magicalAdjustmentY = (th - eh) * zoom;
        
        double viewableWidth = (gameWidth - scaledTileWidth + magicalAdjustmentX - 0.5) / scaledTileWidth;
        double viewableHeight = (gameHeight + scaledTileHeight + magicalAdjustmentY - 0.5) / scaledTileHeight;
        
        double halfWidth = viewableWidth / 2.0;
        double halfHeight = viewableHeight / 2.0;
        
        double viewablePixelsForHalfWidth = tw * halfWidth;
        double viewablePixelsForHalfHeight = th * halfHeight;
        
        //p:16 => botCoordEdge = ' - 0.5) + 1'      : botEdgeY = ' + (16)'
        //p:32 => botCoordEdge = ' - 1) + 1'        : botEdgeY = ' + (0)'
        //p:40 => botCoordEdge = ' - 1.25) + 1'     : botEdgeY = ' + (-8)'
        //p:64 => botCoordEdge = ' - 2) + 1'        : botEdgeY = ' + (-32)'
        //p:g  => botCoordEdge = ' - (eh / th) + 1' : botEdgeY = ' + (th - eh))'
        
        double leftCoordEdge = Math.floor(halfWidth);
        double rightCoordEdge = Math.floor(ww - halfWidth - (ew / tw));
        double topCoordEdge = halfHeight - 1;
        double botCoordEdge = Math.floor(wh - halfHeight - (eh / th)) + 1;
        
        double leftEdgeX = viewablePixelsForHalfWidth;
        double rightEdgeX = (ww * tw - viewablePixelsForHalfWidth) - ew;
        double topEdgeY = viewablePixelsForHalfHeight - th;
        double botEdgeY = (wh * th - viewablePixelsForHalfHeight) + (th - eh);
        
        if (isEdgeLocked && viewableWidth + 1 < ww && viewableHeight + 1 < wh) {
            xCoordToSet = Math.floor(ENumUtil.clamp(xCoordToSet, leftCoordEdge, rightCoordEdge));
            xToSet = ENumUtil.clamp(xToSet, leftEdgeX, rightEdgeX);
            yCoordToSet = Math.floor(ENumUtil.clamp(yCoordToSet, topCoordEdge, botCoordEdge));
            yToSet = ENumUtil.clamp(yToSet, topEdgeY, botEdgeY);
        }
        
        // the entity's startX % tileWidth => the pixel offset of the entity's startX into the current tile
        double tileOffsetX = xToSet % tw;
        // the entity's startY % tileHeight => the pixel offset of the entity's startY into the current tile
        double tileOffsetY = yToSet % th;
        
        double entityPixelX = (tileOffsetX + entityOffsetX) * zoom;
        double entityPixelY = (tileOffsetY + entityOffsetY) * zoom;
        
        focusedCoords.x = xCoordToSet;
        focusedCoords.y = yCoordToSet;
        focusedPoint.x = xToSet;
        focusedPoint.y = yToSet;
        offsetX = entityPixelX;
        offsetY = entityPixelY;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the exact X pixel coordinate that camera is focused on in the world. */
	public double getX() { return focusedPoint.x; }
	/** Returns the exact Y pixel coordinate that camera is focused on in the world. */
	public double getY() { return focusedPoint.y; }
	/** Returns the X world coordinate that camera is focused on. */
	public int getWorldX() { return (int) (double) focusedCoords.x; }
	/** Returns the Y world coordinate that camera is focused on. */
	public int getWorldY() { return (int) (double) focusedCoords.y; }
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
	 * Default range: [0.25, 10]
	 * 
	 * @param zoomIn The zoom to set
	 */
	public void setZoom(double zoomIn) {
		zoom = zoomIn;
		zoom = ENumUtil.clamp(zoom, minZoom, maxZoom);
	}
	
	public void setMinZoom(double zoomIn) { minZoom = zoomIn; }
	public void setMaxZoom(double zoomIn) { maxZoom = zoomIn; }
	
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
			focusedPoint.set(0.0, 0.0);
			offsetX = (focusedPoint.x % theWorld.getTileWidth()) * zoom;
			offsetY = (focusedPoint.y % theWorld.getTileHeight()) * zoom;
		}
		else {
			focusedCoords.set((double) focusedObject.worldX, (double) focusedObject.worldY);
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
	public synchronized void setFocusedPoint(double x, double y) {
		focusedObject = null;
		focusedCoords.set(x / theWorld.getTileWidth(), y / theWorld.getTileHeight());
		focusedPoint.set(x, y);
		offsetX = (focusedPoint.x % theWorld.getTileWidth()) * zoom;
		offsetY = (focusedPoint.y % theWorld.getTileHeight()) * zoom;
	}
	
	/**
	 * Specifies a position in world coordinates that the camera will focus in on.
	 * <p>
	 * Note: The camera will not move from this point.
	 * 
	 * @param x The X pixel coordinate
	 * @param y The Y pixel coordinate
	 */
	public synchronized void setFocusedCoords(double x, double y) {
		focusedObject = null;
		focusedCoords.set(x, y);
		var tw = theWorld.getTileWidth();
		var th = theWorld.getTileHeight();
		focusedPoint.set(x * tw + tw / 2, y * th + th / 2);
		offsetX = (focusedPoint.x % tw) * zoom;
		offsetY = (focusedPoint.y % th) * zoom;
	}
	
}
