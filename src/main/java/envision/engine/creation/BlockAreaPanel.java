package envision.engine.creation;

import envision.engine.creation.block.CreatorBlock;
import envision.engine.internal.windows.windowObjects.advanced.WindowScrollList;
import envision.engine.internal.windows.windowTypes.interfaces.IActionObject;
import envision.engine.internal.windows.windowUtil.windowEvents.events.EventFocus;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_i;

public class BlockAreaPanel extends WindowScrollList {
    
    //========
    // Fields
    //========
    
    public static final int INITIAL_WIDTH = 100000;
    public static final int INITIAL_HEIGHT = 100000;
    
    protected double zoom = 1.0;
    
    /** All blocks actively within this block working area. */
    protected final EList<CreatorBlock> blockList = EList.newList();
    /** Blocks that are actively selected. */
    protected final EList<CreatorBlock> currentSelection = EList.newList();
    /** Clipboard memory. */
    protected final EList<CreatorBlock> blockClipboard = EList.newList();
    
    protected Dimension_i areaSpace;
    protected Dimension_i currentView;
    
    private final Point2i pressPoint = new Point2i(-1, -1);
    private boolean pressed = false;
    
    //==============
    // Constructors
    //==============
    
    public BlockAreaPanel() {
        setBackgroundColor(EColors.steel);
        areaSpace = new Dimension_i(0, 0, INITIAL_WIDTH, INITIAL_HEIGHT);
        currentView = new Dimension_i(0, 0, 1000, 1000);
        
        this.setListSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        super.drawObject_i(dt, mXIn, mYIn);
        
        if (pressed) {
            int dX = pressPoint.x - mXIn;
            int dY = pressPoint.y - mYIn;
            pressPoint.set(mXIn, mYIn);
            panArea(dX, dY);
        }
    }
    
    @Override
    public void mousePressed(int mX, int mY, int button) {
        super.mousePressed(mX, mY, button);
        
        System.out.println("PRESSED");
        
        pressed = true;
        pressPoint.set(mX, mY);
    }
    
    @Override
    public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
        super.mouseDragged(mX, mY, button, timeSinceLastClick);
        
        System.out.println("DRAGGED: " + mX + " : " + mY);
    }
    
    @Override
    public void mouseReleased(int mX, int mY, int button) {
        super.mouseReleased(mX, mY, button);
        
        pressed = false;
        pressPoint.set(-1, -1);
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
    
    @Override
    public void onFocusLost(EventFocus eventIn) {
        super.onFocusLost(eventIn);
        
        pressed = false;
        pressPoint.set(-1, -1);
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void panArea(int dx, int dy) {
        if (dx == 0 && dy == 0) return;
        
        int moveX = dx;
        int moveY = dy;
        
        System.out.println("PAN: " + dx + " : " + dy);
        
        if (dx < 0 && (currentView.startX + dx) < areaSpace.startX) {
            moveX = areaSpace.startX - currentView.startX;
        }
        if (dx > 0 && (currentView.endX + dx) > areaSpace.endX) {
            moveX = areaSpace.endX - currentView.endX;
        }
        if (dy < 0 && (currentView.startY + dy) < areaSpace.startY) {
            moveY = areaSpace.startY - currentView.startY;
        }
        if (dy > 0 && (currentView.endY + dy) > areaSpace.endY) {
            moveY = areaSpace.endY - currentView.endY;
        }
        
        //currentView.translate(moveX, moveY);
        
        
        
        // shift all blocks
//        for (var block : blockList) {
//            block.move(-moveX, -moveY);
//        }
    }
    
    //=========
    // Methods
    //=========
    
    public CreatorBlock addBlock(CreatorBlock blockIn, int x, int y) {
        if (blockIn == null) return null;
        
        blockList.add(blockIn);
        blockIn.setPosition(x, y);
        addObjectToList(blockIn);
        
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
                removeObjectFromList(block);
            }
        }
        
        return removed;
    }
    
    public EList<CreatorBlock> getSelectedBlocks() {
        return currentSelection.copy();
    }
    
    public void deleteSelectedBlocks() {
        removeBlocks(currentSelection);
        currentSelection.clear();
    }
    
}
