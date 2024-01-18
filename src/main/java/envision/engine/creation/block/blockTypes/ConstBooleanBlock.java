package envision.engine.creation.block.blockTypes;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowCheckBox;

public class ConstBooleanBlock extends ConstantValueBlock<Boolean> {

    //========
    // Fields
    //========
    
    private WindowCheckBox checkbox;
    
    //==============
    // Constructors
    //==============
    
    public ConstBooleanBlock() { this("Bool", false); }
    public ConstBooleanBlock(String blockName) { this(blockName, false); }
    public ConstBooleanBlock(boolean value) { this("Bool", value); }
    public ConstBooleanBlock(String blockName, boolean value) {
        super(blockName, value);
        
        setSize(170, 40);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        checkbox = new WindowCheckBox(this, startX + 5, startY + 5, 30, 30);
        checkbox.setAction(() -> setValue(!getValue()));
        
        addObject(checkbox);
        
        super.initChildren();
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        drawString(getValue(), checkbox.endX + 5, checkbox.midY - FontRenderer.HALF_FH + 1);
    }
}
