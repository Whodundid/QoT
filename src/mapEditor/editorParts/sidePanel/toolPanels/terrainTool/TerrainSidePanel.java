package mapEditor.editorParts.sidePanel.toolPanels.terrainTool;

import static mapEditor.editorTools.EditorToolType.*;

import assets.worldTiles.WorldTile;
import assets.worldTiles.WorldTiles;
import mapEditor.MapEditorScreen;
import mapEditor.editorParts.sidePanel.EditorSidePanel;
import mapEditor.editorParts.sidePanel.SidePanel;
import mapEditor.editorParts.sidePanel.SidePanelType;
import mapEditor.editorParts.toolBox.ToolCategory;
import renderUtil.EColors;
import storageUtil.EArrayList;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowTypes.interfaces.IActionObject;

public class TerrainSidePanel extends SidePanel {
	
	public TerrainSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.TERRAIN);
	}

	@Override
	public void loadTool() {
		editor.getToolBox().setToolsWithSelector(ToolCategory.from("Terrain", MOVE, MAGICWAND, PAINTBUCKET, BRUSH, PENCIL, EYEDROPPER, ERASER, LINE, SHAPE));
		
		buildTiles();
	}
	
	private void buildTiles() {
		double curY = startY + 5;
		int bw = 40;
		int rowWidth = ((int) width - 10) / bw;
		
		EArrayList<WindowButton<WorldTile>> buttons = new EArrayList();
		
		for (WorldTile t : WorldTiles.getTiles()) {
			WindowButton<WorldTile> b = new WindowButton(panel) {
				@Override
				public void drawObject(int mXIn, int mYIn) {
					super.drawObject(mXIn, mYIn);
					drawHRect(EColors.black);
				}
			};
			b.setGenericObject(t);
			buttons.add(b);
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			WindowButton<WorldTile> b = buttons.get(i);
			WorldTile tile = b.getGenericObject();
			
			int xPos = i % rowWidth;
			int yPos = i / rowWidth;
			
			double by = startY + 5 + (bw * yPos) + (yPos * 2);
			double bx = startX + 5 + (bw * xPos) + (xPos * 2);
			
			b.setButtonTexture(tile.getTexture());
			b.setHoverText(tile.getName());
			
			//add to list first then dimension
			panel.addObject(b);
			b.setDimensions(bx, by, bw, bw);
		}
	}

	@Override
	public void drawTool(int mXIn, int mYIn) {
	}

	@Override
	public void onAction(IActionObject object, Object... args) {
		if (object instanceof WindowButton b && b.getGenericObject() instanceof WorldTile tile) {
			System.out.println(tile);
		}
	}
	
}
