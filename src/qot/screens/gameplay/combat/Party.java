package qot.screens.gameplay.combat;

import envisionEngine.gameEngine.gameObjects.entity.Entity;

/**
 * Used to keep track of all entities on one team.
 * Also directly used within the BattleScreen for grouping fighters.
 * 
 * @author Hunter Bragg
 */
public class Party {
	
	//max of 4 entities in a party
	private Entity[] partyList = new Entity[4];
	//keeps track of non null party members
	private int partySize = 0;
	
	//--------------
	// Constructors
	//--------------
	
	public Party() {}
	public Party(Entity... entities) {
		if (entities.length > 4) throw new RuntimeException("Party too large! {" + entities.length + "}");
		for (int i = 0; i < entities.length; i++) {
			if (entities[i] == null) continue;
			partyList[i] = entities[i];
			partySize++;
		}
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Updates the total number of entities still ALIVE within this party.
	 * Called whenever there is a potential change to the current set of
	 * party members.
	 */
	private void updatePartySize() {
		partySize = 0;
		for (Entity e : partyList) if (e != null) partySize++;
	}
	
	/** Returns the total number of ALIVE party members. */
	public int getPartySize() { return partySize; }
	/** Returns true if there is no one left ALIVE in the party -- (everyone's null) */
	public boolean isDead() { return partySize == 0; }
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the entity at the given slot -- Does not account for bad
	 * index positions.
	 */
	public Entity getSlot(int slotNum) {
		return partyList[slotNum];
	}
	
	public Entity getSlot1() { return partyList[0]; }
	public Entity getSlot2() { return partyList[1]; }
	public Entity getSlot3() { return partyList[2]; }
	public Entity getSlot4() { return partyList[3]; }
	
	//---------
	// Setters
	//---------
	
	/**
	 * Assigns the entity at the given slot -- Does not account for bad
	 * index positions.
	 */
	public void setSlot(int slotNum, Entity ent) {
		partyList[slotNum] = ent;
		updatePartySize();
	}
	
	public void setSlot1(Entity ent) { partyList[0] = ent; updatePartySize(); }
	public void setSlot2(Entity ent) { partyList[1] = ent; updatePartySize(); }
	public void setSlot3(Entity ent) { partyList[2] = ent; updatePartySize(); }
	public void setSlot4(Entity ent) { partyList[3] = ent; updatePartySize(); }
	
}
