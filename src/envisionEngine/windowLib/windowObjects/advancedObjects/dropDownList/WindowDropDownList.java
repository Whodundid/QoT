package envisionEngine.windowLib.windowObjects.advancedObjects.dropDownList;

import envisionEngine.inputHandlers.Mouse;
import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;
import envisionEngine.windowLib.windowTypes.WindowObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import envisionEngine.windowLib.windowUtil.windowEvents.events.EventFocus;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

public class WindowDropDownList<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	private EArrayList<DropDownListEntry<E>> listContents = new EArrayList();
	private DropDownListEntry<E> selectedEntry;
	private double entryHeight = 17;
	private boolean listOpen = false;
	private boolean fixedWidth = false;
	private boolean globalAction = false;
	private boolean drawTop = true;
	private boolean alwaysOpen = false;
	private boolean drawHighlight = true;
	private int selectedColor = EColors.steel.intVal;
	private int borderColor = EColors.black.intVal;
	private int backColor = EColors.dgray.intVal;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowDropDownList(IWindowObject<?> parentIn, double x, double y) { init(parentIn, x, y, 75, entryHeight); }
	public WindowDropDownList(IWindowObject<?> parentIn, double x, double y, double entryHeightIn) { this(parentIn, x, y, entryHeightIn, false); }
	public WindowDropDownList(IWindowObject<?> parentIn, double x, double y, double entryHeightIn, boolean useGlobalAction) {
		init(parentIn, x, y, width, entryHeightIn);
		entryHeight = entryHeightIn;
		globalAction = useGlobalAction;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(borderColor);
		//drawRect(backColor, 1);
		
		if (!hasFocus() && listOpen && !alwaysOpen) closeList();
		
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
			else offset = -entryHeight;
			
			for (int i = 0; i < listContents.size(); i++) {
				var entry = listContents.get(i);
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
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (button == 0) {
			if (listOpen)	selectListOption();
			else openList();
		}
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MOUSE_PRESS)) {
			if (eventIn.getActionCode() == 0) {
				if (listOpen) selectListOption();
				else openList();
			}
		}
	}
	
	@Override
	public void onFocusLost(EventFocus eventin) {
		closeList();
	}
	
	//---------
	// Methods
	//---------
	
	public void runGlobalAction() {}
	
	public WindowDropDownList<E> addEntry(String title) { return addEntry(title, EColors.lgray, null); }
	public WindowDropDownList<E> addEntry(String title, EColors colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn.intVal, objIn)); }
	public WindowDropDownList<E> addEntry(String title, int colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn, objIn)); }
	public WindowDropDownList<E> addEntry(DropDownListEntry<E> entryIn) {
		entryIn.setEntryID(listContents.size());
		entryIn.setParentList(this);
		entryIn.setGlobalActionPresent(globalAction);
		
		listContents.add(entryIn);
		if (listContents.size() == 1) selectedEntry = entryIn;
		adjustWidth();
		return this;
	}
	
	public synchronized void removeEntry(DropDownListEntry<E> entryIn) {
		var it = listContents.iterator();
		while (it.hasNext()) {
			if (it.next().equals(entryIn)) {
				it.remove();
				break;
			}
		}
		
		if (listContents.size() == 0) selectedEntry = null;
		if (listContents.size() > 0) selectedEntry = listContents.get(0);
		if (!fixedWidth) adjustWidth();
	}
	
	public synchronized void removeEntry(int entryIdIn) {
		var it = listContents.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (i == entryIdIn) { it.remove(); break; }
			i++;
		}
		
		if (listContents.size() == 0) selectedEntry = null;
		if (listContents.size() > 0) selectedEntry = listContents.get(0);
		if (!fixedWidth) adjustWidth();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
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
			
			if (Mouse.getMy() >= startY + offset) {
				int relClickPosY = (int) (Mouse.getMy() - (startY + offset));
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
	
	//---------
	// Getters
	//---------
	
	public DropDownListEntry<E> getHoveringEntry(int mXIn, int mYIn) {
		if (isMouseInside()) {
			
			double offset = drawTop ? entryHeight : 0;
			
			if (Mouse.getMy() >= startY + offset) {
				int relClickPosY = (int) (Mouse.getMy() - (startY + offset));
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
		for (var e : listContents) {
			if (e.getEntryObject().equals(objIn)) return e;
		}
		return null;
	}
	
	//---------
	// Setters
	//---------
	
	public void setDrawHighlight(boolean val) { drawHighlight = val; }
	public void setAlwaysOpen(boolean val) { alwaysOpen = val; if (val) { openList(); } }
	public void setBorderColor(EColors colorIn) { borderColor = colorIn.intVal; }
	public void setBackColor(EColors colorIn) { backColor = colorIn.intVal; }
	public void setSelectedColor(EColors colorIn) { selectedColor = colorIn.intVal; }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setBackColor(int colorIn) { backColor = colorIn; }
	public void setSelectedColor(int colorIn) { selectedColor = colorIn; }
	public void setDrawTop(boolean val) { drawTop = val; }
	public void setFixedWidth(boolean val) { fixedWidth = val; }
	public void setFixedWidth(int newWidth) { fixedWidth = true; setWidth(newWidth); }
	public void setWidth(int widthIn) {  setDimensions(startX, startY, widthIn, height); }
	public void setSelectedEntry(int entry) { selectedEntry = listContents.get(entry); }
	public void setSelectedEntry(DropDownListEntry<E> entry) { setSelectedEntry(entry, false); }
	
	public void setSelectedEntry(DropDownListEntry<E> entry, boolean addIfNotContains) {
		if (entry != null) {
			if (listContents.contains(entry)) { selectedEntry = entry; }
			else if (addIfNotContains) {
				addEntry(entry);
				selectedEntry = entry;
			}
		}
	}
	
}
