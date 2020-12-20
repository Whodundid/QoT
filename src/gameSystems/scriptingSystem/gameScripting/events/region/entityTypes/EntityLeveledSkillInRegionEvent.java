package gameSystems.scriptingSystem.gameScripting.events.region.entityTypes;

import assets.entities.Entity;
import assets.skills.Skill;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.events.region.EntityRegionEvent;

public class EntityLeveledSkillInRegionEvent extends EntityRegionEvent {
	
	private Skill skill;
	private int oldLevel = -1;
	private int newLevel = -1;
	
	public EntityLeveledSkillInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Skill theSkill, int oldLevelIn, int newLevelIn) {
		super(theWorld, theRegion, theEntity);
		skill = theSkill;
		oldLevel = oldLevelIn;
		newLevel = newLevelIn;
	}
	
	public Skill getSkill() { return skill; }
	public int getOldLevel() { return oldLevel; }
	public int getNewLevel() { return newLevel; }
	
}
