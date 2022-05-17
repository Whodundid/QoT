package game.items;

import engine.renderEngine.textureSystem.GameTexture;
import game.entities.Entity;

public abstract class Item {
	
	protected String name;
	private int basePrice;
	private String description;
	private GameTexture texture;
	private int id;

	public Item(String nameIn, int idIn) { this(nameIn, 0, "", idIn); }
	public Item(String nameIn, int basePriceIn, int idIn) { this(nameIn, basePriceIn, "", idIn); }
	public Item(String nameIn, int basePriceIn, String descriptionIn, int idIn) {
		name = nameIn;
		basePrice = basePriceIn;
		description = descriptionIn;
		id = idIn;
	}
	
	/** Override in child classes to define behavior for items which have a use interaction. */
	public void onItemUse(Entity user) {}
	/** Override in child classes to define behavior for items which have an equip interaction. */
	public void onItemEquip(Entity user) {}
	/** Override in child classes to define behavior for items which have an unequip interaction. */
	public void onItemUnequip(Entity user) {}
	
	public String getName() { return name; }
	public int getBasePrice() { return basePrice; }
	public String getDescription() { return description; }
	public int getID() { return id; }
	
	/** Can return null if this item does not have a texture. */
	public GameTexture getTexture() { return texture; }
	
	public Item setTexture(GameTexture textureIn) { texture = textureIn; return this; }
	public Item setDescription(String in) { description = in; return this; }
	public Item setBasePrice(int in) { basePrice = in; return this; }
	
}
