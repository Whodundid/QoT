package envision.game.world;

import java.io.File;

import envision.game.objects.GameObject;
import envision.game.objects.entities.Entity;
import envision.game.objects.entities.EntitySpawn;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.WorldTile;
import envision_lang._launch.EnvisionProgram;
import eutil.datatypes.util.EList;

public interface IGameWorld {
	
	String getWorldName();
	void setWorldName(String nameIn);
	File getWorldFile();
	
	boolean saveWorldToFile();
	boolean loadWorld();
	
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
	
	EList<Region> getRegionData();
	EList<GameObject> getObjectsInWorld();
	EList<Entity> getEntitiesInWorld();
	EList<EntitySpawn> getEntitySpawns();
	
	PlayerSpawnPoint getPlayerSpawn();
	void setPlayerSpawn(int x, int y);
	
	WorldCamera getCamera();
	double getCameraZoom();
	void setCameraZoom(double zoomIn);
	EnvisionProgram getStartupScript();
	void setStartupScript(EnvisionProgram program); 
	
	void onLoad();
	void setLoaded(boolean val);
	boolean isLoaded();
	
	void onGameTick(float dt);
	void onRenderTick(float partial);
	WorldRenderer getWorldRenderer();

	boolean isUnderground();
	void setUnderground(boolean val);
	
	default <E extends GameObject> E addObjectToWorld(E ent) { return null; }
	default <E extends GameObject> void addObjectToWorld(E... ents) {}
	default <E extends GameObject> void removeObjectFromWorld(E... ents) {}
	
	default Entity addEntity(Entity in) { return addObjectToWorld(in); }
	default void addEntity(Entity... ents) { addObjectToWorld(ents); }
	default void removeEntity(Entity... ents) { removeObjectFromWorld(ents); }
	
}
