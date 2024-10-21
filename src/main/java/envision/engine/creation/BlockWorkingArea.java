package envision.engine.creation;

import java.io.File;

import envision.engine.creation.block.CreatorBlock;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_i;

public class BlockWorkingArea extends WindowObject {
    
    public static final int INITIAL_WIDTH = 1000;
    public static final int INITIAL_HEIGHT = 1000;

    //========
    // Fields
    //========
    
    protected double zoom = 1.0;
    
    /** All blocks actively within this block working area. */
    protected final EList<CreatorBlock> blockList = EList.newList();
    /** Blocks that are actively selected. */
    protected final EList<CreatorBlock> currentSelection = EList.newList();
    /** Clipboard memory. */
    protected final EList<CreatorBlock> blockClipboard = EList.newList();
    
    protected Dimension_i areaSpace;
    protected Dimension_i currentView;
    
    //==============
    // Constructors
    //==============
    
    public BlockWorkingArea() {}
    public BlockWorkingArea(File designToLoad) {
        areaSpace = new Dimension_i();
        currentView = new Dimension_i();
    }

    public BlockWorkingArea(BlockWorkingArea areaIn) {
        areaSpace = new Dimension_i(areaIn.areaSpace);
        currentView = new Dimension_i(areaIn.currentView);
        
        blockList.addAll(areaIn.blockList);
        
        zoom = areaIn.zoom;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        super.drawObject_i(dt, mXIn, mYIn);
    }
    
    @Override
    public void mousePressed(int mX, int mY, int button) {
        super.mousePressed(mX, mY, button);
    }
    
    @Override
    public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
        super.mouseDragged(mX, mY, button, timeSinceLastClick);
    }
    
    @Override
    public void mouseReleased(int mX, int mY, int button) {
        super.mouseReleased(mX, mY, button);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        super.keyPressed(typedChar, keyCode);
    }
    
    @Override
    public void keyReleased(char typedChar, int keyCode) {
        super.keyReleased(typedChar, keyCode);
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        super.actionPerformed(object, args);
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void panArea(int dx, int dy) {
        currentView.translate(dx, dy);
        
        // shift all blocks
        for (var block : blockList) {
            block.move(dx, dy);
        }
    }
    
    //=========
    // Methods
    //=========
    
    public CreatorBlock addBlock(CreatorBlock blockIn, int x, int y) {
        if (blockIn == null) return null;
        
        blockList.add(blockIn);
        blockIn.setPosition(x, y);
        
        return blockIn;
    }
    
    public EList<CreatorBlock> removeBlocks(EList<CreatorBlock> toRemove) {
        if (toRemove == null) return null;
        if (toRemove.isEmpty()) return toRemove;
        
        EList<CreatorBlock> removed = EList.newList();
        
        synchronized (blockList) {
            for (CreatorBlock b : toRemove) {
                var block = blockList.getAndRemove(b);
                removed.addIfNotNull(block);
            }
        }
        
        return removed;
    }
    
    public EList<CreatorBlock> getSelectedBlocks() {
        return currentSelection.copy();
    }
    
    public void deleteSelectedBlocks() {
        
    }
    
    public void undoAction() {}
    public void redoAction() {}
    public void copyBlocks() {}
    public void pasteBlocks() {}
    public void cutBlocks() {}
    
}
