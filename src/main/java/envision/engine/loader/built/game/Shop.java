package envision.engine.loader.built.game;

import static envision.game.shops.ShopTransactionResult.*;

import envision.Envision;
import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.ShopDTO;
import envision.game.entities.inventory.EntityInventory;
import envision.game.shops.ShopTransactionResult;
import envision.game.shops.Shopkeeper;
import envision.game.shops.TradingWindow;
import eutil.datatypes.util.EList;

public class Shop implements IEngineResource {
    
    //========
    // Fields
    //========
    
    private String shopName;
    private int inventorySize = 10;
    private EntityInventory shopInventory;
    private EList<Shopkeeper> shopkeepers = EList.newList();
    private TradingWindow windowInstance;
    
    /** The modifier that a shop will add or remove to all item values on purchases. */
    private double shopBuyFromModifier = 1.10;
    /** The modifier that a shop will add or remove to all item values when buying from others. */
    private double shopSellToModifier = 0.75;    
    //==============
    // Constructors
    //==============
    
    public Shop() { this("Shop"); }
    public Shop(String shopNameIn) {
        shopName = shopNameIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public ShopDTO toDto() {
        return null;
    }
    
    //================
    // Window Methods
    //================
    
    public void openShopWindow(Shopkeeper shopkeeper, Entity tradingEntity) {
        if (shopkeepers.notContains(shopkeeper)) {
            throw new RuntimeException("The given shopkeeper: " + shopkeeper + " does not belong to this shop!");
        }
        
        if (windowInstance != null) {
            windowInstance.close();
        }
        
        windowInstance = new TradingWindow(this, shopkeeper, tradingEntity);
        
        Envision.getActiveTopParent().displayWindow(windowInstance);
    }
    
    //==============
    // Shop Methods
    //==============
    
    public ShopTransactionResult buyItemAtSlot(Shopkeeper seller, Entity buyer, int slot) {
        if (buyer == null) return fail_buy("The entity trying to buy is null!");
        if (!checkSlot(slot)) return fail_buy("The given item slot '" + slot + "' is out of range!");
        
        return fail_buy("No item selected!");
    }
    
    public ShopTransactionResult sellItem(Item toSell) {
        return fail_sell("Generic failure reason!");
    }
    
    public void addItem(Item in) {
        if (in != null) {
            shopInventory.addItem(in);
        }
    }
    
    public void storeItems(Item... in) {
        for (Item i : in) {
            addItem(i);
        }
    }
    
    /**
     * Internal check to see if the given item slot is actually in this shop
     * inventory's range.
     */
    private boolean checkSlot(int slot) {
        return slot >= 0 && slot < inventorySize;
    }
    
    public Item getItem(String nameIn) {
        for (Item i : shopInventory.getItems()) {
            if (i.getName().contentEquals(nameIn)) {
                return i;
            }
        }
        return null;
    }
    
    public EntityInventory getInventory() {
        return shopInventory;
    }
    
    public EList<Item> getItems() {
        return shopInventory.getItems();
    }
    
    public void addShopkeeper(Shopkeeper in) {
        shopkeepers.addIfNotContains(in);
    }
    
    public void removeShopkeeper(Shopkeeper in) {
        shopkeepers.remove(in);
    }
    
    public boolean isShopOpen() {
        return windowInstance != null && !windowInstance.isClosed();
    }
    
    public void setBuyFromModifier(double modifier) { this.shopBuyFromModifier = modifier; }
    public void setSellToModifier(double modifier) { this.shopSellToModifier = modifier; }
    
    public double getBuyFromModifier() { return this.shopBuyFromModifier; }
    public double getSellToModifier() { return this.shopSellToModifier; }
    
}
