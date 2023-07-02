package envision.engine.windows.windowObjects.advancedObjects.header;

import envision.engine.windows.windowTypes.WindowObject;

public class HeaderTab extends WindowObject {
	
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
