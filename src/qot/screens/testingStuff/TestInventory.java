package qot.screens.testingStuff;

import org.lwjgl.opengl.GL11;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import qot.QoT;
import qot.screens.main.MainMenuScreen;

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
		
		addObject(close);
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
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == close) closeWindow();
	}
	
	private void closeWindow() {
		QoT.displayScreen(new MainMenuScreen());
	}
	
}
