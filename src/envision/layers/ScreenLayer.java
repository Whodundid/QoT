package envision.layers;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.EArrayList;

public class ScreenLayer {
	
	private EArrayList<IWindowObject<?>> objects = new EArrayList<>();
	
	public <E extends IWindowObject<?>> boolean add(E object) { return objects.add(object); }
	public <E extends IWindowObject<?>> void add(int index, E object) { objects.add(index, object); }
	public <E extends IWindowObject<?>> E addR(E object) { objects.add(object); return object; }
	public <E extends IWindowObject<?>> E addR(int index, E object) { objects.add(index, object); return object; }
	public <E extends IWindowObject<?>> ScreenLayer addRT(E object) { objects.add(object); return this; }
	public <E extends IWindowObject<?>> ScreenLayer addRT(int index, E object) { objects.add(index, object); return this; }
	
	public void clear() { objects.clear(); }
	
	public EArrayList<IWindowObject<?>> getObjects() { return objects; }
	
	public void renderLayer(int mXIn, int mYIn) {
		objects.forEach(o -> o.drawObject(mXIn, mYIn));
	}
	
}
