package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.blockTypes.logic.BufferBlock;
import envision.engine.registry.types.Sprite;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class DisplayBlock extends BufferBlock {
    
    //========
    // Fields
    //========
    
    private WindowImageBox textureDisplay;
    
    //==============
    // Constructors
    //==============
    
    public DisplayBlock() { this("Display"); }
    public DisplayBlock(String blockName) {
        super(blockName);
        
        setSize(200, 200);
        setMinDims(100, 50);
        setResizeable(true);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        double gap = 10;
        double w = width - gap * 2;
        double h = height - gap * 2;
        double sX = midX - w * 0.5;
        
        textureDisplay = new WindowImageBox(this, sX, startY + gap, w, h);
        textureDisplay.setVisible(false);
        
        addObject(textureDisplay);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        textureDisplay.setVisible(false);
        
        if (input.getValue() instanceof GameTexture t) {
            textureDisplay.setImage(t);
            textureDisplay.setVisible(true);
        }
        else if (input.getValue() instanceof EList<?> l) {
            int gap = 4;
            int len = l.size();
            len = ENumUtil.clamp(len, 0, 16); // don't want this to get ridiculous
            
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
            
            int drawW = (int) (((width - (len * gap + gap * 6)) / len) * wRatio);
            int drawH = (int) (drawW * hRatio);
            if ((drawH + gap * 2) > height) {
                drawH = (int) (height - gap * 2);
                drawW = (int) (drawH * wRatio);
            }
            int drawX = (int) (midX - ((drawW * len) + (len * gap) - gap) / 2);
            int drawY = (int) (startY + height / 2 - (drawH / 2 + FontRenderer.FH));
            
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
            drawStringC(input.getValue().getClass().getSimpleName(), midX, drawY + drawH + FontRenderer.HALF_FH);
            drawStringC("size: " + l.size(), midX, drawY + drawH + FontRenderer.HALF_FH + FontRenderer.FH + 3);
        }
        else if (input.getValue() instanceof Sprite s) {
            textureDisplay.setSprite(s);
            textureDisplay.setVisible(true);
        }
        else if (input.getValue() != null) {
            scissor();
            drawStringC(input.getValue(), midX, midY - FontRenderer.HALF_FH + 1 - FontRenderer.HALF_FH);
            drawStringC(input.getValue().getClass().getSimpleName(), midX, midY - FontRenderer.HALF_FH + 1 + FontRenderer.HALF_FH);
            endScissor();
        }
        else {
            scissor();
            drawStringC(input.getValue(), midX, midY - FontRenderer.HALF_FH + 1);
            endScissor();
        }
    }
    
}
