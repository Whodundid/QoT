package envision.engine.creation.block.blockTypes.coding;

import java.lang.reflect.Array;
import java.util.List;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import envision.engine.internal.windows.windowObjects.action.WindowTextField;
import envision.engine.internal.windows.windowObjects.basic.WindowImageBox;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.Sprite;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class ArrayIndexSelector extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint arrayInput;
    protected final BlockConnectionPoint elementOutput;
    
    private WindowImageBox textureDisplayer;
    private WindowTextField elementField;
    
    private String inputRange = ":";
    private Object inputObject;
    private int indexStart = 0;
    private int indexEnd = 0;
    private int indexStep = 0;
    // note this could either be a single element or a list of elements
    private Object selectedElements;    
    //==============
    // Constructors
    //==============
    
    public ArrayIndexSelector() { this("ArrayIndexSelector"); }
    public ArrayIndexSelector(String blockName) {
        super(blockName);
        
        arrayInput = createInputPoint("Array", PointLocation.LEFT);
        elementOutput = createOutputPoint("Element", PointLocation.RIGHT);
        
        setSize(170, 100);
        setMinDims(100, 80);
        setResizeable(true);
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        double vgap = 5;
        double hgap = 5;
        double w = width - (hgap * 2);
        double h = 30;
        double sx = startX + hgap;
        double sy = endY - h - vgap;
        
        elementField = new WindowTextField(this, sx, sy, w, h);
        elementField.setText(indexStart);
        elementField.setTextWhenEmpty("index");
        //elementField.setOnlyAcceptNumbers(true);
        elementField.setAction(this::evaluate);
        if (inputRange != null) elementField.setText(inputRange);
        
        double texEY = (elementField.startY - vgap) - (startY + 5);
        textureDisplayer = new WindowImageBox(this, startX + 5, startY + 5, width - 10, texEY);
        
        addObject(elementField);
        addObject(textureDisplayer);
    }
    
    @Override
    public void evaluate() {
        inputObject = arrayInput.getValue();
        selectedElements = null;
        boolean all = false;
        boolean isRange = false;
        boolean goToEnd = false;
        boolean startAtEnd = false;
        boolean reverse = false;
        
        // don't care if the input object is null
        if (inputObject == null) return;
        
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
                if (rangeParts.length > 3) return;
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
                catch (NumberFormatException e) { return; }
            }
        }
        catch (Exception e) {
            return;
        }
        
        EList<Object> rList = null;
        if (isRange) {
            rList = EList.newList();
            selectedElements = rList;
        }
        
        // try to get the element out of the array/list.
        if (inputObject instanceof Array) {
            try {
                int len = Array.getLength(inputObject);
                if (isRange) {
                    indexStart = (all) ? 0 : indexStart;
                    indexEnd = (all || goToEnd) ? len : ((startAtEnd) ? len + indexEnd : indexEnd);
                    if (reverse) {
                        for (int i = indexStart; i > indexEnd; i--)
                            rList.add(Array.get(inputObject, i));
                    }
                    else {
                        for (int i = indexStart; i < indexEnd; i++)
                            rList.add(Array.get(inputObject, i));
                    }
                }
                else {
                    selectedElements = Array.get(inputObject, indexStart);
                }
            }
            catch (Exception e) {}
        }
        else if (inputObject instanceof List<?> l) {
            try {
                int len = l.size();
                if (isRange) {
                    indexStart = (all) ? 0 : indexStart;
                    indexEnd = (all || goToEnd) ? len : ((startAtEnd) ? len + indexEnd : indexEnd);
                    if (reverse) {
                        for (int i = indexStart; i > indexEnd; i--)
                            rList.add(l.get(i));
                    }
                    else {
                        for (int i = indexStart; i < indexEnd; i++)
                            rList.add(l.get(i));
                    }
                }
                else {
                    selectedElements = l.get(indexStart);
                }
            }
            catch (Exception e) {}
        }
        
        elementOutput.setValue(selectedElements);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        textureDisplayer.setVisible(false);
        
        if (selectedElements instanceof GameTexture t) {
            textureDisplayer.setImage(t);
            textureDisplayer.setVisible(true);
        }
        else if (selectedElements instanceof Sprite s) {
            textureDisplayer.setSprite(s);
            textureDisplayer.setVisible(true);
        }
        else if (selectedElements instanceof EList<?> l) {
            int gap = 4;
            int len = l.size();
            len = ENumUtil.clamp(len, 0, 16); // don't want this to get ridiculous
            double workingHeight = elementField.startY - startY;
            
            double wRatio = 1.0;
            double hRatio = 1.0;
            boolean allTextures = true;
            boolean allSprites = true;
            for (var o : l) {
                if (!(o instanceof GameTexture)) allTextures = false;
                if (!(o instanceof Sprite)) allSprites = false;
            }
            
            if (l.isNotEmpty() && (allTextures || allSprites)) {
                if (allTextures) {
                    GameTexture t = (GameTexture) l.get(0);
                    double w = t.getWidth();
                    double h = t.getHeight();
                    wRatio = w / h;
                    hRatio = h / w;
                }
                else if (allSprites) {
                    Sprite s = (Sprite) l.get(0);
                    double w = s.getWidth();
                    double h = s.getHeight();
                    wRatio = w / h;
                    hRatio = h / w;
                }
            }
            
            int drawW = (int) (((width - (len * gap + gap * 6)) / len) * 1);
            int drawH = (int) (drawW * hRatio);
            if ((drawH + gap * 2) > workingHeight) {
                drawH = (int) (workingHeight - gap * 2);
                drawW = (int) (drawH * wRatio);
            }
            int drawX = (int) (midX - ((drawW * len) + (len * gap) - gap) / 2);
            int drawY = (int) (startY + workingHeight / 2 - drawH / 2);
            
            if (allTextures) {
                for (int i = 0; i < len; i++) {
                    GameTexture t = (GameTexture) l.get(i);
                    drawTexture(t, drawX, drawY, drawW * wRatio, drawH);
                }
            }
            else if (allSprites) {
                for (int i = 0; i < len; i++) {
                    Sprite s = (Sprite) l.get(i);
                    drawSprite(s, drawX + (i * drawW + i * gap), drawY, drawW, drawH);
                }
            }
        }
        else if (selectedElements != null) {
            scissor();
            drawStringC(selectedElements, midX, midY - FontRenderer.HALF_FH + 1 - FontRenderer.HALF_FH);
            drawStringC(selectedElements.getClass().getSimpleName(), midX, midY - FontRenderer.HALF_FH + 1 + FontRenderer.HALF_FH);
            endScissor();
        }
        else {
            scissor();
            drawStringC(selectedElements, midX, midY - FontRenderer.HALF_FH + 1);
            endScissor();
        }
    }
    
}
