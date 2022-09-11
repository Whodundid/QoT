package envision.gameEngine.world.worldEditor.editorParts.sidePanel;

import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.gameEngine.world.worldEditor.editorParts.minimap.EditorMiniMap;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.assetTool.AssetSidePanel;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.regionTool.RegionSidePanel;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.scriptTool.ScriptSidePanel;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.terrainTool.TerrainSidePanel;
import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.math.EDimension;
import game.QoT;

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
	
	protected SidePanel currentPanel = null, lastPanel = null;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorSidePanel(MapEditorScreen in) {
		editor = in;
		
		int w = 251;
		int sx = QoT.getWidth() - w;
		int y = (int) editor.getTopHeader().endY;
		setDimensions(sx, y, w, QoT.getHeight() - y);
		
		miniMap = new EditorMiniMap(this);
		miniMap.setDimensions(startX + 5, startY + 5, width - 10, width - 10);
		int py = (int) miniMap.endY + 5;
		
		//apply panelDims
		panelDims = new EDimension(sx + 5, py, QoT.getWidth() - 5, QoT.getHeight() - 5);
		
		terrainTool = new TerrainSidePanel(this, in);
		assetTool = new AssetSidePanel(this, in);
		regionTool = new RegionSidePanel(this, in);
		scriptTool = new ScriptSidePanel(this, in);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black); //background & border
		drawRect(EColors.mgray, 1); //inner
		
		EUtil.nullDo(getCurrentPanel(), p -> p.drawTool(mXIn, mYIn));
		
		super.drawObject(mXIn, mYIn);
		miniMap.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		EUtil.nullDo(getCurrentPanel(), p -> p.onAction(object, args));
	}
	
	//---------
	// Methods
	//---------
	
	
	
	//---------
	// Getters
	//---------
	
	public SidePanelType getCurrentPanelType() { return (currentPanel != null) ? currentPanel.type : SidePanelType.NONE; }
	public MapEditorScreen getEditor() { return editor; }
	public EDimension getPanelDims() { return panelDims; }
	public EditorMiniMap getMiniMap() { return miniMap; }
	
	//---------
	// Setters
	//---------
	
	public SidePanel getCurrentPanel() { return currentPanel; }
	public EditorSidePanel setCurrentPanel(SidePanelType type) { return setCurrentPanel(type, true); }
	public EditorSidePanel setCurrentPanel(SidePanelType type, boolean updateTop) {
		setActivePanel(type);
		if (updateTop) editor.getTopHeader().updateTool();
		return this;
	}
	
	private void setActivePanel(SidePanelType toolIn) {
		//reveal if hidden
		setHidden(false);
		
		if (currentPanel != null) {
			//remove current tool's objects
			getChildren().clear();
		}
		
		//determine correct tool from type
		SidePanel theTool = null;
		switch (toolIn) {
		case TERRAIN: theTool = terrainTool; break;
		case ASSET: theTool = assetTool; break;
		case REGION: theTool = regionTool; break;
		case SCRIPTS: theTool = scriptTool; break;
		default: theTool = terrainTool;
		}
		
		//set the current tool to the new tool
		lastPanel = currentPanel;
		currentPanel = theTool;
		
		//load the currentTool (if not null)
		if (currentPanel != null) currentPanel.loadTool();
	}
	
}
