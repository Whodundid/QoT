package envision.engine.scripting;

import java.io.File;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.screens.ScreenLevel;
import envision.engine.screens.ScreenRepository;
import envision.engine.windows.windowTypes.WindowParent;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.worldTiles.WorldTile;
import envision_lang.lang.java.annotations.EFunction;
import eutil.EUtil;
import eutil.datatypes.util.EList;
import qot.settings.QoTSettings;

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
    
    @EFunction public void tpEntity(int entityID, int x, int y) { tpEntity_i(entityID, x, y); }
    @EFunction public void tpEntity(int entityID, int toEntityID) { tpEntity_i(entityID, toEntityID); }
    @EFunction public void tpEntity(int entityID, String regionName) { tpEntity_i(entityID, regionName); }
    
    @EFunction public EList<Entity> getEntitiesInRegion(String regionName) { return null; }
    @EFunction public EList<Entity> getLivingEntities() { return Envision.theWorld.getEntitiesInWorld(); }
    
    @EFunction
    public boolean giveEntityItem(Entity entity, Item item) {
        return entity.giveItem(item);
    }
    
    @EFunction
    public boolean removeItemFromEntity(Entity entity, Item item) {
        return entity.getInventory().getItems().removeFirstOccurrence(item);
    }
    
    @EFunction
    public Item getItemAtSlot(int entityID, int slot) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return null;
        return entity.getInventory().getItemAtIndex(slot);
    }
    
    @EFunction public void spawnEntity(String entityType, int x, int y) {}
    @EFunction public void spawnEntity(String entityType, int entityID) {}
    @EFunction public void spawnEntity(String entityType, String regionName) {}
    
    @EFunction public boolean killEntity(int entityID) { return killEntityFromID(entityID); }
    
    @EFunction
    public EList<Entity> killEntitiesInRegion(Region region) {
//        var entities = getEntitiesInRegion(region);
//        entities.forEach(this::killEntity_i);
//        return entities;
        return null;
    }
    
    @EFunction public void loadWorld(String worldName) { loadWorld_i(worldName); }
    @EFunction public void unloadWorld() { Envision.loadWorld(null); }
    
    @EFunction public void setTileAt(WorldTile tile, int x, int y) { Envision.theWorld.setTileAt(tile, x, y); }
    @EFunction public WorldTile getTileAt(int x, int y) { return Envision.theWorld.getTileAt(x, y); }
    
    @EFunction public double getEntitySpeed(Entity entity) { return entity.getSpeed(); }
    @EFunction public void setEntitySpeed(Entity entity, double speed) { entity.setSpeed(speed); }
    
    @EFunction
    public boolean createRegion(Region region) {
        return Envision.theWorld.getRegionData().add(region);
    }
    
    @EFunction
    boolean deleteRegion(Region region) {
        return Envision.theWorld.getRegionData().remove(region);
    }
    
    @EFunction public int getWorldTime() { return Envision.theWorld.getTime(); }
    @EFunction public int getDayLength() { return Envision.theWorld.getDayLength(); }
    @EFunction public boolean isNight() { return Envision.theWorld.isNight(); }
    @EFunction public boolean isDay() { return !isNight(); }
    
    @EFunction public boolean isPaused() { return Envision.isPaused(); }
    @EFunction public void pauseGame() { Envision.pause(); }
    @EFunction public void resumeGame() { Envision.unpause(); }
    
    @EFunction public void displayScreen(String screenName) { displayScreen_i(screenName); }
    @EFunction public void displayWindow(WindowParent window) { Envision.displayWindow(ScreenLevel.SCREEN, window); }
    @EFunction public GameScreen getCurrentScreen() { return Envision.getCurrentScreen(); }
    
    @EFunction
    public EList<WindowParent> getWindowsOnScreen() {
        return getCurrentScreen().getAllActiveWindows().map(w -> (WindowParent) w);
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void tpEntity_i(int entityID, int x, int y) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return;
        entity.setWorldPos(x, y);
    }
    
    private void tpEntity_i(int entityID, int toEntityID) {
        var entity = getEntityFromID(entityID);
        var toEntity = getEntityFromID(toEntityID);
        if (entity == null || toEntity == null) return;
        entity.setWorldPos(toEntity.worldX, toEntity.worldY);
    }
    
    private void tpEntity_i(int entityID, String regionName) {
        var entity = getEntityFromID(entityID);
        if (entity == null) return;
        var region = getRegionFromName(regionName);
        if (region == null) return;
        var regionMid = region.getMid();
        entity.setPixelPos(regionMid.x, regionMid.y);
    }
    
    private Entity getEntityFromID(int entityID) {
        return Envision.theWorld.getEntitiesInWorld().getFirst(e -> e.getWorldID() == entityID);
    }
    
    private Region getRegionFromName(String regionName) {
        return Envision.theWorld.getRegionData().getFirst(r -> EUtil.isEqual(r.getName(), regionName));
    }
    
    private boolean killEntityFromID(int entityID) {
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
        Envision.loadWorld(world);
    }
    
}
