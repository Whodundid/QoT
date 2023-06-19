package envision.engine.windows.windowTypes.interfaces;

import eutil.datatypes.boxes.Box2;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(double xIn, double yIn);
	public Box2<Double, Double> getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent<?> getScreenLocationGui();
	
}