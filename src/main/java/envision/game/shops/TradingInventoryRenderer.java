package envision.game.shops;

import envision.Envision;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import envision.game.entities.Entity;
import envision.game.items.Item;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import qot.screens.character.InventoryRenderer;

public class TradingInventoryRenderer extends InventoryRenderer {
    
    //========
    // Fields
    //========
    
    protected TradingWindow shop;
    
    protected double itemPriceX, itemPriceY;
    protected boolean drawItemPrice = false;
    
    protected double priceModifier = 1.00;
    
    //==============
    // Constructors
    //==============
    
    public TradingInventoryRenderer(TradingWindow shopIn, Entity entIn) {
        this(shopIn, entIn, 5, -1);
    }
    
    public TradingInventoryRenderer(TradingWindow shopIn, Entity entIn, int columns, int padToAmount) {
        super(entIn, columns, padToAmount);
        
        shop = shopIn;
        
        itemPriceX = midX;
        itemPriceY = startY - 30;
        
        allowItemMoving = false;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject_i(long dt, int mXIn, int mYIn) {
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
                
                if (drawItemPrice) {
                    Integer price = getSelectedItemPrice();
                    if (price != null) {
                        String priceString = price + " Gold";
                        drawString(priceString, itemPriceX - FontRenderer.strWidth(priceString) / 2.0, itemPriceY);
                    }
                }
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
    public void setPosition(double newX, double newY) {
        super.setPosition(newX, newY);
        
        double dx = newX - startX;
        double dy = newY - startY;
        
        itemPriceX += dx;
        itemPriceY += dy;
    }
    
    @Override
    public void move(double newX, double newY) {
        super.move(newX, newY);
        
        itemPriceX += newX;
        itemPriceY += newY;
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        if (e.getEventParent() == Envision.getCurrentScreen()) {
            if (e instanceof EventMouse me && me.getMouseType() == MouseType.RELEASED) {
                
            }
        }
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }
    
    /**
     * Creates a viewable/interactable inventory for the current character.
     */
    @Override
    protected void buildCharacterInventory() {
        getChildren().clear();
        int size = workingInventorySize;
        inventorySlots = new EArrayList<>(size);
        
        // create an inventory slot for each slot in the inventory
        for (int i = 0; i < size; i++) {
            var slot = new TradingInventorySlot(this, i);
            inventorySlots.add(slot);
            addObject(slot);
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
    
    //=========
    // Methods
    //=========
    
    protected void onItemSlotClicked(int slotIn) {
        shop.onInventoryClick(this, slotIn);
    }
    
    //=========
    // Getters
    //=========
    
    public double getPriceModifier() {
        return this.priceModifier;
    }
    
    public boolean isItemPriceDrawn() {
        return drawItemPrice;
    }
    
    public Integer getSelectedItemPrice() {
        var item = getSelectedItem();
        if (item == null) return null;
        int price = item.getBasePrice();
        int modifiedPrice = (int) Math.ceil(((double) price) * priceModifier);
        price = ENumUtil.clamp(modifiedPrice, 0, Integer.MAX_VALUE);
        return price;
    }
    
    public Item getSelectedItem() {
        if (lastClickedSlot == null) return null;
        int index = lastClickedSlot.getSlotIndex();
        return getItemAtIndex(index);
    }
    
    //=========
    // Setters
    //=========
    
    public void setItemPriceTextPosition(double xIn, double yIn) {
        itemPriceX = xIn;
        itemPriceY = yIn;
    }
    
    public void setDrawItemPrice(boolean val) {
        this.drawItemPrice = val;
    }
    
    public void setPriceModifier(double val) {
        this.priceModifier = val;
    }
    
}
