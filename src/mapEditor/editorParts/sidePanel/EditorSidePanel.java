package mapEditor.editorParts.sidePanel;

import eutil.EUtil;
import main.Game;
import mapEditor.MapEditorScreen;
import mapEditor.editorParts.minimap.EditorMiniMap;
import mapEditor.editorParts.sidePanel.toolPanels.assetTool.AssetSidePanel;
import mapEditor.editorParts.sidePanel.toolPanels.regionTool.RegionSidePanel;
import mapEditor.editorParts.sidePanel.toolPanels.scriptTool.ScriptSidePanel;
import mapEditor.editorParts.sidePanel.toolPanels.terrainTool.TerrainSidePanel;
import renderUtil.EColors;
import storageUtil.EDimension;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IActionObject;

public class EditorSidePanel extends WindowObject {
	
	//-------
	// Tools
	//-------
	
	protected final TerrainSidePanel terrainTool;
	protected final AssetSidePanel assetTool;
	protected final RegionSidePanel regionTool;
	protected final ScriptSidePanel scriptTool;
	
	//--------
	// Fields
	//--------
	
	protected MapEditorScreen editor;
	protected EditorMiniMap miniMap;
	protected EDimension panelDims;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorSidePanel(MapEditorScreen in) {
		editor = in;
		
		int w = 251;
		int sx = Game.getWidth() - w;
		int y = (int) editor.getTopHeader().endY;
		setDimensions(sx, y, w, Game.getHeight() - y);
		
		miniMap = new EditorMiniMap(this);
		miniMap.setDimensions(startX + 5, startY + 5, width - 10, width - 10);
		int py = (int) miniMap.endY + 5;
		
		//apply panelDims
		panelDims = new EDimension(sx + 5, py, Game.getWidth() - 5, Game.getHeight() - 5);
		
		terrainTool = new TerrainSidePanel(this, in);
		assetTool = new AssetSidePanel(this, in);
		regionTool = new RegionSidePanel(this, in);
		scriptTool = new ScriptSidePanel(this, in);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black); //background & border
		drawRect(EColors.mgray, 1); //inner
		
		EUtil.nullDo(editor.getCurrentPanel(), p -> p.drawTool(mXIn, mYIn));
		
		super.drawObject(mXIn, mYIn);
		miniMap.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		EUtil.nullDo(editor.getCurrentPanel(), p -> p.onAction(object, args));
	}
	
	//---------
	// Getters
	//---------
	
	public MapEditorScreen getEditor() { return editor; }
	public EDimension getPanelDims() { return panelDims; }
	public EditorMiniMap getMiniMap() { return miniMap; }
	
	//---------
	// Setters
	//---------
	
	public void setActivePanel(SidePanelType toolIn) {
		//reveal if hidden
		setHidden(false);
		
		if (editor.getCurrentPanel() != null) {
			//remove current tool's objects
			windowObjects.clear();
		}
		
		//determine correct tool from type
		SidePanel theTool = null;
		switch (toolIn) {
		case TERRAIN: theTool = terrainTool; break;
		case ASSET: theTool = assetTool; break;
		case REGION: theTool = regionTool; break;
		case SCRIPTS: theTool = scriptTool; break;
		}
		
		//set the current tool to the new tool
		editor.setLastPanel(editor.getCurrentPanel());
		editor.setSidePanel(theTool);
		
		//load the currentTool
		editor.getCurrentPanel().loadTool();
	}
	
}
