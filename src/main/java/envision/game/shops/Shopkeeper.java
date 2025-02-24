package envision.game.shops;

import envision.engine.loader.built.game.Entity;
import envision.engine.loader.built.game.Item;
import envision.engine.loader.built.game.Shop;

public interface Shopkeeper {
    
    Shop getShop();
    void setShop(Shop shopIn);
    
    boolean isCurrentlySelling();
    void setCurrentlySelling(boolean val);
    
    /**
     * 
     * @param buyingEntity The entity who sold something to us
     * @param item The item we bought
     */
    void itemWasBought(Entity buyingEntity, Item item);
    
    /**
     * 
     * @param entitySoldTo The entity who bought something from us
     * @param item The item we sold
     */
    void itemWasSold(Entity entitySoldTo, Item item);
    
}
