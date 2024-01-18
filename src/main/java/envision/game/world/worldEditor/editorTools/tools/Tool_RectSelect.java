package envision.game.world.worldEditor.editorTools.tools;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.game.entities.Entity;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;

public class Tool_RectSelect extends EditorTool {

    //========
    // Fields
    //========
    
    private int clickX, clickY;
    
    //==============
    // Constructors
    //==============
    
	public Tool_RectSelect(MapEditorScreen in) {
		super(in);
	}

	//===========
    // Overrides
    //===========
	
	@Override
	protected void drawTool(double x, double y, double w, double h) {
	    if (!pressed) return;
	    
	    double mx = Mouse.getMx_double();
	    double my = Mouse.getMy_double();
	    double[] worldPos = editor.camera.convertScreenAreaToWorldArea(clickX, clickY, mx, my);
	    ENumUtil.floorFromT(worldPos, 0, 2);
	    ENumUtil.ceilFromT(worldPos, 2, 4);
	    
	    //drawStringC("[" + worldPos[0] + ", " + worldPos[1] + "]", clickX, clickY - FontRenderer.FH);
	    //drawStringC("[" + worldPos[2] + ", " + worldPos[3] + "]", mx, my + FontRenderer.FH);
	    
	    double[] screenArea = editor.camera.convertWorldAreaToScreenArea(worldPos);
	    
	    drawHRect(screenArea, 2, EColors.lgray);
	}
	
	@Override
	public void onPress() {
	    if (editor.getCurrentSidePanel() == SidePanelType.ASSET) {
	        clickX = Mouse.getMx();
	        clickY = Mouse.getMy();
	    }
	}

	@Override
	public void onRelease() {
	    // determine all objects that are in the selected area and set them to be the selected list
	    int releaseX = Mouse.getMx();
	    int releaseY = Mouse.getMy();
	    
	    // transform clicked screen area to world space
	    double[] worldArea = editor.camera.convertScreenAreaToWorldArea(clickX, clickY, releaseX, releaseY);
        ENumUtil.floorEachT(worldArea);
        final Dimension_d worldAreaDims = new Dimension_d(worldArea);
	    
        // this should eventually be called using the world itself to perform the calcuation (quad tree)
        EList<EditorObject> objects = EList.newList();
        for (EditorObject o : editor.getEditorWorld().getEditorEntities()) {
            Entity ent = o.getEntity();
            if (worldAreaDims.partiallyContains(ent.getDimensions())) objects.add(o);
        }
        
        if (Keyboard.isCtrlDown()) editor.addToSelected(objects);
        else if (Keyboard.isAltDown()) editor.removeFromSelected(objects);
        else editor.setSelectedObjects(objects);
        
		clickX = 0;
		clickY = 0;
	}

	@Override
	public void onUpdate() {
		
	}
	
}
