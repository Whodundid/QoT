package envision.engine.windows.windowBuilder;

import envision.engine.screens.GameScreen;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

/**
 * Used to edit windows and game screens from within the engine itself.
 * 
 * @author Hunter Bragg
 */
public class WindowBuilderScreen extends GameScreen {
	
	//========
	// Fields
	//========
	
	private IWindowParent editingWindow;
	private GameScreen editingScreen;
	
	private WindowBuilderOperatingMode operatingMode;
	private WindowBuilderToolType currentTool;
	
	private EList<IWindowObject> selectedObjects = new EArrayList<>();
	
	private WindowBuilderToolList toolList;
	
	//==============
	// Constructors
	//==============
	
	public WindowBuilderScreen() {
		
	}
	
	public WindowBuilderScreen(IWindowParent window) {
		
	}
	
	public WindowBuilderScreen(GameScreen screen) {
		
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void initScreen() {

	}
	
	@Override
	public void initChildren() {
		toolList = new WindowBuilderToolList(this);
		
		toolList.setPosition(5, 5);
		
		addObject(toolList);
		
		System.out.println(toolList.getDimensions());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.vdgray);
		
		super.drawScreen(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
	}
	
	//=========
	// Methods
	//=========
	
	public void saveWindow() {
		
	}
	
	//=========
	// Getters
	//=========
	
	public WindowBuilderOperatingMode getOperatingMode() { return operatingMode; }
	public WindowBuilderToolType getCurrentToolType() { return currentTool; }
	
	public EList<IWindowObject> getSelectedObjects() { return selectedObjects; }
	
	public WindowBuilderToolList getToolList() { return toolList; }
	
	//=========
	// Setters
	//=========
	
	public void setOperatingMode(WindowBuilderOperatingMode mode) { operatingMode = mode; }
	public void setCurrentTool(WindowBuilderToolType toolType) { currentTool = toolType; }
	
}
