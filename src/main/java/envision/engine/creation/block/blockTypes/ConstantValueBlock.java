package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;

public abstract class ConstantValueBlock<T> extends FunctionBlock {

    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint<T> output;
    
    //==============
    // Constructors
    //==============
    
    protected ConstantValueBlock(T value) { this("Constant", value); }
    protected ConstantValueBlock(String blockName, T value) {
        super(blockName);
        
        output = createOutputPoint("Output");
        output.setPointLocation(PointLocation.RIGHT);
        output.setValue(value);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void evaluate() {
        // do nothing
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
    }
    
    //=========
    // Getters
    //=========
    
    public T getValue() {
        return output.getValue();
    }
    
    //=========
    // Setters
    //=========
    
    public void setValue(T value) {
        output.setValue(value);
    }
    
}
