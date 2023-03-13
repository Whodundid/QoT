package envision.game.objects.abilities;

import envision.game.objects.entities.Entity;
import eutil.datatypes.boxes.Box2;

public class AbilityManager {
	
	//========
	// Fields
	//========
	
	private Ability[] abilities;
	private Box2<Boolean, Long>[] cooldownTracker;
	private int max = 5;
	
	//==============
	// Constructors
	//==============
	
	public AbilityManager(int maxIn) {
		max = maxIn;
		cooldownTracker = new Box2[max];
	}
	
	public Ability getAbilityAtSlot(int slot) {
		if (slot < 0 || slot >= max) return null;
		
		return abilities[slot];
	}
	
	public void setAbilityAtSlot(Ability a, int slot) {
		if (slot < 0 || slot >= max) return;
		
		abilities[slot] = a;
	}
	
	/**
	 * Attempts to have the given entity use the ability at the given slot.
	 * 
	 * @param ent
	 * @param slot
	 * @return
	 */
	public boolean useAbility(Entity ent, int slot) {
		if (ent == null) return false;
		if (slot < 0 || slot >= max) return false;
		
		var a = abilities[slot];
		if (a == null) return false;
		
		return a.use(ent);
	}
	
	/**
	 * Adds the ability to the first available (null) slot.
	 * Returns true if successfully added.
	 * 
	 * @param a The ability to add
	 * 
	 * @return True if given ability was added
	 */
	public boolean addAbility(Ability a) {
		for (int i = 0; i < max; i++) {
			if (abilities[i] == null) {
				abilities[i] = a;
				return true;
			}
		}
		return false;
	}
	
	public int getTotalAbilities() {
		final int max_i = max;
		int size = 0;
		for (int i = 0; i < max_i; i++) {
			if (abilities[i] != null) size++;
		}
		return size;
	}
	
}
