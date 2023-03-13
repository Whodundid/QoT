package envision.game.world.worldTiles.categories.stone;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;

public class StonePaver extends WorldTile {
	
	public StonePaver() { this(-1); }
	public StonePaver(int id) {
		super(TileIDs.STONE_PAVER);
		//rotation = Rotation.random();
		drawFlipped = ERandomUtil.randomBool();
		numVariants = StoneFloorTextures.stone_paver.getChildren().size();
		
		if (id < 0) {
			setTexture(StoneFloorTextures.stone_paver.getRandVariant());
		}
		else {
			setTexture(StoneFloorTextures.stone_paver.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new StonePaver());
	}
	
}