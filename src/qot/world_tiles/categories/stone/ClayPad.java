package qot.world_tiles.categories.stone;

import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class ClayPad extends WorldTile {
	
	public ClayPad() { this(-1); }
	public ClayPad(int id) {
		super(TileIDs.CLAY_PAD);
		setWall(true);
		wallHeight = 0.1;
		//rotation = Rotation.random();
		drawFlipped = ERandomUtil.randomBool();
		numVariants = StoneFloorTextures.clay_pad.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneFloorTextures.clay_pad.getRandVariant());
		}
		else {
			setTexture(StoneFloorTextures.clay_pad.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new ClayPad());
	}
	
}