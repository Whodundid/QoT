package game.screens.testingStuff;

import org.lwjgl.opengl.GL11;

import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.screens.main.MainMenuScreen;
import main.QoT;

public class TestInventory<E> extends WindowParent<E> {
	
	private WindowButton close;
	
	//--------------
	// Constructors
	//--------------
	
	public TestInventory() {
		//nothing
	}

	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setObjectName("Inventory");
		setMaximizable(true);
		setMinimizable(true);
		setResizeable(true);
		setMinDims(300, 300);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		close = new WindowButton(this, startX + 10, startY + 10, 50, 50);
		close.setActionReceiver(this);
		
		addChild(close);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		var scale = 0.5;
		var fw = FontRenderer.getStringWidth("HELLO");
		var fh = FontRenderer.FONT_HEIGHT;
		var scaleX = (fw * scale) / fw;
		var scaleY = (fh * scale) / fh;
		var sourceCenterX = midX;
		var sourceCenterY = midY;
		
		GL11.glPushMatrix();
			GL11.glTranslated(0.03, 0, 0);
			GL11.glScaled(scaleX, scaleY, 0);
			//GL11.glTranslated(-sourceCenterX, -sourceCenterY, mYIn);
			drawStringC("HELLO", midX, midY);
		GL11.glPopMatrix();
		
		//AT THE END -- IN ORDER TO DRAW CHILD OBJECTS
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == close) closeWindow();
	}
	
	private void closeWindow() {
		QoT.displayScreen(new MainMenuScreen());
	}
	
}
