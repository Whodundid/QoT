package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.FunctionBlock;
import envision.engine.creation.block.PointLocation;
import envision.engine.rendering.fontRenderer.FontRenderer;
import eutil.EUtil;

public class EqualityBlock extends FunctionBlock {
    
    //========
    // Fields
    //========
    
    protected final BlockConnectionPoint<?> inputA;
    protected final BlockConnectionPoint<?> inputB;
    protected final BlockConnectionPoint<Boolean> output;
    
    //==============
    // Constructors
    //==============
    
    public EqualityBlock() { this("Equal"); }
    public EqualityBlock(String blockName) {
        super(blockName);
        
        output = createOutputPoint("Output", PointLocation.RIGHT);
        inputA = createInputPoint("A", 1, PointLocation.LEFT);
        inputB = createInputPoint("B", 1, PointLocation.LEFT);
        
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
        
        boolean equal = true;
        BlockConnectionPoint<?> previous = null;
        
        for (var point : inputPoints) {
            if (previous != null) {
                equal &= EUtil.isEqual(previous.getValue(), point.getValue());
            }
            previous = point;
        }
        
        output.setValue(equal);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        drawStringCS(output.getValue(), midX, midY - FontRenderer.HALF_FH + 1);
    }
    
}
