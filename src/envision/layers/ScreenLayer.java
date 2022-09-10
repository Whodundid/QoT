package envision.layers;

import envision.game.world.gameWorld.IGameWorld;
import envision.util.IDrawable;
import envision.util.InsertionSort;
import eutil.datatypes.EArrayList;

public class ScreenLayer {
	
	private EArrayList<IDrawable> objects = new EArrayList<>();
	
	public void renderLayer(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		InsertionSort.sort(objects);
		objects.forEach(o -> o.draw(world, x, y, w, h, brightness, mouseOver));
	}
	
}
