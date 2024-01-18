package envision.game.world;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.game.GameObject;
import eutil.datatypes.points.Point2d;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_i;
import qot.settings.QoTSettings;

public class WorldCamera {
    
    //========
    // Fields
    //========
    
    /** The world that this camera is viewing. */
    private IGameWorld theWorld;
    /** The object this camera is following. */
    private GameObject focusedObject;
    /** The world coordinates that this camera is focused around. */
    private final Point2d focusedCoords = new Point2d(0.0, 0.0);
    /**
     * The pixel world coordinates that this camera is precisely focused on.
     */
    private final Point2d focusedPoint = new Point2d(0.0, 0.0);
    /** The zoom of the camera. Higher values are more zoomed in. */
    private double zoom = 1.0;
    private double zoomi;
    
    private double minZoom = 0.25;
    private double maxZoom = 10;
    
    /**
     * The number of pixels in the X dimension that the camera is offset by in
     * world coordinates.
     */
    private double offsetX;
    /**
     * The number of pixels in the Y dimension that the camera is offset by in
     * world coordinates.
     */
    private double offsetY;
    
    /** A number of pixels to offset all rendering by in the X axis. */
    private double pixelOffsetX;
    /** A number of pixels to offset all rendering by in the Y axis. */
    private double pixelOffsetY;
    
    private int mXWorldPixel = 0;
    private int mYWorldPixel = 0;
    private int mXWorldTile = 0;
    private int mYWorldTile = 0;
    
    private long panStartTime;
    private long panDuration;
    private boolean isPanning;
    
    private int tileWidth;
    private int tileHeight;
    private double tileWidthInverse;
    private double tileHeightInverse;
    
    /**
     * The dimensions of the screen that can be drawn to. Affects center of the
     * screen offsets.
     */
    private final Dimension_d drawArea = new Dimension_d();
    
    private int currentLayer = -1;
    private int upperCameraLayer = 0;
    
    //==============
    // Constructors
    //==============
    
    public WorldCamera() {
        drawArea.setDimensions(0, 0, Envision.getWidth(), Envision.getHeight());
    }
    
    //========================
    // Implementation Methods
    //========================
    
    /**
     * Called each frame to update camera position.
     */
    public synchronized void onRenderTick(float partialTicks) {
        if (theWorld == null) return;
        
        // update the mouse's position in the world for both pixel and world coordinates
        updateMousePositionInWorld();
        // if focused on an entity, update camera position and limits
        updateFocusedObjectCameraPositionAndLimits();
    }
    
    /**
     * Determines the position of the mouse in the world in both pixel and
     * world coordinates.
     */
    protected void updateMousePositionInWorld() {
        // x world pixel under the mouse with zoom factored out
        mXWorldPixel = (int) ((Mouse.getMx_double() - drawArea.midX) * zoomi + (focusedPoint.x - pixelOffsetX));
        // y world pixel under the mouse with zoom factored out
        mYWorldPixel = (int) ((Mouse.getMy_double() - drawArea.midY) * zoomi + (focusedPoint.y - pixelOffsetY));
        // x world coordinate under the mouse
        mXWorldTile = (int) (mXWorldPixel * tileWidthInverse);
        // y world coordinate under the mouse
        mYWorldTile = (int) (mYWorldPixel * tileHeightInverse);
    }
    
