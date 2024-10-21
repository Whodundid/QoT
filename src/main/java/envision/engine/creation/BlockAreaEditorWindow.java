package envision.engine.creation;

import envision.engine.assets.WindowTextures;
import envision.engine.windows.windowObjects.advanced.menuBar.WindowMenuBar;
import envision.engine.windows.windowObjects.advanced.textArea.WindowTextArea;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowUtil.layouts.WindowBorderLayout;

public class BlockAreaEditorWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    private WindowMenuBar menuBar;
    private WindowTextArea blockList;
    
    //==============
    // Constructors
    //==============
    
    public BlockAreaEditorWindow() {
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
        
        setLayout(new WindowBorderLayout());
        
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
        
        addObject(menuBar, WindowBorderLayout.NORTH);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        super.drawObject_i(dt, mXIn, mYIn);
    }
    
    //==================
    // Internal Methods
    //==================
    
    public void createNewSpace() { System.out.println("LOLOL"); }
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
