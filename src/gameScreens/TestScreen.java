package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import input.Keyboard;
import input.Mouse;
import main.Game;
import openGL_Util.GLObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import util.renderUtil.EColors;

public class TestScreen extends GameScreen {
	
	WindowButton damagePlayer;
	
	float xf = 0;
	float yf = 0;
	
	@Override
	public void initScreen() {
		
	}
	
	@Override
	public void initObjects() {
		Player mainCharacter = new Player("The Guy", 1, 200, 200, 50, 50, 0, 25, 0);
		
		mainCharacter.setPosition(Game.getWidth() / 2, Game.getHeight() / 2);
		
		Game.getGameRenderer().addObject(mainCharacter);
		
		damagePlayer = new WindowButton(this, startX + 5, endX - 60, 100, 55);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			yf += 0.01f;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			xf -= 0.01f;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			yf -= 0.01f;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			xf += 0.01f;
		}
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			Game.stopGame();
		}
		
		float val1, val2, val3;
		
		if (Mouse.isButtonDown(0)) {
			val1 = 0.25f;
			val2 = 0.77f;
			val3 = 0.25f;
		}
		else {
			val1 = 1;
			val2 = 1;
			val3 = 1;
		}
		
		GLObject.drawRect(0, 0, width, height, EColors.lgray);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(val1, 0, 0, 0);
		GL11.glVertex2f(-0.5f + xf, 0.5f + yf);
		GL11.glColor4f(0, val2, 0, 0);
		GL11.glVertex2f(0.5f + xf, 0.5f+yf);
		GL11.glColor4f(0, 0, val3, 0);
		GL11.glVertex2f(0.5f + xf, -0.5f+yf);
		GL11.glColor4f(val1, val2, val3, 0);
		GL11.glVertex2f(-0.5f+xf, -0.5f+yf);
		GL11.glEnd();
		
		super.drawScreen(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			PointerPress p = new PointerPress(mXIn, mYIn);
			Game.getGameRenderer().addObject(p);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}
