package envision.engine.windows.windowBuilder;

import envision.engine.windows.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

/**
 * Holds all of the components that can be placed on a window/screen being edited.
 * 
 * @author Hunter Bragg
 */
public class WindowBuilderToolList extends WindowObject {
	
	//========
	// Fields
	//========
	
	private WindowBuilderScreen parentScreen;
	private EList<WindowBuilderTool> components = new EArrayList<>();
	/** The maximum number of tools that will be drawn per row. */
	private int maxRowWidth = 2;
	private int buttonGap = 2;
	private int buttonSize = 40;
	
	//==============
	// Constructors
	//==============
	
	public WindowBuilderToolList(WindowBuilderScreen screenIn) {
		parentScreen = screenIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void initChildren() {
		components.clear();
		
		// build default tools
		for (var type : WindowBuilderToolType.values()) {
			var tool = new WindowBuilderTool(parentScreen, type);
			components.add(tool);
			addObject(tool);
		}
		
		resizeToolBox();
		positionToolButtons();
	}
	
	protected void resizeToolBox() {
		int numRows = components.size() / maxRowWidth;
		int calculatedWidth = buttonSize * maxRowWidth + (buttonGap * maxRowWidth - 1) + buttonGap;
		int calculatedHeight = numRows * buttonSize + (buttonGap * numRows - 1);
		setDimensions(startX, startY, calculatedWidth, calculatedHeight);
	}
	
	protected void positionToolButtons() {
		int numRows = components.size() / maxRowWidth;
		
		double xPos = startX + buttonGap;
		double yPos = startY + buttonGap;
		int i = 0;
		
		// place buttons top-left to bot-right
		for (var tool : components) {
			tool.setParent(this);
			
			double toolX = i % maxRowWidth;
			double toolY = i / maxRowWidth;
			
			double bx = xPos + (buttonSize * toolX) + toolX + buttonGap * toolX;
			double by = yPos + (buttonSize * toolY) + toolY + buttonGap * toolY;
			
			tool.setDimensions(bx, by, buttonSize, buttonSize);
			
			i++;
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.pdgray, 1);
		
		super.drawObject(mXIn, mYIn);
	}
	
}
