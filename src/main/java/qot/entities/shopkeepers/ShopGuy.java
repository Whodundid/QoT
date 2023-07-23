package qot.entities.shopkeepers;

import envision.game.entities.BasicRenderedEntity;

public class ShopGuy extends BasicRenderedEntity {
    
    public ShopGuy() {
        super("ShopGuy");
        
        this.setTexture(null);
    }

    @Override
    public int getInternalSaveID() { return 0; }
    
}
