package envision.engine.creation.block;

import envision.engine.windows.windowTypes.WindowParent;

public class CreatorBlockVisual extends WindowParent {
    
    //========
    // Fields
    //========
    
    private final CreatorBlock block;
    
    //==============
    // Constructors
    //==============
    
    public CreatorBlockVisual(CreatorBlock blockIn) {
        block = blockIn;
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("New Window");
        setSize(400, 400);
        setMinDims(200, 200);
        setResizeable(true);
        setMaximizable(false);
        setCloseable(false);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        defaultHeader();
        
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        
        
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected void drawInputs() {
        
    }
    
    protected void drawOutputs() {
        
    }
    
}
