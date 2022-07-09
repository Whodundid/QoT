package game.worldTiles.tileCategories.dungeon;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class DungWallA extends WorldTile {
	
	public DungWallA() {
		super(TileIDs.DUNG_WALL_A);
		setBlocksMovement(true);
		setWall(true);
		setTexture(WorldTextures.dungWallA);
	}
	
}