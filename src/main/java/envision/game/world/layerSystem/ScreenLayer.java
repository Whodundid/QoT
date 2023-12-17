package envision.game.world.layerSystem;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.util.EList;

public class ScreenLayer {
	
	private EList<IWindowObject> objects = EList.newList();
	
	public <E extends IWindowObject> boolean add(E object) { return objects.add(object); }
	public <E extends IWindowObject> void add(int index, E object) { objects.add(index, object); }
	public <E extends IWindowObject> E addR(E object) { objects.add(object); return object; }
	public <E extends IWindowObject> E addR(int index, E object) { objects.add(index, object); return object; }
	public <E extends IWindowObject> ScreenLayer addRT(E object) { objects.add(object); return this; }
	public <E extends IWindowObject> ScreenLayer addRT(int index, E object) { objects.add(index, object); return this; }
	
	public void clear() { objects.clear(); }
	
	public EList<IWindowObject> getObjects() { return objects; }
	
	public void renderLayer(long dt, int mXIn, int mYIn) {
		objects.forEach(o -> o.drawObject_i(dt, mXIn, mYIn));
	}
	
}
