package envision.gameEngine.world.gameWorld;

import envision.gameEngine.GameObject;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameObjects.entity.EntitySpawn;
import envision.gameEngine.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldUtil.Region;
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
	
	boolean isLoaded();
	
}
