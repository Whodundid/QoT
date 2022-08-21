package world.mapEditor.editorParts.sidePanel.toolPanels.assetTool;

import static world.mapEditor.editorTools.EditorToolType.MOVE;

import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.GameObject;
import game.GlobalAssetList;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.MapEditorSettings;
import world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import world.mapEditor.editorParts.sidePanel.PaletteSidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanelType;
import world.mapEditor.editorParts.toolBox.ToolCategory;
import world.mapEditor.editorParts.util.EditorItem;
import world.mapEditor.editorTools.EditorToolType;

public class AssetSidePanel extends PaletteSidePanel {

	public static final ToolCategory assetTools = ToolCategory.from("Assets", MOVE);
	private EArrayList<WindowButton<EditorItem>> buttons = new EArrayList();
	
	public AssetSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.ASSET);
	}

	@Override
	public void loadTool() {
		editor.getToolBox().setToolsWithSelector(assetTools);
		buildPanel();
		if (buttons.size() >= 1) editor.getSettings().setPrimaryPalette(buttons.get(0).getGenericObject());
		if (buttons.size() >= 2) editor.getSettings().setSecondaryPalette(buttons.get(1).getGenericObject());
		
		editor.getSettings().setCurrentTool(EditorToolType.SELECTOR);
	}
	
	private void buildPanel() {
		buttons.clear();
		//double curY = startY + 5;
		int bw = 40;
		int rowWidth = ((int) width - 10) / bw;
		
		for (GameObject t : GlobalAssetList.getAssets()) {
			WindowButton<EditorItem> b = new WindowButton(panel) {
				@Override
				public void drawObject(int mXIn, int mYIn) {
					super.drawObject(mXIn, mYIn);
					drawHRect(EColors.black);
				}
			};
			b.setGenericObject(EditorItem.of(t));
			b.setAcceptRightClicks(true);
			buttons.add(b);
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			WindowButton<EditorItem> b = buttons.get(i);
			EditorItem item = b.getGenericObject();
			
			int xPos = i % rowWidth;
			int yPos = i / rowWidth;
			
			//using 'pey' and 'psx' from PaletteSidePanel to get the end location after the palette icons
			double by = pey + 17 + (bw * yPos) + (yPos * 2);
			double bx = psx + (bw * xPos) + (xPos * 2);
			
			b.setButtonTexture(item.getTexture());
			b.setHoverText(item.getName());
			
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
		if (object instanceof WindowButton b && b.getGenericObject() instanceof EditorItem item) {
			int button = b.getPressedButton();
			
			MapEditorSettings s = editor.getSettings();
			
			//Switch the tool back to a pencil if it's currently an eraser
			// -This is a convenience setting
			if (s.getCurrentTool() == EditorToolType.ERASER) {
				s.setCurrentTool(EditorToolType.PENCIL);
			}
			
			if (button == 0) s.setPrimaryPalette(item);
			else s.setSecondaryPalette(item);
		}
	}
	
}
