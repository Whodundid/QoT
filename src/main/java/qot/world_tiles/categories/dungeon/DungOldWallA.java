package qot.world_tiles.categories.dungeon;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.world_tiles.TileIDs;

public class DungOldWallA extends WorldTile {
	
	public DungOldWallA() {
		super(TileIDs.DUNG_FLOOR_Aa);
		setSprite(new Sprite(DungeonWallTextures.dung_wall_b));
		setWall(true);
		this.wallHeight = 0.05;
		setMiniMapColor(0xff222222);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungOldWallA());
	}
	
}