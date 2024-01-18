package envision.engine.creation.block;

/**
 * A type of creator block that can evaluate the contents of its input(s) and
 * use that to set the value(s) of its output point(s).
 * 
 * @author Hunter
 */
public abstract class FunctionBlock extends CreatorBlock {
    
    //==============
    // Constructors
    //==============
    
    protected FunctionBlock(String blockName) {
        super(blockName);
    }
    
    //===========
    // Abstracts
    //===========
    
    public abstract void evaluate();
    
}
