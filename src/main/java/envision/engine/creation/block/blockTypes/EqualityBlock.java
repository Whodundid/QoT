package envision.engine.creation.block.blockTypes;

import envision.engine.creation.block.CreatorBlock;
import envision.engine.creation.block.FunctionBlock;
import eutil.EUtil;

public class EqualityBlock extends FunctionBlock {
    
    //==============
    // Constructors
    //==============
    
    protected EqualityBlock() {
        super("Equality");
        
        // set to 'false' by default
        setValue(false);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void evaluate() {
        if (inputConnections.isEmpty()) {
            setValue(false);
            return;
        }
        
        boolean equal = true;
        CreatorBlock previous = null;
        
        for (var point : inputConnections.keySet()) {
            CreatorBlock input = inputConnections.get(point);
            if (previous != null) {
                equal &= EUtil.isEqual(previous.getValue(), input.getValue());
            }
            previous = input;
        }
        
        setValue(equal);
    }
    
}
