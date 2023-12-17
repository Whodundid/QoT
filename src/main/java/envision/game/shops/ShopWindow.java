package envision.game.shops;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.entities.Entity;
import eutil.EUtil;
import eutil.colors.EColors;
import qot.screens.character.InventoryRenderer;

public class ShopWindow extends WindowParent {
    
    //========
    // Fields
    //========
    
    private Shop theShop;
    private Shopkeeper shopkeeper;
    private Entity shopkeeperEntity;
    private Entity tradingEntity;
    
    private InventoryRenderer shopInventoryRenderer;
    private InventoryRenderer playerInventoryRenderer;
    
    /** Displays the shop's favor status with the trading entity. (Influences prices) */
    private WindowLabel shopFavorLabel;
    /** Displays the value of the trade (buying or selling). */
    private WindowLabel tradeValueLabel;
    
    private WindowButton buy, sell, cancel;
    
    //==============
    // Constructors
    //==============
    
    public ShopWindow(Shop theShopIn, Shopkeeper shopkeeperIn, Entity tradingEntityIn) {
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
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void initChildren() {
        final var pir = playerInventoryRenderer = new InventoryRenderer(tradingEntity);
        final var sir = shopInventoryRenderer = new InventoryRenderer(shopkeeperEntity, 5, playerInventoryRenderer.getInventorySize());
        
        double gap = 30.0;
        double xPos = startX + gap;
        double yPos = midY - pir.height / 2;
        double imaginaryMiddleGap = 50;
        
        pir.setPosition(xPos, yPos);
        double imaginaryEndPosition = pir.endX + imaginaryMiddleGap + sir.width + gap;
        //double imaginaryEndGap = imaginaryEndPosition - pir.endX;
        double shopStartX = pir.endX + imaginaryMiddleGap;
        sir.setPosition(shopStartX, yPos);
        
        setSize(imaginaryEndPosition - startX, height + 100);
        defaultHeader();
        
        double itemTextX = midX;
        double itemTextY = sir.endY + 40;
        pir.setItemTextPosition(itemTextX, itemTextY);
        sir.setItemTextPosition(itemTextX, itemTextY);
        
        pir.setPriceModifier(theShop.getSellToModifier());
        sir.setPriceModifier(theShop.getBuyFromModifier());
        pir.setDrawItemPrice(true);
        sir.setDrawItemPrice(true);
        pir.setItemPriceTextPosition(itemTextX, itemTextY + 55);
        sir.setItemPriceTextPosition(itemTextX, itemTextY + 55);
        
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
    }
    
    @Override
    public void drawObject(long dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        for (int i = 0; i < 10; i++) {            
            drawRect(EColors.chalk.brightness(255 - i * 9), (i * 1) + 1);
        }
        
        drawRect(playerInventoryRenderer, EColors.lgray, -7);
        drawRect(playerInventoryRenderer, EColors.chalk, -5);
        drawStringCS(tradingEntity.getName(), playerInventoryRenderer.midX, playerInventoryRenderer.startY - 50);
        
        drawRect(shopInventoryRenderer, EColors.lgray, -7);
        drawRect(shopInventoryRenderer, EColors.chalk, -5);
        drawStringCS(shopkeeperEntity.getName(), shopInventoryRenderer.midX, shopInventoryRenderer.startY - 50);
        
        checkClose();
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
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }
    
    @Override
    public void onClosed() {
        if (shopkeeper != null) shopkeeper.setCurrentlySelling(false);
    }

    
}
