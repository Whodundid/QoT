package gameScreens.mapEditor.editorScreen;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.advancedObjects.scrollList.WindowScrollList;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.EventType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.MouseType;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventMouse;
import gameScreens.mapEditor.editorScreen.util.EditorItem;
import gameScreens.mapEditor.editorScreen.util.EditorItemCategory;
import main.Game;
import renderUtil.EColors;
import storageUtil.EArrayList;

public class EditorSelectionList extends WindowParent {
	
	MapEditorScreen editor;
	EditorHotbar hotbar;

	WindowScrollList scrollList;
	WindowButton clearHotbar;
	
	EArrayList<EditorItem> list = new EArrayList();
	EditorItemCategory curCat;
	
	boolean holdingItem = false;
	EditorItem curItem = null;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorSelectionList(MapEditorScreen editorIn) { this(editorIn, EditorItemCategory.TILE); }
	public EditorSelectionList(MapEditorScreen editorIn, EditorItemCategory catIn) {
		editor = editorIn;
		curCat = catIn;
		hotbar = editor.getHotbar();
	}
	
	@Override
	public void initWindow() {
		double w = Game.getWidth() / 4;
		double h = Game.getHeight() / 4;
		setDimensions(Game.getWidth() / 2 - w / 2, Game.getHeight() / 2 - h / 2, w, h);
		setMaximizable(true);
		setResizeable(true);
		setMinimizable(true);
		
		//we'll need to listen in on the screen's mouse events
		getTopParent().registerListener(this);
		
		setObjectName("Game Assets");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		double listH = height - ((isMaximized()) ? 50 : 10);
		scrollList = new WindowScrollList(this, startX + 5, startY + 5, width - 10, listH);
		
		addObject(scrollList);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.mgray, 1);
		
		//if the window is maximized, the normal hotbar would be hidden.
		//As such, this window will manually draw it's own visual of the hotbar if needed
		if (isMaximized()) {
			drawHotbar();
		}
		
		drawList();
		drawCurPos();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == clearHotbar) { hotbar.clear(); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	/** If holding an item to place into the hotbar, use this to know where exactly the item is being placed. */
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		tryPlace();
		clearItem();
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void onClosed() {
		//before this window dies we'll need to unregister it from the screen's listeners
		getTopParent().unregisterListener(this);
	}
	
	/** Listen in on the screen's MouseReleased events. */
	@Override
	public void onEvent(ObjectEvent e) {
		if (e.getEventType() == EventType.MOUSE) {
			EventMouse event = (EventMouse) e;
			if (event.getMouseType() == MouseType.RELEASED) {
				tryPlace();
				clearItem();
			}
		}
	}
	
	//----------
	// Builders
	//----------
	
	private void buildList() {
		switch (curCat) {
		case TILE -> buildTileList();
		case ENTITY -> buildEntityList();
		}
	}
	
	private void buildTileList() {
		
	}
	
	private void buildEntityList() {
		
	}
	
	//---------
	// Drawers
	//---------
	
	/** Will draw the hotbar if the window has been maximized. */
	private void drawHotbar() {
		
	}
	
	private void drawList() {
		
	}
	
	/** Highlights the cell currently being hovered over. */
	private void drawCurPos() {
		
	}
	
	//--------------
	// Util Methods
	//--------------
	
	private void tryPlace() {
		if (holdingItem && curItem != null) {
			
		}
	}
	
	private void clearItem() {
		holdingItem = false;
		curItem = null;
	}
	
}
