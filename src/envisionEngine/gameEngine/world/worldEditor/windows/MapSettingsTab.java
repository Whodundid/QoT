package envision.gameEngine.world.worldEditor.windows;

import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.advancedObjects.WindowScrollList;
import envision.windowLib.windowObjects.advancedObjects.tabbedContainer.ContainerTab;
import envision.windowLib.windowObjects.basicObjects.WindowLabel;
import envision.windowLib.windowObjects.basicObjects.WindowTextBox;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.misc.Direction;
import game.assets.textures.window.WindowTextures;

public class MapSettingsTab extends ContainerTab {

	//--------
	// Fields
	//--------
	
	private MapEditorScreen editor;
	
	private WindowScrollList list;
	
	private WindowLabel mapWidth, mapHeight;
	private WindowButton incWidth, decWidth;
	private WindowButton incHeight, decHeight;
	private WindowTextBox widthBox, heightBox;
	
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
		
		mapWidth = new WindowLabel(list, 20, 20, "Map Width: " + editor.getEditorWorld().getWidth());
		incWidth = new WindowButton(list, mapWidth.startX, mapWidth.startY + 30, 50, 50);
		decWidth = new WindowButton(list, incWidth.endX + 6, mapWidth.startY + 30, 50, 50);
		
		incWidth.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		decWidth.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		
		mapHeight = new WindowLabel(list, 20, incWidth.endY + 20, "Map Height: " + editor.getEditorWorld().getHeight());
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
		editor.getEditorWorld().expandWorld(Direction.E, w);
		editor.getEditorWorld().expandWorld(Direction.S, h);
		
		mapWidth.setString("Map Width: " + editor.getEditorWorld().getWidth());
		mapHeight.setString("Map Height: " + editor.getEditorWorld().getHeight());
		//var world = editor.getActualWorld();
		//changeMapSize(world.getWidth() + w, world.getHeight() + h);
	}
	
	private void changeMapSize(int w, int h) {
		//System.out.println(w + " : " + h);
	}
	
}
