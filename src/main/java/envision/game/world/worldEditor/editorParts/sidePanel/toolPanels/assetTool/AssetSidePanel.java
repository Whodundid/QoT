package envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.assetTool;

import static envision.game.world.worldEditor.editorTools.EditorToolType.*;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.GameObject;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.MapEditorSettings;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.PaletteSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.toolBox.ToolCategory;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.GlobalAssetList;

public class AssetSidePanel extends PaletteSidePanel {

	public static final ToolCategory assetTools = ToolCategory.from("Assets", PLACE, MOVE);
	private EList<WindowButton<EditorObject>> buttons = EList.newList();
	
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
			WindowButton<EditorObject> b = new WindowButton<>(panel) {
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
			
			if (item.getSprite() != null) b.setButtonTexture(item.getSprite().getTexture());
			else b.setButtonTexture(item.getSprite());
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
		if (object instanceof WindowButton<?> b && b.getGenericObject() instanceof EditorObject item) {
			int button = b.getPressedButton();
			
			MapEditorSettings s = editor.getSettings();
			
			s.setCurrentTool(EditorToolType.PLACE);
			
			if (button == 0) s.setPrimaryPalette(item);
			else s.setSecondaryPalette(item);
		}
	}
	
}
