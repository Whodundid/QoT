package envision.windowLib.windowObjects.advancedObjects.dropDownList;

import eutil.colors.EColors;

//Author: Hunter Bragg

public class DropDownListEntry<E> {
	
	//--------
	// Fields
	//--------
	
	protected int entryID = -1;
	protected int color = 0xffffffff;
	protected WindowDropDownList<E> parentList;
	protected String text = "";
	protected E entryObject;
	protected boolean visible = true;
	protected boolean enabled = true;
	protected boolean globalAction = false;
	
	//--------------
	// Constructors
	//--------------
	
	DropDownListEntry(String textIn) { this(textIn, EColors.lgray.intVal, null, false); }
	DropDownListEntry(String textIn, EColors colorIn, E objectIn) { this(textIn, colorIn.intVal, objectIn, false); }
	DropDownListEntry(String textIn, int colorIn, E objectIn) { this(textIn, colorIn, objectIn, false); }
	DropDownListEntry(String textIn, EColors colorIn, E objectIn, boolean hasActionIn) { this(textIn, colorIn.intVal, objectIn, hasActionIn); }
	DropDownListEntry(String textIn, int colorIn, E objectIn, boolean hasActionIn) {
		text = textIn;
		color = colorIn;
		entryObject = objectIn;
		globalAction = hasActionIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return text; }
	
	//---------
	// Methods
	//---------
	
	public void runEntryAction() {
		if (globalAction) parentList.runGlobalAction();
	}
	
	//---------
	// Getters
	//---------
	
	public int getEntryID() { return entryID; }
	public WindowDropDownList<E> getParentList() { return parentList; }
	public String getText() { return text; }
	public int getColor() { return color; }
	public E getEntryObject() { return entryObject; }
	public boolean isVisible() { return visible; }
	public boolean isEnabled() { return enabled; }
	public boolean isThereGlobalAction() { return globalAction; }
	
	//---------
	// Setters
	//---------
	
	public void setEntryID(int idIn) { entryID = idIn; }
	public void setParentList(WindowDropDownList<E> parentIn) { parentList = parentIn; }
	public void setText(String textIn) { text = textIn; }
	public void setColor(EColors colorIn) { setColor(colorIn.intVal); }
	public void setColor(int colorIn) { setColor(colorIn); }
	public void setEntryObject(E objectIn) { entryObject = objectIn; }
	public void setVisibility(boolean val) { visible = val; }
	public void setEnabled(boolean val) { enabled = val; }
	public void setGlobalActionPresent(boolean val) { globalAction = val; }
	
}
