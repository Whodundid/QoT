package gameScreens.mapEditor.editorScreen.botHeader;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.MapEditorScreen;
import gameSystems.fontRenderer.FontRenderer;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

public class EditorScreenBotHeader extends WindowObject {
	
	MapEditorScreen editor;
	
	WindowButton incX, decX;
	WindowButton incY, decY;
	
	public EditorScreenBotHeader(MapEditorScreen editorIn) {
		editor = editorIn;
		double h = 30;
		init(editor, 0, Game.getHeight() - h, Game.getWidth(), h);
	}
	
	@Override
	public void initObjects() {
		double w = height - 6;
		double sY = startY + ((height - w) / 2);
		
		decY = new WindowButton(this, endX - w - 2, sY, w, w, "-");
		incY = new WindowButton(this, decY.startX - w - 2, sY, w, w, "+");
		
		double yDist = FontRenderer.getStringWidth("Y " + editor.getViewY());
		
		decX = new WindowButton(this, incY.startX - w - yDist - 16, sY, w, w, "-");
		incX = new WindowButton(this, decX.startX - w - 2, sY, w, w, "+");
		
		addObject(incX, decX, incY, decY);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.mgray, 1);
		
		//draw cur world pos
		double worldX = drawString("World: [" + editor.getXPos() + "," + editor.getYPos() + "]", startX + 10, endY - FontRenderer.FONT_HEIGHT);
		drawRect(worldX + 6, startY, worldX + 8, endY, EColors.black);
		double mouseX = worldX + 14;
		
		if (editor.shouldDrawMouse()) {
			mouseX = drawString("Mouse: [" + editor.getWorldMX() + "," + editor.getWorldMY() + "]", worldX + 14, endY - FontRenderer.FONT_HEIGHT);
			drawRect(mouseX + 6, startY, mouseX + 8, endY, EColors.black);
		}
		
		double xDist = FontRenderer.getStringWidth("X " + editor.getViewX());
		double yDist = FontRenderer.getStringWidth("Y " + editor.getViewY());
		
		drawString("X:" + editor.getViewX(), incX.startX - xDist - 8, incX.midY - FontRenderer.FONT_HEIGHT / 2 + 3);
		drawString("Y:" + editor.getViewY(), incY.startX - yDist - 8, incY.midY - FontRenderer.FONT_HEIGHT / 2 + 3);
		
		String zoomStr = "Zoom: " + NumUtil.roundD2(editor.getZoom()) + "x";
		double zoomX = FontRenderer.getStringWidth(zoomStr);
		
		drawString(zoomStr, incX.startX - xDist - 24 - zoomX, endY - FontRenderer.FONT_HEIGHT);
		
		drawRect(incX.startX - xDist - 16, startY, incX.startX - xDist - 14, endY, EColors.black);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == incX) { editor.setViewX(editor.getViewX() + 1); }
		if (object == decX) { editor.setViewX(editor.getViewX() - 1); }
		
		if (object == incY) { editor.setViewY(editor.getViewY() + 1); }
		if (object == decY) { editor.setViewY(editor.getViewY() - 1); }
	}
	
}
