package envision.game.world.worldEditor.editorParts.topHeader;

import envision.Envision;
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
		
		IActionObject.setActionReceiver(this, testMap);
		
		addObject(testMap);
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX, startY + 2, endX - 2, endY - 2, EColors.pdgray);
		
		super.drawObject(dt, mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == testMap) testMap();
	}
	
	//================
	// Button Actions
	//================
	
	public void testMap() {
		editor.saveWorld();
		Envision.thePlayer = new QoT_Player();
		Envision.displayScreen(new GamePlayScreen());
		Envision.loadWorld(editor.getActualWorld());
	}
	
}
