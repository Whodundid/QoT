package envision.engine.windows.windowTypes.interfaces;

import eutil.datatypes.points.Point2d;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(double xIn, double yIn);
	public Point2d getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent getScreenLocationGui();
	
}
