package envision.game.world.worldTiles;

import envision.game.entity.Sortable;

public class RenderedTile implements Sortable {

	public WorldTile tile;
	public double endY;
	
	public RenderedTile(WorldTile tileIn, double endYIn) {
		tile = tileIn;
		endY = endYIn;
	}
	
	@Override
	public double getYPos() {
		return endY;
	}
	
}