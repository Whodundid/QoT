package envision.game.world.gameWorld;

import envision.game.GameObject;
import envision.game.world.mapEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.util.EntitySpawn;
import envision.game.world.util.Region;
import envision.game.world.worldTiles.WorldTile;
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
	EArrayList<GameObject> getEntitiesInWorld();
	EArrayList<EntitySpawn> getEntitySpawns();
	
	PlayerSpawnPoint getPlayerSpawn();
	//void setPlayerSpawn(int x, int y);
	
	boolean isLoaded();
	
}
