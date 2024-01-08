package envision.engine.scripting;

import java.io.File;

import envision.Envision;
import envision.engine.screens.ScreenLevel;
import envision.engine.screens.ScreenRepository;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.windowTypes.WindowParent;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.worldTiles.WorldTile;
import envision_lang.lang.java.annotations.EClass;
import eutil.EUtil;
import eutil.datatypes.util.EList;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;
import qot.settings.QoTSettings;

@EClass
public class LangAPI {
    
    //=================
    // Static Instance
    //=================
    
    private static LangAPI INSTANCE;
    
    public static LangAPI getInstance() {
        if (INSTANCE == null) INSTANCE = new LangAPI();
        return INSTANCE;
    }
    
    //========
    // Fields
    //========
    
    public boolean isPaused;
    
    //==============
    // Constructors
    //==============
    
    private LangAPI() {
        
    }
    
    //=========
    // Methods
    //=========
    
    public int[] getWorldDims() {
        var world = Envision.theWorld;
        if (world == null) return null;
        return new int[] { world.getWidth(), world.getHeight() };
    }
    
    public int[] getTileDims() {
        var world = Envision.theWorld;
        if (world == null) return null;
        return new int[] { world.getTileWidth(), world.getTileHeight() };
    }
    
    public void tpEntity(String entityID, int x, int y) { tpEntity_i(entityID, x, y); }
    public void tpEntityToEntity(String entityID, String toEntityID) { tpEntity_i(entityID, toEntityID); }
    public void tpEntityToRegion(String entityID, String regionName) { tpEntityToRegion_i(entityID, regionName); }
    
    public EList<String> getEntitiesInRegion(String regionName) {
        var region = getRegionFromName(regionName);
        if (region == null) return null;
        return region.getEntitiesInside().map(e -> e.getWorldID());
    }
    
    public EList<String> getLivingEntities() {
        var world = Envision.theWorld;
        if (world == null) return null;
        return world.getEntitiesInWorld().map(e -> e.getWorldID());
    }
    
    public String getPlayerID() {
        var player = Envision.thePlayer;
        if (player == null) return "NULL";
        return player.getWorldID();
    }
    
    public String getEntityNameFromID(String entityID) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return "NULL";
        return entity.getName();
    }
    
    public int[] getEntityWorldPos(String entityID) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return null;
        return new int[] { entity.worldX, entity.worldY };
    }
    
    public int[] getEntityPixelPos(String entityID) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return null;
        return new int[] { (int) entity.startX, (int) entity.startY };
    }
    
    public boolean giveEntityItem(Entity entity, Item item) {
        return entity.giveItem(item);
    }
    
    public boolean removeItemFromEntity(Entity entity, Item item) {
        return entity.getInventory().getItems().removeFirstOccurrence(item);
    }
    
    public Item getItemAtSlot(String entityID, int slot) {
        //        var entity = getEntityFromID(entityID);
        //        if (entity == null) return null;
        //        return entity.getInventory().getItemAtIndex(slot);
        return null;
    }
    
    public int spawnEntity(String entityType, int x, int y) { return -1; }
    public int spawnEntity(String entityType, int entityID) { return -1; }
    public int spawnEntity(String entityType, String regionName) { return -1; }
    
    public boolean killEntity(String entityID) { return killEntityFromID(entityID); }
    
    public EList<String> killEntitiesInRegion(String regionName) {
        var region = getRegionFromName(regionName);
        if (region == null) return null;
        var entities = region.getEntitiesInside();
        entities.forEach(this::killEntity_i);
        return entities.map(e -> e.getWorldID());
    }
    
    public void loadWorld(String worldName) { loadWorld_i(worldName); }
    public void unloadWorld() { Envision.loadLevel(null); }
    
    public void setTileAt(WorldTile tile, int x, int y) { Envision.theWorld.setTileAt(tile, x, y); }
    public WorldTile getTileAt(int x, int y) { return Envision.theWorld.getTileAt(x, y); }
    
    public double getEntitySpeed(Entity entity) { return entity.getSpeed(); }
    public void setEntitySpeed(Entity entity, double speed) { entity.setSpeed(speed); }
    
    public boolean createRegion(Region region) {
        return Envision.theWorld.getRegionData().add(region);
    }
    
    boolean deleteRegion(Region region) {
        return Envision.theWorld.getRegionData().remove(region);
    }
    
    public int getWorldTime() { return Envision.levelManager.getCurrentTimeOfDay(); }
    public int getDayLength() { return Envision.levelManager.getDayLength(); }
    public boolean isNight() { return Envision.levelManager.isNight(); }
    public boolean isDay() { return !isNight(); }
    
    public boolean isPaused() { return Envision.isPaused(); }
    public void pauseGame() { Envision.pause(); }
    public void resumeGame() { Envision.unpause(); }
    
    public void displayScreen(String screenName) { displayScreen_i(screenName); }
    
    public void displayWindow(WindowParent window) {
        Envision.displayWindow(ScreenLevel.SCREEN, window);
    }
    
    public void displayWindow(ETerminalWindow window) {
        Envision.displayWindow(ScreenLevel.SCREEN, window);
    }
    
    public String getCurrentScreen() {
        var screen = Envision.getCurrentScreen();
        if (screen == null) return null;
        return screen.getObjectName();
    }
    
    public EList<String> getWindowsOnScreen() {
        var screen = Envision.getCurrentScreen();
        if (screen == null) return null;
        return screen.getAllActiveWindows().map(w -> w.getObjectName());
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void tpEntity_i(String entityID, int x, int y) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return;
        entity.setPixelPos(x, y);
    }
    
    private void tpEntity_i(String entityID, String toEntityID) {
        var entity = getEntityFromID(entityID);
        var toEntity = getEntityFromID(toEntityID);
        if (entity == null || toEntity == null) return;
        entity.setPixelPos(toEntity.worldX, toEntity.worldY);
    }
    
    private void tpEntityToRegion_i(String entityID, String regionName) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return;
        var region = getRegionFromName(regionName);
        if (region == null) return;
        var regionMid = region.getMid();
        entity.setPixelPos(regionMid.x, regionMid.y);
    }
    
    private Entity getEntityFromID(String entityID) {
        return Envision.theWorld.getEntitiesInWorld().getFirst(e -> EUtil.isEqual(e.getWorldID(), entityID));
    }
    
    private Region getRegionFromName(String regionName) {
        return Envision.theWorld.getRegionData().getFirst(r -> EUtil.isEqual(r.getName(), regionName));
    }
    
    private boolean killEntityFromID(String entityID) {
        Entity entity = getEntityFromID(entityID);
        return killEntity_i(entity);
    }
    
    private boolean killEntity_i(Entity entity) {
        if (entity == null) return false;
        
        entity.kill();
        Envision.theWorld.removeEntity(entity);
        return true;
    }
    
    private void displayScreen_i(String screenName) {
        if (EUtil.isEqual("null", screenName)) {
            Envision.displayScreen(null);
            return;
        }
        
        var screen = ScreenRepository.getScreen(screenName);
        if (screen != null) return;
        
        Envision.displayScreen(screen);
    }
    
    private GameWorld getWorldFromName(String worldName) {
        File f = new File(QoTSettings.getEditorWorldsDir(), worldName);
        return new GameWorld(f);
    }
    
    private void loadWorld_i(String worldName) {
        var world = getWorldFromName(worldName);
        Envision.setPlayer(new QoT_Player("Test"));
        Envision.displayScreen(new GamePlayScreen());
        Envision.loadLevel(world);
    }
    
    private String getEntityNameFromID_i(String entityID) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return "NULL";
        return entity.getName();
    }
    
}
