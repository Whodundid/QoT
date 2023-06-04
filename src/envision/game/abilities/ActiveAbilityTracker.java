package envision.game.abilities;

import envision.game.entities.Entity;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.util.EList;

/**
 * Keeps track of the abilities an entity currently has equipped, to which
 * slots they are in, and the cooldowns of each.
 * 
 * @author Hunter Bragg
 */
public class ActiveAbilityTracker {
	
	//========
	// Fields
	//========
	
	private final Entity theEntity;
	private Ability[] abilities;
	private int maxNumberOfAbilities = 5;
	/** [On cooldown, number of game ticks still left in cooldown] */
	private Box2<Boolean, Integer>[] cooldownTracker;
	private boolean anyOnCooldown = false;
	
	//==============
	// Constructors
	//==============
	
	public ActiveAbilityTracker(Entity entIn, int maxIn) {
		theEntity = entIn;
		maxNumberOfAbilities = maxIn;
		abilities = new Ability[maxIn];
		cooldownTracker = new Box2[maxIn];
		
		for (int i = 0; i < maxIn; i++) {
			cooldownTracker[i] = new Box2<>(false, 0);
		}
	}
	
	//=========
	// Methods
	//=========
	
	public void onGameTick(float dt) {
		if (!anyOnCooldown) return;
		
		int onCooldown = maxNumberOfAbilities;
		
		for (int i = 0; i < maxNumberOfAbilities; i++) {
			Ability a = abilities[i];
			
			if (a == null) {
				onCooldown--;
				continue;
			}
			
			Box2<Boolean, Integer> tracker = cooldownTracker[i];
			boolean cooldown = tracker.getA();
			int remainingTicks = tracker.getB();
			
			// if not on cooldown
			if (!cooldown) {
				onCooldown--;
				continue;
			}
			
			if (remainingTicks == 0) {
				tracker.setA(false);
				onCooldown--;
			}
			else {
				tracker.setB(remainingTicks - 1);
				onCooldown++;
			}
		}
		
		anyOnCooldown = onCooldown != 0;
	}
	
	public Ability getAbilityAtSlot(int slot) {
		if (slot < 0 || slot >= maxNumberOfAbilities) return null;
		
		return abilities[slot];
	}
	
	public void setAbilityAtSlot(Ability a, int slot) {
		if (slot < 0 || slot >= maxNumberOfAbilities) return;
		
		abilities[slot] = a;
	}
	
	/**
	 * Attempts to have the given entity use the ability at the given slot.
	 * 
	 * @param ent 
	 * @param slot
	 * @return
	 */
	public boolean useAbility(int slot) {
		if (slot < 0 || slot >= maxNumberOfAbilities) return false;
		
		// get ability at specified slot
		var a = abilities[slot];
		if (a == null) return false;

		var box = cooldownTracker[slot];
		// check if still on cooldown -- don't use if so
		if (box.getA()) return false;
		
		boolean used = a.use(theEntity);
		
		int level = box.getB();
		int cooldown = a.getCooldownForTier(level);
		
		if (used && cooldown > 0) {
			// set this ability to be on cooldown
			box.setA(true);
			box.setB(cooldown);
			anyOnCooldown = true;
		}
		
		return used;
	}
	
	public Box2<Boolean, Integer> getCooldownForAbility(Ability abilityIn) {
		int index = -1;
		Ability ability = null;
		for (int i = 0; i < maxNumberOfAbilities; i++) {
			Ability a = abilities[i];
			if (a == null || a != abilityIn) continue;
			index = i;
			ability = a;
			break;
		}
		
		if (ability == null) return null;
		
		return cooldownTracker[index];
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
		for (int i = 0; i < maxNumberOfAbilities; i++) {
			if (abilities[i] == null) {
				abilities[i] = a;
				return true;
			}
		}
		return false;
	}
	
	public int getTotalAbilities() {
		final int max_i = maxNumberOfAbilities;
		int size = 0;
		for (int i = 0; i < max_i; i++) {
			if (abilities[i] != null) size++;
		}
		return size;
	}
	
	public EList<Ability> getActiveAbilities() {
		EList<Ability> active = EList.newList();
		for (int i = 0; i < maxNumberOfAbilities; i++) {
			active.add(abilities[i]);
		}
		return active;
	}
	
}
