package envision.engine.creation;

import envision.engine.assets.WindowTextures;
import envision.engine.creation.block.CreatorBlock;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.menuBar.WindowMenuBar;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea;
import envision.engine.windows.windowTypes.WindowParent;
import eutil.datatypes.util.EList;

public class BlockWorkingArea extends WindowParent {
    
    //========
    // Fields
    //========
    
    private double zoom = 1.0;
    private final EList<CreatorBlock> blocks = EList.newList();
    private final int INITIAL_WIDTH = 1000;
    private final int INITIAL_HEIGHT = 1000;
    private int areaWidth = INITIAL_WIDTH;
    private int areaHeight = INITIAL_HEIGHT;
    private int currentX = 0;
    private int currentY = 0;
    
    private WindowMenuBar menuBar;
    private WindowButton createBlock;
    private WindowTextArea blockList;
    
    //==============
    // Constructors
    //==============
    
    public BlockWorkingArea() {
        this.windowIcon = WindowTextures.refresh;
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
        setMaximizable(true);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        defaultHeader();
        
        menuBar = new WindowMenuBar(this);
        var fileMenu = menuBar.addMenuCategory("File");
        var editMenu = menuBar.addMenuCategory("Edit");
        
        fileMenu.addMenuEntry("New", this::createNewSpace);
        fileMenu.addMenuEntry("Load", this::loadSpace);
        fileMenu.addMenuEntry("Save", this::saveSpace);
        fileMenu.addMenuEntry("Close", this::closeCurrentSpace);
        fileMenu.addMenuEntry("Close All", this::closeAllSpaces);
        
        editMenu.addMenuEntry("Undo", this::undoAction);
        editMenu.addMenuEntry("Redo", this::redoAction);
        editMenu.addMenuEntry("Copy", this::copyBlocks);
        editMenu.addMenuEntry("Cut", this::cutBlocks);
        editMenu.addMenuEntry("Paste", this::pasteBlocks);
        
        addObject(menuBar);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject_i(dt, mXIn, mYIn);
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void panArea(int mXIn, int mYIn) {
        int dX = currentX - mXIn;
        int dY = currentY - mYIn;
        currentX += dX;
        currentY += dY;
        
        boolean blocked = false;
        // move all blocks
        for (var block : blocks) {
            
        }
    }
    
    public void createNewSpace() {}
    public void loadSpace() {}
    public void saveSpace() {}
    public void closeCurrentSpace() {}
    public void closeAllSpaces() {}
    
    public void undoAction() {}
    public void redoAction() {}
    public void copyBlocks() {}
    public void pasteBlocks() {}
    public void cutBlocks() {}
    
}
