package envision.layers;

import eutil.datatypes.EArrayList;

public class LayerHandler {
	
	//------------------
	// Static Singleton
	//------------------
	
	private static final LayerHandler instance = new LayerHandler();
	public static LayerHandler getInstance() { return instance; }
	private LayerHandler() {}
	
	//--------
	// Fields
	//--------
	
	private final EArrayList<ScreenLayer> layers = new EArrayList<>();
	
	//--------
	// Render
	//--------
	
	public void onRenderTick() {
		for (int i = 0; i < layers.size(); i++) {
			ScreenLayer l = layers.get(i);
			l.renderLayer();
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
	
	//---------
	// Getters
	//---------
	
	public int getLayerNum() {
		return layers.size();
	}
	
}
