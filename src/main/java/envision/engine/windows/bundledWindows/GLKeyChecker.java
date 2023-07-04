package envision.engine.windows.bundledWindows;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.colors.EColors;
import qot.assets.textures.taskbar.TaskBarTextures;

public class GLKeyChecker extends WindowParent {
	
	//--------
	// Fields
	//--------
	
	private int lastKeyCode = -1;
	private char lastKey = '\u0000';
	private boolean typed = false;
	
	//--------------
	// Constructors
	//--------------
	
	public GLKeyChecker() {
		super();
		aliases.add("keychecker", "glkey");
		windowIcon = TaskBarTextures.experiment;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setGuiSize(300, 300);
		setResizeable(true);
		setMinimizable(true);
		setMaximizable(true);
		setMinDims(200, 200);
		setObjectName("GLKey");
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawHRect(EColors.black, 2);
		drawRect(EColors.dgray, 3);
		
		if (!typed) {
			drawStringCS("Press a key!", midX, midY - 3, EColors.cyan);
		}
		else {
			drawStringCS("LastKey: " + Keyboard.getKeyName(lastKeyCode), midX, midY - 20, EColors.cyan);
			drawStringCS("GLKey: " + lastKeyCode, midX, midY + 3, EColors.cyan);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		typed = true;
		lastKeyCode = keyCode;
		lastKey = typedChar;
		super.keyPressed(typedChar, keyCode);
	}

}
