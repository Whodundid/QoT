package eWindow;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import input.Mouse;
import main.Main;
import org.lwjgl.opengl.GL11;

public class Button {
	
	public int sX, sY;
	public int eX, eY;
	public int width, height;
	public boolean isPressed = false;
	
	public Button(int xIn, int yIn, int widthIn, int heightIn) {
		sX = xIn;
		sY = yIn;
		width = widthIn;
		eX = sX + widthIn;
		eY = sY + heightIn;
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
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		
		glBegin(GL_QUADS);
		glColor4f(0, 0, 0, 0);
		glVertex2d(tdx(sX), tdy(eY));
		glVertex2d(tdx(eX), tdy(eY));
		glVertex2d(tdx(eX), tdy(sY));
		glVertex2d(tdx(sX), tdy(sY));
		glEnd();
		
		
		if (isPressed) {
			glBegin(GL_QUADS);
			GL11.glColor4d(0.5, 0.5, 0.5, 0.0);
			glVertex2d(tdx(sX + 4), tdy(eY - 4));
			glVertex2d(tdx(eX - 4), tdy(eY - 4));
			glVertex2d(tdx(eX - 4), tdy(sY + 4));
			glVertex2d(tdx(sX + 4), tdy(sY + 4));
			glEnd();
		}
		else {
			glBegin(GL_QUADS);
			GL11.glColor4d(0.3, 0.3, 0.5, 0.0);
			glVertex2d(tdx(sX + 4), tdy(eY - 4));
			glVertex2d(tdx(eX - 4), tdy(eY - 4));
			glVertex2d(tdx(eX - 4), tdy(sY + 4));
			glVertex2d(tdx(sX + 4), tdy(sY + 4));
			glEnd();
		}
	}
	
	public boolean isMouseInside(int mX, int mY) {
		return (mX >= sX && mX <= eX) && (mY >= sY && mY <= eY);
	}
	
	/** 'to double for x' converts a given value into a corresponding double value between -1.0f and 1.0f based on window x size. */
	public static float tdx(Number valIn) {
		float midX = Main.getWindow().getWidth() / 2;
		return ((valIn.floatValue()) - midX) / midX;
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	public static float tdy(Number valIn) {
		float midY = Main.getWindow().getHeight() / 2;
		return (midY - (valIn.floatValue())) / midY;
	}
	
}

