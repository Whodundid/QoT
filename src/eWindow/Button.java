package eWindow;

import input.Mouse;
import openGL_Util.GLObject;
import util.renderUtil.EColors;

public class Button extends GLObject {
	
	public boolean isPressed = false;
	
	public Button(int startX, int startY, int endX, int endY) {
		super(startX, startY, endX, endY);
	}
	
	public void draw(int mXIn, int mYIn) {
		
		//determine if mouse is pressed
		if (isMouseInside(mXIn, mYIn) && Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		else { isPressed = false; }
	
		//actually draw the thing
		drawButton();
		
	}
	
	private void drawButton() {
		drawHRect(EColors.black, 4);
		drawRect(4, (isPressed) ? 0x7700ff00 : 0x77ff0000);
	}
	
	public boolean isMouseInside(int mX, int mY) {
		return (mX >= startX && mX <= endX) && (mY >= startY && mY <= endY);
	}
	
}

