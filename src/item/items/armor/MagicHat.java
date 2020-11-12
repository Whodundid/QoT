package item.items.armor;

public class MagicHat extends ArmorItem {
	
	public MagicHat() {
		super(1000, 5, "The Magic Hat", 5, 30);
	}
	
	@Override
	public String getDescription() {
		return "Wowwww you have a Magic Hat. Done. Bye";
	}

}
