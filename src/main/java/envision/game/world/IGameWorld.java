package envision.game.world;

import java.io.File;

import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import envision.game.items.Item;
import envision.game.items.ItemOnGround;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.WorldTile;
import envision_lang._launch.EnvisionProgram;
import eutil.datatypes.points.Point2d;
import eutil.datatypes.util.EList;
import eutil.misc.Direction;

public interface IGameWorld {
	
	String getWorldName();
	void setWorldName(String nameIn);
	File getWorldFile();
	
	boolean saveWorldToFile();
	boolean loadWorld();
	default boolean isFileLoaded() { return false; }
	
	int getWidth();
	int getHeight();
	int getTileWidth();
	int getTileHeight();
	default int getPixelWidth() { return getWidth() * getTileWidth(); }
	default int getPixelHeight() { return getHeight() * getTileHeight(); }
	
	default WorldTile getTileAt(int x, int y) { return getTileAt(0, x, y); }
	WorldTile getTileAt(int layerIn, int x, int y);
	
	default void setTileAt(WorldTile tile, int x, int y) { setTileAt(tile, 0, x, y); }
	void setTileAt(WorldTile tile, int layerIn, int x, int y);
	
	int getNumberOfLayers();
	
	EList<Region> getRegionData();
	EList<GameObject> getObjectsInWorld();
	EList<Entity> getEntitiesInWorld();
	EList<EntitySpawn> getEntitySpawns();
	
	PlayerSpawnPoint getPlayerSpawn();
	void setPlayerSpawn(double x, double y);
	
	EnvisionProgram getStartupScript();
	void setStartupScript(EnvisionProgram program); 
	
	void onLoad(String... args);
	void setLoaded(boolean val);
	boolean isLoaded();
	
	void onGameTick(float dt);
	void onRenderTick(float partial);
	WorldRenderer getWorldRenderer();
	
	default int getAmbientLightLevel() { return 255; }
	
	boolean isUnderground();
	void setUnderground(boolean val);
	
	default double getDistance(GameObject a, GameObject b) { return -1; }
	default double distanceTo(GameObject ent, Point2d point) { return -1; }
	default EList<Entity> getAllEntitiesWithinDistance(GameObject obj, double maxDistance) { return EList.newList(); }
	default EList<Entity> getAllEntitiesWithinSquaredDistance(GameObject obj, double distSquared) { return EList.newList(); }
	default EList<GameObject> getAllGameObjectsWithinDistance(GameObject obj, double maxDistance) { return EList.newList(); }
	default Direction getDirectionTo(GameObject start, GameObject dest) { return Direction.OUT; }
	default double getAngleInDegressTo(GameObject start, GameObject dest) { return 0.0; }
	
	default <E extends GameObject> E addObjectToWorld(E ent) { return null; }
	default <E extends GameObject> void addObjectToWorld(E... ents) {}
	default <E extends GameObject> void removeObjectFromWorld(E... ents) {}
	
	default Entity addEntity(Entity in) { return addObjectToWorld(in); }
	default void addEntity(Entity... ents) { addObjectToWorld(ents); }
	default void removeEntity(Entity... ents) { removeObjectFromWorld(ents); }
	
	default void dropItemOnGround(Item item, double x, double y) {
	    var loot = new ItemOnGround(item, 20000);
	    loot.setPixelPos(x - loot.width * 0.5, y);
	    addEntity(loot);
	}
	
}
