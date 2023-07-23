package envision.game.shops;

import envision.game.items.Item;
import eutil.datatypes.util.EList;

/**
 * Wraps an item that can be sold from a shop, but keeps track of the
 * item's price.
 * 
 * @author Hunter Bragg
 */
public class ShopItemSlot {
	
	private Item theItemToSell;
	private Item itemToTradeFor;
	private int goldPrice;
	private EList<ItemTransactionRequirement> sellStrats = EList.newList();
	
	public ShopItemSlot(Item theItemToSellIn) {
		theItemToSell = theItemToSellIn;
	}
	
	public Item getItem() { return theItemToSell; }
	public Item getItemToTradeFor() { return itemToTradeFor; }
	public int getGoldPrice() { return goldPrice; }
	public EList<ItemTransactionRequirement> getSellStrats() { return sellStrats.toUnmodifiableList(); }
	
	public void setItemToTradeFor(Item toTradeFor) { itemToTradeFor = toTradeFor; }
	public void setGoldPrice(int gold) { goldPrice = gold; }
	public void setSellStrats(EList<ItemTransactionRequirement> sellStratsIn) { sellStrats.clearThenAddAll(sellStratsIn); }
	public void setSellStrats(ItemTransactionRequirement... stratIn) { sellStrats.clearThenAdd(stratIn); }
	
}
