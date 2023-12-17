package envision.engine.windows.windowObjects.basicObjects;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

//Author: Hunter Bragg

public class WindowImageBox extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private EList<GameTexture> images = EList.newList();
	private int borderColor = EColors.black.c();
	private int backgroundColor = EColors.vdgray.c();
	private boolean drawImage = true;
	private boolean drawBorder = true;
	private boolean drawBackground = true;
	private boolean centerImage = true;
	private boolean drawStretched = false;
	private boolean singleImage = false;
	private String nullText = "Texture is null!";
	private int nullTextColor = EColors.lred.intVal;
	private long updateInterval = 500l;
	private long timeSince = 0l;
	private int curImage = 0;
	private double zoom = 0, zoomX = 0, zoomY = 0;
	private double panX = 0, panY = 0;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, (GameTexture) null); }
	public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, GameTexture imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		images.add(imageIn);
		singleImage = true;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		if (drawBorder) drawRect(startX, startY, endX, endY, borderColor);
		if (drawBackground) drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor);
		if (drawImage) {
			if (images.isNotEmpty() && images.get(0) != null) {
				double posX = startX + 2;
				double posY = startY + 2;
				double w = width - 4;
				double h = height - 4;
				double smaller = w <= h ? w : h;
				
				if (centerImage) {
					posX = startX + 2 + ((w - smaller) / 2);
					posY = startY + 2 + ((h - smaller) / 2);
					
					w = smaller;
					h = smaller;
				}
				else if (!drawStretched) {
					GameTexture cur = images.get(curImage);
					
					double imgW = cur.getWidth();
					double imgH = cur.getHeight();
					
					//image ratio equations
					
					if (w <= h) {
						h = ENumUtil.clamp((w / imgW) * imgH, 0, (height - 4));
						w = (h / imgH) * imgW;
					}
					else {
						w = ENumUtil.clamp((h / imgH) * imgW, 0, (width - 4));
						h = (w / imgW) * imgH;
					}
					
					posY = startY + 2 + ((height - 4) - h) / 2;
					posX = startX + 2 + ((width - 4) - w) / 2;
				}
				
				GameTexture texToDraw;
				
				if (singleImage) {
					texToDraw = images.get(0);
				}
				else {
					if (System.currentTimeMillis() - timeSince >= updateInterval) {
						curImage++;
						if (curImage == images.size()) curImage = 0;
						timeSince = System.currentTimeMillis();
					}
					texToDraw = images.get(curImage);
				}
				
				zoomX = 10 * ((w / h) * zoom);
				zoomY = 10 * ((h / w) * zoom);
				
				scissor(startX + 1, startY + 1, endX - 0.5, endY - 0.5);
				drawTexture(texToDraw, posX - (zoomX / 2), posY - (zoomY / 2), w + zoomX, h + zoomY);
				endScissor();
			}
			else {
				drawStringC(nullText, midX, midY - (FontRenderer.FONT_HEIGHT / 2), nullTextColor);
			}
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		zoom += (change * 5);
		zoom = ENumUtil.clamp(zoom, 0, Double.MAX_VALUE);
	}
	
	@Override
	public void mouseDragged(int mXIn, int mYIn, int button, long timeSinceLastClick) {
		super.mouseDragged(mXIn, mYIn, button, timeSinceLastClick);
	}
	
	//---------
	// Getters
	//---------
	
	public EList<GameTexture> getImages() { return images; }
	public int getBorderColor() { return borderColor; }
	public int getBackgroundColor() { return backgroundColor; }
	public long getUpdateInterval() { return updateInterval; }
	public boolean drawsImage() { return drawImage; }
	public boolean drawsBorder() { return drawBorder; }
	public boolean drawsBackground() { return drawBackground; }
	public boolean drawsStretched() { return drawStretched; }
	
	//---------
	// Setters
	//---------
	
	public void setDrawStretched(boolean val) { drawStretched = val; }
	public void setNullText(String textIn) { nullText = textIn; }
	public void setNullTextColor(EColors colorIn) { setNullTextColor(colorIn.intVal); }
	public void setNullTextColor(int colorIn) { nullTextColor = colorIn; }
	public void setUpdateInterval(long time) { updateInterval = (long) ENumUtil.clamp(time, 0, Long.MAX_VALUE); }
	public void setDrawImage(boolean val) { drawImage = val; }
	public void setDrawBorder(boolean val) { drawBorder = val; }
	public void setDrawBackground(boolean val) { drawBackground = val; }
	public void setCenterImage(boolean val) { centerImage = val; }
	public void setBorderColor(EColors colorIn) { setBorderColor(colorIn.intVal); }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setBackgroundColor(EColors colorIn) { setBackgroundColor(colorIn.intVal); }
	public void setBackgroundColor(int colorIn) { backgroundColor = colorIn; }
	
	public void setImage(GameTexture imageIn) {
		if (imageIn != null) {
			images = new EArrayList(imageIn);
			singleImage = true;
		}
	}
	
	public void setImages(GameTexture... imagesIn) {
		if (imagesIn != null) {
			images = new EArrayList((Object[]) imagesIn);
			singleImage = images.size() == 1;
		}
	}
	
}
