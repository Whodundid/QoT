package gameScreens.mapEditor.editorScreen;

import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.mapSystem.GameWorld;
import main.Game;
import util.renderUtil.EColors;

public class EditorMiniMap extends WindowObject {
	
	MapEditorScreen editor;
	GameWorld world;
	
	public EditorMiniMap(MapEditorScreen editorIn) {
		editor = editorIn;
		world = editor.getWorld();
		double w = Game.getWidth() / 8;
		init(editor, Game.getWidth() - w - 5, editor.getTopHeader().endY + 5, w, w);
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
		
		drawMap();
		drawCameraPos();
		
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
		
		int detail = 2;
		
		// need to math
		
		//int sX = 
		
		for (int i = 0; i < w; i += detail) {
			for (int j = 0; j < h; j += detail) {
				
			}
		}
	}
	
	private void drawCameraPos() {
		
	}
	
}
