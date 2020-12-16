package envisionEngine.eWindow.windowObjects.advancedObjects.dropDownList;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventFocus;
import java.util.Iterator;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class WindowDropDownList<E> extends WindowObject<E> {
	
	EArrayList<DropDownListEntry<E>> listContents = new EArrayList();
	DropDownListEntry<E> selectedEntry;
	double entryHeight = 17;
	boolean listOpen = false;
	boolean fixedWidth = false;
	boolean globalAction = false;
	boolean drawTop = true;
	boolean alwaysOpen = false;
	boolean drawHighlight = true;
	int selectedColor = EColors.steel.intVal;
	int borderColor = EColors.black.intVal;
	int backColor = EColors.dgray.intVal;
	
	public WindowDropDownList(IWindowObject parentIn, double x, double y) { init(parentIn, x, y, 75, entryHeight); }
	public WindowDropDownList(IWindowObject parentIn, double x, double y, double entryHeightIn) { this(parentIn, x, y, entryHeightIn, false); }
	public WindowDropDownList(IWindowObject parentIn, double x, double y, double entryHeightIn, boolean useGlobalAction) {
		init(parentIn, x, y, width, entryHeightIn);
		entryHeight = entryHeightIn;
		globalAction = useGlobalAction;
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(borderColor);
		//drawRect(backColor, 1);
		
		if (!hasFocus() && listOpen && !alwaysOpen) { closeList(); }
		
		if (selectedEntry != null) {
			//drawStringC(selectedEntry, startX + (width / 2), startY + (entryHeight / 3), selectedEntry.getColor());
		}
		
		if (isEnabled() && (alwaysOpen || listOpen)) {
			
			double offset = 0;
			
			if (drawTop) {
				drawRect(borderColor);
				drawRect(selectedColor, 1);
				drawStringC("...", midX, startY + (entryHeight / 4), EColors.lgray);
			}
			else { offset = -entryHeight; }
			
			for (int i = 0; i < listContents.size(); i++) {
				DropDownListEntry entry = listContents.get(i);
				double yPos = startY + (i * entryHeight) + entryHeight + offset;
				
				drawRect(startX, yPos, endX, yPos + entryHeight, borderColor);
				drawRect(startX + 1, yPos + 1, endX - 1, yPos + entryHeight, backColor);
				
				if (drawHighlight) {
					if (isMouseInside() && mY >= yPos && mY < yPos + entryHeight) {
						drawRect(startX + 2, yPos + 2, endX - 2, yPos + entryHeight - 1, 0x44adadad);
					}
				}
				
				drawString(entry, startX + 5, yPos + (entryHeight / 3), entry.getColor());
			}
			
			double endPos = startY + (listContents.size() * entryHeight) + entryHeight + offset;
			drawRect(startX, endPos, endX, endPos + 1, borderColor);
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {

		if (button == 0) {
			if (listOpen) {	selectListOption(); }
			else { openList(); }
		}
		
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			if (eventIn.getActionCode() == 0) {
				if (listOpen) {	selectListOption(); }
				else { openList(); }
			}
		}
	}
	
	@Override public void onFocusLost(EventFocus eventin) { closeList(); }
	
	protected void openList() {
		listOpen = true;
		double newHeight = entryHeight + (listContents.size() * entryHeight) - 1;
		setDimensions(startX, startY, width, newHeight);
	}
	
	protected void closeList() {
		listOpen = false;
		setDimensions(startX, startY, width, entryHeight);
	}
	
	protected void selectListOption() {
		if (isEnabled()) {
			
			double offset = drawTop ? entryHeight : 0;
			
			if (mY >= startY + offset) {
				int relClickPosY = (int) (mY - (startY + offset));
				int selectedPos = (int) (relClickPosY / entryHeight);
				selectedEntry = listContents.get(selectedPos);
				selectedEntry.runEntryAction();
				WindowButton.playPressSound();
				closeList();
			}
			else {
				closeList();
			}
		}
	}
	
	public WindowDropDownList<E> addEntry(String title) { return addEntry(title, EColors.lgray, null); }
	public WindowDropDownList<E> addEntry(String title, EColors colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn.intVal, objIn)); }
	public WindowDropDownList<E> addEntry(String title, int colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn, objIn)); }
	public WindowDropDownList<E> addEntry(DropDownListEntry<E> entryIn) {
		listContents.add(entryIn.setEntryID(listContents.size()).setParentList(this).setGlobalActionPresent(globalAction));
		if (listContents.size() == 1) { selectedEntry = entryIn; }
		adjustWidth();
		return this;
	}
	
	public synchronized WindowDropDownList<E> removeEntry(DropDownListEntry<E> entryIn) {
		Iterator<DropDownListEntry<E>> it = listContents.iterator();
		while (it.hasNext()) {
			if (it.next().equals(entryIn)) { it.remove(); break; }
		}
		if (listContents.size() == 0) { selectedEntry = null; }
		if (listContents.size() > 0) { selectedEntry = listContents.get(0); }
		if (!fixedWidth) { adjustWidth(); }
		return this;
	}
	
	public synchronized WindowDropDownList<E> removeEntry(int entryIdIn) {
		Iterator<DropDownListEntry<E>> it = listContents.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (i == entryIdIn) { it.remove(); break; }
			i++;
		}
		if (listContents.size() == 0) { selectedEntry = null; }
		if (listContents.size() > 0) { selectedEntry = listContents.get(0); }
		if (!fixedWidth) { adjustWidth(); }
		return this;
	}
	
	private void adjustWidth() {
		if (!fixedWidth) {
			
			String longestString = "";
			for (DropDownListEntry e : listContents) {
				String displayString = e.getText();
				if (displayString.length() > longestString.length()) { longestString = displayString; }
			}
			
			width = getStringWidth(longestString) + 10;
			double h = height;
			
			if (alwaysOpen) {
				h = startY + ((listContents.size() - 1) * entryHeight) - 2;
			}
			
			setDimensions(startX, startY, width, h);
		}
	}
	
	public DropDownListEntry<E> getHoveringEntry(int mXIn, int mYIn) {
		if (isMouseInside()) {
			
			double offset = drawTop ? entryHeight : 0;
			
			if (mY >= startY + offset) {
				int relClickPosY = (int) (mY - (startY + offset));
				int selectedPos = (int) (relClickPosY / entryHeight);
				if (selectedPos <= listContents.size() - 1) {
					return listContents.get(selectedPos);
				}
			}
			
		}
		return null;
	}
	
	public EArrayList<DropDownListEntry<E>> getEntries() { return listContents; }
	public DropDownListEntry<E> getSelectedEntry() { return selectedEntry; }
	public DropDownListEntry<E> getEntryFromObject(E objIn) {
		for (DropDownListEntry<E> e : listContents) { if (e.getEntryObject().equals(objIn)) { return e; } }
		return null;
	}
	
	public WindowDropDownList<E> setDrawHighlight(boolean val) { drawHighlight = val; return this; }
	public WindowDropDownList<E> setAlwaysOpen(boolean val) { alwaysOpen = val; if (val) { openList(); } return this; }
	public WindowDropDownList<E> setBorderColor(EColors colorIn) { borderColor = colorIn.intVal; return this; }
	public WindowDropDownList<E> setBackColor(EColors colorIn) { backColor = colorIn.intVal; return this; }
	public WindowDropDownList<E> setSelectedColor(EColors colorIn) { selectedColor = colorIn.intVal; return this; }
	public WindowDropDownList<E> setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowDropDownList<E> setBackColor(int colorIn) { backColor = colorIn; return this; }
	public WindowDropDownList<E> setSelectedColor(int colorIn) { selectedColor = colorIn; return this; }
	public WindowDropDownList<E> setDrawTop(boolean val) { drawTop = val; return this; }
	public WindowDropDownList<E> setFixedWidth(boolean val) { fixedWidth = val; return this; }
	public WindowDropDownList<E> setFixedWidth(int newWidth) { fixedWidth = true; setWidth(newWidth); return this; }
	public WindowDropDownList<E> setWidth(int widthIn) {  setDimensions(startX, startY, widthIn, height); return this; }
	public WindowDropDownList<E> setSelectedEntry(int entry) { selectedEntry = listContents.get(entry); return this; }
	public WindowDropDownList<E> setSelectedEntry(DropDownListEntry<E> entry) { return setSelectedEntry(entry, false); }
	
	public WindowDropDownList<E> setSelectedEntry(DropDownListEntry<E> entry, boolean addIfNotContains) {
		if (entry != null) {
			if (listContents.contains(entry)) { selectedEntry = entry; }
			else if (addIfNotContains) {
				addEntry(entry);
				selectedEntry = entry;
			}
		}
		return this;
	}
	
	public void runGlobalAction() {}
	
}
