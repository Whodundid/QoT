package envision.engine.creation.block.blockTypes.texture;

import java.io.File;

import org.joml.Vector2f;

import envision.Envision;
import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.internal.kernel.developerDesktop.windows.fileExplorer.FileExplorerWindow;
import envision.engine.internal.kernel.developerDesktop.windows.fileExplorer.MovingFileObject;
import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.internal.rendering.textureSystem.TextureUtil;
import envision.engine.internal.windows.windowObjects.action.WindowButton;
import envision.engine.internal.windows.windowObjects.action.WindowCheckBox;
import envision.engine.internal.windows.windowObjects.action.WindowTextField;
import envision.engine.internal.windows.windowObjects.basic.WindowImageBox;
import envision.engine.internal.windows.windowTypes.DragAndDropObject;
import envision.engine.internal.windows.windowUtil.ObjectPosition;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.Sprite;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_i;

public class TextureInputBlock extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint<GameTexture> textureOutput;
    protected final BlockConnectionPoint arrayOutput;
    protected final BlockConnectionPoint widthOutput;
    protected final BlockConnectionPoint heightOutput;
    protected final BlockConnectionPoint sizeOutput;
    
    protected final EList<Sprite> outputSprites = EList.newList(); 
    protected Sprite singleOutputSprite = null;
    protected int spriteWidth = 32;
    protected int spriteHeight = 32;
    
    private String inputRange = ":";
    private boolean isRange;
    private boolean all;
    private boolean goToEnd;
    private boolean startAtEnd;
    private boolean reverse;
    private int indexStart = 0;
    private int indexEnd = 0;
    private int indexStep = 0;
    
    private double dWidthRatio;
    private double dHeightRatio;
    private double elementsX;
    private double elementsY;
    
    private File imageFile;
    private GameTexture texture;
    private FileExplorerWindow explorer;
    private WindowCheckBox splitTexture;
    private WindowTextField widthField, heightField;
    private WindowTextField elementField;
    
    protected WindowImageBox textureDisplayer;
    protected WindowButton setTexture, clearTexture;    
    //==============
    // Constructors
    //==============
    
    public TextureInputBlock() { this("Texture"); }
    public TextureInputBlock(String blockName) {
        super(blockName);
        
        textureOutput = createOutputPoint("Texture", PointLocation.TOP_RIGHT);
        arrayOutput = createOutputPoint("Sprites", PointLocation.RIGHT);
        sizeOutput = createOutputPoint("Number of Elements", PointLocation.BOT_RIGHT);
        heightOutput = createOutputPoint("Height", PointLocation.BOT_RIGHT);
        widthOutput = createOutputPoint("Width", PointLocation.BOT_RIGHT);
        
        textureOutput.setPointColor(EColors.orange);
        widthOutput.setPointColor(EColors.magenta);
        heightOutput.setPointColor(EColors.magenta);
        sizeOutput.setPointColor(EColors.magenta);
        
        setSize(302, 302);
        setMinDims(200, 200);
        setResizeable(true);
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        double gap = 10;
        double sX = startX + gap;
        double sY = startY + gap;
        double buttonHeight = 30;
        double botSpaceHeight = gap + buttonHeight + gap + buttonHeight;
        double bsX = sX;
        double bsY = endY - botSpaceHeight;
        double eX = startX + width - gap;
        double eY = bsY - gap;
        double w = eX - sX;
        double h = eY - sY;
        double buttonWidth = ENumUtil.clamp((w - gap) * 0.5, 10, 1000);
        double fieldWidth = ENumUtil.clamp((w - buttonHeight - gap * 3) / 3, 10, 1000);
        
        textureDisplayer = new WindowImageBox(this, sX, sY, w, h);
        if (texture != null && texture.hasBeenRegistered()) textureDisplayer.setImage(texture);
        
        setTexture = new WindowButton(this, bsX, bsY, buttonWidth, buttonHeight, "Set");
        clearTexture = new WindowButton(this, setTexture.endX + gap, bsY, buttonWidth, buttonHeight, "Clear");
        
        double y = setTexture.endY + gap;
        
        splitTexture = new WindowCheckBox(this, sX, y, buttonHeight, buttonHeight);
        splitTexture.setHoverText("Split Texture");
        
        widthField = new WindowTextField(this, splitTexture.endX + gap, y, fieldWidth, buttonHeight);
        heightField = new WindowTextField(this, widthField.endX + gap, y, fieldWidth, buttonHeight);
        widthField.setText(spriteWidth);
        heightField.setText(spriteHeight);
        widthField.setTextWhenEmpty("width");
        heightField.setTextWhenEmpty("height");
        widthField.setAction(this::evaluate);
        heightField.setAction(this::evaluate);
        widthField.setEnabled(false);
        heightField.setEnabled(false);
        
        elementField = new WindowTextField(this, heightField.endX + gap, y, fieldWidth, buttonHeight);
        elementField.setText(indexStart);
        elementField.setTextWhenEmpty("index");
        //elementField.setOnlyAcceptNumbers(true);
        elementField.setAction(this::evaluate);
        if (inputRange != null) elementField.setText(inputRange);
        
        setTexture.setAction(this::openFileChooser);
        clearTexture.setAction(this::clearImage);
        splitTexture.setAction(() -> {
            widthField.setEnabled(splitTexture.isChecked());
            heightField.setEnabled(splitTexture.isChecked());
            evaluate();
        });
        
        createObjectGroup(textureDisplayer, setTexture, clearTexture);
        
        addObject(textureDisplayer);
        addObject(setTexture, clearTexture);
        addObject(splitTexture, widthField, heightField, elementField);
    }
    
    @Override
    public void evaluate() {
        outputSprites.clear();
        
        if (texture == null || !splitTexture.isChecked()) {
            textureOutput.setValue(texture);
            arrayOutput.setValue(texture);
            widthOutput.setValue((texture != null) ? texture.getWidth() : null);
            heightOutput.setValue((texture != null) ? texture.getHeight() : null);
            sizeOutput.setValue(0);
            return;
        }
        
        Integer w = null;
        Integer h = null;
        
        // parse width
        try { w = Integer.parseInt(widthField.getText()); }
        catch (Exception e) { e.printStackTrace(); }
        
        // parse height
        try { h = Integer.parseInt(heightField.getText()); }
        catch (Exception e) { e.printStackTrace(); }
        
        widthOutput.setValue(w);
        heightOutput.setValue(h);
        
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
        
        EList<Sprite> rList = evaluateRange();
        if (rList.hasOne()) {
            singleOutputSprite = rList.getFirst();
            sizeOutput.setValue(1);
            arrayOutput.setValue(singleOutputSprite);
        }
        else {
            sizeOutput.setValue(rList.size());
            arrayOutput.setValue(rList);
        }
    }
    
    /**
     * Partition the output list using the entered slice indexing.
     */
    protected EList<Sprite> evaluateRange() {
        all = false;
        isRange = false;
        goToEnd = false;
        startAtEnd = false;
        reverse = false;
        
        EList<Sprite> rList = EList.newList();
        
        // try to get the selected index out of the text field
        try {
            inputRange = elementField.getText().strip();
            if (inputRange.equals(":")) {
                isRange = true;
                all = true;
            }
            else if (inputRange.contains(":")) {
                isRange = true;
                String[] rangeParts = inputRange.split(":", -1);
                
                indexStart = 0;
                indexEnd = 1;
                indexStep = 1;
                
                // if there are more than 3 elements, then this isn't valid
                if (rangeParts.length > 3) return rList;
                if (rangeParts.length >= 1) {
                    if (!rangeParts[0].equals(""))
                        indexStart = Integer.parseInt(rangeParts[0]);
                }
                if (rangeParts.length >= 2) {
                    if (rangeParts[1].equals("")) goToEnd = true;
                    else indexEnd = Integer.parseInt(rangeParts[1]);
                }
                if (rangeParts.length >= 3) {
                    if (!rangeParts[2].equals(""))
                        indexStep = Integer.parseInt(rangeParts[2]);
                }
                
                startAtEnd = indexEnd < 0;
                reverse = indexStep < 0;
            }
            else {
                try { indexStart = indexEnd = Integer.parseInt(inputRange); }
                catch (NumberFormatException e) { return rList; }
            }
        }
        catch (Exception e) {
            return rList;
        }
        
        int len = outputSprites.size();
        if (isRange) {
            indexStart = (all) ? 0 : indexStart;
            indexEnd = (all || goToEnd) ? len : ((startAtEnd) ? len + indexEnd : indexEnd);
            if (reverse) {
                for (int i = indexStart; i > indexEnd; i--)
                    rList.add(outputSprites.get(i));
            }
            else {
                for (int i = indexStart; i < indexEnd; i++)
                    rList.add(outputSprites.get(i));
            }
        }
        else {
            rList.add(outputSprites.get(indexStart));
        }
        
        return rList;
    }
    
    @Override
    public boolean allowsSystemDragAndDrop() {
        return true;
    }
    
    @Override
    public void onSystemDragAndDrop(EList<String> droppedFileNames) {
        if (!droppedFileNames.hasOne()) return;
        
        File file = new File(droppedFileNames.getFirst());
        loadImageFile(file);
    }
    
    @Override
    public void onGroupNotification(WindowObjectEvent e) {
        if (e instanceof EventDragAndDrop d) {
            onDragAndDrop(d.getObjectBeingDropped());
        }
    }
    
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        final MovingFileObject mfo = (MovingFileObject) objectBeingDropped;
        final var files = mfo.getFilesBeingMoved().map(f -> f.getFile().getAbsolutePath());
        onSystemDragAndDrop(files);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject_i(dt, mXIn, mYIn);
        if (!splitTexture.isChecked()) return;
        
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
        
        drawDims.endX -= 1;
        drawDims.endY -= 1;
        
        if (drawDims.contains(mXIn, mYIn) && spriteWidth > 0 && spriteHeight > 0) {
            int offsetX = mXIn - drawDims.startX;
            int offsetY = mYIn - drawDims.startY;
            int x = (int) ((offsetX / widthRatio) / spriteWidth);
            int y = (int) ((offsetY / heightRatio) / spriteHeight);
            int element = (int) (x + y * Math.ceil(elementsX));
            
            double dx = drawX + (x * spriteWidth + spriteWidth / 2) * widthRatio;
            double dy = drawY + (y * spriteHeight + spriteHeight / 2) * heightRatio;
            drawStringC(element, dx, dy - FontRenderer.HALF_FH + 5);
        }
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    public void openFileChooser() {
        if (explorer != null) explorer.close();
        explorer = new FileExplorerWindow(this);
        explorer.setSelectionMode(true);
        explorer.setTitle("Texture Selection");
        explorer.setActionWithArg(this::loadImageFile);
        Envision.displayWindow(explorer, ObjectPosition.SCREEN_CENTER);
    }
    
    private void loadImageFile(File theFile) {
        clearImage();
        
        // make sure it exists and is a texture
        if (!TextureUtil.isFileTexture(theFile)) return;
        
        imageFile = theFile;
        updateTexture();
    }
    
    private void updateTexture() {
        texture = new GameTexture(imageFile);
        TextureSystem.getInstance().reg(texture);
        textureDisplayer.setImage(texture);
        evaluate();
//        textureOutput.setValue(texture);
//        widthOutput.setValue(texture.getWidth());
//        heightOutput.setValue(texture.getHeight());
//        sizeOutput.setValue(1);
    }
    
    private void clearImage() {
        if (texture != null && texture.hasBeenRegistered()) {
            TextureSystem.getInstance().destroyTexture(texture);
        }
        
        imageFile = null;
    }
    
}
