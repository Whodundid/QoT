package eWindow.windowObjects.utilityObjects;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowObjects.advancedObjects.scrollList.WindowScrollList;
import eWindow.windowObjects.basicObjects.WindowLabel;
import util.renderUtil.EColors;

public class SettingCategoryContainer {

	WindowScrollList parentList;
	String name = "noname";
	int color = 0xffffff;
	
	public SettingCategoryContainer(WindowScrollList parentListIn, String categoryName, EColors categoryColor) { this(parentListIn, categoryName, categoryColor.intVal); }
	public SettingCategoryContainer(WindowScrollList parentListIn, String categoryName, int categoryColor) {
		parentList = parentListIn;
	}
	
	public SettingCategoryContainer addSetting() {
		
		
		return this;
	}
	
	public int getY() {
		return -1;
	}
	
	private class SettingContainer {
		
		WindowButton button;
		WindowLabel label;
		
		public SettingContainer() {
			
		}
	}
	
}
