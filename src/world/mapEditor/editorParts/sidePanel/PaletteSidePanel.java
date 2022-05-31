package world.mapEditor.editorParts.sidePanel;

import eutil.colors.EColors;
import world.mapEditor.MapEditorScreen;
import world.mapEditor.editorParts.util.EditorItem;

public abstract class PaletteSidePanel extends SidePanel {
	
	//--------------------
	protected double
	psx, //primary start x
	psy, //primary start y
	pw,  //primary width
	ph,  //primary height
	pex, //primary end x
	pey; //primary end y
	//--------------------
	
	protected PaletteSidePanel(EditorSidePanel panelIn, MapEditorScreen in, SidePanelType typeIn) {
		super(panelIn, in, typeIn);
		
		psx = startX + 10;
		psy = startY + 10;
		pw = width / 3;
		ph = width / 3;
		pex = psx + pw;
		pey = psy + ph;
	}
	
	@Override
	public void drawTool(int mXIn, int mYIn) {
		//border
		drawHRect(EColors.black, 2, 0);
		drawRect(startX + 2, startY + 2, endX - 2, pey + 8, EColors.dgray);
		
		EditorItem primary = editor.getSettings().getPrimaryPalette();
		EditorItem secondary = editor.getSettings().getSecondaryPalette();
		
		if (primary != null) { drawTexture(primary.getTexture(), psx, psy, pw, ph); }
		else drawRect(psx, psy, pex, pey, EColors.mgray);
		drawHRect(psx, psy, pex, pey, 1, EColors.black);
		
		if (secondary != null) { drawTexture(secondary.getTexture(), pex + 10, psy, pw, ph); }
		else drawRect(pex + 10, psy, pex + 10 + pw, pey, EColors.dgray);
		drawHRect(pex + 10, psy, pex + 10 + pw, pey, 1, EColors.black);
		
		//separator bar
		drawRect(startX, pey + 8, endX, pey + 10, EColors.black);
	}
	
	
	
}
