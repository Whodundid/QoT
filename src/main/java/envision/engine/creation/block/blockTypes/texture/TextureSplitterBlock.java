package envision.engine.creation.block.blockTypes.texture;

import org.joml.Vector2f;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.registry.types.Sprite;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowObjects.action.WindowTextField;
import envision.engine.windows.windowObjects.basic.WindowImageBox;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_i;

public class TextureSplitterBlock extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint<GameTexture> textureInput;
    protected final BlockConnectionPoint<EList<Sprite>> textureOutput;
    
    protected final EList<Sprite> outputSprites = EList.newList(); 
    
    protected GameTexture texture;
    protected int spriteWidth = 32;
    protected int spriteHeight = 32;
    
    private double dWidthRatio;
    private double dHeightRatio;
    private double elementsX;
    private double elementsY;
    
    private WindowImageBox textureDisplayer;
    private WindowTextField widthField, heightField;
    
    //==============
    // Constructors
    //==============
    
    public TextureSplitterBlock() { this("Texture Splitter"); }
    public TextureSplitterBlock(String blockName) {
        super(blockName);
        
        textureInput = createInputPoint("Texture", PointLocation.LEFT);
        textureOutput = createOutputPoint("Sprites", PointLocation.RIGHT);
        
        setSize(300, 300);
        setMinDims(300, 300);
        setResizeable(true);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        double vgap = 10;
        double hgap = 10;
        double w = 200;
        double h = 30;
        double sx = startX + 5;
        double sy = endY - h - vgap;
        double ex = (sx + w) - (w / 2 - hgap * 2);
        
        widthField = new WindowTextField(this, sx, sy - vgap - h, w / 2 - hgap * 2, h);
        heightField = new WindowTextField(this, ex, sy - vgap - h, w / 2 - hgap * 2, h);
        widthField.setText(spriteWidth);
        heightField.setText(spriteHeight);
        widthField.setTextWhenEmpty("width");
        heightField.setTextWhenEmpty("height");
        widthField.setAction(this::evaluate);
        heightField.setAction(this::evaluate);
        
        double texEY = (widthField.startY - vgap) - (startY + 5);
        textureDisplayer = new WindowImageBox(this, startX + 5, startY + 5, width - 10, texEY);
        
        try {
            GameTexture texture = textureInput.getValue();
            if (texture != null && texture.hasBeenRegistered()) textureDisplayer.setImage(texture);
        }
        catch (Exception e) {}
        
        addObject(widthField, heightField);
        addObject(textureDisplayer);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject_i(dt, mXIn, mYIn);
        if (texture == null) return;
        
        var drawDims = textureDisplayer.getTextureDrawCoordinates();
        double drawX = drawDims.startX_d();
        double drawY = drawDims.startY_d();
        double widthRatio = 1.0;
        double heightRatio = 1.0;
        
        final var texDims = textureDisplayer.getDimensions();
        
        scissor(texDims);
        if (spriteWidth > 0) {
            widthRatio = drawDims.width / (spriteWidth * elementsX);
            for (int i = 0; i <= ((int) elementsX); i++) {
                double x = drawX + (i * spriteWidth) * widthRatio;
                //double ey = drawY + (drawDims.height * dHeightRatio * (int) elementsY) + 1;
                double ey = drawY + drawDims.height + 1;
                drawRect(x, drawY, x + 1, ey, EColors.red);
            }
        }
        
        if (spriteHeight > 0) {
            heightRatio = drawDims.height / (spriteHeight * elementsY);
            for (int i = 0; i <= elementsY; i++) {
                double y = drawY + (i * spriteHeight) * heightRatio;
                double ex = drawX + drawDims.width;
                drawRect(drawX, y, ex, y + 1, EColors.red);
            }
        }
        endScissor();
        
        if (drawDims.contains(mXIn, mYIn) && spriteWidth > 0 && spriteHeight > 0) {
            int offsetX = mXIn - drawDims.startX;
            int offsetY = mYIn - drawDims.startY;
            int x = (int) ((offsetX / widthRatio) / spriteWidth);
            int y = (int) ((offsetY / heightRatio) / spriteHeight);
            int element = (int) (x + y * Math.ceil(elementsX));
            
            drawStringC("index: " + element, midX, endY - FontRenderer.FH - 5);
        }
    }
    
    @Override
    public void evaluate() {
        texture = null;
        outputSprites.clear();
        textureDisplayer.setImage(null);
        
        try { texture = textureInput.getValue(); }
        catch (Exception e) {}
        if (texture == null) {
            textureOutput.setValue(outputSprites);
            return;
        }
        
        if (!texture.hasBeenRegistered()) return;
        textureDisplayer.setImage(texture);
        
        Integer w = null;
        Integer h = null;
        
        // parse width
        try { w = Integer.parseInt(widthField.getText()); }
        catch (Exception e) { e.printStackTrace(); }
        
        // parse height
        try { h = Integer.parseInt(heightField.getText()); }
        catch (Exception e) { e.printStackTrace(); }
        
        // if both are not null, then try to partition the image into smaller pieces
        if (w != null && h != null) {
            if (w < 0 || h < 0) return;
            spriteWidth = w;
            spriteHeight = h;
            
            //BufferedImage img = convertToBufferedImage(texture);
            //if (img == null) return;
            
            int tWidth = texture.getWidth();
            int tHeight = texture.getHeight();
            
            elementsX = (double) tWidth / (double) spriteWidth;
            elementsY = (double) tHeight / (double) spriteHeight;
            dWidthRatio = (double) spriteWidth / tWidth;
            dHeightRatio = (double) spriteHeight / tHeight;
            
            for (int y = 0; y < tHeight; y += spriteHeight) {
                for (int x = 0; x < tWidth; x += spriteWidth) {
                    float maxX = ENumUtil.clamp(x + spriteWidth, 0, tWidth);
                    float maxY = ENumUtil.clamp(y + spriteHeight, 0, tHeight);
                    
                    float minXRatio = (float) x / (float) tWidth;
                    float minYRatio = (float) y / (float) tHeight;
                    float maxXRatio = (float) maxX / (float) tWidth;
                    float maxYRatio = (float) maxY / (float) tHeight;
                    
                    Vector2f br = new Vector2f(maxXRatio, maxYRatio);
                    Vector2f tr = new Vector2f(maxXRatio, minYRatio);
                    Vector2f tl = new Vector2f(minXRatio, minYRatio);
                    Vector2f bl = new Vector2f(minXRatio, maxYRatio);
                    
                    Vector2f[] textureCoords = new Vector2f[] { br, tr, tl, bl };
                    Dimension_i location = new Dimension_i(x, y, maxX, maxY);
                    
//                    System.out.println(x + ", " + y + ", " + spriteWidth + ", " + spriteHeight + " | "
//                                     + minXRatio + ", " + minYRatio + ", " + maxXRatio + ", " + maxYRatio);
                    
                    var sprite = new Sprite(texture, textureCoords, location);
                    outputSprites.add(sprite);
                }
            }
        }
        
        textureOutput.setValue(outputSprites);
    }
    
}
