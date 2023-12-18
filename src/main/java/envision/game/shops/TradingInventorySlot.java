package envision.game.shops;

import eutil.colors.EColors;
import qot.screens.character.InventorySlot;

/**
 * Wraps an item that can be sold from a shop, but keeps track of the
 * item's price.
 * 
 * @author Hunter Bragg
 */
public class TradingInventorySlot extends InventorySlot {
    
    //========
    // Fields
    //========
    
    private TradingInventoryRenderer inventory;
    
    //==============
    // Constructors
    //==============
    
    public TradingInventorySlot(TradingInventoryRenderer inventoryIn, int slotIn) {
        super(inventoryIn, slotIn);
        inventory = inventoryIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject(long dt, int mXIn, int mYIn) {
        super.drawObject(dt, mXIn, mYIn);
        
//        double s = width * 0.08; // 10% offset of w/h
//        double sx = startX + s;
//        double sy = startY + s;
//        double ex = endX - s;
//        double ey = endY - s;
//        double w = width - s * 2;
//        double h = w;
        
        if (inventory.getLastClickedSlot() == this) {
            drawHRect(EColors.yellow, 2, 0);
        }
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);
        
        inventory.onItemSlotClicked(getSlotIndex());
    }
    
    @Override
    public void mouseReleased(int mXIn, int mYIn, int button) {
        super.mouseReleased(mXIn, mYIn, button);
    }
    
    //=========
    // Methods
    //=========
    
    //=========
    // Getters
    //=========
    
    public TradingInventoryRenderer getTradingInventory() { return inventory; }
    
    //=========
    // Setters
    //=========
    
}
