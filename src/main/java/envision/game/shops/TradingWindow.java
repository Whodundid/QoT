package envision.game.shops;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.events.EventKeyboard;
import envision.game.entities.Entity;
import envision.game.items.Item;
import eutil.EUtil;
import eutil.colors.EColors;

public class TradingWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    private Shop theShop;
    private Shopkeeper shopkeeper;
    private Entity shopkeeperEntity;
    private Entity tradingEntity;
    
    private TradingInventoryRenderer shopInventoryRenderer;
    private TradingInventoryRenderer playerInventoryRenderer;
    
    /** Used to display the entity you are trading with's face. */
    protected WindowImageBox tradingEntityFaceDisplay;
    
    /** Displays the shop's favor status with the trading entity. (Influences prices) */
    private WindowLabel shopFavorLabel;
    /** Displays the value of the trade (buying or selling). */
    private WindowLabel tradeValueLabel;
    
    private WindowButton buy, sell, cancel;
    
    private TradingInventoryRenderer lastClickedInventory;
    private int lastClickedSlot = -1;
    private Item selectedItem = null;
    private Integer selectedItemPrice = null;
    private boolean isBuying = false;
    
    protected double gap = 30.0;
    protected double imaginaryEndPosition;
    protected double itemTextX, itemTextY;
    protected double itemPriceX, itemPriceY;
    
    //==============
    // Constructors
    //==============
    
    public TradingWindow(Shop theShopIn, Shopkeeper shopkeeperIn, Entity tradingEntityIn) {
        theShop = theShopIn;
        shopkeeper = shopkeeperIn;
        shopkeeperEntity = (Entity) shopkeeperIn;
        tradingEntity = tradingEntityIn;
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("Shop");
        setGuiSize(400, 450);
        setResizeable(false);
        setMinimizable(false);
        setMaximizable(false);
        EObjectGroup group = new EObjectGroup();
        this.setObjectGroup(group);
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        final var pir = playerInventoryRenderer = new TradingInventoryRenderer(this, tradingEntity);
        final int size = playerInventoryRenderer.getInventorySize();
        final var sir = shopInventoryRenderer = new TradingInventoryRenderer(this, shopkeeperEntity, 5, size);
        
        pir.setItemMovingAllowed(true);
        
        //double faceWidth = 300;
        gap = 30.0;
        double xPos = startX + gap;
        double yPos = midY - pir.height / 2;
        double imaginaryMiddleGap = 50;
        
        pir.setPosition(xPos, yPos);
        imaginaryEndPosition = pir.endX + imaginaryMiddleGap + sir.width + gap;
        //double imaginaryEndGap = imaginaryEndPosition - pir.endX;
        double shopStartX = pir.endX + imaginaryMiddleGap;
        sir.setPosition(shopStartX, yPos);
        double inventoryEndX = imaginaryEndPosition - startX;
        
        setSize(inventoryEndX/* + faceWidth*/, height + 100);
        defaultHeader();
        
        itemTextX = startX + (imaginaryEndPosition - startX) * 0.5;
        itemTextY = sir.endY + 40;
        itemPriceX = itemTextX;
        itemPriceY = itemTextY + 35;
        pir.setItemTextPosition(itemTextX, itemTextY);
        sir.setItemTextPosition(itemTextX, itemTextY);
        
        pir.setPriceModifier(theShop.getSellToModifier());
        sir.setPriceModifier(theShop.getBuyFromModifier());
        //pir.setDrawItemPrice(true);
        //sir.setDrawItemPrice(true);
        pir.setItemPriceTextPosition(itemPriceX, itemPriceY);
        sir.setItemPriceTextPosition(itemPriceX, itemPriceY);
        
        pir.setBorderColor(EColors.gray);
        pir.setSlotFrameColor(EColors.gray);
        pir.setSlotBackColor(EColors.lgray.brightness(240));
        pir.setSlotFillerColor(EColors.gray);
        sir.setBorderColor(EColors.gray);
        sir.setSlotFrameColor(EColors.gray);
        sir.setSlotBackColor(EColors.lgray.brightness(240));
        sir.setSlotFillerColor(EColors.gray);
        
        addObject(shopInventoryRenderer);
        addObject(playerInventoryRenderer);
        
//        tradingEntityFaceDisplay = new WindowImageBox(this, imaginaryEndPosition, startY + 1, faceWidth, faceWidth);
//        tradingEntityFaceDisplay.setImage(shopkeeperEntity.getSprite().getTexture());
//        addObject(tradingEntityFaceDisplay);
        
        double bWidth = width / 5;
        double bHeight = 40;
        double bSX = midX - bWidth * 0.5;
        double bSY = endY - gap - bHeight;
        buy = new WindowButton(this, bSX, bSY, bWidth, bHeight, "Buy");
        sell = new WindowButton(this, bSX, bSY, bWidth, bHeight, "Sell");
        buy.setAction(this::buyItem);
        sell.setAction(this::sellItem);
        IWindowObject.setEnabled(false, buy, sell);
        sell.setVisible(false);
        addObject(sell, buy);
        
        getObjectGroup().clear();
        getObjectGroup().addObject(this);
        getObjectGroup().addObject(pir, sir, buy, sell);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        checkMouseHover();
        
        for (int i = 0; i < 10; i++) {            
            drawRect(EColors.chalk.brightness(255 - i * 9), (i * 1) + 1);
        }
        
        String tradingEntityText =  EColors.lime + tradingEntity.getName() + " " + EColors.mc_gold + tradingEntity.getGold();
        String shopEntityText = EColors.lime + shopkeeperEntity.getName() + " " + EColors.mc_gold + shopkeeperEntity.getGold();
        
        drawRect(playerInventoryRenderer, EColors.lgray, -7);
        drawRect(playerInventoryRenderer, EColors.chalk, -5);
        drawStringCS(tradingEntityText, playerInventoryRenderer.midX, playerInventoryRenderer.startY - 50);
        
        drawRect(shopInventoryRenderer, EColors.lgray, -7);
        drawRect(shopInventoryRenderer, EColors.chalk, -5);
        drawStringCS(shopEntityText, shopInventoryRenderer.midX, shopInventoryRenderer.startY - 50);
        
        if (selectedItem != null) {
            imaginaryEndPosition = shopInventoryRenderer.endX + gap;
            itemTextX = startX + (imaginaryEndPosition - startX) * 0.5;
            itemTextY = shopInventoryRenderer.endY + 40;
            itemPriceX = itemTextX;
            itemPriceY = itemTextY + 35;
            
            String buySell = (isBuying) ? "Shop will buy for: " : "Shop will sell for: ";
            String priceString = buySell + EColors.mc_gold + selectedItemPrice + EColors.white + " Gold";
            String name = selectedItem.getName();
            drawStringS(name, itemTextX - FontRenderer.strWidth(name) / 2.0, itemTextY, EColors.aquamarine);
            drawStringS(priceString, itemPriceX - FontRenderer.strWidth(priceString) / 2.0, itemPriceY, EColors.white);
        }
        
        checkClose();
    }
    
    protected void checkMouseHover() {
        // this is poorly named..
        if (lastClickedInventory == null && playerInventoryRenderer.isMouseInside()) {
            sell.setVisible(true);
            buy.setVisible(false);
            isBuying = false;
        }
        if (lastClickedInventory == null && shopInventoryRenderer.isMouseInside()) {
            sell.setVisible(false);
            buy.setVisible(true);
            isBuying = true;
        }
    }
    
    protected void checkClose() {
        // anyone is null or dead
        if (EUtil.anyNull(shopkeeper, shopkeeperEntity, tradingEntity) ||
            shopkeeperEntity.isDead() || tradingEntity.isDead())
        {
            close();
            return;
        }
        
        // entities are too far away
        var world = shopkeeperEntity.world;
        double dist = world.getDistance(shopkeeperEntity, tradingEntity);
        
        if (dist >= 130) close();
    }
    
    protected void onInventoryClick(TradingInventoryRenderer inventory, int itemSlot) {
        if (!this.containsObject(inventory)) {
            System.out.println(inventory + " tried invoking 'onInventoryClick' but isn't even a part of this object!");
            return;
        }
        
        lastClickedInventory = inventory;
        lastClickedSlot = itemSlot;
        
        selectedItem = lastClickedInventory.getItemAtIndex(lastClickedSlot);
        selectedItemPrice = lastClickedInventory.getSelectedItemPrice();
        
        if (lastClickedInventory == playerInventoryRenderer) {
            shopInventoryRenderer.clearLastClickedSlot();
            shopInventoryRenderer.clearSelectedItem();
            if (selectedItemPrice != null && selectedItemPrice > shopkeeperEntity.getGold()) {
                selectedItemPrice = shopkeeperEntity.getGold();
            }
        }
        else {
            playerInventoryRenderer.clearLastClickedSlot();
            playerInventoryRenderer.clearSelectedItem();
        }
        
        updateBuySellMode(lastClickedInventory == playerInventoryRenderer, selectedItem != null);
    }
    
    protected void updateBuySellMode(boolean val, boolean hasItem) {
        buy.setVisible(!val);
        buy.setEnabled(!val && hasItem);
        sell.setVisible(val);
        sell.setEnabled(val && hasItem);
        isBuying = val;
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }
    
    @Override
    public void onGroupNotification(ObjectEvent e) {
        if (e.getEventType() == EventType.KEYBOARD) {
            EventKeyboard keyEvent = (EventKeyboard) e;
            int keyCode = keyEvent.getEventKey();
            
            if (Keyboard.KEY_ENTER == keyCode) {
                if (buy.isVisible() && buy.isEnabled()) {
                    buyItem();
                }
                else if (sell.isVisible() && sell.isEnabled()) {
                    sellItem();
                }
            }
        }
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.KEY_ENTER == keyCode) {
            if (buy.isVisible() && buy.isEnabled()) {
                buyItem();
            }
            else if (sell.isVisible() && sell.isEnabled()) {
                sellItem();
            }
        }
    }
    
    @Override
    public void onClosed() {
        if (shopkeeper != null) shopkeeper.setCurrentlySelling(false);
    }

    //=========
    // Methods
    //=========
    
    public void buyItem() {
        if (selectedItem == null || selectedItemPrice == null) return;
        
        int entityGold = tradingEntity.getGold();
        boolean goldCheck = entityGold >= selectedItemPrice;
        boolean spaceCheck = !tradingEntity.getInventory().isFull();
        
        if (!goldCheck) return;
        if (!spaceCheck) return;
        
        tradingEntity.setGold(entityGold - selectedItemPrice);
        shopkeeperEntity.setGold(shopkeeperEntity.getGold() + selectedItemPrice);
        Item item = shopkeeperEntity.getInventory().removeItemAtIndex(lastClickedSlot);
        tradingEntity.getInventory().addItem(item);
        shopkeeperEntity.getFavorTracker().increaseFavorWithEntity(tradingEntity, 20);
        
        selectedItem = null;
        selectedItemPrice = null;
        buy.setEnabled(false);
        
        shopkeeper.itemWasSold(tradingEntity, item);
    }
    
    public void sellItem() {
        if (selectedItem == null || selectedItemPrice == null) return;
        
        int sellPrice = selectedItemPrice;
        int entityGold = shopkeeperEntity.getGold();
        boolean goldCheck = entityGold >= selectedItemPrice;
        boolean spaceCheck = !shopkeeperEntity.getInventory().isFull();
        
        if (!goldCheck) sellPrice = entityGold;
        if (!spaceCheck) return;
        
        shopkeeperEntity.setGold(entityGold - sellPrice);
        tradingEntity.setGold(tradingEntity.getGold() + sellPrice);
        Item item = tradingEntity.getInventory().removeItemAtIndex(lastClickedSlot);
        shopkeeperEntity.getInventory().addItem(item);
        shopkeeperEntity.getFavorTracker().increaseFavorWithEntity(tradingEntity, 5);
        
        selectedItem = null;
        selectedItemPrice = null;
        sell.setEnabled(false);
        
        shopkeeper.itemWasBought(tradingEntity, item);
    }
    
    //=========
    // Getters
    //=========
    
    public Shop getShop() { return theShop; }
    
}
