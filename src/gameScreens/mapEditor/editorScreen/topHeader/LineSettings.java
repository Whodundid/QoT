package gameScreens.mapEditor.editorScreen.topHeader;

import envisionEngine.eWindow.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import envisionEngine.eWindow.windowObjects.basicObjects.WindowLabel;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTileTool;
import util.renderUtil.EColors;

public class LineSettings extends HeaderToolSettings {
	
	WindowLabel widthLabel;
	WindowDropDownList widthSelection;
	int curWidth;
	
	public LineSettings(EditorScreenTopHeader headerIn, double xIn, double yIn, double heightIn) {
		super(headerIn, EditorTileTool.LINE);
		init(headerIn, xIn, yIn, 0, heightIn);
	}
	
	@Override
	public void initObjects() {
		widthLabel = new WindowLabel(this, startX + 10, startY + 4, "Width: ");
		widthSelection = new WindowDropDownList(this, widthLabel.endX + 10, startY + 2, height - 4);
		
		widthSelection.setFixedWidth(100);
		for (int i = 0; i < 5; i++) {
			widthSelection.addEntry("" + i, EColors.white, i);
		}
		
		setDimensions((widthSelection.endX + 12) - startX, height);
		
		addObject(widthLabel, widthSelection);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(endX - 2, startY, endX, endY, EColors.black);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
}
