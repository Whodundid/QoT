package envision.game.shops;

import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.entities.Entity;
import qot.screens.character.InventoryRenderer;

public class ShopWindow extends WindowParent {
    
    private Shopkeeper shop;
    private Entity shopkeeperEntity;
    private Entity tradingEntity;
    
    private InventoryRenderer shopInventoryRenderer;
    private InventoryRenderer playerInventoryRenderer;
    
    public ShopWindow(Shopkeeper shopIn, Entity tradingEntityIn) {
        shop = shopIn;
        tradingEntity = tradingEntityIn;
        
        
    }
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("New Window");
        setGuiSize(400, 400);
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
        
    }
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        drawDefaultBackground();
        
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }

    
}
