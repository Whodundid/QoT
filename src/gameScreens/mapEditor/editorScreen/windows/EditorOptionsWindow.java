package gameScreens.mapEditor.editorScreen.windows;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.MapEditorScreen;
import main.Game;

public class EditorOptionsWindow extends WindowParent {
	
	MapEditorScreen editor;
	
	WindowButton ok, close;
	
	public EditorOptionsWindow(MapEditorScreen editorIn) {
		super(editorIn);
		editor = editorIn;
	}
	
	@Override
	public void initWindow() {
		setDimensions(Game.getWidth() / 2, Game.getHeight() / 2);
		setMinDims(300, 300);
		setMaximizable(true);
		setResizeable(true);
		
		setObjectName("Map Editor Options");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		close = new WindowButton(this, endX - 152, endY - 37, 150, 35, "Close");
		
		addObject(close);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == close) { fileUpAndClose(); }
	}
	
}
