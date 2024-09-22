package envision.engine.windows.windowObjects.basic;

import envision.engine.registry.types.Sprite;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_i;

//Author: Hunter Bragg

public class WindowImageBox extends WindowObject {
    
    //========
    // Fields
    //========
    
    private final EList<GameTexture> images = EList.newList();
    private final EList<Sprite> sprites = EList.newList();
    private int borderColor = EColors.black.intVal;
    private int backgroundColor = EColors.vdgray.intVal;
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
    private double drawX, drawY, drawW, drawH;
    private double zoom = 0, zoomX = 0, zoomY = 0;
    private double panX = 0, panY = 0;
    
    //==============
    // Constructors
    //==============
    
    /**
     * 
     * @param objIn
     * @param xIn
     * @param yIn
     * @param widthIn
     * @param heightIn
     */
    public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) {
        this(objIn, xIn, yIn, widthIn, heightIn, (GameTexture) null);
    }
    
    /**
     * 
     * @param objIn
     * @param xIn
     * @param yIn
     * @param widthIn
     * @param heightIn
     * @param imageIn
     */
    public WindowImageBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, GameTexture imageIn) {
        init(objIn, xIn, yIn, widthIn, heightIn);
        images.add(imageIn);
        singleImage = true;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        if (drawBorder) drawRect(startX, startY, endX, endY, borderColor);
        if (drawBackground) drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor);
        if (!drawImage) return;
        
        boolean isTexture = images.isNotEmpty();
        boolean isSprite = sprites.isNotEmpty();
        
        if ((images.isEmpty() || images.get(0) == null) && (sprites.isEmpty() || sprites.get(0) == null)) {
            scissor();
            drawStringC(nullText, midX, midY - (FontRenderer.FONT_HEIGHT / 2), nullTextColor);
            endScissor();
            return;
        }
        
        GameTexture texToDraw = null;
        Sprite spriteToDraw = null;
        
        if (isTexture) {
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
        }
        else if (isSprite) {
            if (singleImage) {
                spriteToDraw = sprites.get(0);
            }
            else {
                if (System.currentTimeMillis() - timeSince >= updateInterval) {
                    curImage++;
                    if (curImage == sprites.size()) curImage = 0;
                    timeSince = System.currentTimeMillis();
                }
                spriteToDraw = sprites.get(curImage);
            }
        }
        
        double imgWidth = (texToDraw != null) ? texToDraw.getWidth() : ((spriteToDraw != null) ? spriteToDraw.getWidth() : 0);
        double imgHeight = (texToDraw != null) ? texToDraw.getHeight() : ((spriteToDraw != null) ? spriteToDraw.getHeight() : 0);
        double imgRatio = (imgWidth != 0 && imgHeight != 0) ? imgWidth / imgHeight : 0;
        
        double posX = startX + 2;
        double posY = startY + 2;
        double w = width - 4;
        double h = height - 4;
        double widthRatio = w / imgWidth;
        double heightRatio = h / imgHeight;
        //drawString(widthRatio + " : " + heightRatio + " : " + imgRatio, midX, endY - 5);
        //drawString(centerImage + " : " + drawStretched, midX, endY);
        //w /= widthRatio;
        //h /= heightRatio;
        //if (heightRatio < 1.0) h *= heightRatio;
        double smaller = w <= h ? w : h;
        
        if (centerImage) {
            posX = startX + 2 + ((w - smaller) / 2);
            posY = startY + 2 + ((h - smaller) / 2);
            
            w = smaller;
            h = smaller;
//            if (widthRatio == 1.0 && heightRatio == 1.0) {
//                
//            }
//            else if (widthRatio < 1.0) {
//                posX = startX + 2 + ((w - smaller) / 2);
//                posY = startY + 2 + ((h - smaller) / 2);
//                
//                w = smaller;
//                h = smaller;
//            }
//            else if (heightRatio < 1.0) {
//                w = imgWidth;
//                h = imgHeight;
//                
//                zoomX = 10 * ((w / h) * zoom);
//                zoomY = 10 * ((h / w) * zoom);
//                
//                drawX = (int) (posX - (zoomX / 2));
//                drawY = (int) (posY - (zoomY / 2));
//                
//                System.out.println(w + " : " + h);
//                System.out.println(drawX + " : " + drawY);
//                
//                posX = startX + 2 + (w / 2);
//                posY = startY + 2 + (h / 2);
//            }
        }
        else if (!drawStretched) {
            double imgW = 0.0;
            double imgH = 0.0;
            
            if (isTexture) {
                GameTexture cur = images.get(curImage);
                imgW = cur.getWidth();
                imgH = cur.getHeight();
            }
            else if (isSprite) {
                Sprite cur = sprites.get(curImage);
                imgW = cur.getWidth();
                imgH = cur.getHeight();
            }
            
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
        
        zoomX = 10 * ((w / h) * zoom);
        zoomY = 10 * ((h / w) * zoom);
        
        drawX = (int) (posX - (zoomX / 2));
        drawY = (int) (posY - (zoomY / 2));
        drawW = w + zoomX;
        drawH = h + zoomY;
        
        scissor(startX + 1, startY + 1, endX - 0.5, endY - 0.5);
        if (texToDraw != null) {
            drawTexture(texToDraw, drawX, drawY, drawW, drawH);
        }
        else if (spriteToDraw != null) {
            drawSprite(spriteToDraw, drawX, drawY, drawW, drawH);
        }
        endScissor();
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
    
    //=========
    // Getters
    //=========
    
    public EList<GameTexture> getImages() { return images; }
    public EList<Sprite> getSprites() { return sprites; }
    public int getBorderColor() { return borderColor; }
    public int getBackgroundColor() { return backgroundColor; }
    public long getUpdateInterval() { return updateInterval; }
    public boolean drawsImage() { return drawImage; }
    public boolean drawsBorder() { return drawBorder; }
    public boolean drawsBackground() { return drawBackground; }
    public boolean drawsStretched() { return drawStretched; }
    
    public Dimension_i getTextureDrawCoordinates() {
        Dimension_i dims = new Dimension_i(drawX, drawY, drawX + drawW, drawY + drawH);
        return dims;
    }
    
    //=========
    // Setters
    //=========
    
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
        sprites.clear();
        images.clear();
        if (imageIn == null) return;
        images.clearThenAdd(imageIn);
        singleImage = true;
    }
    
    public void setImages(GameTexture... imagesIn) {
        sprites.clear();
        images.clear();
        if (imagesIn != null) return;
        images.clearThenAddA(imagesIn);
        singleImage = images.size() == 1;
    }
    
    public void setSprite(Sprite sprite) {
        sprites.clear();
        images.clear();
        if (sprite == null) return;
        sprites.clearThenAdd(sprite);
        singleImage = true;
    }
    
    public void setSprites(Sprite... spritesIn) {
        sprites.clear();
        images.clear();
        if (spritesIn == null) return;
        sprites.clearThenAdd(spritesIn);
        singleImage = sprites.size() == 1;
    }
    
}
