package envision.game.shops;

public interface Shopkeeper {
	
    Shop getShop();
    void setShop(Shop shopIn);
	
    boolean isCurrentlySelling();
	void setCurrentlySelling(boolean val);
	
}
