package envision.engine.creation.block.blockTypes;

import envision.engine.windows.windowObjects.actionObjects.WindowTextField;

public class ConstStringBlock extends ConstantValueBlock<String> {

    //========
    // Fields
    //========
    
    private WindowTextField entryBox;
    
    //==============
    // Constructors
    //==============
    
    public ConstStringBlock() { this("String", ""); }
    public ConstStringBlock(String value) { this("String", value); }
    public ConstStringBlock(String blockName, String value) {
        super(blockName, value);
        
        setSize(170, 70);
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
            String value = entryBox.getText();
            setValue(value);
        });
        
        addObject(entryBox);
        
        super.initChildren();
    }
    
}
