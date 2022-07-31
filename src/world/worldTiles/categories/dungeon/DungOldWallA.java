package world.worldTiles.categories.dungeon;

import assets.textures.WorldTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setTexture(WorldTextures.dungOldWallA);
		setWall(true);
		this.wallHeight = 0.05;
	}
	
}