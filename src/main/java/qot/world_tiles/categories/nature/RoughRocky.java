package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.world_tiles.TileIDs;

public class RoughRocky extends WorldTile {
	
	public RoughRocky() {
		super(TileIDs.ROUGH_ROCKY);
		setSprite(new Sprite(RockTextures.rough_rocky));
		setBlocksMovement(true);
		setWall(true);
		
		randomizeValues();
		setMiniMapColor(0xff646443);
	}
	
	@Override
	public void randomizeValues() {
	    wallHeight = ERandomUtil.getRoll(0.25, 0.75);
	    rotation = Rotation.random();
	}
	
	@Override public boolean hasVariation() { return true; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new RoughRocky());
	}
	
}
