package envision.engine.windows.windowBuilder;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import eutil.colors.EColors;

/**
 * A component that can be placed onto a window/screen that is being edited.
 * 
 * @author Hunter Bragg
 */
public class WindowBuilderTool extends WindowButton {
	
	private final WindowBuilderScreen parentScreen;
	public final WindowBuilderToolType toolType;
	
	public WindowBuilderTool(WindowBuilderScreen screen, WindowBuilderToolType typeIn) {
		parentScreen = screen;
		toolType = typeIn;
		setHoverText(toolType.description);
		setButtonTexture(toolType.toolTexture);
		setRunActionOnPress(true);
		setAction(() -> parentScreen.setCurrentTool(toolType));
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
		
		if (parentScreen.getCurrentToolType() == toolType) {
			drawHRect(EColors.red, 2, -2);
		}
	}
	
	public WindowBuilderToolType getToolType() {
		return toolType;
	}
	
	public String getToolDescription() {
		return toolType.description;
	}
	
}
