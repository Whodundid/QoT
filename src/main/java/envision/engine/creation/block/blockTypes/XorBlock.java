package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.rendering.fontRenderer.FontRenderer;

public class XorBlock extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint<?> inputA;
    protected final BlockConnectionPoint<?> inputB;
    protected final BlockConnectionPoint<Boolean> output;
    
    //==============
    // Constructors
    //==============
    
    public XorBlock() { this("Xor"); }
    public XorBlock(String blockName) {
        super(blockName);
        
        output = createOutputPoint("Output", PointLocation.RIGHT);
        inputA = createInputPoint("Xor-A", 1, PointLocation.LEFT);
        inputB = createInputPoint("Xor-B", 1, PointLocation.LEFT);
        
        // set to 'false' by default
        output.setValue(false);
        
        setSize(170, 70);
        setResizeable(false);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void evaluate() {
        if (inputPoints.isEmpty()) {
            output.setValue(false);
            return;
        }
        
        int amount = 0;
        
        if (allBooleans()) {
            for (var point : inputPoints) {
                Boolean value = (Boolean) point.getValue();
                if (value != null && value) amount++;
            }
        }
        else if (allNumbers()) {
            for (var point : inputPoints) {
                Number value = (Number) point.getValue();
                if (value != null && value.intValue() > 0) amount++;
            }
        }
        
        output.setValue(amount % 2 == 1);
    }
    
    protected boolean allBooleans() {
        for (var point : inputPoints) {
            if (point.getValue() == null) continue;
            if (!(point.getValue() instanceof Boolean)) return false;
        }
        return true;
    }
    
    protected boolean allNumbers() {
        for (var point : inputPoints) {
            if (point.getValue() == null) continue;
            if (!(point.getValue() instanceof Number)) return false;
        }
        return true;
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        drawStringCS(output.getValue(), midX, midY - FontRenderer.HALF_FH + 1);
    }
    
}
