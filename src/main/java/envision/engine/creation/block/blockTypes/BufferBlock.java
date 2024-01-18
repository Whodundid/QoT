package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import eutil.colors.EColors;

public class BufferBlock extends FunctionBlock {

    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint input;
    protected final BlockConnectionPoint output;
    
    //==============
    // Constructors
    //==============
    
    public BufferBlock() { this("Buffer"); }
    public BufferBlock(String blockName) {
        super(blockName);
        
        input = createInputPoint("Input", 1, PointLocation.LEFT);
        output = createOutputPoint("Output", PointLocation.RIGHT);
        
        setSize(150, 50);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        drawRect(startX, midY - 2, endX, midY + 2, EColors.black);
    }
    
    @Override
    public void evaluate() {
        output.setValue(input.getValue());
    }
    
}
