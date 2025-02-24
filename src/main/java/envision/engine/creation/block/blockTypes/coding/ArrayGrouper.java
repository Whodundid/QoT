package envision.engine.creation.block.blockTypes.coding;

import java.lang.reflect.Array;
import java.util.Collection;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.Sprite;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class ArrayGrouper extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint inputA;
    protected final BlockConnectionPoint inputB;
    protected final BlockConnectionPoint inputC;
    protected final BlockConnectionPoint inputD;
    protected final BlockConnectionPoint inputE;
    protected final BlockConnectionPoint arrayOutput;
    
    protected final EList<Object> outputList = EList.newList();    
    //==============
    // Constructors
    //==============
    
    public ArrayGrouper() { this("Array Grouper"); }
    public ArrayGrouper(String blockName) {
        super(blockName);
        
        inputA = createInputPoint("A", PointLocation.LEFT);
        inputB = createInputPoint("B", PointLocation.LEFT);
        inputC = createInputPoint("C", PointLocation.LEFT);
        inputD = createInputPoint("D", PointLocation.LEFT);
        inputE = createInputPoint("E", PointLocation.LEFT);
        arrayOutput = createOutputPoint("OUT", PointLocation.RIGHT);
        
        setSize(200, 100);
        setMinDims(100, 50);
        setResizeable(true);
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
    }
    
    @Override
    public void evaluate() {
        outputList.clear();
        
        // add from each point
        for (var point : inputPoints) {
            addFromPoint(point);            
        }
        
        // combine the output
        arrayOutput.setValue(outputList);
    }
    
    private void addFromPoint(BlockConnectionPoint point) {
        Object value = point.getValue();
        
        if (value != null) {
            if (value instanceof Array) {
                int len = Array.getLength(value);
                for (int i = 0; i < len; i++) {
                    outputList.add(Array.get(value, i));
                }
            }
            else if (value instanceof Collection<?> l) {
                outputList.addAll(l);
            }
            else {
                outputList.add(value);
            }
        }
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        int gap = 4;
        int len = outputList.size();
        len = ENumUtil.clamp(len, 0, 16); // don't want this to get ridiculous
        
        double wRatio = 1.0;
        double hRatio = 1.0;
        boolean allTextures = true;
        boolean allSprites = true;
        for (var o : outputList) {
            if (!(o instanceof GameTexture)) allTextures = false;
            if (!(o instanceof Sprite)) allSprites = false;
        }
        
        if (outputList.isNotEmpty() && (allTextures || allSprites)) {
            if (allTextures) {
                GameTexture t = (GameTexture) outputList.get(0);
                double w = t.getWidth();
                double h = t.getHeight();
                wRatio = w / h;
                hRatio = h / w;
            }
            else if (allSprites) {
                Sprite s = (Sprite) outputList.get(0);
                double w = s.getWidth();
                double h = s.getHeight();
                wRatio = w / h;
                hRatio = h / w;
            }
            
            int drawW = (int) (((width - (len * gap + gap * 6)) / len) * wRatio);
            int drawH = (int) (drawW * hRatio);
            if ((drawH + gap * 2) > height) {
                drawH = (int) (height - gap * 2);
                drawW = (int) (drawH * wRatio);
            }
            int drawX = (int) (midX - ((drawW * len) + (len * gap) - gap) / 2);
            int drawY = (int) (startY + height / 2 - drawH / 2);
            
            if (allTextures) {
                for (int i = 0; i < len; i++) {
                    GameTexture t = (GameTexture) outputList.get(i);
                    drawTexture(t, drawX, drawY, drawW * wRatio, drawH);
                }
            }
            else if (allSprites) {
                for (int i = 0; i < len; i++) {
                    Sprite s = (Sprite) outputList.get(i);
                    drawSprite(s, drawX + (i * drawW + i * gap), drawY, drawW, drawH);
                }
            }
            
            drawStringC(outputList.size(), midX, startY + (height * 0.75));
        }
        else {
            scissor();
            drawStringC(outputList, midX, midY - FontRenderer.HALF_FH + 1 - FontRenderer.HALF_FH);
            drawStringC(outputList.getClass().getSimpleName(), midX, midY - FontRenderer.HALF_FH + 1 + FontRenderer.HALF_FH);
            endScissor();
        }
    }
    
}
