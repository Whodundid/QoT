package windowLib.windowTypes.interfaces;

import eutil.misc.ScreenLocation;
import eutil.storage.Box2;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(double xIn, double yIn);
	public Box2<Double, Double> getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent<?> getScreenLocationGui();
	
}
