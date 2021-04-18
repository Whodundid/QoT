package mapEditor.editorParts.minimap;

import assets.worldTiles.WorldTile;
import gameSystems.mapSystem.GameWorld;
import mapEditor.MapEditorScreen;
import mapEditor.editorParts.sidePanel.EditorSidePanel;
import renderUtil.EColors;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IActionObject;

public class EditorMiniMap extends WindowObject {
	
	EditorSidePanel panel;
	MapEditorScreen editor;
	GameWorld world;
	
	/** Detail relates to the draw ratio.
	 *  A detail level of 1 is equivalent to a 1:1 draw ratio, as in the minimap will draw each pixel of the map.
	 *  Similarly, a detail level of 4 will produce a 4:1 draw ratio where the minimap will draw 1 pixel for every 4 pixels of the map. */
	int detail = 1;
	
	public EditorMiniMap(EditorSidePanel panelIn) {
		panel = panelIn;
		editor = panel.getEditor();
		world = editor.getWorld();
		init(panel);
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.mgray, 1);
		drawRect(EColors.lgray, 3);
		drawRect(EColors.black, 4);
		
		scissor(startX + 4, startY + 4, endX - 4, endY - 4);
		drawMap();
		drawCameraPos();
		endScissor();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	private void drawMap() {
		int w = world.getWidth();
		int h = world.getHeight();
		
		detail = 1;
		
		int sX = (int) startX + 4; //startX
		int sY = (int) startY + 4; //startY
		int dw = 4; //draw width
		int dh = 4; //draw height
		
		//iterating horizontally
		for (int i = 0, ii = 0; i < h; i += detail, ii++) {
			for (int j = 0, jj = 0; j < w; j += detail, jj++) {
				WorldTile tile = world.getTileAt(j, i);
				int color = (tile != null) ? tile.getMapColor() : 0xff000000;
				
				int drawPosX = sX + (jj * dw);
				int drawPosY = sY + (ii * dh);
				
				drawRect(drawPosX, drawPosY, drawPosX + dw, drawPosY + dh, color);
			}
		}
	}
	
	private void drawCameraPos() {
		
	}
	
}
