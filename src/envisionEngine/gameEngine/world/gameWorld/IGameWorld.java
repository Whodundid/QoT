package envisionEngine.gameEngine.world.gameWorld;

import envisionEngine.gameEngine.GameObject;
import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameObjects.entity.EntitySpawn;
import envisionEngine.gameEngine.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldUtil.Region;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import eutil.datatypes.EArrayList;

public interface IGameWorld {
	
	String getWorldName();
	void setWorldName(String nameIn);
	
	int getWidth();
	int getHeight();
	int getTileWidth();
	int getTileHeight();
	default int getPixelWidth() { return getWidth() * getTileWidth(); }
	default int getPixelHeight() { return getHeight() * getTileHeight(); }
	
	WorldTile getTileAt(int x, int y);
	void setTileAt(WorldTile tile, int x, int y);
	
	EArrayList<Region> getRegionData();
	EArrayList<GameObject> getObjectsInWorld();
	EArrayList<Entity> getEntitiesInWorld();
	EArrayList<EntitySpawn> getEntitySpawns();
	
	PlayerSpawnPoint getPlayerSpawn();
	//void setPlayerSpawn(int x, int y);
	
	WorldCamera getCamera();
	double getCameraZoom();
	void setCameraZoom(double zoomIn);
	
	boolean isLoaded();
	boolean isUnderground();
	
}
