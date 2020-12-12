package gameScreens.mapEditor.editorScreen.topHeader;

import envisionEngine.eWindow.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTileTool;
import gameScreens.mapEditor.editorScreen.util.ShapeType;

public class ShapeSettings extends HeaderToolSettings {

	WindowDropDownList shapeSelect;
	
	ShapeType type = ShapeType.SQUARE;
	boolean filled = true;
	
	public ShapeSettings(EditorScreenTopHeader headerIn, double xIn, double yIn, double heightIn) {
		super(headerIn, EditorTileTool.SHAPE);
		init(headerIn, xIn, yIn, 400, heightIn);
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}
