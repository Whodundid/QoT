package eWindow.windowTypes.interfaces;

import util.renderUtil.ScreenLocation;
import util.storageUtil.StorageBox;

//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(double xIn, double yIn);
	public StorageBox<Double, Double> getLocation();
	public ScreenLocation getScreenLocation();
	public IWindowParent getScreenLocationGui();
	
}
