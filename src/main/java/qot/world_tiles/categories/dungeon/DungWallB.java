package qot.world_tiles.categories.dungeon;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.walls.dungeon.DungeonWallTextures;
import qot.world_tiles.TileIDs;

public class DungWallB extends WorldTile {
	
	public DungWallB() {
		super(TileIDs.DUNG_WALL_B);
		setBlocksMovement(true);
		setWall(true);
		wallHeight = 0.5f;
		setSprite(new Sprite(DungeonWallTextures.dung_wall_m_a));
		setMiniMapColor(0xff203120);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungWallB());
	}
	
}