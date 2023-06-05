package envision.game.shops;

import envision.game.entities.Entity;
import envision.game.items.Item;

/**
 * Wraps the result of a shop transaction where an item was either bought
 * or sold, successfully traded or not, and any other data.
 * 
 * @author Hunter Bragg
 */
public class ShopTransactionResult {
	
	//========
	// Fields
	//========
	
	private Entity buyingEntity, sellingEntity;
	private Item theItem;
	private boolean itemWasBought;
	private boolean wasSuccessful = true;
	private String failureReason;
	
	//==============
	// Constructors
	//==============
	
	public ShopTransactionResult() {}
	public ShopTransactionResult(Entity buyerIn, Entity sellerIn) {
		buyingEntity = buyerIn;
		sellingEntity = sellerIn;
	}
	
	//=========
	// Getters
	//=========
	
	public Entity buyer() { return buyingEntity; }
	public Entity seller() { return sellingEntity; }
	public Item item() { return theItem; }
	public boolean wasBought() { return itemWasBought; }
	public boolean wasSuccessful() { return wasSuccessful; }
	public String failureReason() { return failureReason; }
	
	//=========
	// Setters
	//=========
	
	public ShopTransactionResult buyer(Entity entIn) { buyingEntity = entIn; return this; }
	public ShopTransactionResult seller(Entity entIn) { sellingEntity = entIn; return this; }
	public ShopTransactionResult item(Item itemIn) { theItem = itemIn; return this; }
	public ShopTransactionResult wasBought(boolean val) { itemWasBought = val; return this; }
	public ShopTransactionResult wasSuccessful(boolean val) { wasSuccessful = val; return this; }
	public ShopTransactionResult failureReason(String reasonIn) { failureReason = reasonIn; return this; }
	
	public ShopTransactionResult fail() { wasSuccessful = false; return this; }
	public ShopTransactionResult fail(String reason) { fail(); failureReason = reason; return this; }
	
	//========================
	// Static Helper Builders
	//========================
	
	public static ShopTransactionResult success_buy(Item item) { return buy(item); }
	public static ShopTransactionResult success_sell(Item item) { return sell(item); }
	
	public static ShopTransactionResult fail_buy(Item item, String reason) { return buy(item).fail(reason); }
	public static ShopTransactionResult fail_sell(Item item, String reason) { return sell(item).fail(reason); }
	
	//==========================
	// Internal Helper Builders
	//==========================
	
	static ShopTransactionResult base() { return new ShopTransactionResult(); }
	static ShopTransactionResult base(Entity buyer, Entity seller) { return new ShopTransactionResult(buyer, seller); }
	
	static ShopTransactionResult buy() { return base().wasBought(true); }
	static ShopTransactionResult buy(Entity buyer, Entity seller) { return base(buyer, seller).wasBought(true); }
	static ShopTransactionResult buy(Item item) { return buy().item(item); }
	static ShopTransactionResult buy(Entity buyer, Entity seller, Item item) { return buy(buyer, seller).wasBought(true).item(item); }
	static ShopTransactionResult sell() { return base().wasBought(false); }
	static ShopTransactionResult sell(Entity buyer, Entity seller) { return base(buyer, seller).wasBought(false); }
	static ShopTransactionResult sell(Item item) { return sell().item(item); }
	static ShopTransactionResult sell(Entity buyer, Entity seller, Item item) { return sell(buyer, seller).wasBought(false).item(item); }
	
}
