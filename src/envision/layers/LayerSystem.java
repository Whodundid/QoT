package envision.layers;

import java.util.Iterator;

import envision.game.world.gameWorld.IGameWorld;
import eutil.datatypes.EArrayList;

public class LayerSystem implements Iterable<ScreenLayer> {
	
	//------------------
	// Static Singleton
	//------------------
	
	//private static final LayerHandler instance = new LayerHandler();
	//public static LayerHandler getInstance() { return instance; }
	//private LayerHandler() {}
	
	//--------
	// Fields
	//--------
	
	private final EArrayList<ScreenLayer> layers = new EArrayList<>();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public Iterator<ScreenLayer> iterator() {
		return layers.iterator();
	}
	
	//--------
	// Render
	//--------
	
	public void onRenderTick(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		for (int i = 0; i < layers.size(); i++) {
			ScreenLayer l = layers.get(i);
			l.renderLayer(world, x, y, w, h, brightness, mouseOver);
		}
	}
	
	//---------
	// Methods
	//---------
	
	public void pushLayer() {
		layers.push(new ScreenLayer());
	}
	
	public void popLayer() {
		if (layers.isEmpty()) return;
		layers.pop();
	}
	
	public void insertLayerAtIndex(int index) {
		if (index < 0 || index >= layers.size()) return;
		layers.add(index, new ScreenLayer());
	}
	
	public void setNumLayer(int num) {
		for (int i = 0; i < num; i++) {
			pushLayer();
		}
	}
	
	//---------
	// Getters
	//---------
	
	public int getLayerNum() {
		return layers.size();
	}
	
	public ScreenLayer getLayerAt(int index) {
		return layers.get(index);
	}
	
}