    /**
     * Determines where to position the camera if it is currently focused
     * around a single object in the world.
     * <p>
     * Furthermore, calculates the limits on how the camera can move when
     * around edges of the world.
     */
    protected void updateFocusedObjectCameraPositionAndLimits() {
        // if there isn't a focused object, then there's nothing to calculate here
        if (focusedObject == null) return;
        
        //var s = new EStringBuilder();
        
        double xCoordToSet = focusedObject.worldX;
        double yCoordToSet = focusedObject.worldY;
        double xToSet = focusedObject.startX;
        double yToSet = focusedObject.startY;
        
        //s.a(focusedObject.name, "   ");
        //s.a("wxy[", xCoordToSet, ", ", yCoordToSet, "]   ");
        //s.a("pxy[", xToSet, ", ", yToSet, "]   ");
        
        final double ew = focusedObject.width;
        final double eh = focusedObject.height;
        //s.a("ewh[", ew, ", ", eh, "]   ");
        
        final int ww = theWorld.getWidth(); // world width
        final int wh = theWorld.getHeight(); // world height
        final int tw = theWorld.getTileWidth(); // tile width
        final int th = theWorld.getTileHeight(); // tile height
        final int gameWidth = Envision.getWidth();
        final int gameHeight = Envision.getHeight();
        
        double halfTileWidth = tw / 2.0;
        double halfTileHeight = th / 2.0;
        
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
        
        double vw = gameWidth / scaledTileWidth;
        double vh = gameHeight / scaledTileHeight;
        
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
        double topCoordEdge = halfHeight - 1;// - 3;
        double botCoordEdge = Math.floor(wh - halfHeight - (eh / th)) + 1;
        
        double leftEdgeX = viewablePixelsForHalfWidth;
        double rightEdgeX = (ww * tw - viewablePixelsForHalfWidth) - ew;
        double topEdgeY = viewablePixelsForHalfHeight - th * 1;// - th * 3;
        double botEdgeY = (wh * th - viewablePixelsForHalfHeight) + (th - eh);
        
        if (QoTSettings.camreaEdgeLocking.getBoolean() && vw <= ww && vh <= wh) {
            xCoordToSet = Math.floor(ENumUtil.clamp(xCoordToSet, leftCoordEdge, rightCoordEdge));
            yCoordToSet = Math.floor(ENumUtil.clamp(yCoordToSet, topCoordEdge, botCoordEdge));
            xToSet = ENumUtil.clamp(xToSet, leftEdgeX, rightEdgeX);
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
    
    //=========
    // Methods
    //=========
    
    public boolean isMouseInWorld() {
        return isScreenPositionInWorld(Mouse.getMx_double(), Mouse.getMy_double());
    }
    
    /** Returns true if the given screen coordinates are within the rendered world. */
    public boolean isScreenPositionInWorld(double x, double y) {
        if (theWorld == null) return false;
        final double[] area = convertWorldAreaToScreenArea(0, 0, theWorld.getPixelWidth() - 1, theWorld.getPixelHeight() - 1);
        return (x >= area[0] && x <= area[2] && y >= area[1] && y <= area[3]);
    }
    
    public void moveCameraByCoords(double x, double y) { moveCameraByCoords(x, y, true); }
    public void moveCameraByCoords(double x, double y, boolean clampToWorld) {
        moveCameraByPixels(x * tileWidth, y * tileWidth);
    }
    
    public void moveCameraByPixels(double x, double y) { moveCameraByPixels(x, y, true); }
    public void moveCameraByPixels(double x, double y, boolean clampToWorld) {
        double newX = focusedPoint.x + x;
        double newY = focusedPoint.y + y;
        if (clampToWorld && theWorld != null) {
            double maxX = theWorld.getWidth() * theWorld.getTileWidth();
            double maxY = theWorld.getHeight() * theWorld.getTileHeight();
            newX = ENumUtil.clamp(newX, pixelOffsetX * 2.0, maxX);
            newY = ENumUtil.clamp(newY, pixelOffsetY * 2.0, maxY);
        }
        setFocusedPoint(newX, newY);
    }
    
    public double[] convertScreenPxToWorldPx(Point2d pos) { return convertScreenPxToWorldPx(pos.x, pos.y); }
    public double[] convertScreenPxToWorldPx(double x, double y) {
        final double[] r = new double[2];
        
        r[0] = (x - drawArea.midX) * zoomi + (focusedPoint.x - pixelOffsetX);
        r[1] = (y - drawArea.midY) * zoomi + (focusedPoint.y - pixelOffsetY);
        
        return r;
    }
    
    public double[] convertScreenAreaToWorldArea(double[] screenArea) {
        if (screenArea.length != 4) throw new IllegalArgumentException("Given screen area dimension length != 4");
        return convertScreenAreaToWorldArea(screenArea[0], screenArea[1], screenArea[2], screenArea[3]);
    }
    
    public double[] convertScreenAreaToWorldArea(double startX, double startY, double endX, double endY) {
        final double[] r = new double[4];
        
        r[0] = (startX - drawArea.midX) * zoomi + (focusedPoint.x - pixelOffsetX);
        r[1] = (startY - drawArea.midY) * zoomi + (focusedPoint.y - pixelOffsetY);
        r[2] = (endX - drawArea.midX) * zoomi + (focusedPoint.x - pixelOffsetX);
        r[3] = (endY - drawArea.midY) * zoomi + (focusedPoint.y - pixelOffsetY);
        
        return r;
    }
    
    public double[] convertWorldPxToScreenPx(Point2d pos) { return convertWorldPxToScreenPx(pos.x, pos.y); }
    public double[] convertWorldPxToScreenPx(double x, double y) {
        final double[] r = new double[2];
        
        r[0] = (x + pixelOffsetX - focusedPoint.x) * zoom + drawArea.midX;
        r[1] = (y + pixelOffsetY - focusedPoint.y) * zoom + drawArea.midY;
        
        return r;
    }
    
    public double[] convertWorldAreaToScreenArea(double[] worldArea) {
        if (worldArea.length != 4) throw new IllegalArgumentException("Given world area dimension length != 4");
        return convertWorldAreaToScreenArea(worldArea[0], worldArea[1], worldArea[2], worldArea[3]);
    }
    
    public double[] convertWorldAreaToScreenArea(double startX, double startY, double endX, double endY) {
        final double[] r = new double[4];
        
        r[0] = (startX + pixelOffsetX - focusedPoint.x) * zoom + drawArea.midX;
        r[1] = (startY + pixelOffsetY - focusedPoint.y) * zoom + drawArea.midY;
        r[2] = (endX + pixelOffsetX - focusedPoint.x) * zoom + drawArea.midX;
        r[3] = (endY + pixelOffsetY - focusedPoint.y) * zoom + drawArea.midY;
        
        return r;
    }
    
    //=========
    // Getters
    //=========
    
    /**
     * @return the exact X pixel coordinate that camera is focused on in the
     *         world.
     */
    public double getX() {
        return focusedPoint.x;
    }
    /**
     * @return the exact Y pixel coordinate that camera is focused on in the
     *         world.
     */
    public double getY() {
        return focusedPoint.y;
    }
    /** @return the X world coordinate that camera is focused on. */
    public int getWorldX() {
        return (int) focusedCoords.x;
    }
    /** @return the Y world coordinate that camera is focused on. */
    public int getWorldY() {
        return (int) focusedCoords.y;
    }
    /**
     * @return the number of pixels in the X dimension that the camera is
     *         offset by in world coordinates.
     */
    public double getOffsetX() {
        return offsetX;
    }
    /**
     * @return the number of pixels in the Y dimension that the camera is
     *         offset by in world coordinates.
     */
    public double getOffsetY() {
        return offsetY;
    }
    /**
     * @return the current zoom of the camera. Higher values are more zoomed
     *         in!
     */
    public double getZoom() {
        return zoom;
    }
    /** @return the min zoom of the camera. */
    public double getMinZoom() { return minZoom; }
    /** @return the max zoom of the camera. */
    public double getMaxZoom() { return maxZoom; }
    
    /**
     * @return the object the camera is currently focused on. (Could be null)
     */
    public synchronized GameObject getFocusedObject() {
        return focusedObject;
    }
    
    /**
     * @return the size (in pixels) for the width of each tile in the world.
     */
    //	public double getScaledTileWidth() { return theWorld.getTileWidth() * zoom; }
    /**
     * @return the size (in pixels) for the height of each tile in the world.
     */
    //	public double getScaledTileHeight() { return theWorld.getTileHeight() * zoom; }
    
    /**
     * @return the X location of where the camera is in terms of decimal world
     *         coordinates.
     */
    public double getCameraCenterX() {
        if (theWorld == null) return Double.NaN;
        return focusedPoint.x / theWorld.getTileWidth();
    }
    /**
     * @return the Y location of where the camera is in terms of decimal world
     *         coordinates.
     */
    public double getCameraCenterY() {
        if (theWorld == null) return Double.NaN;
        return focusedPoint.y / theWorld.getTileHeight();
    }
    
    public double[] getCenteredTileDrawDimensions() {
        return calculateDrawDimensions(focusedPoint.x - tileWidth, focusedPoint.y - tileHeight, tileWidth, tileHeight);
    }
    
    /**
     * @return the X pixel coordinate of where the mouse is in the world.
     *         (could be out of bounds)
     */
    public double getMousePixelX() {
        return mXWorldPixel;
    }
    /**
     * @return the Y pixel coordinate of where the mouse is in the world.
     *         (could be out of bounds)
     */
    public double getMousePixelY() {
        return mYWorldPixel;
    }
    /**
     * @return the X world coordinate of where the mouse is in the world.
     *         (could be out of bounds)
     */
    public int getMouseTileX() {
        return mXWorldTile;
    }
    /**
     * @return the Y world coordinate of where the mouse is in the world.
     *         (could be out of bounds)
     */
    public int getMouseTileY() {
        return mYWorldTile;
    }
    
    public Dimension_i getDrawableArea() { return new Dimension_i(drawArea); }
    
    public double getDrawableAreaStartX() { return drawArea.startX; }
    public double getDrawableAreaStartY() { return drawArea.startY; }
    public double getDrawableAreaEndX() { return drawArea.endX; }
    public double getDrawableAreaEndY() { return drawArea.endY; }
    
    /** @return the width of the screen area that can be drawn to. */
    public double getDrawableAreaWidth() { return drawArea.width; }
    /** @return the height of the screen area that can be drawn to. */
    public double getDrawableAreaHeight() { return drawArea.height; }
    /** @return the width of the screen area that can be drawn to divided by 2. */
    public double getDrawableAreaHalfWidth() { return drawArea.width * 0.5; }
    /** @return the height of the screen area that can be drawn to divided by 2. */
    public double getDrawableAreaHalfHeight() { return drawArea.height * 0.5; }
    
    public double getPixelOffsetX() { return pixelOffsetX; }
    public double getPixelOffsetY() { return pixelOffsetY; }
    
    //===================
    // Rendering Helpers
    //===================
    
    public double calculateObjectDrawX(GameObject object) { return calculateDrawX(object.startX, object.getCameraLayer()); }
    public double calculateObjectDrawY(GameObject object) { return calculateDrawY(object.startY, object.getCameraLayer()); }
    
    public double calculateDrawX(double x) { return calculateDrawX(x, currentLayer); }
    public double calculateDrawY(double y) { return calculateDrawY(y, currentLayer); }
    
    public double calculateDrawX(double x, int layer) {
        return (x + pixelOffsetX - focusedPoint.x) * zoom + drawArea.midX;
    }
    
    public double calculateDrawY(double y, int layer) {
        return (y + pixelOffsetY - focusedPoint.y) * zoom + drawArea.midY;
    }
    
    /**
     * Returns an array containing the drawX, drawY, drawW, drawH in the form
     * of [x, y, w, h].
     * 
     * @param tile The tile to get draw dimensions for
     * 
     * @return An array containing the object's screen drawing dimensions
     */
    public double[] calculateDrawDimensions(GameObject object) {
        return calculateDrawDimensions(object.startX, object.startY, object.width, object.height, object.getCameraLayer());
    }
    
    public double[] calculateDrawDimensions(double startX, double startY, double width, double height) {
        return calculateDrawDimensions(startX, startY, width, height, currentLayer);
    }
    public double[] calculateDrawDimensions(double startX, double startY, double width, double height, int layer) {
        // [x, y, w, h]
        final double[] r = new double[4];
        
        //      world pixel coords                             screen cords
        r[0] = (startX + pixelOffsetX - focusedPoint.x) * zoom + drawArea.midX;
        r[1] = (startY + pixelOffsetY - focusedPoint.y) * zoom + drawArea.midY;
        r[2] = width * zoom;
        r[3] = height * zoom;
        
        r[1] -= (layer - currentLayer) * tileHeight * 0.75 * zoom;
        
        return r;
    }
    
    /**
     * Checks if the mouse is hovering over the given game object.
     * 
     * @param object The object to check
     * 
     * @return True if mouse is over the object
     */
    public boolean isMouseOverObject(GameObject object) {
        return isMouseInWorldArea(object.startX, object.startY, object.width, object.height);
    }
    
    /**
     * Checks if the mouse is hovering over the given dimension in world space.
     * 
     * @param startX The start X world pixel coordinate
     * @param startY The start Y world pixel coordinate
     * @param width  The width in world pixels
     * @param height The height in world pixels
     * 
     * @return True if the mouse if over the given world area
     */
    public boolean isMouseInWorldArea(double startX, double startY, double width, double height) {
        final double[] draw = calculateDrawDimensions(startX, startY, width, height);
        int mX = Mouse.getMx();
        int mY = Mouse.getMy();
        return (mX >= draw[0] && mX <= draw[0] + draw[2] && mY >= draw[1] && mY <= draw[1] + draw[3]);
    }
    
    //=========
    // Getters
    //=========
    
    public int getCurrentLayer() { return currentLayer; }
    public int getUpperCameraLayer() { return upperCameraLayer; }
    
    //=========
    // Setters
    //=========
    
    /**
     * Sets this camera's zoom. Default range: [0.25, 10]
     * 
     * @param zoomIn The zoom to set
     */
    public void setZoom(double zoomIn) {
        double min = minZoom;
        double max = maxZoom;
        
        if (QoTSettings.camreaEdgeLocking.getBoolean()) {
            min = calcMinEdgeLockZoom();
            min = ENumUtil.clamp(min, minZoom, 1000);
            max = ENumUtil.clamp(max, min, max);
        }
        
        zoom = zoomIn;
        zoom = ENumUtil.clamp(zoom, min, max);
        zoomi = 1.0 / zoom;
    }
    
    protected double calcMinEdgeLockZoom() {
        final int gameWidth = Envision.getWidth();
        final int gameHeight = Envision.getHeight();
        
        double pixelWidth = theWorld.getPixelWidth();
        double pixelHeight = theWorld.getPixelHeight();
        
        double minZoomWidth = gameWidth / pixelWidth;
        double minZoomHeight = gameHeight / pixelHeight;
        
        minZoomWidth = Math.round(minZoomWidth * 4.0) / 4.0;
        minZoomHeight = Math.round(minZoomHeight * 4.0) / 4.0;
        
        return Math.max(minZoomWidth, minZoomHeight);
    }
    
    public void setMinZoom(double zoomIn) { minZoom = zoomIn; }
    public void setMaxZoom(double zoomIn) { maxZoom = zoomIn; }
    
    public void setEdgeLocked(boolean val) {
        QoTSettings.camreaEdgeLocking.set(val);
        QoTSettings.saveConfig();
    }
    
    public boolean isEdgeLocked() { return QoTSettings.camreaEdgeLocking.getBoolean(); }
    
    public synchronized void setActiveWorld(IGameWorld world) {
        this.theWorld = world;
        if (theWorld != null) {
            tileWidth = theWorld.getTileWidth();
            tileHeight = theWorld.getTileHeight();
            tileWidthInverse = 1.0 / tileWidth;
            tileHeightInverse = 1.0 / tileHeight;
        }
    }
    
    public void setCurrentLayer(int layerIn) {
        if (theWorld == null) currentLayer = -1;
        currentLayer = ENumUtil.clamp(layerIn, 0, theWorld.getNumberOfLayers());
    }
    
    public void setUpperCameraLayer(int layerIn) {
        upperCameraLayer = layerIn;
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
            focusedPoint.set(0.0, 0.0);
            offsetX = (focusedPoint.x % theWorld.getTileWidth()) * zoom;
            offsetY = (focusedPoint.y % theWorld.getTileHeight()) * zoom;
        }
        else {
            focusedCoords.set(focusedObject.worldX, focusedObject.worldY);
            focusedPoint.set(focusedObject.startX, focusedObject.startY);
            offsetX = (focusedObject.startX % theWorld.getTileWidth()) * zoom;
            offsetY = (focusedObject.startY % theWorld.getTileHeight()) * zoom;
            currentLayer = object.getCameraLayer();
        }
    }
    
    /**
     * Specifies a location in world pixel coordinates that the camera will
     * focus in on.
     * <p>
     * Note: The camera will not move from this point.
     * 
     * @param x The X pixel coordinate
     * @param y The Y pixel coordinate
     */
    public synchronized void setFocusedPoint(double x, double y) {
        focusedObject = null;
        focusedCoords.set(x / (double) tileWidth, y / (double) tileHeight);
        focusedPoint.set(x, y);
        offsetX = (focusedPoint.x % (double) tileWidth) * zoom;
        offsetY = (focusedPoint.y % (double) tileHeight) * zoom;
    }
    
    /**
     * Specifies a position in world coordinates that the camera will focus in
     * on.
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
        focusedPoint.set(x * tw + tw / 2.0, y * th + th / 2.0);
        offsetX = (focusedPoint.x % tw) * zoom;
        offsetY = (focusedPoint.y % th) * zoom;
    }
    
    public void setDrawableAreaDimensions(int startX, int startY, int endX, int endY) {
        drawArea.setDimensions(startX, startY, endX, endY);
    }
    
    public void setPixelOffsetX(double offsetX) { pixelOffsetX = offsetX; }
    public void setPixelOffsetY(double offsetY) { pixelOffsetY = offsetY; }
    public void setPixelOffset(double offsetX, double offsetY) {
        pixelOffsetX = offsetX;
        pixelOffsetY = offsetY;
    }
    
}
