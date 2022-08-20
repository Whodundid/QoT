package world.mapEditor.windows;

import assets.textures.window.WindowTextures;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.advancedObjects.WindowScrollList;
import engine.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import engine.windowLib.windowObjects.basicObjects.WindowLabel;
import engine.windowLib.windowObjects.basicObjects.WindowTextBox;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import world.mapEditor.MapEditorScreen;

public class MapSettingsTab extends ContainerTab {

	//--------
	// Fields
	//--------
	
	MapEditorScreen editor;
	
	WindowScrollList list;
	
	WindowLabel mapWidth, mapHeight;
	WindowButton incWidth, decWidth;
	WindowButton incHeight, decHeight;
	WindowTextBox widthBox, heightBox;
	
	//--------------
	// Constructors
	//--------------
	
	public MapSettingsTab(EditorTabs parent, MapEditorScreen editorIn) {
		super(parent, "Map");
		editor = editorIn;
		tabTextColor = EColors.seafoam.intVal;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		var dims = getTabDims();
		
		list = new WindowScrollList(this, dims.startX, dims.startY, dims.width, dims.height);
		list.setBackgroundColor(EColors.pdgray);
		
		mapWidth = new WindowLabel(list, 20, 20, "Map Width: " + editor.drawDistX);
		incWidth = new WindowButton(list, mapWidth.startX, mapWidth.startY + 30, 50, 50);
		decWidth = new WindowButton(list, incWidth.endX + 6, mapWidth.startY + 30, 50, 50);
		
		incWidth.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decWidth.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		mapHeight = new WindowLabel(list, 20, incWidth.endY + 20, "Map Height: " + editor.drawDistY);
		incHeight = new WindowButton(list, mapHeight.startX, mapHeight.startY + 30, 50, 50);
		decHeight = new WindowButton(list, incHeight.endX + 6, mapHeight.startY + 30, 50, 50);
		
		incHeight.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decHeight.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		list.addObjectToList(mapWidth, incWidth, decWidth);
		list.addObjectToList(mapHeight, incHeight, decHeight);
		
		IActionObject.setActionReceiver(this, incWidth, decWidth, incHeight, decHeight);
		
		list.fitItemsInList();
		
		addObject(list);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
		
		if (object == incWidth) adjustMapSize(1, 0);
		if (object == incHeight) adjustMapSize(0, 1);
		if (object == decWidth) adjustMapSize(-1, 0);
		if (object == decHeight) adjustMapSize(0, -1);
	}
	
	private void adjustMapSize(int w, int h) {
		var world = editor.getWorld();
		changeMapSize(world.getWidth() + w, world.getHeight() + h);
	}
	
	private void changeMapSize(int w, int h) {
		
	}
	
}
