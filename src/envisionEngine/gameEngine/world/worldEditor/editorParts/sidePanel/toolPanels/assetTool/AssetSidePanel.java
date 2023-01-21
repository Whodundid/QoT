package envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.toolPanels.assetTool;

import static envisionEngine.gameEngine.world.worldEditor.editorTools.EditorToolType.*;

import envisionEngine.gameEngine.GameObject;
import envisionEngine.gameEngine.world.worldEditor.MapEditorScreen;
import envisionEngine.gameEngine.world.worldEditor.MapEditorSettings;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.PaletteSidePanel;
import envisionEngine.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envisionEngine.gameEngine.world.worldEditor.editorParts.toolBox.ToolCategory;
import envisionEngine.gameEngine.world.worldEditor.editorParts.util.EditorObject;
import envisionEngine.gameEngine.world.worldEditor.editorTools.EditorToolType;
import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;
import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import qot.GlobalAssetList;

public class AssetSidePanel extends PaletteSidePanel {

	public static final ToolCategory assetTools = ToolCategory.from("Assets", PLACE, MOVE);
	private EArrayList<WindowButton<EditorObject>> buttons = new EArrayList();
	
	public AssetSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
		super(panelIn, in, SidePanelType.ASSET);
	}

	@Override
	public void loadTool() {
		editor.getToolBox().setToolsWithSelector(assetTools);
		buildPanel();
		if (buttons.size() >= 1) editor.getSettings().setPrimaryPalette(buttons.get(0).getGenericObject());
		if (buttons.size() >= 2) editor.getSettings().setSecondaryPalette(buttons.get(1).getGenericObject());
		
		editor.getSettings().setCurrentTool(EditorToolType.PLACE);
	}
	
	private void buildPanel() {
		buttons.clear();
		//double curY = startY + 5;
		int bw = 40;
		int rowWidth = ((int) width - 10) / bw;
		
		for (GameObject t : GlobalAssetList.getAssets()) {
			WindowButton<EditorObject> b = new WindowButton(panel) {
				@Override
				public void drawObject(int mXIn, int mYIn) {
					super.drawObject(mXIn, mYIn);
					drawHRect(EColors.black);
				}
			};
			b.setGenericObject(EditorObject.of(t));
			b.setAcceptRightClicks(true);
			buttons.add(b);
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			WindowButton<EditorObject> b = buttons.get(i);
			EditorObject item = b.getGenericObject();
			
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
		if (object instanceof WindowButton b && b.getGenericObject() instanceof EditorObject item) {
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
