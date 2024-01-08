package qot.screens.character;

import envision.Envision;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import envision.game.entities.Entity;
import envision.game.entities.inventory.EntityInventory;
import envision.game.items.Item;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class InventoryRenderer extends WindowObject {
    
    //========
    // Fields
    //========
    
    /** The entity for which this inventory pertains to. */
    protected Entity theEntity;
    protected EntityInventory theInventory;
    
    protected EList<InventorySlot> inventorySlots;
    protected int maxInventoryCols = 5;
    protected int slotSize = 70; // 30x30 px by default
    protected InventorySlot slotInside;
    protected InventorySlot clickedSlot; // keeping track of moving items between slots
    protected InventorySlot lastClickedSlot;
    
    protected double itemTextX, itemTextY;
    protected boolean drawItemName = false;
    
    protected boolean allowItemMoving = true;
    protected int workingInventorySize;
    
    public int borderColor = EColors.black.intVal;
    public int slotFrameColor = EColors.black.intVal;
    public int slotBackColor = EColors.dgray.intVal;
    public int slotHoverColor = EColors.yellow.intVal;
    public int slotMovingItemColor = EColors.chalk.opacity(50);
    public int slotFillerColor = EColors.pdgray.intVal;
    
    //==============
    // Constructors
    //==============
    
    public InventoryRenderer(Entity entIn) {
        this(entIn, 5, -1);
    }
    
    public InventoryRenderer(Entity entIn, int columns, int padToAmount) {
        theEntity = entIn;
        theInventory = theEntity.getInventory();
        maxInventoryCols = columns;
        
        workingInventorySize = theInventory.size();
        if (padToAmount > 0 && padToAmount > workingInventorySize) {
            workingInventorySize = padToAmount;
        }
        
        double s = slotSize;
        double cols = ENumUtil.clamp(workingInventorySize, workingInventorySize, columns);
        double rows = workingInventorySize / cols;
        
        setDimensions(startX, startY, cols * s, rows * s);
        
        itemTextX = midX;
        itemTextY = startY - 40;
        
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        Envision.getCurrentScreen().registerListener(this);
        
        buildCharacterInventory();
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        slotInside = null;
        drawRect(borderColor);
        drawRect(slotBackColor, 2);
        
        super.drawObject_i(dt, mXIn, mYIn);
        
        // draw hovered item name (if there is one)
        if (slotInside != null) {
            int index = slotInside.getSlotIndex();
            var item = getItemAtIndex(index);
            
            if (item != null) {
                String name = item.getName();
                if (drawItemName) drawString(name, itemTextX - FontRenderer.strWidth(name) / 2.0, itemTextY);
            }
        }
        
        if (clickedSlot != null) {
            int index = clickedSlot.getSlotIndex();
            var item = getItemAtIndex(index);
            
            if (item != null) {
                drawSprite(item.getSprite(), mXIn - slotSize / 2, mYIn - slotSize / 2, slotSize, slotSize);
            }
        }
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        if (e.getEventParent() == Envision.getCurrentScreen()) {
            if (e instanceof EventMouse me && me.getMouseType() == MouseType.RELEASED) {
                var ms = getMovingSlot();
                if (ms != null && !isMouseInside()) {
                    if (allowItemMoving) onSlotLeftClick(null);
                }
            }
        }
    }
    
    @Override
    public void setPosition(double newX, double newY) {
        super.setPosition(newX, newY);
        
        double dx = newX - startX;
        double dy = newY - startY;
        
        itemTextX += dx;
        itemTextY += dy;
    }
    
    @Override
    public void move(double newX, double newY) {
        super.move(newX, newY);
        
        itemTextX += newX;
        itemTextY += newY;
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Creates a viewable/interactable inventory for the current character.
     */
    protected void buildCharacterInventory() {
        getChildren().clear();
        int size = workingInventorySize;
        inventorySlots = new EArrayList<>(size);
        
        // create an inventory slot for each slot in the inventory
        for (int i = 0; i < size; i++) {
            var slot = new InventorySlot(this, i);
            inventorySlots.add(slot);
            addObject(slot);
            if (getParent().getObjectGroup() != null) {
                getParent().getObjectGroup().addObject(slot);
            }
        }
        
        double s = slotSize;
        
        //the starting X coordinates (top left)
        double invenX = startX;
        //the starting Y coordinates (top left)
        double invenY = startY;
        //used to keep track of the current item column position
        int colPos = 0;
        //keeps track of the current row that items are being positioned on
        int rowNum = 0;
        
        for (int i = 0; i < size; i++) {
            var slot = inventorySlots.get(i);
            
            // reset column position back to zero if over max col length
            // also increment row num
            if (colPos >= maxInventoryCols) {
                colPos = 0;
                rowNum++;
            }
            
            double x = invenX + (colPos * s);
            double y = invenY + (rowNum * s);
            
            slot.setDimensions(x, y, s, s); // w/h same for square slot
            
            colPos++;
        }
    }
    
    public Item getItemAtIndex(int index) {
        return theInventory.getItemAtIndex(index);
    }
    
    public void swapItems(int indexA, int indexB) {
        if (!allowItemMoving) return;
        theInventory.swapItems(indexA, indexB);
    }
    
    public void dropItem(int index) { theEntity.dropItem(index); }
    public void removeItem(int index) { theInventory.setItem(index, null); }
    public int getInventorySize() { return theInventory.size(); }
    
    public void useItem(InventorySlot slot) {
        if (slot == null) return;
        int index = slot.getSlotIndex();
        var item = theInventory.getItemAtIndex(index);
        
        if (item == null) return;
        if (!item.isUsable()) return;
        
        item.onItemUse(theEntity);
        
        if (item.isDestroyedOnUse()) { removeItem(index); }
    }
    
    public InventorySlot getMovingSlot() { return clickedSlot; }
    public InventorySlot getLastClickedSlot() { return lastClickedSlot; }
    protected void setSlotInside(InventorySlot slot) { slotInside = slot; }
    
    protected void onSlotLeftClick(InventorySlot slot) {
        if (allowItemMoving) clickedSlot = slot;
        lastClickedSlot = slot;
    }
    
    public void clearSelectedItem() {
        clickedSlot = null;
    }
    
    public void clearLastClickedSlot() {
        lastClickedSlot = null;
    }
    
    public void setLastClickedSlot(InventorySlot slot) {
        lastClickedSlot = slot;
    }
    
    public boolean isItemMovingAllowed() { return allowItemMoving; }
    
    public void setItemTextPosition(double xIn, double yIn) {
        itemTextX = xIn;
        itemTextY = yIn;
    }
    
    public void setDrawItemName(boolean val) { drawItemName = val; }
    public void setItemMovingAllowed(boolean val) { allowItemMoving = val; }
    
    public void setBorderColor(EColors c) { setBorderColor(c.intVal); }
    public void setSlotFrameColor(EColors c) { setSlotFrameColor(c.intVal); }
    public void setSlotBackColor(EColors c) { setSlotBackColor(c.intVal); }
    public void setSlotHoverColor(EColors c) { setSlotHoverColor(c.intVal); }
    public void setSlotMovingItemColor(EColors c) { setSlotMovingItemColor(c.intVal); }
    public void setSlotFillerColor(EColors c) { setSlotFillerColor(c.intVal); }
    
    public void setBorderColor(int c) { borderColor = c; }
    public void setSlotFrameColor(int c) { slotFrameColor = c; }
    public void setSlotBackColor(int c) { slotBackColor = c; }
    public void setSlotHoverColor(int c) { slotHoverColor = c; }
    public void setSlotMovingItemColor(int c) { slotMovingItemColor = c; }
    public void setSlotFillerColor(int c) { slotFillerColor = c; }
    
}
