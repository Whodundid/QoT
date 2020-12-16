package gameScreens.mapEditor.editorScreen.topHeader;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.basicObjects.WindowLabel;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.EditorSelectionList;
import gameScreens.mapEditor.editorScreen.MapEditorScreen;
import gameScreens.mapEditor.editorScreen.RegionEditorMenu;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTool;
import gameScreens.mapEditor.editorScreen.windows.EditorOptionsWindow;
import main.Game;
import util.renderUtil.EColors;

public class EditorScreenTopHeader extends WindowObject {
	
	MapEditorScreen editor;
	WindowButton options, map, view, scripts, assets, regions;
	WindowLabel tileToolLabel;
	
	BrushSettings brushSettings;
	LineSettings lineSettings;
	ShapeSettings shapeSettings;
	
	EditorTool curTool = EditorTool.NONE;
	HeaderToolSettings curSettings = null;
	
	public EditorScreenTopHeader(MapEditorScreen editorIn) { this(editorIn, editorIn.getCurTileTool()); }
	public EditorScreenTopHeader(MapEditorScreen editorIn, EditorTool toolIn) {
		editor = editorIn;
		init(editor, 0, 0, Game.getWidth(), 60);
		curTool = toolIn;
	}
	
	@Override
	public void initObjects() {
		options = new WindowButton(this, startX + 3, startY + 3, 140, 28, "Options");
		map = new WindowButton(this, options.endX + 3, startY + 3, 140, 28, "Map");
		view = new WindowButton(this, map.endX + 3, startY + 3, 140, 28, "View");
		scripts = new WindowButton(this, view.endX + 3, startY + 3, 140, 28, "Scripts");
		assets = new WindowButton(this, scripts.endX + 3, startY + 3, 140, 28, "Assets");
		regions = new WindowButton(this, assets.endX + 3, startY + 3, 140, 28, "Regions");
		
		tileToolLabel = new WindowLabel(this, startX + 10, options.endY + 7, "" + curTool);
		
		brushSettings = new BrushSettings(this, tileToolLabel.endX + 12, options.endY + 4, (endY - 1) - (options.endY + 4));
		lineSettings = new LineSettings(this, tileToolLabel.endX + 12, options.endY + 4, (endY - 1) - (options.endY + 4));
		shapeSettings = new ShapeSettings(this, tileToolLabel.endX + 12, options.endY + 4, (endY - 1) - (options.endY + 4));
		
		addObject(options, map, view, scripts, assets, regions);
		addObject(tileToolLabel);
		
		addObject(brushSettings, lineSettings, shapeSettings);
		
		setVisible(false, brushSettings, lineSettings, shapeSettings);
		
		updateCurTool(curTool);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(startX, startY, endX, options.endY + 3, EColors.mgray);
		drawHorizontalLine(startX, endX, options.endY + 3, EColors.black);
		drawRect(startX, options.endY + 4, endX, endY - 1, EColors.pdgray);
		drawRect(startX, options.endY + 4, tileToolLabel.endX + 8, endY - 1, EColors.mc_darkaqua);
		drawRect(tileToolLabel.endX + 8, options.endY + 3, tileToolLabel.endX + 10, endY, EColors.black);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == options) { Game.displayWindow(new EditorOptionsWindow(editor)); }
		if (object == assets) { Game.displayWindow(new EditorSelectionList(editor)); }
		if (object == regions) { Game.displayWindow(new RegionEditorMenu(editor)); }
	}
	
	public void updateCurTool(EditorTool tool) {
		//hide the current tool's settings
		hideCur();
		
		curTool = tool;
		tileToolLabel.setString("" + curTool);
		
		//display the settings for the current tool (if there are any)
		curSettings = getSettingsForTool(tool);
		if (curSettings != null) {
			curTool = curSettings.getToolType();
			curSettings.setPosition(tileToolLabel.endX + 12, curSettings.startY);
			curSettings.setVisible(true);
		}
	}
	
	private void hideCur() {
		if (curSettings != null) { curSettings.setVisible(false); }
	}
	
	public HeaderToolSettings getSettingsForTool(EditorTool toolIn) {
		switch (toolIn) {
		case BRUSH: return brushSettings;
		case LINE: return lineSettings;
		case SHAPE: return shapeSettings;
		default: return null;
		}
	}
	
	public LineSettings getLineSettings() { return lineSettings; }
	public BrushSettings getBrushSettings() { return brushSettings; }
	public ShapeSettings getShapeSettigns() { return shapeSettings; }
	
	public double getToolSettingsEndX() {
		HeaderToolSettings s = getSettingsForTool(curTool);
		return (s != null) ? s.endX : tileToolLabel.endX + 12;
	}
	
}
