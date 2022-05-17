package engine.scripting.builder.events.region.entityTypes;

import engine.scripting.builder.events.region.EntityRegionEvent;
import game.entities.Entity;
import game.skills.Skill;
import world.GameWorld;
import world.Region;

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
