package envision.layers;

import java.util.Iterator;

import envision.inputHandlers.Mouse;
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
	
	public void onRenderTick() {
		for (int i = 0; i < layers.size(); i++) {
			ScreenLayer l = layers.get(i);
			l.renderLayer(Mouse.getMx(), Mouse.getMy());
		}
	}
	
	//---------
	// Methods
	//---------
	
	public ScreenLayer pushLayer() {
		return layers.pushR(new ScreenLayer());
	}
	
	public ScreenLayer popLayer() {
		if (layers.isEmpty()) return null;
		return layers.pop();
	}
	
	public ScreenLayer insertLayerAtIndex(int index) {
		if (index < 0 || index >= layers.size()) return null;
		return layers.addR(index, new ScreenLayer());
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
	
	public boolean isEmpty() { return layers.isEmpty(); }
	public boolean isNotEmpty() { return layers.isNotEmpty(); }
	
}
