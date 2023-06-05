package envision.game.shops;

public interface Shopkeeper {
	
	boolean isCurrentlySelling();
	Shop getShop();
	
	void setCurrentlySelling(boolean val);
	void setShop(Shop shopIn);
	
}
