package engine.windowLib.windowObjects.advancedObjects.header;

import engine.windowLib.windowTypes.WindowObject;

public class HeaderTab<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	private String tabName;
	private boolean selected;
	
	//---------
	// Getters
	//---------
	
	public String getTabName() { return tabName; }
	public boolean isSelected() { return selected; }
	
	//---------
	// Setters
	//---------
	
	public void setTabName(String nameIn) { tabName = nameIn; }
	public void setSelected(boolean val) { selected = val; }
	
}
