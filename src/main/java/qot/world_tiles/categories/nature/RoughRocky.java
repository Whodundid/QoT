package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.world_tiles.TileIDs;

public class RoughRocky extends WorldTile {
	
	public RoughRocky() {
		super(TileIDs.ROUGH_ROCKY);
		setSprite(new Sprite(RockTextures.rough_rocky));
		setBlocksMovement(true);
		setWall(true);
		minWallHeight = 0.25f;
        maxWallHeight = 0.75f;
        randomizeRotation = true;
        randomizeWallHeight = true;
		
		randomizeValues();
		setMiniMapColor(0xff646443);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new RoughRocky());
	}
	
}
