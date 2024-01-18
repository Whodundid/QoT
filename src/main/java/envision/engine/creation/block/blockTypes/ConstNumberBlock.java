package envision.engine.creation.block.blockTypes;

import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import eutil.math.ENumUtil;

public class ConstNumberBlock extends ConstantValueBlock<Number> {

    //========
    // Fields
    //========
    
    private WindowTextField entryBox;
    
    //==============
    // Constructors
    //==============
    
    public ConstNumberBlock() { this("Number", 0L); }
    public ConstNumberBlock(String blockName) { this(blockName, 0L); }
    public ConstNumberBlock(Number value) { this("Number", value); }
    public ConstNumberBlock(String blockName, Number value) {
        super(blockName, value);
        
        setSize(170, 50);
        setResizeable(true);
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        entryBox = new WindowTextField(this, startX + 10, startY + 10, width - 30, height - 20);
        entryBox.setPerformActionOnTextChange(true);
        entryBox.setAction(() -> {
            Number value = ENumUtil.parseNumber(entryBox.getText());
            setValue(value);
        });
        
        addObject(entryBox);
        
        super.initChildren();
    }
    
}
