package envisionEngine.eWindow.windowUtil;

import gameSystems.fontRenderer.FontRenderer;
import gameSystems.textureSystem.GameTexture;
import main.Game;
import util.openGL_Util.GLObject;
import util.renderUtil.EColors;
import util.renderUtil.WindowSize;

//Author: Hunter Bragg

public class EGui extends GLObject {

	public Game game = Game.getGame();
	public FontRenderer fontRenderer = game.getFontRenderer();
	public WindowSize res = game.getWindowSize();
	public double startXPos, startYPos, startWidth, startHeight;
	public double startX, startY, endX, endY;
	public double width, height;
	public double midX, midY;
	public int mX, mY;
	public double minWidth = 0;
	public double minHeight = 0;
	public double maxWidth = Double.MAX_VALUE;
	public double maxHeight = Double.MAX_VALUE;
	
	//---------------
	//Drawing Helpers
	//---------------
	
	public void drawRect(EColors color) { drawRect(color.c()); }
	public void drawRect(int color) { drawRect(startX, startY, endX, endY, color); }
	
	public void drawHRect(EColors color) { drawHRect(color.c()); }
	public void drawHRect(int color) { drawHRect(startX, startY, endX, endY, 1, color); }
	
	public void drawRect(EColors color, int offset) { drawRect(color.c(), offset); }
	public void drawRect(int color, int offset) { drawRect(startX + offset, startY + offset, endX - offset, endY - offset, color); }
	
	public void drawHRect(EColors color, int offset) { drawHRect(color.c(), offset); }
	public void drawHRect(int color, int offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, 1, color); }
	
	public void drawTexture(GameTexture texture) { drawTexture(startX, startY, width, height, texture); }
	public void drawTexture(GameTexture texture, int offset) { drawTexture(startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), texture); }
	
	public void scissor() { scissor(startX, startY, endX, endY); }
	public void scissor(double offset) { scissor(startX + offset, startY + offset, endX - offset, endY - offset); }
	
}
