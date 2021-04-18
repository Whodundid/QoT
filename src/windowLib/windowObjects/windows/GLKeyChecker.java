package windowLib.windowObjects.windows;

import input.Keyboard;
import renderUtil.EColors;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IActionObject;

public class GLKeyChecker extends WindowParent {
	
	int lastKeyCode = -1;
	char lastKey = '\u0000';
	boolean typed = false;
	
	public GLKeyChecker() {
		super();
		aliases.add("keychecker", "glkey");
	}
	
	@Override
	public void initWindow() {
		setDimensions(300, 300);
		setResizeable(true);
		setMinimizable(true);
		setMaximizable(true);
		setMinDims(200, 200);
		setObjectName("GLKey");
	}
	
	@Override
	public void initObjects() {
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
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		//no actions
	}

}
