package envision.game.world.worldEditor.editorParts.sidePanel;

import envision.Envision;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.minimap.EditorMiniMap;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.assetTool.AssetSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.regionTool.RegionSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.scriptTool.ScriptSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.terrainTool.TerrainSidePanel;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;

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
	protected Dimension_d panelDims;
	
	protected SidePanel currentPanel = null, lastPanel = null;
	
	//--------------
	// Constructors
	//--------------
	
	public EditorSidePanel(MapEditorScreen in) {
		editor = in;
		
		int w = 251;
		int sx = Envision.getWidth() - w;
		int y = (int) editor.getTopHeader().endY;
		setDimensions(sx, y, w, Envision.getHeight() - y);
		
		miniMap = new EditorMiniMap(this);
		miniMap.setDimensions(startX + 5, startY + 5, width - 10, width - 10);
		int py = (int) miniMap.endY + 5;
		
		//apply panelDims
		panelDims = new Dimension_d(sx + 5, py, Envision.getWidth() - 5, Envision.getHeight() - 5);
		
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
		addObject(miniMap);
	}
	
	@Override
	public void drawObject_i(float dt, int mXIn, int mYIn) {
		drawRect(EColors.black); //background & border
		drawRect(EColors.dgray, 1); //inner
		
		EUtil.nullDo(getCurrentPanel(), p -> p.drawTool(mXIn, mYIn));
		
		super.drawObject_i(dt, mXIn, mYIn);
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
	public Dimension_d getPanelDims() { return panelDims; }
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
		if (currentPanel != null) {
		    reInitChildren();
		    currentPanel.loadTool();
		}
	}
	
}
