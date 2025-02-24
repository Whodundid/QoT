package envision.game.world.layerSystem;

import envision.Envision;
import envision.debug.Profiler;
import envision.game.GameObject;
import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.types.RenderingComponent;
import envision.game.entities.GroundClutter;
import envision.game.util.IDrawable;
import envision.game.util.InsertionSort;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class WorldDrawLayer {
    
    //========
    // Fields
    //========
    
    private int layer = 0;
    public int camLayer = 0;
    
    private IGameWorld world;
    private WorldTile[][] worldData;
    private EList<GameObject> gameObjects;
    
    private EList<IDrawable> builtLayer = new EArrayList<>();
    private boolean built = false;    
    //==============
    // Constructors
    //==============
    
    public WorldDrawLayer(IGameWorld worldIn, int layerIn, int camLayerIn) {
        world = worldIn;
        layer = layerIn;
        camLayer = camLayerIn;
        worldData = new WorldTile[world.getHeight()][world.getWidth()];
        gameObjects = EList.newList();
    }    
    //=========
    // Methods
    //=========
    
    /**
     * Gathers all world tiles and entities within the given region to be
     * rendered. Can be called independently from renderLayer in order to
     * save on processing time. For instance, this method could be called
     * every game tick instead of every frame tick.
     * 
     * @param left
     * @param top
     * @param right
     * @param bot
     */
    public void buildLayer(int left, int top, int right, int bot) {
        var p = Profiler.getProfiler("FRAME_TICK");
        
        built = false;
        builtLayer.clear();
        //worldData.clear();
        gameObjects.clear();
        
        // this code should be completely rethought
        // furthermore, world layers altogether need to be redesigned now that 3D is a thing..
        
        var camera = Envision.levelManager.getCamera();
        int cameraUpper = camera.getUpperCameraLayer();
        var theEntity = camera.getFocusedObject();
        int entX = -1;
        int entY = -1;
        int entLayer = camLayer;
        if (theEntity != null) {
            var colDims = theEntity.getCollisionDims();
            entX = (int) (colDims.midX / world.getTileWidth());
            entY = (int) (colDims.midY / world.getTileHeight());
            entLayer = theEntity.getCameraLayer();
        }
        int max = world.getNumberOfLayers() - 1;
        
        //p.startSection("build tiles");
        boolean camLowerThanMax = cameraUpper != max;
        final int width = world.getWidth();
        final int height = world.getHeight();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (camLowerThanMax) {
                    if (camLayer <= entLayer || j > entX + 5 || j < entX - 5 || i > entY + 5 || i < entY - 5) {
                        worldData[i][j] = world.getTileAt(camLayer, j, i);
                    }
                }
                else {
                    worldData[i][j] = world.getTileAt(camLayer, j, i);
                }
            }
        }
        //p.endSection("build tiles");
        
        //p.startSection("filter entities");
        //p.startSection("filter");
        var entities = world.getEntitiesInWorld().filter(e -> e.getCameraLayer() == camLayer);
        gameObjects.addAll(entities);
        //p.endSection();
        
        // add all world tiles within the specified area
        //p.startSection("arr");
        for (int i = top; i <= bot; i++) {
            for (int j = left; j <= right; j++) {
                var tile = worldData[i][j];
                if (tile == null || tile == VoidTile.instance) continue;
                if (tile.getRenderLayer() == layer) builtLayer.add(tile);
            }
        }
        //p.endSection();
        
        double w_left = (left - 1) * world.getTileWidth();
        double w_top = (top - 1) * world.getTileHeight();
        double w_right = (right + 1) * world.getTileWidth();
        double w_bot = (bot + 1) * world.getTileHeight();
        //p.endSection("filter entities");
        
        // add all objects within the specified area
        //p.startSection("add entities");
        for (var obj : gameObjects) {
            if (obj.sprite == null) continue;
            
            if (obj instanceof GroundClutter) {
                if (layer == 0) {
                    if (obj.getDimensions().contains(w_left, w_top, w_right, w_bot)) {
                        builtLayer.add(obj);
                    }
                }
            }
            else {
                if (obj == Envision.thePlayer) {
                    builtLayer.add(obj);
                    continue;
                }
                if (obj.getDimensions().contains(w_left, w_top, w_right, w_bot)) {
                    builtLayer.add(obj);
                }
            }
        }
        //p.endSection("add entities");
        
        //System.out.println(builtLayer);
        
        built = true;
    }
    
    /**
     * To be called on every frame tick.
     * 
     * @param world
     * @param midX TODO
     * @param midY TODO
     * @param x
     * @param y
     * @param w
     * @param h
     * @param brightness
     * @param mouseOver
     */
    public void renderLayer(IGameWorld world, WorldCamera camera) {
        if (!built) return;
        
        //sort the layer before draw
        
        InsertionSort.sort(builtLayer);
//        if (layer == 1) {
//            for (var e : builtLayer) {
//                DebugToolKit.println(e, e.getSortPoint());
//            }
//        }
        
        //if (layer == 1) System.out.println(builtLayer);
        //System.out.println(builtLayer);
        
        // draw each object on the layer
        final int size = builtLayer.size();
        for (int i = 0; i < size; i++) {
            var obj = builtLayer.get(i);
            if (obj instanceof ComponentBasedObject e && e.hasComponent(ComponentType.RENDERING)) {
                RenderingComponent r = e.getComponent(ComponentType.RENDERING);
                r.draw(world, camera);
            }
        }
    }
    
    public void addObject(GameObject object) {
        gameObjects.add(object);
    }    
    //=========
    // Getters
    //=========
    
    public IGameWorld getWorld() { return world; }
    public int getLayer() { return layer; }
    public WorldTile[][] getWorldData() { return worldData; }
    public EList<GameObject> getGameObjects() { return gameObjects; }
    public EList<IDrawable> getDrawnObjects() { return builtLayer; }
    public boolean isBuilt() { return built; }    
    //=========
    // Setters
    //=========
    
    public void setTileAt(WorldTile in, int xIn, int yIn) {
        worldData[yIn][xIn] = in;
    }
    
}
