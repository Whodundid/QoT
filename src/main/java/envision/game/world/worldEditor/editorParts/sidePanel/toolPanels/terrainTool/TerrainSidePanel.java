package envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.terrainTool;

import static envision.game.world.worldEditor.editorTools.EditorToolType.*;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.MapEditorSettings;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.PaletteSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.toolBox.ToolCategory;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import envision.game.world.worldTiles.WorldTile;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.world_tiles.GlobalTileList;

public class TerrainSidePanel extends PaletteSidePanel {
    
    public static final ToolCategory terrainTools = ToolCategory.from("Terrain", MOVE, MAGICWAND, PAINTBUCKET, BRUSH,
                                                                      PENCIL, EYEDROPPER, ERASER, LINE, SHAPE);
    private EList<WindowButton<WorldTile>> buttons = EList.newList();
    
    public TerrainSidePanel(EditorSidePanel panelIn, MapEditorScreen in) {
        super(panelIn, in, SidePanelType.TERRAIN);
    }
    
    @Override
    public void loadTool() {
        editor.getToolBox().setToolsWithSelector(terrainTools);
        buildTiles();
        if (buttons.size() >= 1) {
            editor.getSettings().setPrimaryPalette(EditorObject.of(buttons.get(0).getGenericObject()));
        }
        
        if (buttons.size() >= 2) {
            editor.getSettings().setSecondaryPalette(EditorObject.of(buttons.get(1).getGenericObject()));
        }
        
        var tool = editor.getSettings().getCurrentTool();
        if (tool != EditorToolType.SELECTOR && tool != EditorToolType.RECTSELECT) {
            editor.getSettings().setCurrentTool(EditorToolType.SELECTOR);
        }
    }
    
    private void buildTiles() {
        buttons.clear();
        //double curY = startY + 5;
        int bw = 40;
        int rowWidth = ((int) width - 10) / bw;
        
        for (WorldTile t : GlobalTileList.getTiles()) {
            if (!t.hasSprite()) continue;
            
            WindowButton<WorldTile> b = new WindowButton(panel) {
                @Override
                public void drawObject(float dt, int mXIn, int mYIn) {
                    super.drawObject(dt, mXIn, mYIn);
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
            
            b.setButtonTexture(tile.sprite);
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
            
            MapEditorSettings s = editor.getSettings();
            EditorObject item = EditorObject.of(tile);
            
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
