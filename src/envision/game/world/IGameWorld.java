package envision.game.world;

import envision.game.objects.GameObject;
import envision.game.objects.entities.Entity;
import envision.game.objects.entities.EntitySpawn;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

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
	
	EList<Region> getRegionData();
	EList<GameObject> getObjectsInWorld();
	EList<Entity> getEntitiesInWorld();
	EList<EntitySpawn> getEntitySpawns();
	
	PlayerSpawnPoint getPlayerSpawn();
	//void setPlayerSpawn(int x, int y);
	
	WorldCamera getCamera();
	double getCameraZoom();
	void setCameraZoom(double zoomIn);
	
	boolean isLoaded();
	boolean isUnderground();
	
}
