package game.worldTiles.tileCategories.dungeon;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setTexture(WorldTextures.dungOldWallA);
		setWall(true);
		this.wallHeight = 0.05;
	}
	
}