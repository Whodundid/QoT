package world.mapEditor.editorParts.sidePanel.toolPanels.terrainTool;

import static world.mapEditor.editorTools.EditorToolType.*;

import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import world.mapEditor.editorParts.sidePanel.PaletteSidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanelType;
import world.mapEditor.editorParts.toolBox.ToolCategory;
import world.mapEditor.editorParts.util.EditorItem;
import world.resources.GlobalTileList;
import world.resources.WorldTile;

public class TerrainSidePanel extends PaletteSidePanel {
	
	public static final ToolCategory terrainTools = ToolCategory.from("Terrain", MOVE, MAGICWAND,
			  														  PAINTBUCKET, BRUSH, PENCIL,
			  														  EYEDROPPER, ERASER, LINE, SHAPE);
	private EArrayList<WindowButton<WorldTile>> buttons = new EArrayList();
	
	public TerrainSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.TERRAIN);
	}

	@Override
	public void loadTool() {
		editor.getToolBox().setToolsWithSelector(terrainTools);
		buildTiles();
		if (buttons.size() >= 1) editor.getSettings().setPrimaryPalette(EditorItem.of(buttons.get(0).getGenericObject()));
		if (buttons.size() >= 2) editor.getSettings().setSecondaryPalette(EditorItem.of(buttons.get(1).getGenericObject()));
	}
	
	private void buildTiles() {
		buttons.clear();
		//double curY = startY + 5;
		int bw = 40;
		int rowWidth = ((int) width - 10) / bw;
		
		for (WorldTile t : GlobalTileList.getTiles()) {
			WindowButton<WorldTile> b = new WindowButton(panel) {
				@Override
				public void drawObject(int mXIn, int mYIn) {
					super.drawObject(mXIn, mYIn);
					drawHRect(EColors.black);
				}
			};
			b.setGenericObject(t);
			b.setAcceptRightClicks(true);
			buttons.add(b);
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			WindowButton<WorldTile> b = buttons.get(i);
			WorldTile tile = b.getGenericObject();
			
			int xPos = i % rowWidth;
			int yPos = i / rowWidth;
			
			//using 'pey' and 'psx' from PaletteSidePanel to get the end location after the palette icons
			double by = pey + 17 + (bw * yPos) + (yPos * 2);
			double bx = psx + (bw * xPos) + (xPos * 2);
			
			b.setButtonTexture(tile.getTexture());
			b.setHoverText(tile.getName());
			
			//add to list first then dimension
			panel.addObject(b);
			b.setDimensions(bx, by, bw, bw);
		}
	}

	@Override
	public void drawTool(int mXIn, int mYIn) {
		super.drawTool(mXIn, mYIn);
	}

	@Override
	public void onAction(IActionObject object, Object... args) {
		if (object instanceof WindowButton b && b.getGenericObject() instanceof WorldTile tile) {
			int button = b.getPressedButton();
			
			if (button == 0) editor.getSettings().setPrimaryPalette(EditorItem.of(tile));
			else editor.getSettings().setSecondaryPalette(EditorItem.of(tile));
		}
	}
	
}
