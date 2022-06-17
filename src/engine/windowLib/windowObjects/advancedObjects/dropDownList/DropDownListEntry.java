package engine.windowLib.windowObjects.advancedObjects.dropDownList;

import eutil.colors.EColors;

//Author: Hunter Bragg

public class DropDownListEntry<E> {
	
	protected int entryID = -1;
	protected int color = 0xffffffff;
	protected WindowDropDownList<E> parentList;
	protected String text = "";
	protected E entryObject;
	protected boolean visible = true;
	protected boolean enabled = true;
	protected boolean globalAction = false;
	
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
	
	public int getEntryID() { return entryID; }
	public WindowDropDownList<E> getParentList() { return parentList; }
	public String getText() { return text; }
	public int getColor() { return color; }
	public E getEntryObject() { return entryObject; }
	public boolean isVisible() { return visible; }
	public boolean isEnabled() { return enabled; }
	public boolean isThereGlobalAction() { return globalAction; }
	
	public DropDownListEntry<E> setEntryID(int idIn) { entryID = idIn; return this; }
	public DropDownListEntry<E> setParentList(WindowDropDownList<E> parentIn) { parentList = parentIn; return this; }
	public DropDownListEntry<E> setText(String textIn) { text = textIn; return this; }
	public DropDownListEntry<E> setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public DropDownListEntry<E> setColor(int colorIn) { setColor(colorIn); return this; }
	public DropDownListEntry<E> setEntryObject(E objectIn) { entryObject = objectIn; return this; }
	public DropDownListEntry<E> setVisibility(boolean val) { visible = val; return this; }
	public DropDownListEntry<E> setEnabled(boolean val) { enabled = val; return this; }
	public DropDownListEntry<E> setGlobalActionPresent(boolean val) { globalAction = val; return this; }
	
	public void runEntryAction() {
		if (globalAction) parentList.runGlobalAction();
	}
	
	@Override public String toString() { return text; }
	
}
