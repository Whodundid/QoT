package envision.engine.creation.block.blockTypes;

import envision.engine.rendering.fontRenderer.FontRenderer;

public class DisplayBlock extends BufferBlock {
    
    //==============
    // Constructors
    //==============
    
    public DisplayBlock() { this("Display"); }
    public DisplayBlock(String blockName) {
        super(blockName);
        
        setSize(200, 200);
        setResizeable(true);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        if (input.getValue() != null) {
            drawStringC(input.getValue(), midX, midY - FontRenderer.HALF_FH + 1 - FontRenderer.HALF_FH);
            drawStringC(input.getValue().getClass().getSimpleName(), midX, midY - FontRenderer.HALF_FH + 1 + FontRenderer.HALF_FH);
        }
        else {
            drawStringC(input.getValue(), midX, midY - FontRenderer.HALF_FH + 1);
        }
    }
    
}
