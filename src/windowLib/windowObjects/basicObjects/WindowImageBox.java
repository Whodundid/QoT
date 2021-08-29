package windowLib.windowObjects.basicObjects;

import eutil.colors.EColors;
import eutil.math.NumberUtil;
import eutil.storage.EArrayList;
import renderEngine.GLSettings;
import renderEngine.fontRenderer.FontRenderer;
import renderEngine.resources.ResourceUtil;
import renderEngine.textureSystem.GameTexture;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowImageBox<E> extends WindowObject<E> {
	
	EArrayList<GameTexture> images = new EArrayList();
	int borderColor = EColors.black.c();
	int backgroundColor = EColors.vdgray.c();
	boolean drawImage = true;
	boolean drawBorder = true;
	boolean drawBackground = true;
	boolean centerImage = true;
	boolean drawStretched = false;
	boolean singleImage = false;
	String nullText = "Texture is null!";
	int nullTextColor = EColors.lred.intVal;
	long updateInterval = 500l;
	long timeSince = 0l;
	int curImage = 0;
	double zoom = 0, zoomX = 0, zoomY = 0;
	double panX = 0, panY = 0;
	
	public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, (GameTexture) null); }
	public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, GameTexture imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		images.add(imageIn);
		singleImage = true;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); }
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); }
		if (drawImage) {
			if (images.isNotEmpty() && images.get(0) != null) {
				GLSettings.enableAlpha();
				GLSettings.enableBlend();
				
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
					
					double imgW = ResourceUtil.getImageWidth(cur);
					double imgH = ResourceUtil.getImageHeight(cur);
					
					//image ratio equations
					
					if (w <= h) {
						h = NumberUtil.clamp((w / imgW) * imgH, 0, (height - 4));
						w = (h / imgH) * imgW;
					}
					else {
						w = NumberUtil.clamp((h / imgH) * imgW, 0, (width - 4));
						h = (w / imgW) * imgH;
					}
					
					posY = startY + 2 + ((height - 4) - h) / 2;
					posX = startX + 2 + ((width - 4) - w) / 2;
				}
				
				if (singleImage) {
					bindTexture(images.get(0));
				}
				else {
					if (System.currentTimeMillis() - timeSince >= updateInterval) {
						curImage++;
						if (curImage == images.size()) { curImage = 0; }
						timeSince = System.currentTimeMillis();
					}
					bindTexture(images.get(curImage));
				}
				
				GLSettings.color(2.0f, 2.0f, 2.0f, 2.0f);
				
				zoomX = 10 * ((w / h) * zoom);
				zoomY = 10 * ((h / w) * zoom);
				
				scissor(startX + 1, startY + 1, endX - 0.5, endY - 0.5);
				drawTexture(posX - (zoomX / 2), posY - (zoomY / 2), w + zoomX, h + zoomY);
				endScissor();
				
				GLSettings.disableAlpha();
				GLSettings.disableBlend();
			}
			else {
				drawStringC(nullText, midX, midY - (FontRenderer.FONT_HEIGHT / 2), nullTextColor);
			}
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		zoom += (change * 5);
		zoom = NumberUtil.clamp(zoom, 0, Double.MAX_VALUE);
	}
	
	@Override
	public void mouseDragged(int mXIn, int mYIn, int button, long timeSinceLastClick) {
		super.mouseDragged(mXIn, mYIn, button, timeSinceLastClick);
		
		
	}
	
	public EArrayList<GameTexture> getImages() { return images; }
	public int getBorderColor() { return borderColor; }
	public int getBackgroundColor() { return backgroundColor; }
	public long getUpdateInterval() { return updateInterval; }
	public boolean drawsImage() { return drawImage; }
	public boolean drawsBorder() { return drawBorder; }
	public boolean drawsBackground() { return drawBackground; }
	public boolean drawsStretched() { return drawStretched; }
	
	public WindowImageBox<E> setImage(GameTexture imageIn) {
		if (imageIn != null) {
			images = new EArrayList(imageIn);
			singleImage = true;
		}
		return this;
	}
	
	public WindowImageBox<E> setImages(GameTexture... imagesIn) {
		if (imagesIn != null) {
			images = new EArrayList((Object[]) imagesIn);
			singleImage = images.size() == 1;
		}
		return this;
	}
	
	public WindowImageBox<E> setDrawStretched(boolean val) { drawStretched = val; return this; }
	public WindowImageBox<E> setNullText(String textIn) { nullText = textIn; return this; }
	public WindowImageBox<E> setNullTextColor(EColors colorIn) { return setNullTextColor(colorIn.intVal); }
	public WindowImageBox<E> setNullTextColor(int colorIn) { nullTextColor = colorIn; return this; }
	public WindowImageBox<E> setUpdateInterval(long time) { updateInterval = (long) NumberUtil.clamp(time, 0, Long.MAX_VALUE); return this; }
	public WindowImageBox<E> setDrawImage(boolean val) { drawImage = val; return this; }
	public WindowImageBox<E> setDrawBorder(boolean val) { drawBorder = val; return this; }
	public WindowImageBox<E> setDrawBackground(boolean val) { drawBackground = val; return this; }
	public WindowImageBox<E> setCenterImage(boolean val) { centerImage = val; return this; }
	public WindowImageBox<E> setBorderColor(EColors colorIn) { return setBorderColor(colorIn.c()); }
	public WindowImageBox<E> setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowImageBox<E> setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.c()); }
	public WindowImageBox<E> setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	
}
