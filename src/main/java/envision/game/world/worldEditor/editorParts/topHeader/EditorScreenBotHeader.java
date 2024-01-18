package envision.game.world.worldEditor.editorParts.topHeader;

import envision.Envision;
import envision.engine.windows.windowObjects.actionObjects.WindowToggleSetting;
import envision.engine.assets.WindowTextures;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import eutil.colors.EColors;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;

public class EditorScreenBotHeader extends WindowObject {
	
	private MapEditorScreen editor;
	private WindowButton testMap;
	private WindowToggleSetting toggleTileAlign;
	private WindowButton moveUpLayer, moveDownLayer;
	private WindowButton addLayerAbove, addLayerBelow;
	private WindowButton deleteLayer;
	public double headerHeight = 40.0;
	
	public EditorScreenBotHeader(MapEditorScreen editorIn) {
		editor = editorIn;
		
		var dims = editor.getMapDrawDims();
		var sideDims = editor.getSidePanel().getDimensions();
		
		var sx = dims.startX;
		var sy = dims.endY - headerHeight;
		var w = sideDims.startX - dims.startX;
		var h = headerHeight;
		
		init(editorIn, sx, sy, w, h);
	}
	
	//===========================
	// Overrides : IWindowObject
	//===========================
	
	@Override
	public void initChildren() {
		var sx = startX;
		var sy = startY;
		var bb = 5; // button border
		var bb2 = bb * 2;
		
		testMap = new WindowButton(this, sx + bb, sy + bb, 150, headerHeight - bb2, "Test Map");
		
		toggleTileAlign = new WindowToggleSetting();
		toggleTileAlign.setPosition(testMap.endX + 5, sy + bb);
		toggleTileAlign.setSize(310, headerHeight - bb2);
		toggleTileAlign.setString("Lock to Tile Grid");
		toggleTileAlign.updateState(editor.getSettings().lockToTileGrid);
		
		double gap = 4;
		double x = toggleTileAlign.endX + 20;
		double h = headerHeight - gap * 2;
		double w = h;
		sy = startY + gap;
		addLayerBelow = new WindowButton(this, x, sy, w, h);
		moveDownLayer = new WindowButton(this, addLayerBelow.endX + gap, sy, w, h);
		moveUpLayer = new WindowButton(this, moveDownLayer.endX + gap, sy, w, h);
		addLayerAbove = new WindowButton(this, moveUpLayer.endX + gap, sy, w, h);
		deleteLayer = new WindowButton(this, addLayerAbove.endX + gap, sy, w, h);
		
		addLayerBelow.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		moveUpLayer.setTextures(WindowTextures.file_up, WindowTextures.file_up_sel);
		moveDownLayer.setTextures(WindowTextures.arrow_down, WindowTextures.arrow_down_sel);
		addLayerAbove.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		deleteLayer.setTextures(WindowTextures.close, WindowTextures.close_sel);
		
		addLayerBelow.setHoverText("Add Layer Below Current");
		moveDownLayer.setHoverText("Move Down Layer");
		moveUpLayer.setHoverText("Move Up Layer");
		addLayerAbove.setHoverText("Add Layer Above Current");
		deleteLayer.setHoverText("Delete Current Layer");
		
		IActionObject.setActionReceiver(this, testMap, toggleTileAlign);
		IActionObject.setActionReceiver(this, addLayerBelow, moveUpLayer, moveDownLayer, addLayerAbove, deleteLayer);
		
		addObject(testMap, toggleTileAlign);
		addObject(addLayerBelow, moveUpLayer, moveDownLayer, addLayerAbove, deleteLayer);
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX, startY + 2, endX - 2, endY - 2, EColors.pdgray);
		
		double ttax = toggleTileAlign.endX + 10;
		drawRect(ttax, startY, ttax + 2, endY, EColors.vdgray);
		
		if (editor.camera != null) {
		    double x = deleteLayer.endX + 5;
		    double y = deleteLayer.midY - FONT_MID_Y;
		    drawString("Layer: " + editor.camera.getCurrentLayer(), x, y, EColors.yellow);		    
		}
		
		super.drawObject(dt, mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == testMap) testMap();
		if (object == toggleTileAlign) editor.getSettings().lockToTileGrid = !editor.getSettings().lockToTileGrid;
		
		if (object == addLayerBelow) {
		    editor.addWorldLayerBelow();
		    editor.moveDownLayer();
		}
		
		if (object == moveDownLayer) {
		    editor.moveDownLayer();
		}
		
		if (object == moveUpLayer) {
		    editor.moveUpLayer();
		}
		
		if (object == addLayerAbove) {
		    editor.addWorldLayerAbove();
		    editor.moveUpLayer();
		}
		
		if (object == deleteLayer) {
		    editor.removeCurrentLayer();
		}
	}
	
	//=========
    // Methods
    //=========
	
	public void update() {
	    toggleTileAlign.updateState(editor.getSettings().lockToTileGrid);
	}
	
	//================
	// Button Actions
	//================
	
	public void testMap() {
		editor.saveWorld();
		Envision.thePlayer = new QoT_Player();
		Envision.displayScreen(new GamePlayScreen());
		Envision.loadLevel(editor.getActualWorld());
	}
	
}
